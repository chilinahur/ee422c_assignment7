package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;
import java.util.Set;

public class ChatServer extends Observable {
	
	private static HashMap<String, String> users = new HashMap<String, String>();
	private static Set<String> online = new HashSet<String>();
	private static ArrayList<HashSet<String>> convos = new ArrayList<HashSet<String>>();
	
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
		
		//Set<String> users = getUsers();
		Set<String> userz = new HashSet<String>();
		for(String s: online){
			if(!exist.contains(s)){
				userz.add(s);
			}
		}
		
		return userz;
		
	}
	
	public static boolean checkConvos(HashSet<String> list){
		
		for(HashSet<String> set: convos){
			boolean bool = true;
			if(set.size() != list.size()) bool = false;
			for(String s: list){
				if(!set.contains(s)) bool = false;
			}
			if(bool) return true;
		}
		
		return false;
		
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
			addObserver(writer);
			
			for(String a : online){
				setChanged();
				notifyObservers("fixadd " + a);
			}
			
			
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
					Scanner scanner = new Scanner(message);
					String code = scanner.next();
					
					if(code.equals("ONLINEADD")){
						if(scanner.hasNext()){
							String username = scanner.next();
							if(scanner.hasNext()){
								String password = scanner.next();
								if(checkUser(username) && !online.contains(username) && checkPass(username, password)){
									System.out.println(username + " logged in");
									online.add(username);
									setChanged();
									notifyObservers("true " + username);
								}
							}
						}
					}
					else if(code.equals("ONLINEREMOVE")){
						if(scanner.hasNext()){
							String username = scanner.next();
							System.out.println(username + " logged out");
							online.remove(username);
							setChanged();
							notifyObservers("removed " + username);
						}
					}
					else if(code.equals("USERSADD")){
						if(scanner.hasNext()){
							String username = scanner.next();
							if(scanner.hasNext()){
								String password = scanner.next();
								if(!checkUser(username)){
								users.put(username, password);
								}
							}
						}
					}
					else if(code.equals("CHATWITH")){
						String x = "";
						HashSet<String> usernames= new HashSet<String>();
						while(scanner.hasNext()){
							String temp = scanner.next();
							x += temp + " ";
							usernames.add(temp);
						}
						if(!checkConvos(usernames)){
							convos.add(usernames);
							setChanged();
							notifyObservers("BEGINCHAT " + x);
						}
					}
					else{
						/*String temp;
						String names = "";
						while(scanner.hasNext()){
							temp = scanner.next();
							if(temp.equals("|||")) break;
							else{
								names += temp;
							}							
						}*/
					System.out.println("server read " + message);
					setChanged();
					notifyObservers(message);
					}
					scanner.close();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
}
