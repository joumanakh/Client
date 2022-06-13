package client;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		  String serverIP = "10.0.0.7";
		  int serverPort = 5555;
		  Client client=new Client(serverPort,serverIP);
		  client.connection();
		  client.inputConnection();
		  client.displayOnConsole();
}
}
