package test;

import static org.junit.Assert.*;


import org.junit.Test;
import client.RoomManager;

public class TestRoomManager {

	@Test
	public void test() {
		RoomManager rm1 = new RoomManager("rm1");
		RoomManager rm2 = new RoomManager("rm2");
		
		rm1.hostRoom(5000);
		rm2.joinRoom("localhost", 5000);
		rm1.acceptNewConnections(0);
		
		rm1.sendMessage("chat,Hello World", 0, false);
		rm1.sendMessage("chat,:)", 0, false);
		rm2.sendMessage("chat,Hello World", 0, false);
		
		rm1.receiveMessage(0);
		rm2.receiveMessage(0);
		
		String[] rm1ChatLog = rm1.getMessageLog(0);
		String[] rm2ChatLog = rm2.getMessageLog(0);
		assertEquals(rm2ChatLog[0], "rm1,userList,rm1");
		for(int i = 0; i < rm1ChatLog.length; i++) {
			assertEquals(rm1ChatLog[i], rm2ChatLog[i+1]);
		}
	
		RoomManager rm3 = new RoomManager("rm3");
		rm3.joinRoom("localhost", 5000);
		rm1.acceptNewConnections(0);
		
		rm1.receiveMessage(0);
		rm2.receiveMessage(0);
		rm3.receiveMessage(0);
		
		assertArrayEquals(rm1.getUsers(0), rm2.getUsers(0));
		assertEquals(rm1.getUsers(0).length, 3);
		
		rm2.leaveRoom(0);
		rm1.receiveMessage(0);
		rm3.receiveMessage(0);
		
		assertArrayEquals(rm1.getUsers(0), rm3.getUsers(0));
		
		rm1.leaveRoom(0);
		rm3.receiveMessage(0);
		
		assertEquals(rm3.getNumberOfRooms(), 0);
	}

}
