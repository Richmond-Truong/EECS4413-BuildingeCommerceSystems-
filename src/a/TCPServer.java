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
	
	Set<InetAddress> whitelist;
	
	public TCPServer(int port, PrintStream log, File file)throws Exception {
		ServerSocket server = new ServerSocket(port);
		System.out.println("Listening on: " + server.getLocalPort());
		whitelist = new HashSet<InetAddress>();
		whitelist.add(server.getInetAddress());
		//whitelist.add(other);
		while (file.exists())
		{
			Socket client = server.accept();
			// check the client's IP and filter it
			log.println((new Date()).toString()+"|"+"Connection"+"|"+client.getInetAddress());
			Worker worker = (new Worker(client)).handle();
			log.println((new Date()).toString()+"|"+"Disconnected"+"|"+client.getInetAddress());
		}
		server.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TCPServer s = new TCPServer(0);
	}

}
