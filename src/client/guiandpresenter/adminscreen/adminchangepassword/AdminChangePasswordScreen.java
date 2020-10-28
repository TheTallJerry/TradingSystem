package client.guiandpresenter.adminscreen.adminchangepassword;

import client.controllers.AdminSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.adminscreen.adminmenu.AdminMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminChangePasswordScreen extends Screen implements ActionListener {
    private final JButton confirmBtn;
    private final JLabel passwordLbl;
    private final JTextField passwordInputField;
    private final JLabel oldPasswordLbl;
    private final AdminSystem adminSystem;
    private final AdminChangePasswordPresenter presenter;

    /**
     * Construct a <code>UserChangePwScreen</code> for <code>users</code> to reset the password
     *
     * @param serializer  the serializer that will be used for initialize
     * @param adminSystem a instance of <code>AdminSystem</code>, which is a controller for admins
     */
    public AdminChangePasswordScreen(DataSerializer serializer, AdminSystem adminSystem) {
        super(serializer);

        this.adminSystem = adminSystem;
        presenter = new AdminChangePasswordPresenter();

        confirmBtn = new JButton(presenter.getConfirmBtnMsg());


        passwordInputField = new JTextField(stringTextFieldSize);
        oldPasswordLbl = new JLabel(presenter.currentPasswordLblMsg(adminSystem.getPassword()));

        passwordLbl = new JLabel(presenter.getPwLblMsg());

        setup();
    }

    /*Set up the screen*/
    private void setup() {
        returnItem.addActionListener(this);

        GridBagConstraints c = setupConstraint();
        Container pane = frame.getContentPane();

        oldPasswordLbl.setFont(fontPrompt);
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;
        pane.add(oldPasswordLbl, c);

        c.gridy++;
        passwordLbl.setFont(fontPrompt);
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        pane.add(passwordLbl, c);

        passwordInputField.setFont(fontInput);
        c.anchor = GridBagConstraints.WEST;
        c.gridx++;
        pane.add(passwordInputField, c);

        confirmBtn.addActionListener(this);
        confirmBtn.setPreferredSize(btnDim);
        confirmBtn.setFont(fontPrompt);
        c.gridx = 0;
        c.gridy++;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(confirmBtn, c);

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
            JOptionPane.showMessageDialog(frame, presenter.getPasswordChangedMsg(
                    adminSystem.setPassword(passwordInputField.getText())));
            passwordInputField.setText("");
            oldPasswordLbl.setText(presenter.currentPasswordLblMsg(adminSystem.getPassword()));
        } else if (e.getSource() == returnItem) {
            new AdminMenuScreen(serializer, adminSystem);
            frame.dispose();
        }

    }
}
