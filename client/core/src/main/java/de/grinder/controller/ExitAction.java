package de.grinder.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ExitAction extends AbstractAction{
  private static final long serialVersionUID = 1L;

  public ExitAction() {
    putValue(NAME, "Exit");
    putValue(SHORT_DESCRIPTION, "Some short description");
  }

  public void actionPerformed(ActionEvent e) {
    System.exit(0);
  }
}
