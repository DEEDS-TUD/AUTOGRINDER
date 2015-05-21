package de.grinder.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;

import de.grinder.GrinderClient;
import de.grinder.database.Target;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TargetSummary extends JPanel {
  private static final long serialVersionUID = 1L;
  
  public TargetSummary(final Target target, final String name) {
    setBorder(new TitledBorder(null, "Target System", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[] {65, 232, 90};
    gbl_panel.rowHeights = new int[] { 20, 0 };
    gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0 };
    gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
    this.setLayout(gbl_panel);

    JLabel lblName = new JLabel("Name");
    GridBagConstraints gbc_lblName = new GridBagConstraints();
    gbc_lblName.anchor = GridBagConstraints.WEST;
    gbc_lblName.insets = new Insets(0, 0, 0, 5);
    gbc_lblName.gridx = 0;
    gbc_lblName.gridy = 0;
    this.add(lblName, gbc_lblName);

    JTextField textFieldName = new JTextField(name);
    lblName.setLabelFor(textFieldName);
    textFieldName.setEditable(false);
    GridBagConstraints gbc_textFieldName = new GridBagConstraints();
    gbc_textFieldName.insets = new Insets(0, 0, 0, 5);
    gbc_textFieldName.fill = GridBagConstraints.HORIZONTAL;
    gbc_textFieldName.gridx = 1;
    gbc_textFieldName.gridy = 0;
    this.add(textFieldName, gbc_textFieldName);
    textFieldName.setColumns(10);
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.gridx = 2;
    gbc_btnNewButton.gridy = 0;
    JButton btnNewButton = new JButton("Open");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame();
        frame.setTitle(name);
        frame.add(new TargetPanel(GrinderClient.getGrinder().getExecutor().addController(target)));
        frame.pack();
        frame.setVisible(true);
      }
    });
    add(btnNewButton, gbc_btnNewButton);
  }
}
