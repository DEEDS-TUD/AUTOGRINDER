package de.grinder.controller;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.grinder.GrinderClient;
import de.grinder.Settings;
import de.grinder.database.Database;
import de.grinder.database.Target;
import de.grinder.ui.TargetOverview;

/**
 * Action to add a new target system description to the database.
 * 
 * Note that adding a target to the database does not instantiate a
 * TargetController, but saves the information how the TargetController should
 * be instantiated.
 * 
 * @author Michael Tretter
 * 
 */
public class NewTargetAction extends AbstractAction {
  /*
   * TODO Reimplement in the configurator module.
   * 
   * This is a very crude implementation to add targets to the database. This
   * functionality should be contained in the configurator.
   */

  private static final long serialVersionUID = 1L;

  public NewTargetAction() {
    putValue(NAME, "New target...");
    putValue(SHORT_DESCRIPTION,
        "Create a new target from a given xml-Description");
  }

  public void actionPerformed(ActionEvent e) {

    JComponent component = (JComponent) e.getSource();
    JFrame frame = (JFrame) SwingUtilities.getRoot(component);

    JFileChooser fileChooser = new JFileChooser(Settings.getInstance()
        .getSetting("defaultConfigPath"));

    final File file;

    switch (fileChooser.showOpenDialog(frame)) {
    case JFileChooser.APPROVE_OPTION:
      file = fileChooser.getSelectedFile();
      break;
    default:
      return;
    }

    String configuration = getConfiguration(file);
    String name = getName(file);

    Target target = new Target();
    target.setName(name);
    target.setConfiguration(configuration);

    Database database = GrinderClient.getGrinder().getDatabase();
    database.addTarget(target);

    TargetOverview overview = ((TargetOverview) GrinderClient.window
        .getContentPane());
    overview.addTarget(target);
  }

  private String getName(File file) {
    return file.getName();
  }

  private String getConfiguration(File file) {
    StringBuilder sb = new StringBuilder();
    String line;
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      line = br.readLine();
      while (null != line) {
        sb.append(line);
        line = br.readLine();
      }
    } catch (IOException e) {
      // TODO Use logger for error message
      return "";
    }

    return sb.toString();
  }
}