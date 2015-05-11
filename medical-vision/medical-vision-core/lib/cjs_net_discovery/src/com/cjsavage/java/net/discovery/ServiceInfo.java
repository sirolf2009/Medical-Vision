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
 * Describes a service that can be announced by a {@link ServiceAnnouncer}.
 * @since 1.0
 */
public class ServiceInfo {
    public static final int SERVICE_ID_MAX_LENGTH = 32;
    public static final String SERVICE_ID_REGEX = "^[A-Za-z0-9]+$";

    private final boolean mIsSecure;
    private final String mServerName;
    private final String mServiceId;
    private final String mServiceHost;
    private final int mServicePort;

    /**
     * Creates a new service instance.
     * <p>The service ID can only have the characters 0-9 and A-Z (upper or 
     * lowercase) and must be no longer than 32 characters, it will be truncated
     * if it is longer. It is used to uniquely identify the service being 
     * provided. It is intended you use the MD5 hash of the application
     * package.</p>
     * @param serviceId the 32 character service ID
     * @param serviceHost the hostname or IP address the service is available at
     * @param servicePort the port number the service is available at
     * @throws IllegalArugmentException if serviceId contains illegal characters
     */
    public ServiceInfo(String serverName, String serviceId, String serviceHost,
            int servicePort, boolean isSecure) {
        if (serviceId.length() > SERVICE_ID_MAX_LENGTH) {
            serviceId = serviceId.substring(0, SERVICE_ID_MAX_LENGTH);
        }
        if (!ServiceInfo.validateServiceId(serviceId)) {
            throw new IllegalArgumentException("ClientRequest: serviceId " +
                    "must only contain characters 0-9 and A-Z.");
        }
        mServerName = serverName.replace(";", "");
        mServiceId = serviceId;
        mServiceHost = serviceHost;
        mServicePort = servicePort;
        mIsSecure = isSecure;
    }

    /**
     * Returns the server name.
     * @return the server name
     */
    public String getServerName() {
        return mServerName;
    }

    /**
     * Returns the service ID.
     * @return the service ID
     */
    public String getServiceId() {
        return mServiceId;
    }

    /**
     * Returns the hostname/IP address where this service can be connected to.
     * @return the hostname/IP address where this service can be connected to
     */
    public String getServiceHost() {
        return mServiceHost;
    }

    /**
     * Returns the port on which this service can be connected to.
     * @return the port on which this service can be connected to
     */
    public int getServicePort() {
        return mServicePort;
    }

    /**
     * Returns true if this server is secure. This could mean anything depending
     * on the service reporting to be secure.
     * @return true if the server is secure
     */
    public boolean isSecure() {
        return mIsSecure;
    }

    /**
     * Checks the supplied service ID against the SERVICE_ID_REGEX to ensure
     * it is valid.
     * @param serviceId the service ID to check
     * @return true if the service ID matches the regex
     */
    public static boolean validateServiceId(String serviceId) {
        return serviceId.matches(SERVICE_ID_REGEX);
    }
}
