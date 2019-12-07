package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.HashMap;

import com.google.gson.Gson;

import Util.BufferUtil;
import Util.CONFIG;
import Util.FilesUtil;
import Util.JsonUtil;

public class Server_ThreadDTP extends Thread {
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	private String UserSession;
	private String UserToken; 

	public Server_ThreadDTP() {
		super();
	}

	public Server_ThreadDTP(Socket _socket) throws IOException {
		super();
		this.socket = _socket;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));	
	}
	//@Override
	public void run() {
//		try {
//			while (true) {
//				String Request = BufferUtil.Read(br);
//				
//				BufferUtil.Write(bw, Response);
//			}			
//		} catch (Exception e) {
//		}
	}
//	private String Verify(String _session, String _token) {
//		UserSession = _session;
//		UserToken = _token;
//		return "success";
//	}
	private String isFolder(String _path) {
		String OriginPath = CONFIG.PATH_UPLOAD + "\\" +_path;
		boolean response = FilesUtil.isFolder(OriginPath);
		CONFIG.print("DTP->isFolder->" + OriginPath + " = " + response);
		return response ? "success" : "fail";
	}
	private String isFile(String _path) {
		String OriginPath = CONFIG.PATH_UPLOAD + "\\" + _path;
		boolean response = FilesUtil.isFile(OriginPath);
		CONFIG.print("DTP->isFile->" + OriginPath + " = " + response);
		return response ? "success" : "fail";
	}	
}
