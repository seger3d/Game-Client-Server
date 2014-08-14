package client.base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import packets.client.InteractPacket;
import data.Entity;
import data.Location;

public class Client extends JFrame {
	boolean running = true;
	int fps = 60;
	int windowWidth = 500;
	int windowHeight = 500;
	BufferedImage backBuffer;
	Insets insets;
	Connection con = null;
	Map map;
	int tileWH = 25;

	public Client() {
		initialize();
		run();
	}

	public static String generateString(int length) {
		String characters = "abcdefghijklmnopqrstuvwxyz";
		Random rng = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}

	public void run() {
		running = true;

		con = new Connection(this);

		con.connect("localhost", generateString(5), "pass");
		new Thread() {
			public void run() {
				while (running) {
					long time = System.currentTimeMillis();
					draw();
					// delay for each frame - time it took for one frame
					time = (1000 / fps) - (System.currentTimeMillis() - time);
					if (time > 0) {
						try {
							Thread.sleep(time);
						} catch (Exception e) {
						}
					}

				}
			}

		}.start();

		new Thread() {
			public void run() {
				while (running) {
					long start = System.currentTimeMillis();
					update();
					long end = System.currentTimeMillis();
					int time = (int) (end - start);
					try {
						sleep(10 - (time > 10 ? 0 : time));

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.start();

	}

	// public int x;
	// public int y;

	/**
	 * This method will set up everything need for the game to run
	 */
	void initialize() {
		setTitle("mmo");
		setSize(windowWidth, windowHeight);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		add(panel);
		panel.addMouseListener(new MouseAdapter() {// empty implementation of
													// all
			public void mousePressed(MouseEvent e) {
				boolean empty = true;
				Entity t = null;
				for (Entity ent : con.getEntities()) {
					if (ent.getLoc().equals(
							new Location(e.getX() / tileWH, e.getY() / tileWH))) {
						t = ent;
						empty = false;
						break;
					}
				}

				if (empty) {
					con.getSelf().setDestinationLoc(
							new Location(e.getX() / tileWH, e.getY() / tileWH));
					con.sendMovePacket();
				} else {
					InteractPacket ip = new InteractPacket();
					ip.entityId = t.getId();
					con.getSendQueue().add(ip);
				}
			}

		});
		setVisible(true);

		insets = getInsets();
		setSize(insets.left + windowWidth + insets.right, insets.top
				+ windowHeight + insets.bottom);

		backBuffer = new BufferedImage(windowWidth, windowHeight,
				BufferedImage.TYPE_INT_RGB);

		this.map = new Map(50, 50);

	}

	/**
	 * This method will check for input, move things around and check for win
	 * conditions, etc
	 */
	void update() {

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

	/**
	 * This method will draw everything
	 */
	void draw() {

		Graphics g = getGraphics();
		Graphics2D bbg = (Graphics2D) backBuffer.getGraphics();
		if (con.getSocket().isConnected()) {
			bbg.setColor(Color.WHITE);
			bbg.fillRect(0, 0, windowWidth, windowHeight);

			bbg.setColor(Color.gray);
			for (int y = 0; y < map.getMapheight(); y++) {
				for (int x = 0; x < map.getMapWidth(); x++) {
					Rectangle destination = new Rectangle(x * tileWH, y
							* tileWH, tileWH, tileWH);
					switch (map.getTiles()[x][y]) {
					case 0:
						bbg.setColor(Color.GREEN);
						break;
					case 1:
						bbg.setColor(Color.BLUE);
						break;
					}
					bbg.fillRect(destination.x, destination.y, tileWH, tileWH);
					bbg.setColor(Color.BLACK);
					bbg.draw(destination);
				}
			}

			bbg.setColor(Color.BLACK);
			for (Entity o : con.getEntities()) {
				if (!con.getSelf().getName().equals(o.getName())) {
					bbg.setColor(Color.BLACK);
					bbg.fillOval((int) (o.getLoc().x * tileWH),(int) (o.getLoc().y * tileWH), 15, 15);
					bbg.drawString(o.getName(), (int) (o.getLoc().x * tileWH),(int) (o.getLoc().y * tileWH));
					bbg.setColor(Color.white);
					bbg.drawString(o.getType() + "", (int) (o.getLoc().x * tileWH),(int) (o.getLoc().y * tileWH) + 10);
				}
			}
			bbg.setColor(Color.RED);
			bbg.fillOval((int) (con.getSelf().getLoc().x * tileWH), (int) (con
					.getSelf().getLoc().y * tileWH), 15, 15);
			bbg.setColor(Color.BLACK);
			bbg.drawString(con.getSelf().toString(), (int) (con.getSelf()
					.getLoc().x * tileWH),
					(int) (con.getSelf().getLoc().y * tileWH));
			g.drawImage(backBuffer, insets.left, insets.top, this);
		} else {
			bbg.setColor(Color.RED);
			bbg.drawString("Not connected", insets.left, insets.top);
			g.drawImage(backBuffer, insets.left, insets.top, this);
		}
	}

}