package client;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.List;

import client.Room;
import connection.ChatClientConnection;
import connection.HostCreation;

public class RoomManager {

	private List<Room> rooms;
	private String username;
	
	public RoomManager(String user) {
		username = user;
		rooms = new ArrayList<Room>();
	}
	
	public void hostRoom(int port) {
		HostCreation hostCreation = null;
		try {
			hostCreation = new HostCreation(port, 3);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Room room = new Room("localhost", port, hostCreation, this);
		room.addUser(new User(username, "localhost"));
		
		rooms.add(room);
	}
	
	public void joinRoom(String ip, int port) {
		ChatClientConnection chatClientConnection = null;
		try {
			chatClientConnection = new ChatClientConnection(ip, port, 3);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Room room = new Room(ip, port, chatClientConnection, this);
		
		
		rooms.add(room);
		
		sendMessage("newUser,", rooms.size()-1, false);
	}
	
	public void sendMessage(String message, int roomNumber, boolean hostDistribute) {
		Room room = rooms.get(roomNumber);
		sendMessage(message, room, hostDistribute);
	}
	
	public void sendMessage(String message, Room room, boolean hostDistribute) {
		try {
			if(!hostDistribute) {
				message = username + "," + message;
			}
			room.getRoomConnection().sendMessage( message);
			User[] users = room.getUsers();
			if(users.length > 0 && users[0].getIP() == "localhost" && !hostDistribute) {
				room.addMessage(message);
				String[] breakdown = message.split(",", 3);
				if(breakdown[1].equals("tictactoe")) {
					room.getGame(0).receiveMove(breakdown[2]);
				}else if(breakdown[1].equals("chess")) {
					room.getGame(1).receiveMove(breakdown[2]);
				}else if(breakdown[1].equals("checkers")) {
					room.getGame(2).receiveMove(breakdown[2]);
				}
			}
		} catch (UTFDataFormatException e) {
			e.printStackTrace();
		}
	}
	
	public boolean receiveMessage(int roomNumber) {
		Room room = rooms.get(roomNumber);
		List<String> messages = null;
		boolean isHost = false;
		User[] users = room.getUsers();
		if(users.length > 0 && users[0].getIP() == "localhost") {
			isHost = true;
		}
		try {
			messages = room.getRoomConnection().receiveMessage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String message : messages) {
			String[] breakdown = message.split(",", 3);
			if(breakdown[1].equals("newUser")) {
				room.addUser(new User(breakdown[0], "IP?"));
			}else if(breakdown[1].equals("userList")) {
				String[] userList = breakdown[2].split(",");
				for(String user : userList) {
					room.addUser(new User(user, "IP?"));
				}
			}else if(breakdown[1].equals("tictactoe")) {
				room.getGame(0).receiveMove(breakdown[2]);
			}else if(breakdown[1].equals("chess")) {
					room.getGame(1).receiveMove(breakdown[2]);
			}else if(breakdown[1].equals("checkers")) {
				room.getGame(2).receiveMove(breakdown[2]);
			}else if(breakdown[1].equals("leaveRoom")) {
				room.removeUser(breakdown[0]);
			}else if(breakdown[1].equals("closeRoom")) {
				try {
					room.getRoomConnection().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				rooms.remove(room);
				return true;
			}
			room.addMessage(message);
			if(isHost) {
				sendMessage(message, roomNumber, true);
			}
		}
		return false;
	}
	
	public void acceptNewConnections(int roomNumber) {
		Room room = rooms.get(roomNumber);
		User[] users = room.getUsers();
		if(users.length > 0 && users[0].getIP() == "localhost") {
			String userList = username + ",userList";
			for(int i = 0; i < users.length; i++) {
				userList += "," + users[i].getName();
			}
			try {
				((HostCreation) room.getRoomConnection()).acceptNewConnection(userList);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public String[] getMessageLog(int roomNumber) {
		return rooms.get(roomNumber).getMessageLog();	
	}
	
	public String[] getUsers(int roomNumber) {
		Room room = rooms.get(roomNumber);
		User[] users = room.getUsers();
		String[] usernames = new String[users.length];
		for(int i = 0; i < users.length; i++) {
			usernames[i] = users[i].getName();
		}
		return usernames;
	}
	
	public void leaveRoom(int roomNumber) {
		Room room = rooms.get(roomNumber);
		User[] users = room.getUsers();
		
		if(users.length > 0 && users[0].getIP() == "localhost") {
			
			sendMessage("closeRoom", roomNumber, false);
			
			try {
				room.getRoomConnection().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			rooms.remove(room);
			
		}else {
			
			sendMessage("leaveRoom", roomNumber, false);
			
			try {
				room.getRoomConnection().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			rooms.remove(room);
		}
	}
	
	public int getNumberOfRooms() {
		return rooms.size();
	}
	
	public String getRoomID(int roomNumber) {
		Room room = rooms.get(roomNumber);
		return room.getIP() + " " + room.getPort();
	}

	public void startGame(int roomNumber, int gameType) {
		Room room = rooms.get(roomNumber);
		room.startGame(gameType);
	}
	
	public Game getGame(int roomNumber, int gameType) {
		
		return rooms.get(roomNumber).getGame(gameType);
	}
}
