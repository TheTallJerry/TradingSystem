package client.guiandpresenter.adminscreen.adminthreshold;

import client.controllers.AdminSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.adminscreen.adminmenu.AdminMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class responsible for admins to search information by username, trade and meetings
 */
public class AdminThresholdScreen extends Screen implements ActionListener {
    private final JPanel panel;
    private final JComboBox<String> targetBox;
    private final JTextField newLimFld;
    private final AdminThresholdPresenter presenter;
    private final JButton changeBtn;
    private final JLabel targetLbl, currLimLbl, currLimFld, newLimLbl;

    private final AdminSystem adminSystem;

    /**
     * Construct a <code>AdminThresholdScreen</code> for the administration users to set the threshold of the screen
     *
     * @param serializer  the serializer that will be used for initialize
     * @param adminSystem a instance of AdminSystem, which is a controller for administration users
     */
    public AdminThresholdScreen(DataSerializer serializer, AdminSystem adminSystem) {
        super(serializer);
        this.adminSystem = adminSystem;
        this.presenter = new AdminThresholdPresenter();
        panel = new JPanel();
        targetBox = new JComboBox<>(presenter.targetThreshold());
        targetLbl = new JLabel(presenter.limitType());
        currLimLbl = new JLabel(presenter.currentLimit());
        currLimFld = new JLabel("");
        newLimLbl = new JLabel(presenter.newLimit());
        newLimFld = new JTextField();
        changeBtn = new JButton(presenter.changeBtn());

        setup();
    }

    /*Set up the screen*/
    private void setup() {
        targetLbl.setFont(fontPrompt);
        targetBox.setFont(fontPrompt);
        currLimLbl.setFont(fontPrompt);
        newLimLbl.setFont(fontPrompt);
        changeBtn.setFont(fontPrompt);
        newLimFld.setFont(fontInput);
        currLimFld.setFont(fontInput);
        frame.setTitle(presenter.getTitle());
        returnItem.addActionListener(this);
        changeBtn.addActionListener(this);

        frame.setTitle(presenter.thresholdScreenTitle());
        panel.setLayout(new GridBagLayout());
        targetBox.setEditable(false);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(targetBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        targetBox.addActionListener(this);

        panel.add(targetLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;

        panel.add(currLimLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(currLimFld, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        currLimFld.setText(String.valueOf(adminSystem.getDataBundle().minLendBorrowDifference));

        panel.add(newLimLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(newLimFld, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(changeBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Listen to the actions performed and response to these actions
     *
     * @param e action performed by the admin who is using the system
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnItem) {
            new AdminMenuScreen(serializer, adminSystem);
            frame.dispose();
        } else if (e.getSource() == targetBox) {
            String msg;
            if (targetBox.getSelectedItem() == null) msg = "";
            else msg = (String) targetBox.getSelectedItem();
            if (msg != null) {
                if (msg.equals(presenter.targetThreshold()[0]))
                    currLimFld.setText(adminSystem.getBorrowLendDifference());
                if (msg.equals(presenter.targetThreshold()[1]))
                    currLimFld.setText(adminSystem.getWeeklyTransactionLimit());
                if (msg.equals(presenter.targetThreshold()[2]))
                    currLimFld.setText(adminSystem.getMeetingEditLimit());
                if (msg.equals(presenter.targetThreshold()[3]))
                    currLimFld.setText(adminSystem.getIncompleteTrade());
                if (msg.equals(presenter.targetThreshold()[4]))
                    currLimFld.setText(adminSystem.getMeetingLateLimit());
            }
        } else if (e.getSource() == changeBtn) {
            String msg = (String) targetBox.getSelectedItem();
            if (msg != null) {
                if (msg.equals(presenter.targetThreshold()[0])) {
                    JOptionPane.showMessageDialog(frame, presenter.changeThreshold(adminSystem.setBorrowLendDifference(
                            newLimFld.getText())));
                    currLimFld.setText(adminSystem.getBorrowLendDifference());
                }
                if (msg.equals(presenter.targetThreshold()[1])) {
                    JOptionPane.showMessageDialog(frame, presenter.changeThreshold(adminSystem.setWeeklyTransactionLimit(
                            newLimFld.getText())));
                    currLimFld.setText(adminSystem.getWeeklyTransactionLimit());
                }
                if (msg.equals(presenter.targetThreshold()[2])) {
                    JOptionPane.showMessageDialog(frame, presenter.changeThreshold(adminSystem.setMeetingEditLimit(
                            newLimFld.getText())));
                    currLimFld.setText(adminSystem.getMeetingEditLimit());
                }
                if (msg.equals(presenter.targetThreshold()[3])) {
                    JOptionPane.showMessageDialog(frame, presenter.changeThreshold(adminSystem.setIncompleteTradeLimit(
                            newLimFld.getText())));
                    currLimFld.setText(adminSystem.getIncompleteTrade());
                }
                if (msg.equals(presenter.targetThreshold()[4])) {
                    JOptionPane.showMessageDialog(frame, presenter.changeThreshold(adminSystem.setMeetingLateLimit(
                            newLimFld.getText())));
                    currLimFld.setText(adminSystem.getMeetingLateLimit());
                }
            }
            newLimFld.setText("");
        }
    }
}
