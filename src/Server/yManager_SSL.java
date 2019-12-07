package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.KeyStore;

import Server.PI;
import Util.CONFIG;

public class yManager_SSL {
	
	public static final String KeystorePath = "C:\\Users\\Administrator\\KeyStore";
	public static final String KeystorePassword = "ftpk16";
	public static final String TrustKeystorePath = "C:\\Users\\Administrator\\TrustKeyStore";
	public static final String TrustKeystorePassword = "ftpk16";
    private SSLServerSocket _ServerSocket = null;
    private Socket _Socket = null;

    public yManager_SSL(int port) throws IOException {
    	
    	System.setProperty("javax.net.debug", "ssl,handshake");
    	
        System.setProperty("javax.net.ssl.keyStore", KeystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", KeystorePassword);
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12" );
        
        System.setProperty("javax.net.ssl.trustStore", TrustKeystorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", TrustKeystorePassword);
        
        ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
        _ServerSocket = (SSLServerSocket) factory.createServerSocket(port);
        _ServerSocket.setNeedClientAuth(true);
    
        CONFIG.print("PI->*" + _ServerSocket);
    }
    
    public void listen() throws IOException {
        while (true) {
        	System.out.println("Waiting for Client...");
            _Socket = _ServerSocket.accept();
            CONFIG.print("PI->" + _Socket);
            yPI_SSL st = new yPI_SSL(_Socket);
            st.start();
        }
    }
}
