package connection;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
* ChatClientConnection manages the socket connecting the
* client to the host.
*
* @author  Felix
* 
*/
public class ChatClientConnection implements RoomConnection{
	
	private Socket clientConnection;
	private DataInputStream input;
	private DataOutputStream output;
	private int retryAmount;
	
	/**
	 * Constructor for the ChatClientConnection class.
	 * @param socket Socket to communicate with the host.
	 * @param retry Number of times to try to send a message.
	 * @throws IOException Thrown if data streams cannot be obtained.
	 */
	public ChatClientConnection(String ip,int port,  int retry) throws IOException{
		this(new Socket(ip, port), retry);
	}
	
	public ChatClientConnection(Socket socket,  int retry) throws IOException{
		clientConnection = socket;
		try {
			input = new DataInputStream(new BufferedInputStream(clientConnection.getInputStream()));
			output = new DataOutputStream(clientConnection.getOutputStream());
		}catch(IOException ioe) {
			throw ioe;
		}
		
		retryAmount = retry;
	}
	
	/**
	 * Sends a message to the host through the socket.
	 * @param message Message to send to the host.
	 * @return True if message was sent. False if it took more than retry
	 * @throws UTFDataFormatException Thrown if message is too long
	 */
	public boolean sendMessage(String message) throws UTFDataFormatException{
		
		int tries = 0;
		
		while(tries < retryAmount) {
			try {
				byte[] data = message.getBytes(StandardCharsets.UTF_8);
				output.writeInt(data.length);
				output.write(data);
				return true;
			}catch(UTFDataFormatException dfe) {
				throw dfe;
			}catch(IOException ioe) {
				tries++;
			}
		}
		
		return false;
	}
	
	/**
	 * Receives a message from the host through the socket.
	 * @return Message received from the host.
	 * @throws IOException Thrown if message could not be received.
	 */
	public List<String> receiveMessage() throws IOException {
		List<String> messages = new ArrayList<String>();
		while(input.available() != 0) {
			try {
				int length = input.readInt();
				byte[] data = new byte[length];
				input.read(data);
				messages.add(new String(data, StandardCharsets.UTF_8));
			}catch(IOException ioe) {
				throw ioe;
			}
		}
		return messages;
	}
	
	/**
	 * Close Data Streams make for client.
	 * @throws IOException Thrown if data streams cannot close.
	 */
	public void close() throws IOException {
		try {
			input.close();
			output.close();
		} catch (IOException e) {
			throw e;
		}
	}
}
