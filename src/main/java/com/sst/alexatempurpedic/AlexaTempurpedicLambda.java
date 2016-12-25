/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sst.alexatempurpedic;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author sjensen
 */
public class AlexaTempurpedicLambda extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add("amzn1.ask.skill.e8913c1c-5cb5-4774-8bff-e567eba4c65f");
    }
    
    public AlexaTempurpedicLambda() {
        super(new MySpeechlet(), supportedApplicationIds);
    }
    
}
