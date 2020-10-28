package client.guiandpresenter.adminscreen.adminmenu;

import client.controllers.AdminSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.adminscreen.adminchangepassword.AdminChangePasswordScreen;
import client.guiandpresenter.adminscreen.admincreation.AdminCreationScreen;
import client.guiandpresenter.adminscreen.adminrequest.AdminRequestScreen;
import client.guiandpresenter.adminscreen.adminsearch.AdminSearchScreen;
import client.guiandpresenter.adminscreen.adminthreshold.AdminThresholdScreen;
import client.guiandpresenter.adminscreen.adminundo.AdminUndoScreen;
import client.guiandpresenter.adminscreen.adminviolation.AdminViolationScreen;
import client.guiandpresenter.loginscreen.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class responsible for showing the options one admin can choose, different options represents different actions
 */
public class AdminMenuScreen extends Screen implements ActionListener {
    private final AdminMenuPresenter presenter;
    private final JButton logout, req, thresh, create, search, undo, announcement, changePw, violation;
    private final AdminSystem adminSystem;

    /**
     * Construct a <code>AdminScreen</code> for the administration users, this is the first screen showed by admins.
     * Can send announcements and go other screens.
     *
     * @param serializer  the serializer that will be used for initialize
     * @param adminSystem a instance of AdminSystem, which is a controller for administration users
     */
    public AdminMenuScreen(DataSerializer serializer, AdminSystem adminSystem) {
        super(serializer);
        presenter = new AdminMenuPresenter();

        this.adminSystem = adminSystem;
        logout = new JButton(presenter.textLogOut());
        req = new JButton(presenter.textViewRequests());
        thresh = new JButton(presenter.textViewModifyThresholds());
        create = new JButton(presenter.textCreateNewAdmin());
        search = new JButton(presenter.textSearchInfo());
        undo = new JButton(presenter.textUndoAction());
        announcement = new JButton(presenter.textSendAnnouncement());
        changePw = new JButton(presenter.textChangePw());
        violation = new JButton(presenter.textCheckViolation());

        setup();
    }

    /*Set up the screen*/
    private void setup() {

        frame.setFont(fontPrompt);
        menuBar.remove(returnItem);
        frame.remove(menuBar);
        Container pane = frame.getContentPane();

        GridBagConstraints c = setupConstraint();
        c.fill = GridBagConstraints.CENTER;

        for (JButton btn : new JButton[]{req, thresh, create, search, undo, announcement, changePw, violation, logout}) {
            btn.setFont(fontPrompt);
            btn.setPreferredSize(btnDim);
            btn.addActionListener(this);
            pane.add(btn, c);
            c.gridy++;
        }

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
        if (e.getSource() == announcement) {
            String message = JOptionPane.showInputDialog(presenter.messageEnter());
            if (!(message == null || message.isEmpty())) {
                adminSystem.setAnnouncement(message);
                JOptionPane.showMessageDialog(frame, presenter.announcementSent());
            }
        } else {
            if (e.getSource() == logout) {
                new LoginScreen(adminSystem.getDataBundle(), serializer, false);
            } else if (e.getSource() == req) {
                new AdminRequestScreen(serializer, adminSystem);
            } else if (e.getSource() == thresh) {
                new AdminThresholdScreen(serializer, adminSystem);
            } else if (e.getSource() == create) {
                new AdminCreationScreen(serializer, adminSystem);
            } else if (e.getSource() == search) {
                new AdminSearchScreen(serializer, adminSystem);
            } else if (e.getSource() == undo) {
                new AdminUndoScreen(serializer, adminSystem);
            } else if (e.getSource() == changePw) {
                new AdminChangePasswordScreen(serializer, adminSystem);
            } else if (e.getSource() == violation) {
                new AdminViolationScreen(serializer, adminSystem);
            }
            frame.dispose();
        }
    }
}
