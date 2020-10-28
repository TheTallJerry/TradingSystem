package client.guiandpresenter.userscreen.useraccount;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.useraccount.useraccountmenu.UserAccountMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class, for user to reset the on vacation status .
 */
public class UserChangeOnVacationScreen extends Screen implements ActionListener {
    private final JLabel statusLbl;
    private final UserSystem userSystem;

    /**
     * Construct a <code>UserChangeOnVacationScreen</code> for <code>users</code> to change the on vacation status
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of  <code>UserSystem</code>, which is a controller for users
     */
    public UserChangeOnVacationScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        this.userSystem = userSystem;
        UserAccountPresenter presenter = new UserAccountPresenter();

        statusLbl = new JLabel(presenter.getOnVacationStatusMsg(userSystem.switchVacationStatus()));

        if (userSystem.isCurrUserGuest())
            shouldSerialize = false;

        setup();
    }

    /**
     * Set up the screen
     */
    private void setup() {
        returnItem.addActionListener(this);
        setupConstraint();
        Container pane = frame.getContentPane();

        statusLbl.setFont(fontPrompt);
        pane.add(statusLbl);

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
        if (e.getSource() == returnItem) {
            new UserAccountMenuScreen(serializer, userSystem);
            frame.dispose();
        }

    }
}
