package assignment7;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class ChatClient {
	private JTextArea incoming;
	private JTextField outgoing;
	private JTextField userNameInput;
	private JTextField passwordInput;
	private JTextField ipin;
	private BufferedReader reader;
	private PrintWriter writer;
	private String username;
	private String password;
	private JFrame frame;
	private String zzz;
	private Set<String> cw = new HashSet<String>();
	private String ip;
	private JComboBox<String> cb = new JComboBox<String>();
	private DefaultListModel<String> model = new DefaultListModel<>();
	private JList<String> list = new JList<>(model);
	
	public String getUsername(){
		return username;
	}

	public Set<String> getDroplist(){
		
		Set<String> set = new HashSet<String>();
		
		for(int i = 0; i < cb.getItemCount(); i++){
			set.add(cb.getItemAt(i));
		}
		
		return set;
	}
	
	public void run() throws Exception {
		ipView();
	}

	
	private void userNameView() {
		frame = new JFrame("Enter a username/password");
		JPanel mainPanel = new JPanel();

		userNameInput = new JTextField(20);
		passwordInput = new JTextField(20);
		JButton registerButton = new JButton("Register");
		JButton loginButton = new JButton("Login");
		frame.getRootPane().setDefaultButton(loginButton);
		registerButton.addActionListener(new RegisterButtonListener());
		loginButton.addActionListener(new LoginButtonListener());

		mainPanel.add(userNameInput);
		mainPanel.add(passwordInput);
		mainPanel.add(registerButton);
		mainPanel.add(loginButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(500, 125);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private void ipView() {
		frame = new JFrame("Enter server IP address");
		JPanel mainPanel = new JPanel();

		
		ipin = new JTextField(20);
		JButton ipButton = new JButton("Select");
		frame.getRootPane().setDefaultButton(ipButton);
		ipButton.addActionListener(new ipButtonListener());

		mainPanel.add(ipin);
		mainPanel.add(ipButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(500, 125);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private void chooseView() {
		JFrame frame = new JFrame("Control Panel");
		JPanel mainPanel = new JPanel();

		mainPanel.add(list);
		
		JButton chatButton = new JButton("Chat");
		frame.getRootPane().setDefaultButton(chatButton);
		chatButton.addActionListener(new ChatButtonListener());

		mainPanel.add(chatButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(650, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
				writer.println("ONLINEREMOVE " + username);
				writer.flush();
				//username = null;
				//e.getWindow().dispose();
		    }
		});
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
		frame.getRootPane().setDefaultButton(sendButton);
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(650, 450);
		frame.setVisible(true);
		/*
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
				writer.println("ONLINEREMOVE " + username);
				writer.flush();
				//username = null;
				//e.getWindow().dispose();
		    }
		});
		*/
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket(ip, 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		
		System.out.println("networking established");

		Thread readerThread = new Thread(new IncomingReader());		
		readerThread.start();
	}

	class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			//System.out.print("HAIIAA");
			writer.println(zzz + "|||" + " " + username + ": " + outgoing.getText());
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}
	
	
	class ipButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			//System.out.print("HAIIAA");
			boolean flag = false;
			ip = ipin.getText();
			try{
			setUpNetworking();
			}
			catch(Exception e){
			flag = true;
			}
			if(!flag){
				frame.dispose();
				userNameView();
			}
		}
	}
	
	class ChatButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			String s = "CHATWITH " + username + " ";
			int[] selected = list.getSelectedIndices();
			for(int i = 0; i < selected.length; i++){
				s = s.concat(model.getElementAt(selected[i]) + " ");
			}
			writer.println(s);
			writer.flush();
			
		}
	}
	
	class RegisterButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			username = userNameInput.getText();
			//if(!ChatServer.checkUser(username)){
			password = passwordInput.getText();
				
			writer.println("USERSADD " + username + " " + password);
			writer.flush();
				//ChatServer.addUser(username, password);
			//}
		}
	}
	
	class LoginButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			username = userNameInput.getText();
			//if(ChatServer.checkUser(username) && !ChatServer.getOnline().contains(username)){
			password = passwordInput.getText();
				//if(ChatServer.checkPass(username, password)){
			writer.flush();
			writer.println("ONLINEADD " + username + " " + password);
			writer.flush();
					//ChatServer.addOnline(username);

				//}
			//}
		}
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
				while ((message = reader.readLine()) != null) {
					if(message.equals("true " + username)){
						frame.dispose();
						chooseView();
						//initView();
					}
					else if(message.startsWith("true ")){
						cb.addItem(message.substring(5));
						model.addElement(message.substring(5));
					}
					else if(message.startsWith("removed ")){
						cb.removeItem(message.substring(8));
						model.removeElement(message.substring(8));
					}
					else if(message.startsWith("fixadd ")){
						if(!message.equals("fixadd " + username)){
							if(!getDroplist().contains(message.substring(7))){
								cb.addItem(message.substring(7));
								model.addElement(message.substring(7));
							}
						}
					}
					else if(message.startsWith("BEGINCHAT ")){
						
						//Set<String> others = new HashSet<String>();
						Scanner scanner = new Scanner(message);
						boolean bool = false;
						while(scanner.hasNext()){
							String s = scanner.next();
							if(s.equals(username)){
								initView();
								bool = true;
							}
							/*else{
								others.add(s);
							}*/
						}
						if(bool){
							cw.add(message.substring(10));
							zzz = message.substring(10);
						}
						scanner.close();
					}
					else{
						Scanner scanner = new Scanner(message);
						//scanner.next();
						String temp;
						Boolean bool = false;
						
						while(scanner.hasNext()){
							temp = scanner.next();
							if(temp.equals(username)) bool = true;
							if(temp.equals("|||")) break;						
						}
						
						if(bool){
						
							String send = "";
							
							while(scanner.hasNext()){
								send += scanner.next() + " ";
							}
							
							System.out.println("server read " + message);
							
							try{
								incoming.append(send + "\n");
							} catch(Exception e){
								
							}
							
							scanner.close();
							//incoming.append(message + "\n");
						}
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
