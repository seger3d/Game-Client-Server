package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import packets.*;

public class Location {
	public float x, y;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + Float.floatToIntBits(x);
		//result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
	
	public Location clone() {
		return new Location(x, y);
	}
	
	public Location()
	{
		
	}
	

	public Location(DataInput badi) throws IOException {
		parseFromInput(badi);
	}

	public Location(float x, float y) {
		this.x = x;
		this.y = y;
		return;
	}

	public void writeToOutput(DataOutput bado) throws IOException {
		bado.writeFloat(this.x);
		bado.writeFloat(this.y);
	}

	public void parseFromInput(DataInput badi) throws IOException {
		this.x = badi.readFloat();
		this.y = badi.readFloat();
	}

	public String toString() {
		return "location [" + x + ", " + y + "]";
	}
	
	public float getAngleTo(Location l) {
		return (float) (180-Math.atan2(l.x - this.x, l.y - this.y)*180/Math.PI);
	}
	
	public double distanceTo(float dx, float dy) {
		return Math.sqrt((dx - x)*(dx - x) + (dy - y)*(dy - y));
	}
	
	public double distanceTo(Location loc) {
		return distanceTo(loc.x, loc.y);
	}
	
	public double distanceToSq(float dx, float dy) {
		return (dx - x)*(dx - x) + (dy - y)*(dy - y);
	}
	
	public double distanceToSq(Location loc) {
		return distanceToSq(loc.x, loc.y);
	}
	
	public Location subtract(Location l) {
		return new Location(l.x - x, l.y - y);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
