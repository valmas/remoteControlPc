package com.remote.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioCapturerOld {

	private static boolean stop;

	private static AudioFormat format;
	
	static File wavFile = new File("C:/Users/valmas/Desktop/RecordAudio.wav");
	
	static TargetDataLine line;
	 
    // format of audio file
	static AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	public static void main(String args[]) throws LineUnavailableException, UnknownHostException, IOException {
		float sampleRate = 44100.0F;
	    int sampleSizeInBits = 16;
	    int channels = 2;
	    boolean signed = true;
	    boolean bigEndian = true;
	    format =  new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	    capture();

	}

	private static void capture() throws UnknownHostException, IOException {
		Socket sock = new Socket("127.0.0.1", 4000);
		BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
		
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try {
			SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
			byte buffer[] = new byte[bufferSize];

			int count;
			while ((count = in.read(buffer, 0, buffer.length)) != -1) {
				if (count > 0) {
					line.write(buffer, 0, count);
				}
			}
			line.drain();
			line.close();
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
	}

}
