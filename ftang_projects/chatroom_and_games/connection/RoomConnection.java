package connection;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.List;
import java.util.Map;

public interface RoomConnection {
	public abstract boolean sendMessage(String message) throws UTFDataFormatException;
	public abstract List<String> receiveMessage() throws IOException;
	public abstract void close() throws IOException;
}
