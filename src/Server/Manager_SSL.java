package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import Server.PI;
import Util.CONFIG;

public class Manager_SSL extends Thread {
	
	public static final String KeystorePath = "C:\\Users\\Administrator\\KeyStore";
	public static final String KeystorePassword = "ftpk16";
    private SSLServerSocket _ServerSocket = null;
    private SSLSocket _Socket = null;

    public Manager_SSL(int port) throws IOException {
    	
    	SetSystemProperty();
        ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
        _ServerSocket = (SSLServerSocket) factory.createServerSocket(port);
        _ServerSocket.setEnabledProtocols(new String[] {"TLSv1"});
        
        CONFIG.print("PI->*" + _ServerSocket);
    }
    
    private void SetSystemProperty() {
    	System.setProperty("javax.net.debug", "ssl,handshake");
        System.setProperty("javax.net.ssl.keyStore", KeystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", KeystorePassword);
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12" );		
	}
    
    @Override
    public void run() {
    	super.run();
    	while (true) {
        	CONFIG.print("Waiting for Client...");
            try {
				_Socket = (SSLSocket) _ServerSocket.accept();
				CONFIG.print("PI->" + _Socket);
	            PI st = new PI(_Socket, "TLS");
	            st.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
    }

}
