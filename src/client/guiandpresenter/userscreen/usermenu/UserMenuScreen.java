package client.guiandpresenter.userscreen.usermenu;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.loginscreen.LoginScreen;
import client.guiandpresenter.userscreen.useraccount.useraccountmenu.UserAccountMenuScreen;
import client.guiandpresenter.userscreen.usermessage.UserMessageScreen;
import client.guiandpresenter.userscreen.usertrade.usertrademenuscreen.UserTradeMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class responsible for showing the options one <code>user</code> can choose, different options represents
 * different actions
 */
public class UserMenuScreen extends Screen implements ActionListener {

    private final JButton tradeBtn, accountBtn, messageBtn, logOutBtn;
    private final UserMenuPresenter presenter;
    private final UserSystem userSystem;

    /**
     * Construct a <code>UserScreen</code>, first screen <code>users</code> will see, showing the options
     * one <code>user</code> can choose, different options represents different actions
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserMenuScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        this.userSystem = userSystem;
        presenter = new UserMenuPresenter();
        String[] buttons = presenter.buttons();
        tradeBtn = new JButton(buttons[0]);
        accountBtn = new JButton(buttons[1]);
        messageBtn = new JButton(buttons[2]);
        logOutBtn = new JButton(buttons[3]);

        shouldSerialize = false;

        setup();
    }

    /**
     * Set up the screen
     */
    private void setup() {
        menuBar.remove(returnItem);
        frame.remove(menuBar);

        frame.setFont(fontPrompt);
        frame.setTitle(presenter.getTitle());
        Container pane = frame.getContentPane();

        GridBagConstraints c = setupConstraint();
        c.fill = GridBagConstraints.CENTER;

        for (JButton btn : new JButton[]{tradeBtn, accountBtn, messageBtn, logOutBtn}) {
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
        if (e.getSource() == tradeBtn) {
            new UserTradeMenuScreen(serializer, userSystem);
        } else if (e.getSource() == accountBtn) {
            new UserAccountMenuScreen(serializer, userSystem);
        } else if (e.getSource() == logOutBtn) {
            new LoginScreen(userSystem.getDataBundle(), serializer, userSystem.isCurrUserGuest());
        } else if (e.getSource() == messageBtn) {
            new UserMessageScreen(serializer, userSystem);
        }
        frame.dispose();
    }
}
