package de.grinder.ui;

import javax.swing.JPanel;

import de.grinder.GrinderClient;
import de.grinder.database.Target;

import java.util.Collection;
import java.awt.BorderLayout;

import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

/**
 * Displays the overview of all targets that are registered at GRINDER.
 * 
 * @author Michael Tretter
 * 
 */
public class TargetOverview extends JPanel {
  private static final long serialVersionUID = 1L;

  private JPanel inner = new JPanel();

  public TargetOverview() {
    super();
    
    setLayout(new BorderLayout(0, 0));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    add(inner, BorderLayout.NORTH);
    inner.setLayout(new GridLayout(0, 1, 5, 5));

    Collection<Target> targets = GrinderClient.getGrinder().getDatabase()
        .getTargets();
    for (Target target : targets) {
      addTarget(target);
    }
  }

  public void addTarget(Target target) {
    TargetSummary summary = new TargetSummary(target, target.getName());
    inner.add(summary);
    validate();
    repaint();
  }
}
