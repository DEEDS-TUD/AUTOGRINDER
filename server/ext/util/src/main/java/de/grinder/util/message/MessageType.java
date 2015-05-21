package de.grinder.util.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Types of messages for communication with the target system.
 * 
 * @author Michael Tretter
 * 
 */
public enum MessageType {

  NONE((short) 0), 
  GET_CONFIGURATION((short) 1), 
  FINISH_EXPERIMENT((short) 2), 
  LOG((short) 3), 
  SEND_CONFIGURATION((short) 4);

  private final short id;
  private static final Map<Short, MessageType> lookup = new HashMap<Short, MessageType>();

  private MessageType(short id) {
    this.id = id;
  }

  static {
    for (MessageType messageType : MessageType.values()) {
      lookup.put(messageType.id, messageType);
    }
  }

  public static MessageType getMessageType(short id) {
    MessageType type = lookup.get(id);
    if (type == null) {
      throw new RuntimeException(String.format("Illegal message type: %d", id));
    }
    return type;
  }

  public short getId() {
    return id;
  }
}
