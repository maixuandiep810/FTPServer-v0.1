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


public class zPI_SSL extends Thread {
	public static int NumOfPI = 0;
    private SSLSocket _SocketPI = null;
    private zDTP_SSL _DTP = null;

    private BufferedReader _BrPI = null;
    private BufferedWriter _BwPI = null;

    private String _UserToken;
    private String _UserSession;
    
    public zPI_SSL(SSLSocket socket) throws IOException {
    	NumOfPI++;
    	_DTP = new zDTP_SSL(10000 + NumOfPI);
    	_SocketPI = socket;
        _BrPI = new BufferedReader(new InputStreamReader(_SocketPI.getInputStream(), "UTF-8"));
        _BwPI = new BufferedWriter(new OutputStreamWriter(_SocketPI.getOutputStream(), "UTF-8"));
    } 
    
    public void run() {

    	try {
			BufferUtil.Write(_BwPI, "220 Connection established");
			String Request = null;
	    	String Command = null;
	    	User user = new User();
	    	while (true) {
	        	Request = BufferUtil.Read(_BrPI);
	        	System.out.println(Request);
	        	Command = Request.split("\\s")[0];
	        	switch (Command) {
	        	
				case "USER":
					user.setUsername(Request.split("\\s")[1]);
					zCommand.USERcommand(user, _BwPI);
					break;
				case "PASS":
					user.setPassword(Request.split("\\s")[1]);
					zCommand.PASScommand(user, _BwPI);
					break;
				case "SYST":
					zCommand.SYSTcommand(_BwPI);
				case "FEAT":
					zCommand.FEATcommand(_BwPI);				
					break;
				case "PWD":
					zCommand.PWDcommand(_BwPI);
					break;
				case "TYPE":
					String type = Request.split("\\s")[1];
					zCommand.TYPEcommand(type, _BwPI);
					break;
				case "PASV":
					int port = _DTP.get_Port();
					zCommand.PASVcommand(port, _BwPI);
					break;
//				case "PORT":
//					BufferUtil.Write(bw, "125 abc");
//					break; 
				case "LIST": 
					zCommand.LISTcommand_TLS(_DTP, _BwPI);
					break;
				case "CWD":
					zCommand.CWDcommand(_BwPI);
					break;
				case "RETR":
					zCommand.RERTcommand(_DTP, _BwPI);
					break;
				case "PBSZ": // Tham so 0
					zCommand.PBSZcommand(_BwPI);
					break;
				case "PROT":
					String protectMode = Request.split("\\s")[1];
					zCommand.PROTcommand(protectMode, _BwPI);
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
