package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class BufferUtil {
	public static void Write(BufferedWriter _bw, String _str) throws IOException {
		_bw.write(_str);
		_bw.newLine();
		_bw.flush();
	}
	public static String Read(BufferedReader _br) throws IOException {
		return _br.readLine();
	}
}
