package de.grinder.executor.servers;

import org.junit.Test;

import static org.mockito.Mockito.*;
import de.grinder.executor.MessageHandler;
import de.grinder.executor.servers.MessageStream;
import de.grinder.util.message.Message;
import de.grinder.util.message.MessageType;

public class MessageStreamTest {
  @Test
  public void testLogMessage() {
    MessageStream stream = new MessageStream();
    
    MessageHandler handler = mock(MessageHandler.class);
    stream.setHandler(handler);
    
    createMessage("Hello World!");
    // TODO Implement test
    
    //verify(handler).handle(any(Message.class));
  }
  
  private Message createMessage(String content){
    Message message = new Message();
    message.setType(MessageType.LOG);
    message.setRunId((short) 0);
    message.setBody(content.getBytes());
    return message;
  }
}