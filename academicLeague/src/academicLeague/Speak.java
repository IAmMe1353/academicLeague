package academicLeague;

import marytts.LocalMaryInterface;

import marytts.util.data.audio.AudioPlayer;

import javax.sound.sampled.*;

public class Speak {
	// this.clip.stop() stops audio from playing
	public Clip clip;

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

	public Speak(String sentance) {
		this(sentance, null);
	}

	public Speak() {

	}

	public void speak(String sentance) {
		try {
			clip = AudioSystem.getClip();
			// Initialize MaryTTS and generate audio
			LocalMaryInterface mary = new LocalMaryInterface();
			AudioInputStream audio = mary.generateAudio(sentance);
			clip.open(audio);
			clip.start();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}