package packets.client;

import java.io.IOException;

import packets.ByteArrayDataInput;
import packets.ByteArrayDataOutput;
import packets.Packets;
import packets.Parsable;

public class InteractPacket implements Parsable {
	public long entityId;

	
	public InteractPacket() {
		
	}

	
	public void parseFromInput(ByteArrayDataInput badi) throws IOException {
		entityId = badi.readLong();
	}

	public void writeToOutput(ByteArrayDataOutput bado) throws IOException {
		bado.writeLong(entityId);
	}

	public int getId() {
		return Packets.INTERACT;
	}

	@Override
	public String toString() {
		return "InteractPacket [entityId=" + entityId + "]";
	}



}
