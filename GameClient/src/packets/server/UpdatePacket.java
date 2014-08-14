package packets.server;

import java.io.IOException;
import java.util.ArrayList;

import data.Entity;
import packets.ByteArrayDataInput;
import packets.ByteArrayDataOutput;
import packets.Packets;
import packets.Parsable;

public class UpdatePacket implements Parsable {
	public int time;
	public ArrayList<Entity> objs = null;

	public UpdatePacket() {

	}

	public void parseFromInput(ByteArrayDataInput badi) throws IOException {
		time = badi.readInt();
		objs = null;
		int size = badi.readShort();
		// System.out.println("updae size");
		// System.out.println(size);
		objs = new ArrayList<Entity>(size);
		if (size != 0) {
			for (int i = 0; i < size; i++) {
				objs.add(new Entity(badi));
			}
		}

	}

	public void writeToOutput(ByteArrayDataOutput bado) throws IOException {
		bado.writeInt(time);
		if (objs == null) {
			bado.writeShort(0);
		} else {
			bado.writeShort(objs.size());
			for (int i = 0; i < objs.size(); i++) {
				objs.get(i).writeToOutput(bado);
			}
		}
	}

	public int getId() {
		return Packets.UPDATE;
	}

	@Override
	public String toString() {
		return "UpdatePacket [objs=" + objs + "]";
	}

}
