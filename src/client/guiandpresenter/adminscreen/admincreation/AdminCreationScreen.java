package client.guiandpresenter.adminscreen.admincreation;

import client.controllers.AdminSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.adminscreen.adminmenu.AdminMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class responsible for the information showed on screen of admin creation
 */
public class AdminCreationScreen extends Screen implements ActionListener {
    private final AdminSystem adminSystem;
    private final AdminCreationPresenter presenter;
    private final JButton create;
    private final JTextField usernameF, passwordF;
    private final JLabel usernameL, passwordL;

    /**
     * Construct a <code>AdminCreationScreen</code> for the creation of administration users
     *
     * @param serializer  the serializer that will be used for initialize
     * @param adminSystem a instance of AdminSystem, which is a controller for administration users
     */
    public AdminCreationScreen(DataSerializer serializer, AdminSystem adminSystem) {
        super(serializer);
        this.adminSystem = adminSystem;
        presenter = new AdminCreationPresenter();


        create = new JButton(presenter.textCreate());
        usernameF = new JTextField();
        passwordF = new JTextField();
        usernameL = new JLabel(presenter.getUsernameLblMsg());
        passwordL = new JLabel(presenter.getPwLblMsg());

        setup();
    }

    /*Set up the screen*/
    private void setup() {
        returnItem.addActionListener(this);
        create.addActionListener(this);
        create.setFont(fontPrompt);
        usernameF.setFont(fontInput);
        passwordF.setFont(fontInput);
        usernameL.setFont(fontInput);
        passwordL.setFont(fontInput);
        frame.setTitle(presenter.getTitle());
        final JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pane.add(usernameF, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        pane.add(usernameL, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        pane.add(passwordL, gbc);
        passwordF.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pane.add(passwordF, gbc);
        create.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        pane.add(create, gbc);

        frame.add(pane);
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
            new AdminMenuScreen(serializer, adminSystem);
            frame.dispose();
        } else if (e.getSource() == create) {
            JOptionPane.showMessageDialog(frame, presenter.adminCreation(
                    adminSystem.createAdmin(usernameF.getText(), passwordF.getText())));
        }
    }
}
