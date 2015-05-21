package de.grinder.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AboutDialog extends JDialog {

  private static final long serialVersionUID = 1L;

  /**
   * Create the dialog.
   */
  public AboutDialog(JFrame owner) {
    super(owner, true);
    setResizable(false);
    setSize(300, 200);
    setLocationRelativeTo(getParent());
    setTitle("About Injection Framework");
    
    JPanel content = new JPanel();
    getContentPane().add(content, BorderLayout.CENTER);
    GridBagLayout gbl_content = new GridBagLayout();
    gbl_content.columnWidths = new int[]{29, 0};
    gbl_content.rowHeights = new int[]{254, 0};
    gbl_content.columnWeights = new double[]{1.0, Double.MIN_VALUE};
    gbl_content.rowWeights = new double[]{1.0, Double.MIN_VALUE};
    content.setLayout(gbl_content);
    
    JLabel lblNewLabel = new JLabel("Injection Framework");
    lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
    gbc_lblNewLabel.gridx = 0;
    gbc_lblNewLabel.gridy = 0;
    content.add(lblNewLabel, gbc_lblNewLabel);
    
    JPanel buttons = new JPanel();
    getContentPane().add(buttons, BorderLayout.SOUTH);
    
    JButton btnOk = new JButton("Ok");
    btnOk.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        setVisible(false);
      }
    });
    btnOk.setActionCommand("Ok");
    buttons.add(btnOk);
  }
}
