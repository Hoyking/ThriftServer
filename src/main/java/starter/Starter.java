package starter;

import server.ServerHandler;

public class Starter {

	public static void main(String[] args) {
		ServerHandler serverHandler = new ServerHandler();
		serverHandler.handle();
	}
	
}
