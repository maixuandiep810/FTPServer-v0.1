package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Server.PI;
import Util.CONFIG;

public class Manager_Plain extends Thread {
	
    private ServerSocket _ServerSocket = null;
    private Socket _Socket = null;

    public Manager_Plain(int port) throws IOException {
        _ServerSocket = new ServerSocket(port);
        CONFIG.print("PI->*" + _ServerSocket);
    }
    
    @Override
    public void run() {
    	super.run();
    	 while (true) {
             try {
				_Socket = _ServerSocket.accept();
				CONFIG.print("PI->" + _Socket);
	             PI st = new PI(_Socket);
	             st.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
    }
}
