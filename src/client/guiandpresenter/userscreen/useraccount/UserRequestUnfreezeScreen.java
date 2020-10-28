package client.guiandpresenter.userscreen.useraccount;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.useraccount.useraccountmenu.UserAccountMenuScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class, for <code>users</code> to request unfreezing the account
 */
public class UserRequestUnfreezeScreen extends Screen implements ActionListener {

    private final UserSystem userSystem;
    private final UserAccountPresenter presenter;

    /**
     * Construct a <code>UserRequestUnfreezeScreen</code> for <code>users</code> to request unfreezing the account
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserRequestUnfreezeScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        this.userSystem = userSystem;
        presenter = new UserAccountPresenter();

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

        frame.pack();
        frame.setVisible(true);

        JOptionPane.showMessageDialog(frame, presenter.displayUnfreezeRequestStatus(userSystem.requestUnfreeze()));

        new UserAccountMenuScreen(serializer, userSystem);
        frame.dispose();
    }

    /**
     * Listen to the actions performed and response to these actions
     *
     * @param e action performed by the admin who is using the system
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        new UserAccountMenuScreen(serializer, userSystem);
        frame.dispose();
    }
}
