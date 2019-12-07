package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import Util.CONFIG;

public class yDTP_SSL {
	public static final String KeystorePath = "C:\\Users\\Administrator\\KeyStore";
	public static final String KeystorePassword = "ftpk16";
	public static final String TrustKeystorePath = "C:\\Users\\Administrator\\TrustKeyStore";
	public static final String TrustKeystorePassword = "ftpk16";
    private SSLServerSocket _ServerSocket = null;
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
	public yDTP_SSL(int port) throws IOException {
		super();
		
		System.setProperty("javax.net.debug", "ssl,handshake");
    	
        System.setProperty("javax.net.ssl.keyStore", KeystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", KeystorePassword);
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12" );
        
        System.setProperty("javax.net.ssl.trustStore", TrustKeystorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", TrustKeystorePassword);
        
        ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
        _ServerSocket = (SSLServerSocket) factory.createServerSocket(port);
        _ServerSocket.setNeedClientAuth(true);
		_Port = _ServerSocket.getLocalPort();
		CONFIG.print("DTP->*" + _ServerSocket);
		_CurrentPath = null;
	}
	
	public Socket Accept() throws IOException {
		Socket ss = _ServerSocket.accept();
		CONFIG.print("DTP->" + ss);
		return ss;
	}
}
