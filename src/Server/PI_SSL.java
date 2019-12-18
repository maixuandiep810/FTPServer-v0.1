package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import Util.*;
import Model.BEAN.*;


public class PI_SSL extends Thread {
	public static int NumOfPI = 0;
    private SSLSocket _SocketPI = null;
    private DTP_SSL _DTP = null;

    private BufferedReader _BrPI = null;
    private BufferedWriter _BwPI = null;

    private String _UserToken;
    private String _UserSession;
    
    public PI_SSL(SSLSocket socket) throws IOException {
    	NumOfPI++;
    	_DTP = new DTP_SSL(10000 + NumOfPI);
    	_SocketPI = socket;
        _BrPI = new BufferedReader(new InputStreamReader(_SocketPI.getInputStream(), "UTF-8"));
        _BwPI = new BufferedWriter(new OutputStreamWriter(_SocketPI.getOutputStream(), "UTF-8"));
    } 
    
    public void run() {

    	try {
			BufferUtil.Write(_BwPI, "220 Connection established");
			String Request = null;
	    	String cmd = null;
	    	User user = new User();
	    	while (true) {
	        	Request = BufferUtil.Read(_BrPI);
	        	System.out.println(Request);
	        	cmd = Request.split("\\s")[0];
	        	switch (cmd) {
	        	
				case "USER":
					user.setUsername(Request.split("\\s")[1]);
					Command.USERcommand(user, _BwPI);
					break;
				case "PASS":
					user.setPassword(Request.split("\\s")[1]);
					Command.PASScommand(user, _BwPI);
					break;
				case "SYST":
					Command.SYSTcommand(_BwPI);
				case "FEAT":
					Command.FEATcommand(_BwPI);				
					break;
//				case "PWD":
//					Command.PWDcommand(_BwPI);
//					break;
				case "TYPE":
					String type = Request.split("\\s")[1];
					Command.TYPEcommand(type, _BwPI);
					break;
				case "PASV":
					int port = _DTP.get_Port();
					Command.PASVcommand(port, _BwPI);
					break;
//				case "PORT":
//					BufferUtil.Write(bw, "125 abc");
//					break; 
//				case "LIST": 
//					Command.LISTcommand_TLS(_DTP, _BwPI);
//					break;
//				case "CWD":
//					Command.CWDcommand(_BwPI);
//					break;
//				case "RETR":
//					Command.RERTcommand(_DTP, _BwPI);
//					break;
				case "PBSZ": // Tham so 0
					Command.PBSZcommand(_BwPI);
					break;
				case "PROT":
					String protectMode = Request.split("\\s")[1];
					Command.PROTcommand(protectMode, _BwPI);
					break;
					/*
				case:
					BufferUtil.Write(bw, "");
					break;
					*/
				default:
					break;
				}	    			
	    	}
		} catch (IOException e) {
			CONFIG.print(e.toString());
		}
    	
    }
}
    
    
    
//    private void PORTCommand () throws IOException {
//    	_SocketDTP = _DTP.Accept();
//		_BwDTP = new BufferedWriter(new OutputStreamWriter(_SocketDTP.getOutputStream(), "UTF-8"));
//		BufferUtil.Write(_BwPI, "200 Port command");
//    }
//    
//    private void RETRCommand () throws IOException {
//    	// Tai sao lai ACCEPT, trong khi PORT dax ACCEPT
//    	_SocketDTP = _DTP.Accept();
//    	_BwDTP = new BufferedWriter(new OutputStreamWriter(_SocketDTP.getOutputStream(), "UTF-8"));
//    	BufferUtil.Write(_BwPI, "150 Opening");
//		String path = "H:\\CLIENT\\TEST_01\\Test_01_f_01.txt";
//		Send(path);
//		BufferUtil.Write(_BwPI, "226 Successfully transferred");
//		_SocketDTP.close();
//    }
//    
//    private void Send(String path) throws IOException {
//    	File f = new File(path);
//    	FileInputStream fin = new FileInputStream(f);
//        int c;
//        do {
//            c = fin.read();
//            if(c != -1)
//            	BufferUtil.Write(_BwDTP, String.valueOf(c));
//            else
//            	break;
//        } while(true);
//        fin.close();
//    }
//}
