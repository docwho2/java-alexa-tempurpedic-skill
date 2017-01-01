/*
 * Copyright 2016 sjensen.
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
package com.sst.alexatempurpedic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 * Class that implements the UDP Protocol to talk with the Tempurpedic Ergo
 * Premier Base.
 *
 * @author Steve Jensen <docwho2@gmail.com>
 */
public class TempurpedicErgoPremier {

    /**
     * Last resort hard coded IP address, set here if you don't want to use
     * environment variable;
     */
    public final static String ERGO_IP_ADDRESS = "10.0.0.35";

    /**
     * Environment variable name used to store IP Address of the Wi-Fi Module
     */
    public final static String ENV_IP_NAME = "ERGO_BASE_IP_ADDRESS";

    
    
    // Port Number used by Wi-Fi module for sending UDP
    private final static int portNum = 50007;
    // Logging
    private static final Logger log = Logger.getLogger(TempurpedicErgoPremier.class);
    // Resolved IP address of the Wi-Fi Module
    private static InetAddress bedIPAddr;

    static {
        // Use ENV var if set, otherwise use hard-coded value
        String ip = System.getenv(ENV_IP_NAME) != null ? System.getenv(ENV_IP_NAME) : ERGO_IP_ADDRESS;
        try {
            bedIPAddr = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            log.error("Invalid Name or IP Provided", ex);
        }
    }

    /**
     * Send Command to the bed module
     * @param cmd - Command to send to the bed module
     * @return true if UDP packet was sent, otherwise false
     */
    public static boolean sendCommand(BedCommand cmd) {
        if ( bedIPAddr == null ) {
            log.error("IP Address not resolvable, thus cannot send command, please check configuration");
            return false;
        }
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(cmd.getCommand(), cmd.getCommand().length, bedIPAddr, portNum);
            datagramSocket.send(packet);
            log.info("Successfully sent command " + cmd + " to " + bedIPAddr);
            return true;
        } catch (SocketException ex) {
            log.error("Could Not Create Socket", ex);
        } catch (IOException ex) {
            log.error("Error sending UDP packet", ex);
        }
        return false;
    }

    /**
     * Simple Command line interface for testing
     * send "flat" position if wrong or no input provided
     * 
     * Example to set to Memory Position 1
     *   java -jar target/AlexaTempurpedic-1.0.jar 1
     * Set to Flat
     *   java -jar target/AlexaTempurpedic-1.0.jar
     * 
     * @param argv
     */
    public static void main(String[] argv) {
        // Command to Exceute
        BedCommand cmd;
        if ( argv.length == 1 ) {
            cmd = BedCommand.getByPosition(argv[0]);
            if ( cmd == null ) {
                cmd = BedCommand.BED_FLAT;
            }
        } else {
            cmd = BedCommand.BED_FLAT;
        }
        TempurpedicErgoPremier.sendCommand(cmd);
    }
}
