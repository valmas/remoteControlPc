package com.remote.mouse;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

// http://codesmith.in/control-pc-from-android-app-using-java/
// https://stackoverflow.com/questions/5143417/streaming-audio-from-a-socket-on-android-using-audiotrack

public class MouseTest extends JFrame implements KeyListener {
	
	private PrintWriter out;

    public void keyPressed(KeyEvent e) {
        
    }

    public void keyReleased(KeyEvent e) {
    		char[] arr = {'M', (char) e.getKeyCode()};
			out.write(arr);
			out.flush();

    }
    public void keyTyped(KeyEvent e) {
    		char[] arr = {'K', (char) e.getKeyCode()};
			out.write(arr);
			out.flush();
    }

    public MouseTest(){
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        try {
			Socket sock = new Socket("127.0.0.1", 3335);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock
					.getOutputStream())), true);
			while(true) {
			//out.println(KeyEvent.VK_DOWN);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MouseTest frame = new MouseTest();
                frame.setTitle("Square Move Practice");
                frame.setResizable(false);
                frame.setSize(600, 600);
                frame.setMinimumSize(new Dimension(600, 600));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}