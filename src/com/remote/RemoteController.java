package com.remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.remote.audio.AudioCapturer;
import com.remote.mouse.MouseAnimator;

public class RemoteController {
	
	public static final int CONTROL_PORT = 3333;
	public static final int AUDIO_PORT = 3334;
	public static final int MOUSE_PORT = 3335;
	
	public static void main(String[] atgs){
		AudioCapturer audioCapturer = new AudioCapturer();
		MouseAnimator mouseAnimator = new MouseAnimator();
		
		Socket clientSocket = null;
		try {
			ServerSocket mainService = new ServerSocket(CONTROL_PORT);
			clientSocket = mainService.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String clientIP = clientSocket.getInetAddress().getCanonicalHostName();
		audioCapturer.init(clientIP);
	}

}
