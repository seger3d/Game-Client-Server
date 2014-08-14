package packets;

import java.util.HashMap;
import java.util.Map;

import packets.server.*;
import packets.client.*;

public class Packets {
	public final static int PING = 5;
	public final static int PONG = 4;
	public final static int MOVE = 3;
	public final static int HELLO = 2;
	public final static int DISCONNECT = 1;
	public final static int UPDATE = 6;
	public final static int INTERACT = 7;
	
	public static Parsable findPacket(int i) {
		if (i == PING) {
			return new PingPacket();
		} else if (i == PONG) {
			return new PongPacket();
		} else if (i == MOVE) {
			return new MovePacket();
		} else if (i == HELLO) {
			return new HelloPacket();
		} else if (i == DISCONNECT) {
			return new DisconnectPacket();
		} else if (i == UPDATE){
			return new UpdatePacket();
		} else if (i == INTERACT){
			return new InteractPacket();
		}
		return null;
	}

}
