package de.grinder.util.message;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy for communication with GRINDER
 * 
 * @author Michael Tretter
 * 
 */
public class Proxy {
  private static final Logger LOGGER = LoggerFactory.getLogger(Proxy.class);
  
  private Socket socket;
  private String host;
  private Integer port;

  public Proxy(String host, int port) {
    super();
    this.host = host;
    this.port = port;
  }

  /**
   * Establishes a connection to the server
   * 
   * @throws IOException
   */
  public void connect() throws IOException {
    socket = new Socket(host, port);
    LOGGER.info("Connected to {}:{}.", host, port);
  }

  /**
   * Closes the connection to the server
   * 
   * @throws IOException
   */
  public void disconnect() throws IOException {
    socket.close();
    LOGGER.info("Disconnected from {}:{}.", host, port);
  }

  /**
   * Tells the current experiment configuration as String.
   * 
   * @return String containing the current configuration
   * @throws IOException
   */
  public String getConfiguration() throws IOException {
    Message request = new Message(MessageType.GET_CONFIGURATION);
    LOGGER.debug("Sending request: {}", request);
    
    socket.getOutputStream().write(request.getBytes());
    socket.getOutputStream().flush();

    LOGGER.debug("Processing Response.");
    Message response = new Message();
    byte[] header = new byte[Message.HEADER_SIZE];
    socket.getInputStream().read(header);
    response.setHeader(header);

    byte[] config = new byte[response.getLength()];
    socket.getInputStream().read(config);

    LOGGER.debug("Received configuration.");
    return new String(config);
  }

  public void log(String logEntry) throws IOException {
    Message request = new Message(MessageType.LOG);
    request.setBody(logEntry.getBytes());
    LOGGER.info("Sending request: {}", request);
    
    socket.getOutputStream().write(request.getBytes());
    socket.getOutputStream().flush();
    
    return;
  }

  public void finishExperiment() throws IOException {
    Message request = new Message(MessageType.FINISH_EXPERIMENT);
    LOGGER.info("Sending request: {}", request);
    
    socket.getOutputStream().write(request.getBytes());
    socket.getOutputStream().flush();
    
    return;
  }
}