package net.johjoh.nexus.cloud.server.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ManagedFileOutputStream extends OutputStream {

	private FileOutputStream fileOut;
	
	public ManagedFileOutputStream(FileOutputStream fileOut) {
		this.fileOut = fileOut;
	}
	
	public void setFileOutputStream(FileOutputStream fileOut) {
		this.fileOut = fileOut;
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		fileOut.write(b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		fileOut.write(b, off, len);
	}
	
	@Override
	public void write(int b) throws IOException {
		fileOut.write(b);
	}
	
	@Override
	public void flush() throws IOException {
		fileOut.flush();
	}
	
	@Override
	public void close() throws IOException {
		fileOut.flush();
	}

}
