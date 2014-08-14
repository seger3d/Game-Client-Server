package packets.server;

import java.io.IOException;

import packets.ByteArrayDataInput;
import packets.ByteArrayDataOutput;
import packets.Packets;
import packets.Parsable;

public class PingPacket implements Parsable {
	public int time; // int

	public void parseFromInput(ByteArrayDataInput badi) throws IOException {
		this.time = badi.readInt();
	}

	public void writeToOutput(ByteArrayDataOutput bado) throws IOException {
		bado.writeInt(time); // Int
	}

	public int getId() {
		return Packets.PING;
	}

	public String toString() {
		return "PING [" + time + "]";
	}
}
