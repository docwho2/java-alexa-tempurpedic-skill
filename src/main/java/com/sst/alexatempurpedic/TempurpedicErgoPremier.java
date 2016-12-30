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

/**
 * Class that implements the UDP Protocol to talk with the Tempurpedic Ergo Premier
 * Base.
 * @author sjensen
 */
public class TempurpedicErgoPremier {
    
    
    
    /*
    Convert Hex String from Wire Sharks to byte array to put into
    UDP packets.
    */
    private static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] buffer = new byte[length / 2];
        for (int i = 0 ; i < length ; i += 2) {
            buffer[i / 2] = (byte)((toByte(hexString.charAt(i)) << 4) | toByte(hexString.charAt(i+1)));
        }
        return buffer;
    }  
    
    private static int toByte(char c) {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 10);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 10);
        throw new RuntimeException ("Invalid hex char '" + c + "'");
    }
}
