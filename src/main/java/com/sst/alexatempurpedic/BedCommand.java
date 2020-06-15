/*
 * Copyright 2017 sjensen.
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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Steve Jensen <docwho2@gmail.com>
 */
public enum BedCommand {

    /**
     * Flat Position
     */
    BED_FLAT("flat", "The Flat Position", "3305320A945C0400CC"),

    /**
     * Memory Position #1 - #4
     */
    BED_MEM_1("1", "Memory Position 1", "33053203945C0000C8"),
    BED_MEM_2("2", "Memory Position 2", "33053203945C0100C9"),
    BED_MEM_3("3", "Memory Position 3", "33053203945c0100CA"),
    BED_MEM_4("4", "Memory Position 4", "33053203945c0100CB"),

    
    /**
     * All Vibration Off
     */
    BED_VIBE_OFF("off","Vibration Off", "3305320A9486000012"),
    
    
    /**
     * Vibration Setting #1 - #4
     */
    BED_VIBE_1("v1", "Vibration Setting 1", "33053203948D007861"),
    BED_VIBE_2("v2", "Vibration Setting 2", "33053203948D017860"),
    BED_VIBE_3("v3", "Vibration Setting 3", "33053203948D027863"),
    BED_VIBE_4("v4", "Vibration Setting 4", "33053203948D037862");
    
    // The bed position in short form which is in LIST_OF_POSITIONS 
    private final String position;
    // Verbal response sent back after the command is successful
    private final String response;
    // The binary byte buffer that will be sent on the network to the base
    private final byte[] command;
    // Map that is key'd by position
    private final static Map<String,BedCommand> cmds;

    static {
        cmds = new HashMap<>();
        for( BedCommand cmd : BedCommand.values() ) {
            cmds.put(cmd.getPosition(), cmd);
        }
    }
    
    private BedCommand(String position, String response, String hexCommand) {
        this.position = position;
        this.response = response;
        // Retain as byte array insteaod of converting on the fly
        this.command = hexStringToByteArray(hexCommand);
    }

    /**
     *
     * @return position which is the short name that Alexa will send in for the intent
     */
    public String getPosition() {
        return (position);
    }

    /**
     *
     * @return response that will be incorporated back to the requester
     */
    public String getReponse() {
        return (response);
    }

    /**
     *
     * @return bytes to send to the Wi-Fi control module
     */
    public byte[] getCommand() {
        return (command);
    }

    /**
     *
     * @param position which is short name used by Alexa intent
     * @return BedCommand or null if the command is unknown
     */
    public static BedCommand getByPosition(String position) {
        return( cmds.get(position) );
    }
    
    
    /**
     * Convert Hex String from Wire Sharks to byte array to put into UDP packets.
     * @param hexString that was captured via packet capture tool
     * @return byte array representation of the command
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] buffer = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            buffer[i / 2] = (byte) ((toByte(hexString.charAt(i)) << 4) | toByte(hexString.charAt(i + 1)));
        }
        return buffer;
    }

    /**
     * Char to Byte conversion
     * @param c char to convert to byte
     * @return byte representation of the char
     */
    private static int toByte(char c) {
        if (c >= '0' && c <= '9') {
            return (c - '0');
        }
        if (c >= 'A' && c <= 'F') {
            return (c - 'A' + 10);
        }
        if (c >= 'a' && c <= 'f') {
            return (c - 'a' + 10);
        }
        throw new RuntimeException("Invalid hex char '" + c + "'");
    }

}
