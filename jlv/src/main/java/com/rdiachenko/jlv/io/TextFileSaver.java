package com.rdiachenko.jlv.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TextFileSaver {

	private PrintWriter fileWriter = null;
	
	public TextFileSaver(File file) throws FileNotFoundException {
		if (file == null) {
			throw new NullPointerException("File should not be null.");
		}
		fileWriter = new PrintWriter(file);
	}
	
	public void sendTextToFile(String text) {
		fileWriter.println(text);
	}
	
	public void flushAndClose() {
		if (fileWriter != null) {
			fileWriter.flush();
			fileWriter.close();
		}
	}
}
