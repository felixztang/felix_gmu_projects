package client;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {

	private int gameType; // Integer for what game is being played
	private List<User> players; // List of players
	private int turn; // Index of the user whos turn it is
	private int winner; // Index of the user who won
	private RoomManager rm;
	private Room room;
	
	public Game(int gameType, RoomManager rm, Room room) {
		this.gameType = gameType;
		players = new ArrayList<User>();
		this.rm = rm;
		this.room = room;
	}
	
	public int getGameType() {
		return gameType;
	}
	
	public User[] getPlayers() {
		User[] playerArray = new User[players.size()];
		playerArray = players.toArray(playerArray);
		return playerArray;
	}
	
	public void addPlayer(User player) {
		players.add(player);
	}
	
	public User getWhosTurn() {
		return players.get(turn);
	}
	
	public void setWhosTurn(User player) {
		turn = players.indexOf(player);
	}
	
	public User getWinner() {
		return players.get(winner);
	}
	
	public void setWinner(User player) {
		winner = players.indexOf(player);
	}
	
	public void sendMove(String move) {
		rm.sendMessage(move, room, false);
	}
	
	public abstract void receiveMove(String move);
}
