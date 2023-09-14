package academicLeague;

import marytts.LocalMaryInterface;
import marytts.util.data.audio.AudioPlayer;

import javax.sound.sampled.*;

public class Speak {
    public Speak(String sentance, String voice) {
        try {
            // Initialize MaryTTS and generate audio
            LocalMaryInterface mary = new LocalMaryInterface();
            AudioInputStream audio = mary.generateAudio(sentance);

            AudioPlayer tts = new AudioPlayer(audio);
            tts.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
