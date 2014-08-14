// GOOD Name: PONG, Original AS3 class: _-kj.as

package packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import packets.*;

public class PongPacket implements Parsable {
	public int time; // int

	public void parseFromInput(ByteArrayDataInput badi) throws IOException {
		this.time = badi.readInt(); // Int
	}

	public void writeToOutput(ByteArrayDataOutput bado) throws IOException {
		bado.writeInt(this.time); // Int
	}

	public int getId() {
		return Packets.PONG;
	}

	public String toString() {
		return "PONG [" + time + "]";
	}

}
