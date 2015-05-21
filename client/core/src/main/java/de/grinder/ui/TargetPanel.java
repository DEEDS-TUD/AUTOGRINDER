package de.grinder.ui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

import java.awt.GridLayout;
import java.awt.Component;
import java.awt.BorderLayout;

import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.GrinderClient;
import de.grinder.database.Campaign;
import de.grinder.executor.TargetController;

import java.awt.FlowLayout;

import javax.swing.SwingConstants;

import java.awt.CardLayout;

import javax.swing.JTable;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TargetPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TargetPanel.class);
  
  private TargetController controller;

  private final Action startAction = new StartAction();
  private final Action stopAction = new StopAction();
  private final Action resetAction = new ResetAction();
  private JTable table;
  private final Action action = new LoadAction();

  public TargetPanel(TargetController controller) {
    super();
    
    this.controller = controller;

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 449, 0 };
    gridBagLayout.rowHeights = new int[] { 57, 74, 0 };
    gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);
    JPanel controlPanel = new JPanel();
    GridBagConstraints gbc_controlPanel = new GridBagConstraints();
    gbc_controlPanel.anchor = GridBagConstraints.NORTH;
    gbc_controlPanel.fill = GridBagConstraints.HORIZONTAL;
    gbc_controlPanel.insets = new Insets(0, 0, 5, 0);
    gbc_controlPanel.gridx = 0;
    gbc_controlPanel.gridy = 0;
    add(controlPanel, gbc_controlPanel);
    controlPanel.setBorder(new TitledBorder(null, "Control",
        TitledBorder.LEADING, TitledBorder.TOP, null, null));
    controlPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

    JButton btnStart = new JButton("Start");
    btnStart.setHorizontalAlignment(SwingConstants.LEADING);
    controlPanel.add(btnStart);
    btnStart.setAction(startAction);
    btnStart.setAlignmentY(Component.TOP_ALIGNMENT);

    JButton btnStop = new JButton("Stop");
    btnStop.setHorizontalAlignment(SwingConstants.LEADING);
    controlPanel.add(btnStop);
    btnStop.setAction(stopAction);
    btnStop.setAlignmentY(Component.TOP_ALIGNMENT);

    JButton btnReset = new JButton("Reset");
    controlPanel.add(btnReset);
    btnReset.setAction(resetAction);
    btnReset.setAlignmentY(Component.TOP_ALIGNMENT);

    JPanel campaignPanel = new JPanel();
    GridBagConstraints gbc_campaignPanel = new GridBagConstraints();
    gbc_campaignPanel.anchor = GridBagConstraints.NORTH;
    gbc_campaignPanel.fill = GridBagConstraints.HORIZONTAL;
    gbc_campaignPanel.gridx = 0;
    gbc_campaignPanel.gridy = 1;
    add(campaignPanel, gbc_campaignPanel);
    campaignPanel.setBorder(new TitledBorder(null, "Campaign",
        TitledBorder.LEADING, TitledBorder.TOP, null, null));
    campaignPanel.setLayout(new BorderLayout(2, 2));

    JPanel panel_1 = new JPanel();
    campaignPanel.add(panel_1, BorderLayout.CENTER);
    panel_1.setLayout(new CardLayout(0, 0));

    JPanel panel_4 = new JPanel();
    panel_1.add(panel_4, "name_19502791517129");
    panel_4.setLayout(new BorderLayout(0, 0));

    table = new JTable();
    table
        .setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
    table.setFillsViewportHeight(true);
    panel_4.add(table);

    JPanel panel_2 = new JPanel();
    campaignPanel.add(panel_2, BorderLayout.EAST);
    panel_2.setLayout(new GridLayout(0, 1, 2, 2));

    JButton btnNew = new JButton("New");
    btnNew.setAction(action);
    panel_2.add(btnNew);

  }

  private class StartAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public StartAction() {
      putValue(NAME, "Start");
      putValue(SHORT_DESCRIPTION, "Starts the target system.");
    }

    public void actionPerformed(ActionEvent e) {
      LOGGER.info("Request target start.");
      controller.start();
    }
  }

  private class StopAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public StopAction() {
      putValue(NAME, "Stop");
      putValue(SHORT_DESCRIPTION, "Stops the target system.");
    }

    public void actionPerformed(ActionEvent e) {
      controller.stop();
    }
  }

  private class ResetAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public ResetAction() {
      putValue(NAME, "Reset");
      putValue(SHORT_DESCRIPTION, "Resets the target system.");
    }

    public void actionPerformed(ActionEvent e) {
      LOGGER.info("Request target reset.");
      controller.reset();
    }
  }

  private class LoadAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public LoadAction() {
      putValue(NAME, "Load");
      putValue(SHORT_DESCRIPTION, "Loads a campaign");
    }

    public void actionPerformed(ActionEvent e) {
      Object[] campaigns =  GrinderClient.getGrinder().getDatabase().getCampaigns().toArray();
      
      LOGGER.debug("# Campaigns = " + campaigns.length);

      
      Campaign campaign = (Campaign) JOptionPane.showInputDialog(
          TargetPanel.this, "Choose campaign to load on target",
          "Choose campaign", JOptionPane.PLAIN_MESSAGE, null, campaigns,
          campaigns[0]);

      LOGGER.debug("Campaign selected: {}", campaign);

      if (null != campaign) {
        controller.setCampaign(campaign);
      table.setModel(getTableModel(campaign));
      }
    }
  }

  /**
   * Returns a {@link TableModel} for the given {@link Campaign}
   * 
   * @param campaign
   *          The campaign that is represented by the TableModel
   * @return tableModel for the given campaign
   */
  private TableModel getTableModel(final Campaign campaign) {
    /*
     * This is a very crude preliminary implementation. It shows the intention
     * of presenting the test cases but lacks everything else.
     */

    return new AbstractTableModel() {
      private static final long serialVersionUID = 1L;

      private Campaign c = campaign;

      @Override
      public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
          return c.getTestCases().get(rowIndex).getId();
        case 1:
          return c.getTestCases().get(rowIndex).getConfiguration();
        default:
          return "-";
        }
      }

      @Override
      public int getRowCount() {
        return c.size();
      }

      @Override
      public int getColumnCount() {
        return 2;
      }
    };
  }
}
