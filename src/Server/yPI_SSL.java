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

import Util.*;


public class yPI_SSL extends Thread {
	public static int NumOfPI = 0;
    private Socket _SocketPI = null;
    private yDTP_SSL _DTP = null;
    private Socket _SocketDTP = null;
    private BufferedReader _BrPI = null;
    private BufferedWriter _BwPI = null;
    private BufferedReader _BrDTP = null;
    private BufferedWriter _BwDTP = null;
    private String _UserToken;
    private String _UserSession;
    
    public yPI_SSL(Socket socket) throws IOException {
    	NumOfPI++;
    	_DTP = new yDTP_SSL(10000 + NumOfPI);
    	_SocketPI = socket;
        _BrPI = new BufferedReader(new InputStreamReader(_SocketPI.getInputStream()));
        _BwPI = new BufferedWriter(new OutputStreamWriter(_SocketPI.getOutputStream()));
    } 
    
    public void run() {
    	System.out.println("Helu");
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
					if (checkLogin(user)) {
						BufferUtil.Write(_BwPI, "331 User " + user.getUsername() + " OK. Password required");
	    			}
	    			else {
	    				BufferUtil.Write(_BwPI, "500 USER: Operation not permitted");
	    			}
					break;
				case "PASS":
					user.setPassword(Request.split("\\s")[1]);
					if (checkLogin(user)) {
						BufferUtil.Write(_BwPI, "230-Your bandwidth usage is restricted");
						BufferUtil.Write(_BwPI,  "230 Current directory is /");
	    			}
	    			else {
	    				BufferUtil.Write(_BwPI, "500 USER: Operation not permitted");
	    			}
					break;
				case "SYST":
					BufferUtil.Write(_BwPI, "215 " + System.getProperty("os.name"));
				case "FEAT":
					BufferUtil.Write(_BwPI, "211-Features:");
					BufferUtil.Write(_BwPI, "AUTH PLAIN");
					BufferUtil.Write(_BwPI, "CCC");
					BufferUtil.Write(_BwPI, "CLNT");
					BufferUtil.Write(_BwPI, "EPRT");
					BufferUtil.Write(_BwPI, "PASV");
					BufferUtil.Write(_BwPI, "211 End");					
					break;
				case "PWD":
					BufferUtil.Write(_BwPI, "257 \"/\" is your current location");
					break;
				case "TYPE":
					if(Request.split("\\s")[1].equalsIgnoreCase("I")) {
						BufferUtil.Write(_BwPI, "200 TYPE is now 8-bit binary");
					}
					else if(Request.split("\\s")[1].equalsIgnoreCase("A")) {
						BufferUtil.Write(_BwPI, "200 TYPE is now ASCII");
					}
					break;
				case "PASV":
					int Port = _DTP.get_Port();
					int a, b;
					a = Port/256;
					b = Port%256;
					BufferUtil.Write(_BwPI, "227 Entering Passive Mode (127,0,0,1," + a + "," + b + ")");
					break;
//				case "PORT":
//					BufferUtil.Write(bw, "125 abc");
//					break; 
				case "LIST": 
					PORTCommand();
					ArrayList<String> listFileAndFolder = FilesUtil.ListFileAndFolder("H:\\CLIENT\\TEST_01");
					for (String string : listFileAndFolder) {
						BufferUtil.Write(_BwDTP, string);
					}			
					BufferUtil.Write(_BwPI, "226 Xong Port command");
					_SocketDTP.close();
					break;
				case "CWD":
					BufferUtil.Write(_BwPI, "250 Okay.");
					break;
				case "RETR":
					RETRCommand();
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
    
    private boolean checkLogin(User user) {
    	return true;
    }
    
    private void PORTCommand () throws IOException {
    	_SocketDTP = _DTP.Accept();
		_BwDTP = new BufferedWriter(new OutputStreamWriter(_SocketDTP.getOutputStream()));
		BufferUtil.Write(_BwPI, "200 Port command");
    }
    
    private void RETRCommand () throws IOException {
    	_SocketDTP = _DTP.Accept();
    	_BwDTP = new BufferedWriter(new OutputStreamWriter(_SocketDTP.getOutputStream()));
    	BufferUtil.Write(_BwPI, "150 Opening");
		String path = "H:\\CLIENT\\TEST_01\\Test_01_f_01.txt";
		Send(path);
		BufferUtil.Write(_BwPI, "226 Successfully transferred");
		_SocketDTP.close();
    }
    
    private void Send(String path) throws IOException {
    	File f = new File(path);
    	FileInputStream fin = new FileInputStream(f);
        int c;
        do {
            c = fin.read();
            if(c != -1)
            	BufferUtil.Write(_BwDTP, String.valueOf(c));
            else
            	break;
        } while(true);
        fin.close();
    }
}
