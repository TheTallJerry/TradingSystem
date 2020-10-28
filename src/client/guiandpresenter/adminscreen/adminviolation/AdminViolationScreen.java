package client.guiandpresenter.adminscreen.adminviolation;

import client.controllers.AdminSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.adminscreen.adminmenu.AdminMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * A gui class responsible for the information showed on screen of admin creation
 */
public class AdminViolationScreen extends Screen implements ActionListener {
    private final AdminSystem adminSystem;
    private final AdminViolationPresenter presenter;

    private final JPanel panel1;
    private final JComboBox<String> threshold;
    private final JButton unfreezeBtn;
    private final JButton freezeBtn;
    private final JList<String> userLst;
    private final DefaultListModel<String> model;

    /**
     * Construct a <code>AdminCreationScreen</code> for the creation of administration users
     *
     * @param serializer  the serializer that will be used for initialize
     * @param adminSystem a instance of AdminSystem, which is a controller for administration users
     */
    public AdminViolationScreen(DataSerializer serializer, AdminSystem adminSystem) {
        super(serializer);
        this.adminSystem = adminSystem;
        presenter = new AdminViolationPresenter();

        panel1 = new JPanel();
        userLst = new JList<>();
        freezeBtn = new JButton(presenter.freezeBtn());
        unfreezeBtn = new JButton(presenter.unfreezeBtn());
        threshold = new JComboBox<>(presenter.violationType());
        model = new DefaultListModel<>();
        setupGUI();
        updateList();
    }

    private void updateList() {
        Map<String, Integer> violationMap = adminSystem.getUserViolateLimit(String.valueOf(threshold.getSelectedItem()));
        model.removeAllElements();
        violationMap.forEach((key, val) -> model.addElement(presenter.violationInfo(key, val, threshold.getSelectedItem())));
    }

    /*Set up the screen*/
    private void setupGUI() {
        returnItem.addActionListener(this);
        panel1.setLayout(new GridBagLayout());
        final JScrollPane scrollPane1 = new JScrollPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane1, gbc);
        scrollPane1.setViewportView(userLst);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(freezeBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(unfreezeBtn, gbc);
        final JLabel label1 = new JLabel();
        label1.setText(presenter.chooseThreshold());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label1, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(threshold, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        userLst.setModel(model);
        unfreezeBtn.addActionListener(this);
        freezeBtn.addActionListener(this);
        threshold.addActionListener(this);

        frame.add(panel1);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Listen to the actions performed and response to these actions
     *
     * @param e action performed by the admin who is using the system
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == threshold) {
            updateList();
        } else if (e.getSource() == returnItem) {
            new AdminMenuScreen(serializer, adminSystem);
            frame.dispose();
        } else if (e.getSource() == freezeBtn) {
            if (userLst.getSelectedIndex() >= 0) {
                JOptionPane.showMessageDialog(frame, presenter.frozeResult(
                        adminSystem.freezeUser(
                                presenter.retrieveUsername(model.getElementAt(userLst.getSelectedIndex())))));
                updateList();
            }
        } else if (e.getSource() == unfreezeBtn) {
            if (userLst.getSelectedIndex() >= 0) {
                JOptionPane.showMessageDialog(frame, presenter.unfreezeResult(
                        adminSystem.unfreezeUser(
                                presenter.retrieveUsername(model.getElementAt(userLst.getSelectedIndex())))));
                updateList();
            }
        }
    }
}
