package packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import packets.*;

public class HelloPacket implements Parsable {
	private String username;
	private String password;


	public HelloPacket(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public HelloPacket() {
	
	}

	public int getId() {
		return Packets.HELLO;
	}

	@Override
	public String toString() {
		return "HelloPacket [username=" + username + ", password=" + password
				+ "]";
	}

	@Override
	public void writeToOutput(ByteArrayDataOutput out) throws IOException {
		out.writeUTF(username);
		out.writeUTF(password);
		
	}

	@Override
	public void parseFromInput(ByteArrayDataInput in) throws IOException {
		this.username = in.readUTF();
		this.password = in.readUTF();
		
	}
}
