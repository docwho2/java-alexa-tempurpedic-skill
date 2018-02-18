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

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import org.apache.log4j.Logger;

/**
 *
 * @author @author Steve Jensen <docwho2@gmail.com>
 */
public class AlexaSpeechlet implements SpeechletV2 {

    private static final Logger log = Logger.getLogger(AlexaSpeechlet.class);

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> reqEnv) {
        return (getWelcomeResponse());
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> reqEnv) {
        log.info("Session Started with ID " + reqEnv.getSession().getSessionId());
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> reqEnv) {
        log.info("Session Ended with ID " + reqEnv.getSession().getSessionId());
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> reqEnv) {

        Intent intent = reqEnv.getRequest().getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if (null == intentName) {
            return getHelpResponse();
        } else switch (intentName) {
            case "GotoPosition":
                String position = intent.getSlot("Position").getValue();
                log.debug("User askied for position [" + position + "]");
                BedCommand cmd = BedCommand.getByPosition(position);
                if (cmd == null) {
                    // Error Response TODO: send back help response to indicate we didn't get the position)
                    getHelpResponse();
                } else {
                    // Send the Command
                    if (TempurpedicErgoPremier.sendCommand(cmd)) {
                        // success
                        return getOKResponse(cmd);
                    } else {
                        // failure TODO:  response with error message
                        getHelpResponse();
                    }
                }   break;
            case "Vibration":
                String vibration = intent.getSlot("Position").getValue();
                log.debug("User askied for vibration [" + vibration + "]");
                BedCommand vibe = vibration.equalsIgnoreCase("off") ? BedCommand.getByPosition(vibration) : BedCommand.getByPosition("v" + vibration);
                if (vibe == null) {
                    // Error Response TODO: send back help response to indicate we didn't get the position)
                    getHelpResponse();
                } else {
                    // Send the Command
                    if (TempurpedicErgoPremier.sendCommand(vibe)) {
                        // success
                        return getOKResponse(vibe);
                    } else {
                        // failure TODO:  response with error message
                        getHelpResponse();
                    }
                }
                break;
            case "AMAZON.HelpIntent":
                return getHelpResponse();
            default:
                return getHelpResponse();
        }
        // Never Hit
        return(null);
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Welcome to the Tempurpedic Ergo Premier Bed Skill, what is your wish?";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Welcome");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getOKResponse(BedCommand cmd) {
        String speechText = "The Bed has Executed your wish and will now go to " + cmd.getReponse();

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Postion OK");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "You can say goto position flat or you can say goto memory position 1";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Help");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}
