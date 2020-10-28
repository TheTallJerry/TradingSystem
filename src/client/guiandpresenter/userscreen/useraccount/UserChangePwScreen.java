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
 * A gui class, for user to reset the on password.
 */
public class UserChangePwScreen extends Screen implements ActionListener {
    private final JButton confirmBtn;
    private final JLabel passwordLbl;
    private final JTextField passwordInputField;
    private final JLabel oldPasswordLbl;
    private final UserSystem userSystem;
    private final UserAccountPresenter presenter;

    /**
     * Construct a <code>UserChangePwScreen</code> for <code>users</code> to reset the password
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserChangePwScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        this.userSystem = userSystem;
        presenter = new UserAccountPresenter();

        confirmBtn = new JButton(presenter.getConfirmBtnMsg());

        passwordInputField = new JTextField(20);
        oldPasswordLbl = new JLabel(presenter.currentPasswordLblMsg(userSystem.getCurrPassword()));

        passwordLbl = new JLabel(presenter.getPwLblMsg());

        if (userSystem.isCurrUserGuest())
            shouldSerialize = false;

        setup();
    }

    /**
     * Set up the screen
     */
    private void setup() {
        returnItem.addActionListener(this);

        GridBagConstraints c = setupConstraint();
        Container pane = frame.getContentPane();

        oldPasswordLbl.setFont(fontPrompt);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        pane.add(oldPasswordLbl);

        passwordLbl.setFont(fontPrompt);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        pane.add(passwordLbl);

        passwordInputField.setFont(fontInput);
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 1;
        pane.add(passwordInputField);

        confirmBtn.addActionListener(this);
        confirmBtn.setPreferredSize(btnDim);
        confirmBtn.setFont(fontPrompt);
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        pane.add(confirmBtn, c);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Listen to the actions performed and response to these actions
     *
     * @param e action performed by the user who is using the system
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmBtn) {
            JOptionPane.showMessageDialog(frame, presenter.getPasswordChangedMsg(
                    userSystem.changeCurrUserPassword(passwordInputField.getText())));
            passwordInputField.setText("");
            oldPasswordLbl.setText(presenter.currentPasswordLblMsg(userSystem.getCurrPassword()));
        } else if (e.getSource() == returnItem) {
            new UserAccountMenuScreen(serializer, userSystem);
            frame.dispose();
        }

    }
}
