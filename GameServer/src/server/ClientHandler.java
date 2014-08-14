package server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.xml.bind.DatatypeConverter;

import data.Location;
import data.Entity;
import data.StatData;
import util.Angles;
import packets.ByteArrayDataInput;
import packets.ByteArrayDataOutput;
import packets.Packets;
import packets.Parsable;
import packets.Parsable;
import packets.client.HelloPacket;
import packets.client.InteractPacket;
import packets.client.MovePacket;
import packets.client.PongPacket;
import packets.server.PingPacket;

public class ClientHandler extends Thread {
	private Entity self = new Entity();
	private Socket s;
	private DataInputStream in;
	private DataOutputStream out;
	public ArrayList<Parsable> sendQueue = new ArrayList<Parsable>();
	private int currentTick = 0;
	int lastpongtime = 0;
	int pongtimenow = 0;
	int[] pingpongtime;
	// int pingpongid;
	private final static Random rand = new Random();

	// begin new tick status info
	public int _changedSinceLastTick;
	public int _movedSinceLastTick;
	// end new tick status info

	public int ineditor = 0;
	public int editorx = 0, editory = 0;

	private int pingCounter;
	private int unansweredPings;
	private Boolean disconnected = false;
	private Map map;
	Random generator = new Random();

	public ClientHandler(Socket s, int objNum, Map map) {
		this.map = map;
		self = new Entity();
		try {
			this.s = s;
			this.in = new DataInputStream(s.getInputStream());
			this.out = new DataOutputStream(s.getOutputStream());
			startThreads();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startThreads() {
		System.out.println("Starting threads");
		new Thread() {
			public void run() {
				try {
					while (s.isConnected()) {
						long start = System.currentTimeMillis();
						Parsable rw = readPacket();
						if (rw != null) {
							handlePacket(rw);
						}

						long end = System.currentTimeMillis();
						// System.out.println("Done in " + ((int) (end-start)) +
						// " | " + (50 - (int) (end-start)));
						int time = (int) (end - start);
						sleep(100 - (time > 100 ? 0 : time));
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				disconnect();
			}
		}.start();
		new Thread() {
			public void run() {
				try {

					while (s.isConnected()) {
						long start = System.currentTimeMillis();

						while (sendQueue.size() != 0) {
							Parsable rw = sendQueue.remove(0);
							writePacket(rw);
						}
						long end = System.currentTimeMillis();
						// System.out.println("Done in " + ((int) (end-start)) +
						// " | " + (50 - (int) (end-start)));
						int time = (int) (end - start);
						sleep(150 - (time > 150 ? 0 : time));
						currentTick++;
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				disconnect();
			}
		}.start();
	}

	protected void handlePacket(Parsable rw) {
		if (Consts.print)
			System.out.println(rw);

		if (rw instanceof HelloPacket) {
			HelloPacket hp = (HelloPacket) rw;
			self.setName(hp.getUsername());

		} else if (rw instanceof PongPacket) {
			PongPacket pp = (PongPacket) rw;
			unansweredPings--;
			if (lastpongtime != pp.time) {
				pongtimenow = pp.time - lastpongtime;
				lastpongtime = pp.time;
				// System.out.println(pongtimenow);
			}

		} else if (rw instanceof MovePacket) {
			MovePacket mp = (MovePacket) rw;
			self.setDestinationLoc(mp.destination);
			self.setInteractingWithId(0);

		} else if (rw instanceof InteractPacket) {
			long id = ((InteractPacket) rw).entityId;
			System.out.println(id);
			self.setInteractingWithId(id);
			for (ClientHandler ch : map.getPlayers()) {
				if (ch.getSelf().getId() == id) {
					self.setDestinationLoc(ch.getSelf().getLoc());
				}
			}

		}
	}

	int checked = 0;
	int check_packetLength = 0;

	int check_packetId = 0;

	protected Parsable readPacket() throws IOException {

		if (s.isClosed() == true) {
			System.out
					.println("socket is closed why closed the connection why??");
		}

		if (in.available() >= 5) {
			int packetLength;
			int packetId;

			if (checked == 0) {
				packetLength = in.readInt();

				packetId = in.readByte();
				check_packetLength = packetLength;
				check_packetId = packetId;
				checked = 1;
			} else// checked==1
			{
				packetLength = check_packetLength;

				packetId = check_packetId;
			}

			if (in.available() >= packetLength - 5) {
				checked = 0;

				byte[] buf = new byte[packetLength - 5]; // read the rest of the
															// packet
				in.readFully(buf);
				// System.out.println(packetId);
				Parsable rw = Packets.findPacket(packetId);
				if (rw != null)
					rw.parseFromInput(new ByteArrayDataInput(buf));
				return rw;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public void writePacket(int i, byte[] buf) throws IOException {
		byte[] encr = buf;// cipher_toClient.rc4(buf);
		out.writeInt(encr.length + 5);
		out.writeByte(i);
		out.write(encr);
		out.flush();
	}

	public void writePacket(Parsable rw) throws IOException {

		if (rw != null) {
			if (Consts.print)
				System.out.println(rw);

			ByteArrayDataOutput bado = new ByteArrayDataOutput(100000);
			rw.writeToOutput(bado);
			writePacket(rw.getId(), bado.getArray());
		}
	}

	public int getNextTickNum() {
		return currentTick++;
	}

	// returns the current tick this client is on
	public int getCurrentTick() {
		return currentTick;
	}

	// sends a ping and if required, kick client for missed pings
	public void sendPing() { // sent every 5sec
		PingPacket pp = new PingPacket();
		sendQueue.add(pp);
		pp.time = pingCounter++;
		if (unansweredPings++ > 20) { // if missed 5 pings (not including this)
			disconnect();
		}
	}

	public void disconnect() {
		System.out.println("Disconnecting");

		try {
			s.close();

		} catch (IOException e) {
		}
		disconnected = true;
		map.disconnect(this);
	}

	public Entity getself() {
		return self;
	}

	public void setself(Entity self) {
		this.self = self;
	}

	public Boolean getDisconnected() {
		return disconnected;
	}

	public void setDisconnected(Boolean disconnected) {
		this.disconnected = disconnected;
	}

	public Entity getSelf() {
		return self;
	}

	public void setSelf(Entity self) {
		this.self = self;
	}

}
