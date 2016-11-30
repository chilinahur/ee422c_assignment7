package assignment7;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.event.*;

public class ChatClient {
	private JTextArea incoming;
	private JTextField outgoing;
	private JTextField userNameInput;
	private JTextField passwordInput;
	private BufferedReader reader;
	private PrintWriter writer;
	private String username;
	private String password;
	private JFrame frame;
	private static JComboBox<String> cb = new JComboBox<String>();
	
	public String getUsername(){
		return username;
	}

	public void run() throws Exception {
		setUpNetworking();
		userNameView();
	}

	
	private void userNameView() {
		frame = new JFrame("Enter a username");
		JPanel mainPanel = new JPanel();

		userNameInput = new JTextField(20);
		passwordInput = new JTextField(20);
		JButton registerButton = new JButton("Register");
		JButton loginButton = new JButton("Login");

		mainPanel.add(userNameInput);
		mainPanel.add(passwordInput);
		mainPanel.add(registerButton);
		mainPanel.add(loginButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(500, 250);
		frame.setVisible(true);
	}
	
	private void initView() {
		JFrame frame = new JFrame("Yousef and Sri's Chat Engine");
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		mainPanel.add(cb);
		
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(650, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		});
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		
		System.out.println("networking established");
		/*
		Set<String> users = ChatServer.updateUsers(getDroplist());
		if(users != null){
			for(String s: users){
					cb.addItem(s);
			}
		}
		*/
		Thread readerThread = new Thread(new IncomingReader());		
		readerThread.start();
	}

	public static void main(String[] args) {
		try {
			new ChatClient().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
