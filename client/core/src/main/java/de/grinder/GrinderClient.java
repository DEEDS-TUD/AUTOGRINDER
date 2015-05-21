package de.grinder;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.ui.MainWindow;
import de.grinder.ui.TargetOverview;

/**
 * This class implements the user interface for GRINDER.
 * 
 * @author Michael Tretter
 * 
 */
public class GrinderClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(GrinderClient.class);

  private static final Grinder grinder = new Grinder();
  
  public static JFrame window;

  /**
   * Returns the instance of the GRINDER server.
   * 
   * @return The GRINDER server
   */
  public static Grinder getGrinder() {
    return grinder;
  }

  /**
   * Starts the execution of the GRINDER user interface.
   * 
   * @param args
   *          Command line arguments are not used
   */
  public static void main(final String[] args) {
    LOGGER.info("Starting the GRINDER client");

    try {
//      UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      LOGGER.warn("Could not get system look and feel - Using default");
    }

    window = new MainWindow();
    window.setContentPane(new TargetOverview());
    window.pack();
    window.setVisible(true);
  }

}
