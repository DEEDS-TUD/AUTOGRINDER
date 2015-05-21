package de.grinder.executor.servers;

import de.grinder.executor.MessageHandler;

public abstract class AbstractListener implements Listener {

  protected MessageHandler handler;
  
  public void setMessageHandler(MessageHandler handler) {
    this.handler = handler;
  }
}