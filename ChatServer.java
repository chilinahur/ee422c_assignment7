package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class ChatServer extends Observable {
	
	private static HashMap<String, String> users = new HashMap<String, String>();
	private static HashMap<String, ClientObserver> observers = new HashMap<String, ClientObserver>();
	private static HashMap<String, Observable> observables = new HashMap<String, Observable>();
	private static Queue<ClientObserver> qobservers = new ArrayDeque<ClientObserver>();
	private static Set<String> online = new HashSet<String>();
	
	public static void addUser(String username, String password){
		
		users.put(username, password);
		
	}
	
	public static void addOnline(String username){
		
		online.add(username);
		
	}
	
	public static boolean checkUser(String username){
		
		if(users.containsKey(username)) return true;
		
		return false;
		
	}
	
	public static boolean checkPass(String username, String password){
		
		if((users.get(username)).equals(password)) return true;
		
		return false;
		
	}
	
	public static Set<String> getUsers(){
		
		return users.keySet();
		
	}
	
	public static Set<String> getOnline(){
		
		return online;
		
	}
	
	public static Set<String> updateUsers(Set<String> exist){
		
		Set<String> users = getUsers();
		Set<String> userz = new HashSet<String>();
		for(String s: online){
			if(!exist.contains(s)){
				userz.add(s);
			}
		}
		
		return userz;
		
	}
	
	public static void main(String[] args) {
		try {
			new ChatServer().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			
			qobservers.add(writer);
			
			System.out.println("got a connection");
		}
	}
	class ClientHandler implements Runnable {
		private BufferedReader reader;
		//private ClientObserver writer;
		public ClientHandler(Socket clientSocket) {
			Socket sock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				//writer = new ClientObserver(clientSocket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
											
						System.out.println("server read " + message);
						setChanged();
						//notifyObservers(message);
						(observables.get(names)).notifyObservers(message);
					}
					scanner.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
