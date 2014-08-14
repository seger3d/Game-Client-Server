package client.base;

import java.util.Random;

import packets.Parsable;
import packets.client.MovePacket;
import packets.client.PongPacket;
import packets.server.PingPacket;
import packets.server.UpdatePacket;
import data.Entity;
import data.Location;


public class GameListener {
	private Connection con;
	private Client cli;
	public GameListener(Client cli, Connection con) {
		this.cli = cli;
		this.con = con;
	}

	public void loop(){
		
	}

	public void packetRecieved(Parsable rw) {
		if (rw instanceof PingPacket) {
			PingPacket pp = (PingPacket) rw;
			PongPacket pop = new PongPacket();
			pop.time = con.currentTime();
			con.sendQueue.add(pop);
		} else if (rw instanceof UpdatePacket) {
			con.setLastUpdateTime(con.currentTime());
			con.setEntities(((UpdatePacket) rw).objs);
			for(Entity e: con.getEntities()){
				if (e.getName().equals(con.getSelf().getName())){
					con.setSelf(e);
				}
			}
		}
		
	}

}
