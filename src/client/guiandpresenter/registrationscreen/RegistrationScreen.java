package client.guiandpresenter.registrationscreen;

import client.controllers.RegistrationSystem;
import client.databundle.DataBundle;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.mainscreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class, for user and admins registration.
 */
public class RegistrationScreen extends Screen implements ActionListener {

    private final JTextField usernameInput, emailInput;
    private final JPasswordField pwInput;
    private final JButton confirmBtn;
    private final RegistrationSystem registrationSystem;
    private final RegistrationPresenter presenter;
    private final DataBundle dataBundle;
    private final JLabel username, password, title, email;
    private final JComboBox<String> registrationChoices;

    /**
     * Construct a <code>UserAndAdminRegistrationScreen</code> for users and admins registration
     * from this screen
     *
     * @param dataBundle the data bundle that will be used initially.
     * @param serializer the serializer that will be used for initialize
     */
    public RegistrationScreen(DataBundle dataBundle, DataSerializer serializer) {
        super(serializer);

        this.dataBundle = dataBundle;

        registrationSystem = new RegistrationSystem(dataBundle);
        presenter = new RegistrationPresenter();
        frame.setTitle(presenter.getTitle());

        registrationChoices = new JComboBox<>(presenter.getRegChoices());

        usernameInput = new JTextField(20);
        pwInput = new JPasswordField(20);
        emailInput = new JTextField(20);

        confirmBtn = new JButton(presenter.getConfirmBtnMsg());

        username = new JLabel(presenter.getUsernameLblMsg());
        password = new JLabel(presenter.getPwLblMsg());
        title = new JLabel(presenter.getTitleLblMsg());
        email = new JLabel(presenter.getEmailLblMsg());

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

        email.setFont(fontPrompt);
        c.gridy++;
        pane.add(email, c);

        usernameInput.setFont(fontInput);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        pane.add(usernameInput, c);

        pwInput.setFont(fontInput);
        c.gridy++;
        pane.add(pwInput, c);

        emailInput.setFont(fontInput);
        c.gridy++;
        pane.add(emailInput, c);

        confirmBtn.addActionListener(this);
        confirmBtn.setPreferredSize(btnDim);
        confirmBtn.setFont(fontPrompt);
        c.gridx = 0;
        c.gridy++;
        pane.add(confirmBtn, c);

        // Placement tested: works.
        registrationChoices.addActionListener(this);
        registrationChoices.setFont(fontPrompt);
        registrationChoices.setSelectedItem(presenter.getRegAdminChoice());
        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 2;
        pane.add(registrationChoices, c);

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
        if (e.getSource() == confirmBtn) {
            if (registrationChoices.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(frame, presenter.regChoiceMustBeMadePrompt());
            } else {
                //Switch does NOT work because the cases aren't compile time constants
                if (String.valueOf(registrationChoices.getSelectedItem()).equals(presenter.getRegAdminChoice())) {
                    if (!registrationSystem.requestAdmin(usernameInput.getText(), new String(pwInput.getPassword()),
                            emailInput.getText())) {
                        JOptionPane.showMessageDialog(frame, presenter.getInvalidInputMsg());
                    } else {
                        JOptionPane.showMessageDialog(frame, presenter.adminRequestPrompt(usernameInput.getText(),
                                new String(pwInput.getPassword())));
                    }
                } else {
                    if (!registrationSystem.createUser(usernameInput.getText(), new String(pwInput.getPassword()))) {
                        JOptionPane.showMessageDialog(frame, presenter.failurePrompt());
                    } else {
                        JOptionPane.showMessageDialog(frame, presenter.userCreationSuccessPrompt());
                    }
                }
            }
            usernameInput.setText("");
            pwInput.setText("");
            emailInput.setText("");
        } else if (e.getSource() == returnItem) {
            new MainScreen(dataBundle, serializer);
            frame.dispose();
        }
    }
}
