package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunner {
	public static Map map = new Map();

	public static void main(String[] args) throws IOException {
		map.start();
		
		System.out.println("Started");
		ServerSocket ss = new ServerSocket(2050);
		int objNum = 1;
		ClientHandler ch = null;
		while (ss.isBound()) {
			final Socket s = ss.accept();
			System.out.println(s.getRemoteSocketAddress().toString());
			ch = new ClientHandler(s, objNum, map);
			ch.start();
			map.addPlayer(ch);
			objNum++;

		}

	}

}
