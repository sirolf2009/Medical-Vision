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

/**
 * Provides service/server network discovery using UDP multicast datagram 
 * packets. 
 * <h1>Announcing servers/services</h1>
 * <p>Servers wishing to announce themselves on the network use the 
 * {@link com.cjsavage.java.net.discovery.ServiceAnnouncer} class to listen for 
 * requests that match their registered services.  Services are defined with the
 * {@link com.cjsavage.java.net.discovery.ServiceInfo} class
 * and registered with the announcer using the 
 * {@link com.cjsavage.java.net.discovery.ServiceAnnouncer#addService(ServiceInfo)}
 * method. The default group/port is 229.14.8.83:9814.</p>
 * <p>Services are identified using a 32-character ID, the intention being that
 * this ID is the MD5 sum of the package that is running the server making it
 * highly unlikely to have ID conflicts on the same network.</p>
 * <p>ServiceAnnouncer example:</p>
 * <pre>
 * private static final String SERVICE_ID = "e9ababe5872f24caf1a504f1d675470c";
 * 
 * private String getLocalIp() {
 *     String ip;
 *     try{
 *         InetAddress ia = InetAddress.getLocalHost();
 *         ip = ia.getHostAddress();
 *     } catch (UnknownHostException e) { 
 *         System.out.println("Cannot determine system IP, using loopback.");
 *         ip = "127.0.0.1";
 *     }
 *     return ip;
 * }
 * 
 * private void initAnnouncer() {
 *     String ip = getLocalIp();
 *     ServiceAnnouncer announcer = new ServiceAnnouncer();
 *     ServiceInfo si = 
 *             new ServiceInfo("My Server", SERVICE_ID, ip, 3218, false);
 *     announcer.addService(si);
 *     announcer.startListening();
 * }</pre>
 * <p>You should shutdown the internal listener socket and thread with
 * <code>announcer.stopListening()</code> when you are done or upon exiting your
 * application.</p>
 * <h1>Finder servers/services</h1>
 * <p>To find servers on the network use the 
 * {@link com.cjsavage.java.net.discovery.ServiceFinder} class. This class will
 * send out a multicast request with the service ID to any listening 
 * {@link com.cjsavage.java.net.discovery.ServiceAnnouncer}s.  The ServiceFinder
 * can receive responses either by multicast or a TCP socket.</p>
 * <p>The ServiceFinder will tell the 
 * {@link com.cjsavage.java.net.discovery.ServiceAnnouncer} 
 * how to respond to it in the request. The response method is determined by the
 * constructor used to instantiate the ServiceFinder. When he default constructor
 * is used responses are received via multicast. If a port number is passed to
 * the constructor, responses are received via a TCP socket on that port.</p> 
 * <p>ServiceFinder example:</p>
 * <pre>
 * private static final String SERVICE_ID = "e9ababe5872f24caf1a504f1d675470c";
 *
 * private void initFinder() {
 *     // Receive responses via TCP port 1234
 *     // ServiceFinder finder = new ServiceFinder(1234);
 *     // Receive response via multicast 
 *     ServiceFinder finder = new ServiceFinder(); 
 *     finder.addListener(mListener);
 *     finder.startListening();
 *     finder.findServers(SERVICE_ID, 0);
 * } 
 *
 * private ServiceFinder.Listener mListener = new ServiceFinder.Listener() {
 *     public void serverFound(ServiceInfo si, int requestId,
 *             ServiceFinder finder) {
 *         System.out.println("\r\nFound service provider named " +
 *                 si.getServerName() + " at " + si.getServiceHost() + ":" +
 *                 + si.getServicePort() + "\r\nCommand: ");
 *     }
 *
 *     public void listenStateChanged(ServiceFinder finder, boolean listening) {
 *         if (listening) {
 *             System.out.println("ServiceFinder is listening for responses.");
 *         } else {
 *             System.out.println("ServiceFinder has shut down.");
 *         }
 *     }
 * };
 * </pre>
 * <p>You should shutdown the internal listener socket and thread with
 * <code>finder.stopListening()</code> when you are done or upon exiting your
 * application.</p>
 * @since 1.0
 */
package com.cjsavage.java.net.discovery;

