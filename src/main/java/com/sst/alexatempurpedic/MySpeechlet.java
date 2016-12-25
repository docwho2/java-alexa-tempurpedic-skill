/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sst.alexatempurpedic;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 *
 * @author sjensen
 */
public class MySpeechlet implements Speechlet {
    private static final Logger log = Logger.getLogger(MySpeechlet.class);
    
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}" + request.getRequestId() + session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("GotoPosition".equals(intentName)) {
            String position = intent.getSlot("Position").getValue();
            log.debug("User askied for position [" + position + "]");
            if ( "flat".equalsIgnoreCase(position) ) {
                goBed("3305320A945C0400CC");
            } else if ( "one".equalsIgnoreCase(position) || "1".equals(position)) {
                goBed("33053203945C0000C8");
            } else if ( "two".equalsIgnoreCase(position) || "2".equals(position)) {
                goBed("33053203945C0100C9");
            }
            return getOKResponse(position);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    
    private void goBed(String hex) {
        try {
            byte[] flatCmd = hexStringToByteArray(hex);
            InetAddress bedIP = InetAddress.getByName("10.0.0.35");
            DatagramSocket datagramSocket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(flatCmd, flatCmd.length, bedIP, 50007);
            datagramSocket.send(packet);
        } catch (UnknownHostException | SocketException ex) {
            log.error("Error",ex);
        } catch (IOException ex) {
            log.error("Error",ex);
        }
    }
    
    public static byte[] hexStringToByteArray(String hexString)
    {
        int length = hexString.length();
        byte[] buffer = new byte[length / 2];
        for (int i = 0 ; i < length ; i += 2)
        {
            buffer[i / 2] = (byte)((toByte(hexString.charAt(i)) << 4) | toByte(hexString.charAt(i+1)));
        }
        
        return buffer;
    }  
    private static int toByte(char c)
    {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 10);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 10);
        throw new RuntimeException ("Invalid hex char '" + c + "'");
    }
    
    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}" + request.getRequestId() + session.getSessionId());
        return getWelcomeResponse();
    }
    
    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}" + request.getRequestId() + session.getSessionId());
        // any cleanup logic goes here
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
        card.setTitle("Dude");
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
    private SpeechletResponse getOKResponse(String position ) {
        String speechText = "The Bed has Executed your wish and will now go ";

        if ( "flat".equalsIgnoreCase(position) ) {
            speechText += "flat";
        } else if ( "1".equals(position) ) {
            speechText += " to memory position 1";
        } else if ( "2".equals(position) ) {
            speechText += " to memory position 2";
        }
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("DudeSweet");
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
        card.setTitle("DudeSweet");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
    
    public static void main(String[] argc) {
        
    }
}
