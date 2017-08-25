package com.remote.mouse;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.remote.RemoteController;

public class MouseAnimator implements Runnable {
	
	private Robot bot;
	
	private Socket inputSocket;
	private ServerSocket mouseService;
	private BufferedReader in;
	
	private static boolean stop;
	
	public void init(){
		try {
			bot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		try {
			mouseService = new ServerSocket(RemoteController.MOUSE_PORT);
			inputSocket = mouseService.accept();
			in = new BufferedReader(new InputStreamReader(inputSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread t = new Thread(this);
		t.start();
	}
	
	public void click() throws AWTException{
	    bot.mousePress(InputEvent.BUTTON1_MASK);
	    bot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	public void move(int x, int y) throws AWTException{
		Point p = MouseInfo.getPointerInfo().getLocation();
	    bot.mouseMove(p.x+x, p.y+y);    
	}
	
	public void keyTyped(int keycode){
		bot.keyPress(keycode);
		bot.keyRelease(keycode);
	}

	@Override
	public void run() {
		System.out.println("start moving");

		try {
			while (!stop) {
				String line = in.readLine();
				if(line.contains(",")){
					int movex = Integer.parseInt(line.split(",")[0]);//extract movement in x direction
					int movey = Integer.parseInt(line.split(",")[1]);//extract movement in y direction
					move(movex, movey);
				} else {
					keyTyped(Integer.parseInt(line));
				}
			}
		} catch (IOException | AWTException e) {
			e.printStackTrace();
		}
		
	}
	
	public void mousetest(int c) throws AWTException{
		if(c == KeyEvent.VK_RIGHT) {
			move(10,0);
		} else if(c == KeyEvent.VK_LEFT) {
			move(-10,0);
		} else if(c == KeyEvent.VK_UP) {
			move(0,-10);
		} else if(c == KeyEvent.VK_DOWN) {
			move(0,10);
		}
	}
	
	public static void main(String[] args){
		MouseAnimator ma = new MouseAnimator();
		ma.init();
	}
}
