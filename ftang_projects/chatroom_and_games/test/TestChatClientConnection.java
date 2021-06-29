package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.junit.Test;

import connection.ChatClientConnection;

public class TestChatClientConnection {

	@Test
	public void test() {
		try {
			ServerSocket hostSocket = new ServerSocket(5000);
			Socket clientSocket = new Socket("localhost", 5000);
			Socket socket = hostSocket.accept();
			
			ChatClientConnection client1 = new ChatClientConnection(clientSocket, 3);
			ChatClientConnection client2 = new ChatClientConnection(socket, 3);
			
			String message1 = ":)";
			String message2 = "Hello World";
			assertTrue(client1.sendMessage(message1));
			assertTrue(client1.sendMessage(message2));
			List<String> messages = client2.receiveMessage();
			assertEquals(messages.get(0), message1);
			assertEquals(messages.get(1), message2);
			
			client1.close();
			client2.close();
			
			clientSocket.close();
			socket.close();
			hostSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
