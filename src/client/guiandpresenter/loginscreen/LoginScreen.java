package client.guiandpresenter.loginscreen;

import client.controllers.AdminSystem;
import client.controllers.LoginSystem;
import client.controllers.LoginType;
import client.controllers.UserSystem;
import client.databundle.DataBundle;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.adminscreen.adminmenu.AdminMenuScreen;
import client.guiandpresenter.mainscreen.MainScreen;
import client.guiandpresenter.userscreen.usermenu.UserMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

/**
 * A gui class responsible for admins and users to login
 */
public class LoginScreen extends Screen implements ActionListener, WindowListener {
    private final JTextField usernameInput;
    private final JPasswordField pwInput;
    private final JButton confirmBtn, enterAsGuestBtn;
    private final LoginSystem loginSystem;
    private final LoginPresenter presenter;
    private DataBundle dataBundle;
    private final JLabel username, password, title;
    private final String GUEST_USERNAME;
    private final boolean isLastUserGuest;

    /**
     * Construct a <code>LoginScreen</code> for the administration users to undo the user actions
     *
     * @param dataBundle      the data bundle that will be used initially.
     * @param serializer      the serializer that will be used for initialize
     * @param isLastUserGuest a boolean indicate whether the last one who use this system is guest or not
     */
    public LoginScreen(DataBundle dataBundle, DataSerializer serializer, boolean isLastUserGuest) {
        super(serializer);

        loginSystem = new LoginSystem(dataBundle);
        presenter = new LoginPresenter();
        frame.setTitle(presenter.getTitle());
        this.isLastUserGuest = isLastUserGuest;
        if (isLastUserGuest) {
            this.dataBundle = serializer.getStartingInfo().value1;
        } else {
            this.dataBundle = dataBundle;
            serializer.getStartingInfo().value1 = this.dataBundle;
        }
        GUEST_USERNAME = loginSystem.getGuestUsername();

        usernameInput = new JTextField(stringTextFieldSize);
        pwInput = new JPasswordField(stringTextFieldSize);
        confirmBtn = new JButton(presenter.getConfirmBtnMsg());
        enterAsGuestBtn = new JButton(presenter.enterAsGuestBtnMsg());
        username = new JLabel(presenter.getUsernameLblMsg());
        password = new JLabel(presenter.getPwLblMsg());
        title = new JLabel(presenter.getTitleLblMsg());


        if (isLastUserGuest)
            shouldSerialize = false;

        setup();
    }

    /*Set up the screen*/
    private void setup() {
        returnItem.addActionListener(this);
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = setupConstraint();

        title.setFont(fontPrompt);
        c.gridwidth = 2;
        pane.add(title, c);

        username.setFont(fontPrompt);
        c.gridy++;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        pane.add(username, c);

        password.setFont(fontPrompt);
        c.gridy++;
        pane.add(password, c);

        usernameInput.setFont(fontInput);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        pane.add(usernameInput, c);

        pwInput.setFont(fontInput);
        c.gridy++;
        pane.add(pwInput, c);

        confirmBtn.addActionListener(this);
        confirmBtn.setPreferredSize(btnDim);
        confirmBtn.setFont(fontPrompt);
        c.gridx = 0;
        c.gridy++;
        pane.add(confirmBtn, c);

        enterAsGuestBtn.addActionListener(this);
        enterAsGuestBtn.setPreferredSize(btnDim);
        enterAsGuestBtn.setFont(fontPrompt);
        c.gridx = 1;
        pane.add(enterAsGuestBtn, c);

        frame.add(pane);
        frame.addWindowListener(this);
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
        if (e.getSource() == confirmBtn) {
            // verify username/password, etc
            LoginType result
                    = loginSystem.login(usernameInput.getText(), new String(pwInput.getPassword()));
            switch (result) {
                case FAILED:
                    JOptionPane.showMessageDialog(frame, presenter.loginFailed());
                    pwInput.setText("");
                    usernameInput.setText("");
                    break;
                case USER:
                    JOptionPane.showMessageDialog(frame, presenter.userLogin());
                    UserSystem userSystem = new UserSystem(dataBundle, usernameInput.getText());
                    new UserMenuScreen(serializer, userSystem);
                    frame.dispose();
                    break;
                case ADMIN:
                    JOptionPane.showMessageDialog(frame, presenter.adminLogin());
                    AdminSystem adminSystem = new AdminSystem(dataBundle, usernameInput.getText());
                    new AdminMenuScreen(serializer, adminSystem);
                    frame.dispose();
                    break;
            }
        } else if (e.getSource() == enterAsGuestBtn) {
            new UserMenuScreen(serializer, new UserSystem(dataBundle, GUEST_USERNAME));
            frame.dispose();
        } else {
            new MainScreen(dataBundle, serializer);
            frame.dispose();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        try {
            if (isLastUserGuest) {
                serializer.deserialize();
                this.dataBundle = serializer.getStartingInfo().value1;
            }
        } catch (IOException | ClassNotFoundException | RuntimeException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}