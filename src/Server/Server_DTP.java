package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Util.CONFIG;

public class Server_DTP {
	
	private ServerSocket _ServerSocket;
	private int _Port; 
	/**
	 * 
	 * 
	 */
	public int get_Port() {
		return _Port;
	}
	/**
	 * 
	 * 
	 */
	public Server_DTP(int port) throws IOException {
		super();
		_ServerSocket = new ServerSocket(port);
		_Port = _ServerSocket.getLocalPort();
		CONFIG.print("DTP->ServerSocket = " + _ServerSocket);
	}
	
	public void listen() throws IOException {
		while (true) {
			Socket socket = _ServerSocket.accept();
			Server_ThreadDTP abc = new Server_ThreadDTP(socket);
			CONFIG.print("DTP->Socket = " + socket);
			abc.start();
		}
	}
}
