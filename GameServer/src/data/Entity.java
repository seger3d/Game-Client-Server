package data;

import java.io.IOException;
import java.util.Arrays;

import packets.ByteArrayDataInput;
import packets.ByteArrayDataOutput;

public class Entity {
	private int type;
	private long id;
	private Location loc = new Location();
	private Location destinationLoc = new Location();
	private double speed;
	private int[] data = new int[15];
	private String name;
	private long interactingWithId;
	

	public static final int Tree = 1;
	public Entity() {
		
	}
	
	public Entity(int type, long id, Location loc) {
		switch(type){
		case Tree:
			type = Tree;
			this.id = id;
			this.loc = loc;
			this.name = "Tree";
			break;
		}
	}
	
	public Entity(Entity obj) {
		this.type = obj.getType();
		this.id = obj.getId();
		this.loc = obj.getLoc().clone();
		this.destinationLoc = obj.getDestinationLoc();
		this.speed = obj.speed;
		this.data = obj.getData().clone();
		this.name = obj.getName();
		this.interactingWithId = obj.getInteractingWithId();
		
	}


	public Entity(ByteArrayDataInput badi) throws IOException {
		parseFromInput(badi);
	}

	private void parseFromInput(ByteArrayDataInput badi) throws IOException {
		type = badi.readShort();
		id = badi.readLong();
		loc.parseFromInput(badi);
		destinationLoc.parseFromInput(badi);
		speed = badi.readDouble();
		for (int i = 0; i < data.length; i++) {
			data[i] = badi.readShort();
		}
		name = badi.readUTF();
		interactingWithId = badi.readLong();
	}

	public void writeToOutput(ByteArrayDataOutput bado) throws IOException {
		bado.writeShort(type);
		bado.writeLong(id);
		loc.writeToOutput(bado);
		destinationLoc.writeToOutput(bado);
		bado.writeDouble(speed);

		for (int i = 0; i < data.length; i++) {
			bado.writeShort(data[i]);
		}
		bado.writeUTF(name);
		bado.writeLong(interactingWithId);
	}

	public void settype(int type) {
		this.type = type;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}
	
	public int getNum(){
		return data[0];
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (destinationLoc == null) {
			if (other.destinationLoc != null)
				return false;
		} else if (!destinationLoc.equals(other.destinationLoc))
			return false;
		if (id != other.id)
			return false;
		if (loc == null) {
			if (other.loc != null)
				return false;
		} else if (!loc.equals(other.loc))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(speed) != Double
				.doubleToLongBits(other.speed))
			return false;
		if (type != other.type)
			return false;
		return true;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public long getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Location getDestinationLoc() {
		return destinationLoc;
	}


	public void setDestinationLoc(Location destinationLoc) {
		this.destinationLoc = destinationLoc;
	}


	public double getSpeed() {
		return speed;
	}


	public void setSpeed(double speed) {
		this.speed = speed;
	}


	@Override
	public String toString() {
		return "Entity [type=" + type + ", id=" + id + ", loc=" + loc
				+ ", destinationLoc=" + destinationLoc + ", speed=" + speed
				+ ", data=" + Arrays.toString(data) + ", name=" + name
				+ ", interactingWithId=" + interactingWithId + "]";
	}


	public long getInteractingWithId() {
		return interactingWithId;
	}


	public void setInteractingWithId(long interactingWithId) {
		this.interactingWithId = interactingWithId;
	}


	public void setId(long id) {
		this.id = id;
	}


}
