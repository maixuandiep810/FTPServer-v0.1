package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Util.CONFIG;

public class DTP {
	private ServerSocket _ServerSocket;
	private int _Port; 
	private String _CurrentPath;
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
	public DTP(int port) throws IOException {
		super();
		_ServerSocket = new ServerSocket(port);
		_Port = _ServerSocket.getLocalPort();
		CONFIG.print("DTP->*" + _ServerSocket);
		_CurrentPath = null;
	}
	
	public Socket Accept() throws IOException {
		Socket ss = _ServerSocket.accept();
		CONFIG.print("DTP->" + ss);
		return ss;
	}
	
	public void Close() throws IOException {
		if (_ServerSocket != null) {
			_ServerSocket.close();
		}
	}
}
