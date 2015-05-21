package de.grinder.executor.servers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import de.grinder.executor.MessageHandler;
import de.grinder.executor.servers.TCPServer;
import de.grinder.util.message.Message;
import de.grinder.util.message.MessageType;

public class TCPServerTest {
  
  private static final String HOSTNAME = "localhost";
  private static final int PORT = 7890;

  private class Client implements Runnable {
    public void run() {
      try (Socket socket = new Socket(HOSTNAME, PORT);
          OutputStream toServer = socket.getOutputStream();
          InputStream fromServer = socket.getInputStream();) {
        byte[] message = new byte[6];
        message[0] = 0;
        message[1] = 2;
        message[2] = 0;
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        toServer.write(message);
      } catch (Exception e) {
        fail("Exception: " + e);
      }
    }
  }

  @Test
  @Ignore
  public void test() throws InterruptedException {
    MessageHandler handler = mock(MessageHandler.class);
    when(handler.handle(any(Message.class))).thenReturn(null);
    
    TCPServer server = new TCPServer();
    server.setPort(PORT);
    server.setMessageHandler(handler);
    new Thread(server).start();
    
    new Client().run();
    
    ArgumentCaptor<Message> message = ArgumentCaptor.forClass(Message.class);
    verify(handler).handle(message.capture());
    assertEquals(MessageType.FINISH_EXPERIMENT, message.getValue().getType());
  }
}