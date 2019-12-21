package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import Util.CONFIG;

public class DTP_SSL {
	public static final String KeystorePath = "C:\\Users\\Administrator\\KeyStore";
	public static final String KeystorePassword = "ftpk16";
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
	public DTP_SSL(int port) throws IOException {
		super();
		System.setProperty("javax.net.debug", "ssl,handshake");
        System.setProperty("javax.net.ssl.keyStore", KeystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", KeystorePassword);
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12" );
        ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
        _ServerSocket = (SSLServerSocket) factory.createServerSocket(port);
        _ServerSocket.setEnabledProtocols(new String[] {"TLSv1"});
		_Port = _ServerSocket.getLocalPort();
		CONFIG.print("DTP->*" + _ServerSocket);
		_CurrentPath = null;
	}
	
	public SSLSocket Accept() throws IOException {
		SSLSocket ss = (SSLSocket) _ServerSocket.accept();
		CONFIG.print("DTP->" + ss);
		return ss;
	}
	public void Close() throws IOException {
		_ServerSocket.close();
	}
}
