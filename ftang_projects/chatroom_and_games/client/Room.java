package client;
import java.util.ArrayList;
import java.util.List;

import client.User;
import connection.RoomConnection;

public class Room {

	private List<User> users;
	private String ip;
	private int port;
	private RoomConnection roomConnection;
	private RoomManager roomManager;
	private List<String> messageLog;
	private Game[] games;
	
	public Room(String ip, int port, RoomConnection roomConnection, RoomManager rm) {
		this.ip = ip;
		this.port = port;
		users = new ArrayList<User>();
		messageLog = new ArrayList<String>();
		games = new Game[4];
		this.roomConnection = roomConnection;
		this.roomManager = rm;
	}
	
	public String getIP() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
	
	public RoomConnection getRoomConnection() {
		return roomConnection;
	}
	
	public void addUser(User u) {
		users.add(u);
	}
	
	public void removeUser(String name) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getName().equals(name)) {
				users.remove(i);
				break;
			}
		}
	}
	
	public void startGame(int gameType) {
		if(games[gameType] == null) {
			switch(gameType) {
				case 0:
					games[gameType] = new TicTacToe(gameType, roomManager, this);
					break;
				case 1:
					games[gameType] = new Chess(gameType, roomManager, this);
					break;
				case 2:
					games[gameType] = new checkers(gameType, roomManager, this);
					break;
			}
		}
	}
	
	public Game getGame(int gameType) {
		return games[gameType];
	}
	
	public User[] getUsers(){
		User[] u = new User[users.size()];
		return users.toArray(u);
	}
	
	public void addMessage(String message) {
		messageLog.add(message);
	}
	
	public String[] getMessageLog() {
		String[] m = new String[messageLog.size()];
		return messageLog.toArray(m);
	}
}
