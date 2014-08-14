package packets.client;

import java.io.IOException;

import data.Location;
import packets.ByteArrayDataInput;
import packets.ByteArrayDataOutput;
import packets.Packets;
import packets.Parsable;

public class MovePacket implements Parsable {
	public Location destination;

	public void parseFromInput(ByteArrayDataInput badi) throws IOException {
		destination = new Location(badi);
	}

	public void writeToOutput(ByteArrayDataOutput bado) throws IOException {
		destination.writeToOutput(bado);
	}

	public int getId() {
		return Packets.MOVE;
	}

	@Override
	public String toString() {
		return "MovePacket [destination="
				+ destination + "]";
	}

}
