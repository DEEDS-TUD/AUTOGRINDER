package de.grinder.executor.servers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.executor.MessageHandler;
import de.grinder.util.message.Message;

/**
 * Translates between GRINDER messages and streams.
 * 
 * @author Michael Tretter
 * 
 */
public class MessageStream implements Runnable {

  /*
   * FIXME Bad implementation of IO.
   * 
   * Must close input and output streams.
   */

  private final static Logger LOGGER = LoggerFactory
      .getLogger(MessageStream.class);

  /**
   * Stream for receiving data from the target system.
   */
  private InputStream fromTarget = null;

  /**
   * Stream for sending data to the target system.
   */
  private OutputStream toTarget = null;

  /**
   * Handler of received messages.
   */
  private MessageHandler handler;

  @Override
  public void run() {
    LOGGER.debug("Start receiving messages.");
    while (true) {
      try {
        Message message = receive();
        if (null == message) {
          LOGGER.error("Received invalid message: {}", message);
        }

        // No response, if response is null.
        Message response = handler.handle(message);
        if (null != response) {
          send(response);
        }
      } catch (IOException e) {
        LOGGER.error("IOException while receiving message: {}", e.getMessage());
        break;
      }
    }
  }

  /**
   * Sets the handler that handles incoming messages.
   * 
   * @param handler
   */
  public void setHandler(final MessageHandler handler) {
    this.handler = handler;
  }

  public void setFromTarget(final InputStream fromTarget) {
    this.fromTarget = fromTarget;
  }

  public void setToTarget(final OutputStream toTarget) {
    this.toTarget = toTarget;
  }

  /**
   * Receives the next message from the stream.
   * 
   * @return Received message
   * @throws IOException
   *           If the stream cannot be read
   */
  private Message receive() throws IOException {
    Message message = new Message();

    // Read the header
    final byte[] header = new byte[Message.HEADER_SIZE];
    if (-1 == fromTarget.read(header)) {
      throw new IOException("Reached end of stream.");
    }

    try {
      message.setHeader(header);
    } catch (RuntimeException e) {
      LOGGER.error(e.getMessage());
      return null;
    }

    // Read the body, if required
    if (message.hasBody()) {
      final byte[] body = new byte[message.getLength()];
      fromTarget.read(body);
      message.setBody(body);
    }

    return message;
  }

  /**
   * Sends the given message to the target system.
   * 
   * @param message
   *          The message to be sent
   */
  public void send(final Message message) {
    byte[] buffer = message.getBytes();

    try {
      toTarget.write(buffer);
      toTarget.flush();
    } catch (IOException e) {
      LOGGER.error("Failed to send message.");
    }
  }
}
