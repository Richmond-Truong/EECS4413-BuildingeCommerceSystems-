package a;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class Worker {
	private Socket client = null;
	private boolean servicing = true;

	public Worker(Socket Client) {
		client = Client;
	}

	public void handle() throws Exception {
		if (client != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintStream out = new PrintStream(client.getOutputStream());
			while (servicing) {
				String job = in.readLine();
				if (job.equals("getTime"))
					out.println(getTime());
				else if (job.equals("Bye"))
					bye();
				else if (job.contains("Punch"))
					Punch(InetAddress.getByName(job.split(" ")[1]));
				else if (job.contains("Plug"))
					Plug(InetAddress.getByName(job.split(" ")[1]));
			}
			out.flush();
		}
	}

	public String getTime() {
		return (new Date()).toString();
	}

	public void bye() throws Exception {
		client.close();
		servicing = false;
	}

	public void Punch(InetAddress ip) {
		TCPServer.addToWhiteList(ip);
	}

	public void Plug(InetAddress ip) {
		TCPServer.removeFromWhiteList(ip);
	}
}
