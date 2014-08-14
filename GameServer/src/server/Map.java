package server;

import java.math.BigDecimal;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import packets.Parsable;
import packets.server.UpdatePacket;

import java.io.File;

import data.Location;
import data.Entity;
import data.StatData;

public class Map extends Thread {
	public int width;
	public int height;
	int[][] mapData;
	public ArrayList<ClientHandler> players = new ArrayList<ClientHandler>();
	long nextId = 1;
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public Map(){
		entities.add(new Entity(Entity.Tree,nextId(),new Location(4,4)));
		
	}
	
	public long nextId(){
		nextId ++;
		return nextId;
	}
	
	public void addPlayer(ClientHandler clientHandler) {
		clientHandler.getSelf().setSpeed(.005);
		clientHandler.getSelf().setId(nextId);
		nextId++;
		players.add(clientHandler);
		
	}

	private long lastTick = 0;

	public void sendToAll(Parsable rw) {
		for (ClientHandler p : players) {
			p.sendQueue.add(rw);
		}
	}

	public Location getNewPosition(int time, Entity e) {
		Location self = e.getLoc().clone();
		Location targ2 = e.getDestinationLoc().clone();
		float angle = self.getAngleTo(targ2);
		
		// System.out.println("self " + self);
		boolean xLess = false, yLess = false;

		if (self.x < targ2.x)
			xLess = true;
		if (self.y < targ2.y)
			yLess = true;
		// oringal speed selfation
		double speed = e.getSpeed();
		double xmove;
		double ymove;

		xmove = time * speed * Math.abs(Math.sin(Math.toRadians(angle)));
		ymove = time * speed * Math.abs(Math.cos(Math.toRadians(angle)));

		if (xLess) {
			self.x += xmove;
			if (self.x > targ2.x)
				self.x = targ2.x;
		} else {
			self.x -= xmove;
			if (self.x < targ2.x)
				self.x = targ2.x;
		}
		if (yLess) {
			self.y += ymove;
			if (self.y > targ2.y)
				self.y = targ2.y;
		} else {
			self.y -= ymove;
			if (self.y < targ2.y)
				self.y = targ2.y;
		}
		
		return self;
	}
	
	public void run() {
		
		while (true) {
			long start = System.currentTimeMillis();
			for (ClientHandler p : players) {
				if (!p.getSelf().getDestinationLoc().equals(p.getSelf().getLoc())){
					p.getSelf().setLoc(getNewPosition(100,p.getSelf()));
					
				}
			}
			
			
			ArrayList<Entity> o = new ArrayList<Entity>();
			for (ClientHandler p : players) {
				if (p.getCurrentTick() % 15 == 0) {
					p.sendPing(); // also checks for missed pings
				}
				o.add(p.getSelf());
			}
			
			for(Entity e: entities){
				o.add(e);
			}
			for (ClientHandler p : players) {
				UpdatePacket up = new UpdatePacket();
				up.objs = o;
				p.sendQueue.add(up);
			}

			lastTick = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			// System.out.println("Done in " + ((int) (end-start)) + " | " + (50
			// - (int) (end-start)));
			int time = (int) (end - start);
			// System.out.println(timeSinceLastTick);

			try {
				sleep(150 - (time > 150 ? 0 : time));

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	float angleFrom(float APositionX, float APositionY, float BPositionX,
			float BPositionY) {
		float dx = BPositionX - APositionY;
		float dy = BPositionY - APositionY;

		return (float) Math.atan2(dy, dx);
	}

	public float toRotmgAngle(float angle) {
		return (float) ((angle - 90) / (360d / Math.PI / 2));
	}

	int point_direction(float x1, float y1, float x2, float y2) {
		float angle, x, y;
		x = x1 - x2;
		y = y1 - y2;
		if (y != 0) {
			angle = 90 - (float) -(Math.atan(x / y) * 180 / Math.PI);
		} else {
			if (x1 > x2) {
				angle = 180;
			} else if (x1 < x2) {
				angle = 0;
			} else {
				angle = 0;
			}
		}

		if (y > 0) {
			angle -= 180;
		}

		if (angle > 0) {
			angle -= 360;
		}
		return (int) -angle;
	}

	public float point_distance(float x1, float y1, float x2, float y2) {
		float x, y;
		x = (float) Math.pow(Math.floor(x1) - Math.floor(x2), 2);
		y = (float) Math.pow(Math.floor(y1) - Math.floor(y2), 2);
		return (float) Math.sqrt(x + y);
	}

	public void disconnect(ClientHandler ch) {
		players.remove(ch);
	}

	public ArrayList<ClientHandler> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<ClientHandler> players) {
		this.players = players;
	}

}
