package de.grinder.ui;

import javax.swing.JFrame;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import javax.swing.JSeparator;

import de.grinder.controller.AboutAction;
import de.grinder.controller.ActionHandler;
import de.grinder.controller.ExitAction;
import de.grinder.controller.NewTargetAction;

public class MainWindow extends JFrame {
  private static final long serialVersionUID = 1L;
  private static final String TITLE = "GRINDER";

  public MainWindow() {
    super(TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setJMenuBar(menuBar());
  }

  /**
   * Builds the menu bar for the main window.
   * 
   * @return menubar
   */
  private JMenuBar menuBar() {
    JMenuBar menuBar = new JMenuBar();
    
    JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);
    
    JMenuItem mntmNewTarget = new JMenuItem();
    mntmNewTarget.setAction(ActionHandler.getActionFor(NewTargetAction.class));
    mnFile.add(mntmNewTarget);
    
    JSeparator separator = new JSeparator();
    mnFile.add(separator);
    
    JMenuItem mntmQuit = new JMenuItem();
    mntmQuit.setAction(ActionHandler.getActionFor(ExitAction.class));
    mnFile.add(mntmQuit);
    
    JMenu mnHelp = new JMenu("Help");
    menuBar.add(mnHelp);
    
    JMenuItem mntmAboutInjectionFramework = new JMenuItem();
    mntmAboutInjectionFramework.setAction(ActionHandler.getActionFor(AboutAction.class));
    mnHelp.add(mntmAboutInjectionFramework);
    
    return menuBar;
  }
}
