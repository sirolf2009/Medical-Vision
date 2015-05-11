/*
 * Copyright (C) 2012 Chris Savage (cjsavage.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cjsavage.java.net.discovery;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to announce the presence of a server that provides specific services. 
 * <p>See the <a href="package-summary.html">package summary</a> for example
 * usage.</p>
 * <p>A single ServiceAnnouncer can announce for several services and servers
 * simultaneously.  Services/servers can be registered with 
 * {@link #addService(ServiceInfo)}.</p>
 * <p>{@link ServiceFinder} instances are used to send requests. The request
 * contains the response method (multicast or TCP). Multicast responses are
 * always sent on the same group/port that is listened on.  TCP responses are
 * sent to the sending IP address and the request contains the port to respond
 * on.</p>
 * @since 1.0
 */
public class ServiceAnnouncer {
    /**
     * The default class D IP address that this responder will listen on.
     */
    public static final String DEFAULT_GROUP = "229.14.8.83";
    /**
     * The default port that the {@link MulticastSocket} will listen on.
     */
    public static final int DEFAULT_PORT = 9814;
    /**
     * Default timeout for writing TCP responses.
     */
    private static final int DEFAULT_TIMEOUT = 15000;
    private static final int MAX_THREADS = 10;
    private static final int PROTOCOL_VERSION = 1;
    protected static final int TCP_PORT_MIN = 1;
    protected static final int TCP_PORT_MAX = 65535;

    private ArrayList<Listener> mListeners = new ArrayList<Listener>();
    private HashMap<String, ServiceInfo> mServices =
            new HashMap<String, ServiceInfo>();
    private InetAddress mGroup;
    private MulticastSocket mMcSocket;
    private Thread mListenerThread;

    private volatile boolean mRunning = false;
    private volatile int mResponderThreads = 0; 
    private int mMaxThreads = MAX_THREADS;
    private int mPort = ServiceAnnouncer.DEFAULT_PORT;

    /**
     * Creates a new ServerAnnouncer that will listen on the default group
     * and default port.
     */
    public ServiceAnnouncer() {
        try {
            InetAddress group = InetAddress.getByName(DEFAULT_GROUP);
            setMulticastGroup(group);
            setMulticastPort(DEFAULT_PORT);
        } catch (UnknownHostException e) {
            // Shouldn't happen as we're using an IP address
            e.printStackTrace();
        }
    }

    /**
     * Returns true if this ServerAnnouncer is listening for requests.
     * @return true if this ServerAnnouncer is listening for requests
     */
    public boolean isListening() {
        return mRunning;
    }

    /**
     * Starts this listening for requests from {@link ServiceFinder}s.
     */
    public void startListening() {
        if (!isListening()) {
            mRunning = true;
            fireListenStateChangedEvent();
            mListenerThread = new Thread(new ListenerRunnable());
            mListenerThread.start();
        }
    }

    /**
     * Stops listening for requests from {@link ServiceFinder}s.
     */
    public void stopListening() {
        mRunning = false;
        if (mMcSocket != null) {
            mMcSocket.close();
        }
    }

    /**
     * Adds a listener for receiving events from this ServiceAnnouncer.
     * @param listener the listener to add
     */
    public void addListener(Listener listener) {
        synchronized(this) {
            mListeners.add(listener);
        }
    }

    /**
     * Removes a listener previously added with {@link #addListener}.
     * @param listener the listener to remove
     */
    public void removeListener(Listener listener) {
        synchronized(this) {
            mListeners.remove(listener);
        }
    }

    /**
     * Adds a service that this ServerAnnouncer will respond to requests for.
     * @param service a ServiceInfo instance to accept
     */
    public void addService(ServiceInfo service) {
        final String id = service.getServiceId();
        synchronized(this) {
            if (!mServices.containsKey(id)) {
                mServices.put(id, service);
            }
        }
    }

    /**
     * Returns true if this announcer contains a ServiceInfo instance for the 
     * service ID.
     * @param id the service ID to check
     * @return true if this responder contains the service ID
     */
    public boolean hasServiceId(String id) {
        boolean contains;
        synchronized(this) {
            contains = mServices.containsKey(id);
        }
        return contains;
    }

    private void fireListenStateChangedEvent() {
        final boolean listening = mRunning;
        synchronized(this) {
            for (Listener l : mListeners) {
                l.listenStateChanged(this, listening);
            }
        }
    }

    private void fireRequestReceivedEvent(ClientRequest request, 
            InetAddress source) {
        synchronized(this) {
            for (Listener l : mListeners) {
                l.receiveRequest(request, source, this);
            }
        }
    }

    /**
     * Wrapper around {@link #stopListening} and {@link #startListening}.
     */
    public void restart() {
        stopListening();
        startListening();
    }

    /**
     * Removes a previously added service. This method passes the service's ID
     * to {@link #removeService(String id)}.
     * @param service the ServiceInfo containing the service ID to remove
     */
    public void removeService(ServiceInfo service) {
        removeService(service.getServiceId());
    }

    /**
     * Removes a previously added service ID.
     * @param id the service ID to remove
     */
    public void removeService(String id) {
        synchronized(this) {
            mServices.remove(id);
        }
    }

    /**
     * Returns the group the internal {@link MulticastSocket} will join. This is
     * the group that was passed to the constructor or set via 
     * {@link #setMulticastGroup}.
     * @return the InetAddress passed to the constructor or setMulticastGroup
     */
    public InetAddress getMulticastGroup() {
        return mGroup;
    }

    /**
     * Sets the group the internal {@link MulticastSocket} will join. If this 
     * is called on an actively listening ServerAnnouncer, {@link #stopListening}
     * and {@link #startListening} will be called.
     * @param group the group to join
     * @throws NullPointerException if group is null
     */
    public void setMulticastGroup(InetAddress group) {
        if (group == null) {
            throw new NullPointerException("setMulticastGroup(group): group " +
                    "cannot be null.");
        }
        mGroup = group;
        if (isListening()) {
            restart();
        }
    }

    /**
     * Returns the port that this ServerAnnouncer will listen on.
     * @return the port that this ServerAnnouncer will listen on
     */
    public int getMulticastPort() {
        return mPort;
    }

    /**
     * Sets the port that this ServerAnnouncer will listen on.  If this 
     * is called on an actively listening ServerAnnouncer, {@link #stopListening}
     * and {@link #startListening} will be called.
     * @param port the port that this ServerAnnouncer will listen on
     * @throws IllegalArgumentException if port is not between 1 and 65535
     */
    public void setMulticastPort(int port) {
        if (port >= TCP_PORT_MIN && port <= TCP_PORT_MAX) {
            mPort = port;
            if (isListening()) {
                restart();
            }
        } else {
            throw new IllegalArgumentException("setMulticastPort(port): port " +
                    "must be between " + TCP_PORT_MIN + " and " + TCP_PORT_MAX +".");
        }
    }

    /**
     * Interface for responding to ServerAnnouncer events.
     */
    public interface Listener {
        /**
         * Called when the listening state of a ServiceAnnouncer has changed.
         * @param announcer the ServiceAnnouncer who's state has changed
         * @param listening true if the ServiceAnnouncer is listening for requests
         */
        public void listenStateChanged(ServiceAnnouncer announcer, boolean listening);
        
        /**
         * Called when a ServerAnnouncer has received a request from a 
         * client.
         * @param request the ClientRequest that was received
         * @param source the InetAddress that sent the request
         * @param announcer the ServiceAnnouncer that received the request
         */
        public void receiveRequest(ClientRequest request, InetAddress source,
                ServiceAnnouncer announcer);
    }

    /**
     * Runnable to listen for requests from {@link ServiceFinder}s.
     */
    private class ListenerRunnable implements Runnable {
        @Override
        public void run() {
            MulticastSocket s;
            try {
                s = new MulticastSocket(mPort);
                s.joinGroup(mGroup);
                mMcSocket = s;

                while (mRunning) {
                    try {
                        byte[] buf = new byte[128];
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        s.receive(packet);
                        InetAddress a = packet.getAddress();
                        ClientRequest request = parsePacket(packet);
                        if (mResponderThreads < mMaxThreads && request != null) {
                            new Thread(new ResponderRunnable(request, a)).start();
                            fireRequestReceivedEvent(request, a);
                        }
                    } catch (SocketException e) { /* Socket closed */ }
                }
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fireListenStateChangedEvent();
        }

        /**
         * Parses a datagram packet and if it contains a valid request, creates
         * a {@link ClientRequest} instance containing the details.
         * @param packet the packet to parse
         * @return a ClientRequest instance or null
         */
        private ClientRequest parsePacket(DatagramPacket packet) {
            ClientRequest request = null;
            byte[] data = packet.getData();
            String message = new String(data, 0, data.length);
            String[] parts = message.split(";");
            if ("CJSMCANNREQ".equals(parts[0])) {
                int requestId = Integer.parseInt(parts[2]);
                if (Integer.parseInt(parts[3]) == ServiceFinder.RECEIVE_MULTICAST) {
                    request = new ClientRequest(parts[1], requestId);
                } else {
                    int port = Integer.parseInt(parts[4]);
                    request = new ClientRequest(parts[1], requestId, port);
                }
            }
            return request;
        }
    }

    /**
     * Runnable for sending a response to the {@link ServiceFinder}s.
     */
    private class ResponderRunnable implements Runnable {
        private ClientRequest mRequest;
        private InetAddress mSender;

        public ResponderRunnable(ClientRequest request, InetAddress a) {
            mRequest = request;
            mSender = a;
        }

        @Override
        public void run() {
            mResponderThreads++;

            ServiceInfo si = mServices.get(mRequest.getServiceId());
            if (si != null) {
                String responseStr = buildResponseString(si, mRequest);
                if (mRequest.getResponseMethod() == ClientRequest.RESPOND_MULTICAST) {
                    sendMulticastResponse(mRequest, responseStr);
                } else {
                    sendTcpResponse(mRequest, responseStr);
                }
            }

            mResponderThreads--;
        }

        private String buildResponseString(ServiceInfo si, ClientRequest request) {
            StringBuilder sb = new StringBuilder("CJSMCANNRES;");
            sb.append(PROTOCOL_VERSION).append(";")
                    .append(si.getServiceId()).append(";")
                    .append(si.getServiceHost()).append(";")
                    .append(si.getServicePort()).append(";")
                    .append(si.isSecure() ? "1" : "0").append(";")
                    .append(request.getRequestId()).append(";")
                    .append(si.getServerName()).append(";");
            return sb.toString();
        }

        private void sendMulticastResponse(ClientRequest request,
                String response) {
            try {
                if (mMcSocket != null && mMcSocket.isBound()) {
                    DatagramPacket p = new DatagramPacket(response.getBytes(),
                            response.length(), mGroup, mPort);
                    mMcSocket.send(p);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendTcpResponse(ClientRequest request, String response) {
            try {
                Socket s = new Socket(mSender, request.getResponsePort());
                s.setSoTimeout(DEFAULT_TIMEOUT);
                PrintWriter pr = 
                        new PrintWriter(
                                new OutputStreamWriter(s.getOutputStream()));
                pr.println(response);
                pr.close();
                s.close();
            } catch (ConnectException e) { 
                System.out.println("Connection to " + mSender.getHostAddress() +
                        ":" + request.getResponsePort() + " refused.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
}
