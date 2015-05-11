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

/**
 * Represents a client request for service information. This is used internally
 * by the {@link ServiceAnnouncer}.
 * @since 1.0
 */
public final class ClientRequest {
    /**
     * Value returned from {@link #getResponseMethod()} indicating this request
     * should be responded to via a multicast datagram.
     */
    public static final int RESPOND_MULTICAST = 0;
    /**
     * Value returned from {@link #getResponseMethod()} indicating this request
     * should be responded to via a TCP socket.
     */
    public static final int RESPOND_TCP_SOCKET = 1;

    private final String mServiceId;
    private final int mRequestId;
    private final int mResponseMethod;
    private final int mResponsePort;

    /**
     * Creates a new multicast client request.
     * @param serviceId the service ID
     * @param requestId the request ID
     */
    protected ClientRequest(String serviceId, int requestId) {
        mServiceId = serviceId;
        mRequestId = requestId;
        mResponseMethod = RESPOND_MULTICAST;
        mResponsePort = -1;
    }

    /**
     * Creates a new TCP client request.
     * @param serviceId the service ID
     * @param requestId the request ID
     * @param responsePort the port to respond on
     */
    protected ClientRequest(String serviceId, int requestId, int responsePort) {
        final int maxPort = ServiceAnnouncer.TCP_PORT_MAX;
        final int minPort = ServiceAnnouncer.TCP_PORT_MIN;
        if (responsePort < minPort || responsePort > maxPort) {
            throw new IllegalArgumentException("ClientRequest: responsePort " +
                    "must be between " + minPort + " and " + maxPort + ".");
        }
        if (!ServiceInfo.validateServiceId(serviceId)) {
            throw new IllegalArgumentException("ClientRequest: serviceId " +
                    "must only contain characters 0-9 and A-Z.");
        }
        mServiceId = serviceId;
        mRequestId = requestId;
        mResponseMethod = RESPOND_TCP_SOCKET;
        mResponsePort = responsePort;
    }

    /**
     * Returns the service ID for this request.
     * @return the service ID for this request
     */
    public String getServiceId() {
        return mServiceId;
    }

    /**
     * Returns the request ID for this request.
     * @return the request ID for this request
     */
    public int getRequestId() {
        return mRequestId;
    }

    /**
     * Returns the preferred response method for this request. This will be
     * one of RESPOND_MULTICAST or RESPOND_TCP_SOCKET. 
     * @return the preferred response method for this request
     */
    public int getResponseMethod() {
        return mResponseMethod;
    }

    /**
     * Returns the port on which to respond on. This will be -1 if 
     * {@link #getResponseMethod} returns RESPOND_MULTICAST or a port number
     * between 1 and 65535 otherwise.
     * @return the port on which to respond on
     */
    public int getResponsePort() {
        return mResponsePort;
    }
}
