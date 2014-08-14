package client.base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import data.Entity;
import packets.ByteArrayDataInput;
import packets.ByteArrayDataOutput;
import packets.Packets;
import packets.Parsable;
import packets.client.HelloPacket;
import packets.client.MovePacket;
import packets.server.UpdatePacket;

public class Connection {
	private Entity self = new Entity();
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private GameListener gl;
	private static final int LOOP_SLEEP_TIME = 100;
	private boolean running = false;
	private Socket socket = new Socket();
	private DataInputStream in;
	private DataOutputStream out;
	public ArrayList<Parsable> sendQueue = new ArrayList<Parsable>();
	public long start;
	public int tempin = 0;
	private long startTime;
	private Client cli;
	private long lastUpdateTime;
	
	public Connection(Client cli) {
		this.cli = cli;
		this.gl = new GameListener(this.cli,this);
		startTime = System.currentTimeMillis();
	}

	public void connect(String server, String username, String password) {
		self.setName(username);
		self.setSpeed(.002);
		connect(server, new HelloPacket(username, password));
	}

	private void connect(String server, HelloPacket hp) {
		running = true;
		sendQueue.clear();
		sendQueue.add(hp);
		System.out.println("Connecting to " + server + ".");
		if (this.socket != null) {
			try {
				this.socket.close();
			} catch (IOException e) {
			}
		}

		new Thread(){
			public void run(){
				while (running) {
					long start = System.currentTimeMillis();
					gl.loop();
					long end = System.currentTimeMillis();
					int time = (int) (end - start);
					try {
						sleep(150 - (time > 150 ? 0 : time));

					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}.start();
		try {
			this.socket = new Socket(server, 2050);
			System.out.println(socket);
			this.in = new DataInputStream(this.socket.getInputStream());
			this.out = new DataOutputStream(this.socket.getOutputStream());
			new Thread() {
				public void run() {
					Socket sock = socket;
					try {
						while (sock.isConnected() && running) {
							start = System.currentTimeMillis();
							// System.out.println("send quque size "+sendQueue.size());
							while (sendQueue.size() != 0) {
								Parsable rw = sendQueue.remove(0);
								writePacket(rw);
							}
							// System.out.println(sock.isConnected());

							

							long end = System.currentTimeMillis();
							int time = (int) (end - start);
							sleep(LOOP_SLEEP_TIME
									- (time > LOOP_SLEEP_TIME ? 0 : time));
						}
						// } catch (IOException e) {
						// nothing
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						sock.close();
					} catch (IOException e) {
					}
				}
			}.start();
			new Thread() {
				public void run() {
					Socket sock = socket;
					try {
						while (sock.isConnected() && running) {
							// System.out.println("read packet 1");
							Parsable rw = readPacket();

							// System.out.println(in.available());
							if (in.available() != tempin) {
								tempin = in.available();
								// System.out.println(in.available());
							}
							if (in.available() == 1) {
								System.out.println("end");
								// System.out.println(in.readByte());
							}

							
								gl.packetRecieved(rw);
						}
						// } catch (IOException e) {
						// nothing
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						sock.close();
					} catch (IOException e) {
					}
				}
			}.start();
		} catch (IOException e) {
			System.err.println("Failed to connect to " + server + " : "
					+ e.getLocalizedMessage());
		}
	}

	private Parsable readPacket() throws IOException {

		// System.out.println(in.available());
		if (in.available() == 1) {
			// System.out.println(in.readByte());
		}

		int packetLength = in.readInt();
		int packetId = in.readByte();

		byte[] buf = new byte[packetLength - 5]; // read the rest of the packet
		in.readFully(buf); // its waiting for the full packet
		byte[] decr = buf;
		Parsable rw = Packets.findPacket(packetId);
		if (rw != null) {
			rw.parseFromInput(new ByteArrayDataInput(decr));
			if(Consts.print)
			System.out.println(rw);
		}
		return rw;
	}

	private void writePacket(Parsable rw) throws IOException {
		if(Consts.print)
		System.out.println(rw);
		ByteArrayDataOutput bado = new ByteArrayDataOutput(100000);
		rw.writeToOutput(bado);
		byte[] encr = bado.getArray();
		out.writeInt(encr.length + 5);
		out.writeByte(rw.getId());
		out.write(encr);
		out.flush();
	}

	public String getCurrentAddress() {
		return socket.getInetAddress().getHostAddress();
	}

	public int currentTime() {
		return (int) (System.currentTimeMillis() - startTime);
	}
	
	public void disconnect() {
		System.out.println("DC CALLED");
		try {
			while (sendQueue.size() != 0)
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			if (socket != null)
				socket.close();
			running = false;
		} catch (IOException e) {
		}
	}
	
	
	public void sendMovePacket(){
		MovePacket mp = new MovePacket();
		mp.destination = getSelf().getDestinationLoc();
		sendQueue.add(mp);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setObjs(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	public Entity getSelf() {
		return self;
	}

	public void setSelf(Entity self) {
		this.self = self;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public long getCurrentUpdateDuration(){
		return currentTime() - this.lastUpdateTime;
	}

	public ArrayList<Parsable> getSendQueue() {
		return sendQueue;
	}

	public void setSendQueue(ArrayList<Parsable> sendQueue) {
		this.sendQueue = sendQueue;
	}



}
