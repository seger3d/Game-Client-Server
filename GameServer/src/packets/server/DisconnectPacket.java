package packets.server;

import java.io.IOException;

import packets.ByteArrayDataInput;
import packets.ByteArrayDataOutput;
import packets.Packets;
import packets.Parsable;

public class DisconnectPacket implements Parsable{
	private String username;

	@Override
	public int getId() {
		return Packets.DISCONNECT;
	}

	@Override
	public String toString() {
		return "DisconnectPacket [username=" + username + "]";
	}

	@Override
	public void writeToOutput(ByteArrayDataOutput out) throws IOException {
		out.writeUTF(username);
		
	}

	@Override
	public void parseFromInput(ByteArrayDataInput in) throws IOException {
		username = in.readUTF();
	}
	
}
