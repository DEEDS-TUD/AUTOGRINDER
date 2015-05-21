package de.grinder.autosar;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiDebugger {
  private static final Logger LOGGER = LoggerFactory.getLogger(MultiDebugger.class);

  private Socket socket;

  private PrintWriter out; 

  private BufferedReader debuggerOutput;

  public void start(File image, int port) throws IOException {
  /*
   * Uncomment the following block if MULTI debugger shall be invoked by AUTOGRINDER
   */

	/*
	System.out.println("<> Executing MultiDebugger.start with port = " + port);

    // Configure command
    final ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
    processBuilder.command(new String[] { "C:\\ghs\\multi_614\\multi.exe",
        "-socket", Integer.toString(port), image.getAbsolutePath() });

    // Start the debugger
    try {
      process = processBuilder.start();
    } catch (IOException e) {

      // Cleanup failed process
      if (process != null) {
        process.destroy();
        process = null;
      }
      throw e;
    }

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e1) {
      // empty
    }
*/
    // Open socket to debugger
    try {
      socket = new Socket("127.0.0.1", port);
      
      out = new PrintWriter(socket.getOutputStream(), true);
      debuggerOutput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
    	System.out.println("<> Socket opening on port " + port + " failed with : " + e.getMessage());
      // TODO Auto-generated catch block
    }
  }
  
  public boolean isAvailable() {
    return !(socket == null || socket.isClosed());
  }

  public MultiDebugger send(String command) throws IOException {
    if (!isAvailable()) {
      throw new IOException("MULTI Debugger is not available");
    }

    LOGGER.info("Sending \"{}\" to debugger.", command);
    out.println(command);
    
    return this;
  }

  public String readLine() throws IOException {
	  return debuggerOutput.readLine();
  }

  public void stop() throws IOException {
	  LOGGER.info("Closing socket {}.", socket.toString());
	  socket.close();
  }
}
