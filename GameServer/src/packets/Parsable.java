package packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Parsable {
	public void writeToOutput(ByteArrayDataOutput out) throws IOException;
	public void parseFromInput(ByteArrayDataInput  in) throws IOException;
	public int getId();
}
