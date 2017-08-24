package com.remote.audio;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import com.remote.RemoteController;

public class AudioCapturer implements Runnable {

	private static boolean stop;
	
	private AudioFormat format;
	private BufferedOutputStream out;
	private Socket outputSocket;
	private ServerSocket audioService;
	
	public void init(String IP) {
		float sampleRate = 44100.0F;
	    int sampleSizeInBits = 16;
	    int channels = 2;
	    boolean signed = true;
	    boolean bigEndian = true;
	    format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	    
		try {
			audioService = new ServerSocket(RemoteController.AUDIO_PORT);
			outputSocket = audioService.accept();
			out = new BufferedOutputStream(outputSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread t = new Thread(this);
		t.start();
	}

	private void capture() {
		
		System.out.println("Capture started");
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		DataLine.Info dataLineInfo = new DataLine.Info(
                TargetDataLine.class, format);
        Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);
        TargetDataLine line = null;
		try {
			line = (TargetDataLine) mixer.getLine(dataLineInfo);
			line.open(format);
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		}
		
		int numBytesRead;
		byte[] data = new byte[line.getBufferSize() / 5];

		// Begin audio capture.
		line.start();

		try {
			// Here, stopped is a global boolean set by another thread.
			while (!stop) {
				// Read the next chunk of data from the TargetDataLine.
				numBytesRead = line.read(data, 0, data.length);
				// Save this chunk of data.
				out.write(data, 0, numBytesRead);
				System.out.println(numBytesRead);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("Capture ended");
				out.close();
				line.stop();
		        line.close();
		        outputSocket.close();
		        audioService.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopCapturing(){
		stop = true;
	}

	@Override
	public void run() {
		capture();
		
	}
	
}
