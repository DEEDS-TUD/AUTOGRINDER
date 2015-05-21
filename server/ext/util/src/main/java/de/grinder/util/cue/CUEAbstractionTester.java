package de.grinder.util.cue;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;

/**
 * This class provides a simple GUI for tests of a CUEAbstraction.
 * 
 * TODO This is a quick and dirty implementation that definitely needs review.
 * 
 * @author Michael Tretter
 * 
 */
public class CUEAbstractionTester {
  private JTextArea output;
  
  private final CUEAbstraction cueAbstraction;
  
  public CUEAbstractionTester(CUEAbstraction cueAbstraction) {
    super();
    this.cueAbstraction = cueAbstraction;
  }

  public void show() {
    redirectSystemStreams();

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      // Fall back to default look and feel
    }

    JFrame frame = new JFrame("CUEAbstraction Tester");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout(0, 0));

    JPanel main = new JPanel();
    frame.getContentPane().add(main, BorderLayout.CENTER);
    GridBagLayout gbl_main = new GridBagLayout();
    gbl_main.columnWidths = new int[] { 650, 0 };
    gbl_main.rowHeights = new int[] { 33, 400, 0 };
    gbl_main.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
    gbl_main.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    main.setLayout(gbl_main);

    JPanel buttons = new JPanel();
    GridBagConstraints gbc_buttons = new GridBagConstraints();
    gbc_buttons.anchor = GridBagConstraints.NORTH;
    gbc_buttons.fill = GridBagConstraints.HORIZONTAL;
    gbc_buttons.insets = new Insets(0, 0, 5, 0);
    gbc_buttons.gridx = 0;
    gbc_buttons.gridy = 0;
    main.add(buttons, gbc_buttons);

    JButton start = new JButton("Start");
    buttons.add(start);

    JButton stop = new JButton("Stop");
    buttons.add(stop);

    JButton reset = new JButton("Reset");
    buttons.add(reset);

    JButton run = new JButton("Run");
    buttons.add(run);
    run.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cueAbstraction.runExperiment();
      }
    });
    reset.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cueAbstraction.reset();
      }
    });
    stop.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cueAbstraction.stop();
      }
    });
    start.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cueAbstraction.start();
      }
    });

    output = new JTextArea();
    output.setLineWrap(true);
    output.setEditable(false);
    output.setTabSize(2);

    JScrollPane scrollPane = new JScrollPane(output);
    scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null,
        null, null, null));
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 1;
    main.add(scrollPane, gbc_scrollPane);

    frame.pack();
    frame.setVisible(true);
  }

  private void updateTextArea(final String text) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        output.append(text);
      }
    });
  }

  private void redirectSystemStreams() {
    OutputStream out = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        updateTextArea(String.valueOf((char) b));
      }

      @Override
      public void write(byte[] b, int off, int len) throws IOException {
        updateTextArea(new String(b, off, len));
      }

      @Override
      public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
      }
    };

    System.setOut(new PrintStream(out, true));
    System.setErr(new PrintStream(out, true));
  }
}