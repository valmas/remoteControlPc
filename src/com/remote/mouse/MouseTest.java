package com.remote.mouse;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JFrame;


public class MouseTest extends JFrame implements KeyListener{
	
	private OutputStreamWriter out;

    public void keyPressed(KeyEvent e) {
        
    }

    public void keyReleased(KeyEvent e) {
    	try {
    		char[] arr = {'M', (char) e.getKeyCode()};
			out.write(arr);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

    }
    public void keyTyped(KeyEvent e) {
    	try {
    		char[] arr = {'K', (char) e.getKeyCode()};
			out.write(arr);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }

    public MouseTest(){
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        try {
			Socket sock = new Socket("127.0.0.1", 3335);
			out = new OutputStreamWriter(sock.getOutputStream());
			
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