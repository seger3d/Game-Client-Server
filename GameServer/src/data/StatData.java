// 123.0.0 StatData.as

package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import packets.*;

public class StatData {
	public static final int NAME = 0;
	public static final int MAX_HEALTH = 1;
	public static final int HEALTH = 2;

    public int type = 0; // ubyte
	public int numValue;
    public String stringValue;
    
	public StatData(int type, int numValue) {
		this.type = type;
		this.numValue = numValue;
	}

	public StatData(DataInput badi) throws IOException {
		parseFromInput(badi);
	}
	
	public StatData(int type, String stringValue) {
		this.type = type;
		this.stringValue = stringValue;
	}

	public void parseFromInput(DataInput badi) throws IOException {
		this.type = badi.readUnsignedByte();
		//if (type == NAME || type == GUILD) { // theese have a string value
		if (type == 31 || type == 62 || type ==82 || type ==38 || type ==54) {//new 14.2
			this.stringValue = badi.readUTF();
			return;
		}
		this.numValue = badi.readInt();
	}

	public void writeToOutput(DataOutput bado) throws IOException {
		bado.write(type);
		if (type == NAME) { // theese have a string value
			bado.writeUTF(stringValue);
			return;
		}
		bado.writeInt(numValue);
	}

	public String toString() {
		return "statdata [" + type + ", " + (stringValue == null ? numValue : stringValue) + "]";
	}
}
