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

public class zManager_SSL {
	
	public static final String KeystorePath = "C:\\Users\\Administrator\\KeyStore";
	public static final String KeystorePassword = "ftpk16";
    private SSLServerSocket _ServerSocket = null;
    private SSLSocket _Socket = null;

    public zManager_SSL(int port) throws IOException {
    	
    	SetSystemProperty();
        ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
        _ServerSocket = (SSLServerSocket) factory.createServerSocket(port);
        _ServerSocket.setEnabledProtocols(new String[] {"TLSv1"});
        
        CONFIG.print("PI->*" + _ServerSocket);
    }
    
    public void listen() throws IOException {
        while (true) {
        	CONFIG.print("Waiting for Client...");
            _Socket = (SSLSocket) _ServerSocket.accept();
            CONFIG.print("PI->" + _Socket);
            zPI_SSL st = new zPI_SSL(_Socket);
            st.start();
        }
    }
    
    private void SetSystemProperty() {
    	System.setProperty("javax.net.debug", "ssl,handshake");
        System.setProperty("javax.net.ssl.keyStore", KeystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", KeystorePassword);
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12" );		
	}
    
//    public PrivateKey readPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
//    {
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(readFileBytes(filename));
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        return keyFactory.generatePrivate(keySpec);     
//    }
//    
//    public byte[] readFileBytes(String filename) throws IOException
//    {
//        Path path = Paths.get(filename);
//        return Files.readAllBytes(path);        
//    }


}
