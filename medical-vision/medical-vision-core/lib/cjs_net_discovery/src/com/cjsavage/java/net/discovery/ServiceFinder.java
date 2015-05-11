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
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Used to send requests for {@link ServiceAnnouncer}s to respond to. 
 * <p>See the <a href="package-summary.html">package summary</a> for example
 * usage.</p>
 * <p>The ServiceFinder can receive responses either by multicast or a TCP
 * socket depending on how it is instantiated. The default constructor will use
 * multicast to receive responses while the {@link #ServiceFinder(int)} 
 * constructor will receive responses on the port number supplied.</p>
 * <p>You must call {@link #startListening} before calling 
 * {@link #findServers(String, int)}.</p>
 * @since 1.0
 */
public class ServiceFinder {
    public static final int RECEIVE_MULTICAST = ClientRequest.RESPOND_MULTICAST;
    public static final int RECEIVE_TCP_SOCKET = ClientRequest.RESPOND_TCP_SOCKET;
    private static final int MAX_PROTOCOL_VERSION = 1;
    private static final int MAX_THREADS = 10;
    private static final int READ_TIMEOUT = 15000;

    private ArrayList<Listener> mListeners = new ArrayList<Listener>();
    private InetAddress mGroup;
    private MulticastSocket mMcSocket;
    private ServerSocket mTcpSocket;
    private volatile int mResponseThreads = 0; 
    private int mMaxThreads = MAX_THREADS;
    private volatile boolean mRunning = false;
    private volatile boolean mPrintProtocolErrors = false;
    private int mMulticastPort;
    private int mReceiveBufferSize = 256;
    private int mReceiveMode;
    private int mReceivePort;

    /**
     * Creates a new ServiceFinder that will listen for multicast responses
     * using the default group and port.  See the {@link ServiceAnnouncer} for
     * the default group and port.
     */
    public ServiceFinder() {
        try {
            mGroup = InetAddress.getByName(ServiceAnnouncer.DEFAULT_GROUP);
        } catch (UnknownHostException e) {
            // Should not happen as we're using an IP address
            e.printStackTrace();
        }
        mReceiveMode = RECEIVE_MULTICAST;
        mMulticastPort = ServiceAnnouncer.DEFAULT_PORT;
    }

    /**
     * Creates a new ServiceFinder that will listen for responses on a TCP 
     * socket listening on the supplied port.
     * @param port the port to listen for responses on
     * @throws IllegalArgumentException if port is not a valid port number
     */
    public ServiceFinder(int port) {
        this();
        if (port < ServiceAnnouncer.TCP_PORT_MIN
                || port > ServiceAnnouncer.TCP_PORT_MAX) {
            throw new IllegalArgumentException("ServiceFinder(port): port must "
                    +"be between 1 and 65535.");
        }
        mReceiveMode = RECEIVE_TCP_SOCKET;
        mReceivePort = port;
    }

    /**
     * Adds a listener to receiver server and state change events.
     * @param listener the listener to add
     */
    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    /**
     * Removes a listener previously added with {@link #addListener}.
     * @param listener the listener to remove
     */
    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    /**
     * Notifies all listeners of a change in the listening state of this
     * ServiceFinder.
     */
    private void fireListenStateChangedEvent() {
        final boolean listening = mRunning;
        synchronized(this) {
            for (Listener l : mListeners) {
                l.listenStateChanged(this, listening);
            }
        }
    }

    /**
     * Notifies all listeners of a server found event.
     * @param si the ServiceInfo instance containing the server information
     * @param requestId the request ID from the client
     */
    private void fireServerFoundEvent(ServiceInfo si, int requestId) {
        synchronized(this) {
            for (Listener l : mListeners) {
                l.serverFound(si, requestId, this);
            }
        }
    }

    /**
     * Returns true if this ServiceFinder is currently listening for responses.
     * @return true if this ServiceFinder is currently listening for responses
     */
    public boolean isListening() {
        return mRunning;
    }

    /**
     * Starts listening for responses from {@link ServiceAnnouncer}s. Depending
     * on the constructor used, this ServiceFinder will listen for multicast
     * or TCP responses.
     */
    public void startListening() {
        if (!mRunning) {
            mRunning = true;
            if (mReceiveMode == RECEIVE_MULTICAST) {
                new Thread(new MulticastListener()).start();
            } else {
                new Thread(new TcpListener()).start();
            }
            fireListenStateChangedEvent();
        }
    }

    /**
     * Stops listening for responses from {@link ServiceAnnouncer}s.
     */
    public void stopListening() {
        if (mRunning) {
            mRunning = false;
            if (mReceiveMode == RECEIVE_MULTICAST && mMcSocket != null) {
                mMcSocket.close();
            } else if (mTcpSocket != null) {
                try {
                    mTcpSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sends the multicast broadcast asking for servers to announce themselves
     * for the serviceId supplied.
     * <p>If the serviceId is longer than 32-characters it is truncated. Found
     * servers can be retrieved by registering a {@link Listener}.
     * @param serviceId the service ID to find
     * @param requestId 
     * @throws IllegalArgumentException if the service ID contains illegal 
     *         characters
     * @throws IllegalStateException if this ServiceFinder is not listening for
     *         responses
     */
    public void findServers(String serviceId, int requestId) {
        if (!mRunning) {
            throw new IllegalStateException("findServers(): not listening for "
                    + "responses, call startListening() before findServers().");
        }
        if (!ServiceInfo.validateServiceId(serviceId)) {
            throw new IllegalArgumentException("findServers() : serviceId is " +
                    "not valid.");
        }
        if (serviceId.length() > ServiceInfo.SERVICE_ID_MAX_LENGTH) {
            serviceId = serviceId.substring(0, ServiceInfo.SERVICE_ID_MAX_LENGTH);
        }

        new Thread(new RequestSender(serviceId, requestId)).start();
    }

    /**
     * Parses a response message and fires the listener if the message contains
     * a server response.
     * @param message the message to parse
     */
    private void parseResponse(String message) {
        ServiceInfo si = null;
        String[] parts = message.split(";");
        if (parts.length == 9 && "CJSMCANNRES".equals(parts[0])) {
            try {
                int protocolVersion = Integer.parseInt(parts[1]);
                if (protocolVersion > MAX_PROTOCOL_VERSION &&
                        mPrintProtocolErrors) {
                    System.err.println("ServiceFinder: Unsupported protocol " +
                            "version " + protocolVersion);
                    return;
                }
                String serviceId = parts[2];
                String host = parts[3];
                int port = Integer.parseInt(parts[4]);
                boolean secure = parts[5].equals("1");
                int requestId = Integer.parseInt(parts[6]);
                String serverName = parts[7];
                si = new ServiceInfo(serverName, serviceId, host, port, secure);
                fireServerFoundEvent(si, requestId);
            } catch (NumberFormatException e) {
                // Invalid response data, ignore it, but don't throw exception
            }
        }
    }

    /**
     * Sets the maximum number of threads that can be spawned to handle TCP
     * responses.  Must be > 0.
     * @param maxThreads the maximum number of threads that can be spawned
     */
    public void setMaxThreads(int maxThreads) {
        if (maxThreads > 0) {
            mMaxThreads = maxThreads;
        }
    }

    /**
     * Sets the multicast group to use for sending requests.
     * <p>This same group is used to receive requests if this is a ServiceFinder
     * was instantiated as a multicast receiver.</p>
     * <p>If this ServiceFinder is listening, it will be restarted and the new
     * address used immediately.</p>
     * @param group the group to use for sending and receiving requests
     * @throws NullPointerException if group is null
     */
    public void setMulticastGroup(InetAddress group) {
        if (group == null) {
            throw new NullPointerException("group cannot be null");
        }
        mGroup = group;
        if (mRunning) {
            stopListening();
            startListening();
        }
    }

    /**
     * Sets the multicast port to use for sending requests.
     * <p>This same port is used to receive requests if this is a ServiceFinder
     * was instantiated as a multicast receiver.</p>
     * <p>If this ServiceFinder is listening, it will be restarted and the new
     * address used immediately.</p>
     * @param port the port number to use for sending and receiving requests
     * @throws IllegalArgumentException if port is not a valid port number
     */
    public void setMulticastPort(int port) {
        if (port < ServiceAnnouncer.TCP_PORT_MIN 
                || port > ServiceAnnouncer.TCP_PORT_MAX) {
            throw new IllegalArgumentException("port must be between 1 and " +
                    "65535.");
        }
        mMulticastPort = port;
        if (mRunning) {
            stopListening();
            startListening();
        }
    }

    /**
     * Sets whether this ServiceFinder should print a line to System.err when
     * it receives a response for a protocol version higher than is supported.
     * Default is false. 
     * @param printErrors true to print protocol errors to System.err
     */
    public void setPrintProtocolErrors(boolean printErrors) {
        mPrintProtocolErrors = printErrors;
    }

    /**
     * Sets the receive buffer size.
     * <p>The default and minimum is 256 bytes which should be plenty under normal
     * circumstances.</p>
     * @param size the size of the receive buffer
     */
    public void setReceiveBufferSize(int size) {
        if (size >= 256) {
            mReceiveBufferSize = size;
        }
    }

    /**
     * Interface for receiving details of discovered servers and state change
     * events for the ServiceFinder.
     */
    public interface Listener {
        /**
         * Called when a server responds with connection details for a service.
         * @param serviceInfo information about the service
         * @param requestId the ID sent with the request this response is to
         * @param finder the ServiceFinder that received the response
         */
        public void serverFound(ServiceInfo serviceInfo, int requestId,
                ServiceFinder finder);

        /**
         * Called when the listening state of this ServiceFinder changes.
         * @param finder the ServiceFinder who's state changed
         * @param listening true if the ServiceFinder is listening for responses
         */
        public void listenStateChanged(ServiceFinder finder, boolean listening);
    }

    /**
     * Multicast listener for receiving and parsing multicast responses to
     * requests.
     */
    private class MulticastListener implements Runnable {
        @Override
        public void run() {
            MulticastSocket s;
            try {
                s = new MulticastSocket(mMulticastPort);
                s.joinGroup(mGroup);
                mMcSocket = s;

                while (mRunning) {
                    try {
                        byte[] buf = new byte[mReceiveBufferSize];
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        s.receive(packet);
                        String message = parseDatagram(packet);
                        System.out.println(message.getBytes().length);
                        parseResponse(message);
                    } catch (SocketException e) { /* Socket closed */ }
                }
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fireListenStateChangedEvent();
        }

        /**
         * Parses the datagram packet into a string.
         * @param packet the DatagramPacket to parse
         * @return a string containing the data from the packet
         */
        private String parseDatagram(DatagramPacket packet) {
            byte[] data = packet.getData();
            return new String(data, 0, data.length);
        }
    }

    /**
     * TCP listener for accepting connections from servers responding via TCP.
     */
    private class TcpListener implements Runnable {
        @Override
        public void run() {
            try {
                ServerSocket s = new ServerSocket(mReceivePort);
                mTcpSocket = s;
                
                while (mRunning) {
                    Socket client = s.accept();
                    if (mResponseThreads < mMaxThreads) {
                        new Thread(new TcpResponseHandler(client)).start();
                    }
                }
            } catch (IOException e) { }
        }
    }

    /**
     * Runnable to receive and parse TCP responses to a request.
     */
    private class TcpResponseHandler implements Runnable {
        private Socket mClient;

        public TcpResponseHandler(Socket c) {
            mClient = c;
        }

        @Override
        public void run() {
            Socket s = mClient;
            try {
                mClient.setSoTimeout(READ_TIMEOUT);
                int read;
                char[] data = new char[mReceiveBufferSize];
                InputStreamReader isr = new InputStreamReader(s.getInputStream());
                read = isr.read(data);
                String message = String.valueOf(data, 0, read);
                parseResponse(message);
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runnable that will send a broadcast asking servers with the supplied
     * service ID to respond with their connection details.
     */
    private class RequestSender implements Runnable {
        private String mServiceId;
        private int mRequestId;
        
        public RequestSender(String serviceId, int requestId) {
            mServiceId = serviceId;
            mRequestId = requestId;
        }
        
        @Override
        public void run() {
            String reqStr = buildRequestString();
            
            try {
                MulticastSocket s = new MulticastSocket(mMulticastPort);
                s.joinGroup(mGroup);
                DatagramPacket request = new DatagramPacket(reqStr.getBytes(),
                        reqStr.length(), mGroup, mMulticastPort);
                s.send(request);
                s.leaveGroup(mGroup);
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Builds the request string that is to be outputted.
         * @return the request string that is to be outputted
         */
        private String buildRequestString() {
            StringBuilder sb = new StringBuilder("CJSMCANNREQ;");
            sb.append(mServiceId).append(";").append(mRequestId).append(";")
                    .append(mReceiveMode).append(";").append(mReceivePort)
                    .append(";");
            return sb.toString();
        }
    }
}
