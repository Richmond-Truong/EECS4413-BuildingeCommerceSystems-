package a;

import java.io.File;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TCPServer {

	private static Set<InetAddress> whitelist;

	public TCPServer(int port, PrintStream log, File file) throws Exception {
		ServerSocket server = new ServerSocket(port);
		System.out.println("Listening on: " + server.getLocalPort());
		log.println((new Date()).toString() + "|" + "Server started Listening on: " + server.getLocalPort());
		whitelist = new HashSet<InetAddress>();
		log.println((new Date()).toString() + "|" + server.getInetAddress());
		whitelist.add(server.getInetAddress());
		whitelist.add(InetAddress.getByName("127.0.0.1"));
		while (file.exists()) {
			Socket client = server.accept();
			if (whitelist.contains(client.getInetAddress())) {
				log.println((new Date()).toString() + "|" + "Connection" + "|" + client.getInetAddress());
				Worker worker = new Worker(client);
				worker.handle();
				log.println((new Date()).toString() + "|" + "Disconnected" + "|" + client.getInetAddress());
			} else {
				client.close();
				log.println((new Date()).toString() + "|" + "FireWall violation on" + "|" + client.getInetAddress());
			}
		}
		server.close();
		log.println((new Date()).toString() + "|" + "Server closed");
	}

	public static void addToWhiteList(InetAddress add) {
		whitelist.add(add);
		System.out.println(whitelist.toString());
	}

	public static void removeFromWhiteList(InetAddress add) {
		if (whitelist.contains(add))
			whitelist.remove(add);
		System.out.println(whitelist.toString());
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		File file = new File("running.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		File logfile = new File("Log.txt");
		if (!logfile.exists()) {
			logfile.createNewFile();
		}
		PrintStream log = new PrintStream(logfile);
		TCPServer s = new TCPServer(0, log, file);
	}

}
