package net.johjoh.nexus.cloud.server.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TeePrintStream extends PrintStream {
	
    private final PrintStream second;
    private final PrintStream main;

    public TeePrintStream(OutputStream main, PrintStream second) {
        super(main);
        this.main = new PrintStream(main, true);
        this.second = second;
    }

    /**
     * Closes the main stream. 
     * The second stream is just flushed but <b>not</b> closed.
     * @see java.io.PrintStream#close()
     */
    @Override
    public void close() {
        super.close();
    }

    @Override
    public void flush() {
        super.flush();
        second.flush();
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        second.write(buf, off, len);
    }

    @Override
    public void write(int b) {
        super.write(b);
        second.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        second.write(b);
    }
    
    public PrintStream getFilePrintStream() {
    	return main;
    }

}
