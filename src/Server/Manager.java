package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Server.PI;
import Util.CONFIG;

public class Manager {
	
    private ServerSocket _ServerSocket = null;
    private Socket _Socket = null;

    public Manager(int port) throws IOException {
        _ServerSocket = new ServerSocket(port);
        CONFIG.print("PI->*" + _ServerSocket);
    }
    
    public void listen() throws IOException {
        while (true) {
            _Socket = _ServerSocket.accept();
            CONFIG.print("PI->" + _Socket);
            PI st = new PI(_Socket);
            st.start();
        }
    }
}
