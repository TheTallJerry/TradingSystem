package client.guiandpresenter.mainscreen;

import client.controllers.StartingSystem;
import client.databundle.DataBundle;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.loginscreen.LoginScreen;
import client.guiandpresenter.registrationscreen.RegistrationScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class, which is the first screen showing to all users, admins and guests for choosing different performances
 */
public class MainScreen extends Screen implements ActionListener {
    private final JButton loginButton, regButton, creditsButton, exitButton;
    private final DataBundle dataBundle;
    private final MainScreenPresenter presenter;

    /**
     * Construct a <code>MainScreen</code>, the first screen of the system, who using the system can choose
     * the next performance from this screen
     *
     * @param dataBundle the data bundle that will be used initially.
     * @param serializer the serializer that will be used for initialize
     */
    public MainScreen(DataBundle dataBundle, DataSerializer serializer) {
        super(serializer);

        this.dataBundle = dataBundle;
        StartingSystem startingSystem = new StartingSystem(dataBundle);
        this.presenter = new MainScreenPresenter();
        String[] buttonList = presenter.buttonList();
        loginButton = new JButton(buttonList[0]);
        regButton = new JButton(buttonList[1]);
        creditsButton = new JButton(buttonList[2]);
        exitButton = new JButton(buttonList[3]);

        shouldSerialize = false;

        startingSystem.initializeSystem();

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

        for (JButton btn : new JButton[]{loginButton, regButton, creditsButton, exitButton}) {
            btn.setFont(fontPrompt);
            btn.setPreferredSize(btnDim);
            btn.addActionListener(this);
            pane.add(btn, c);
            c.gridy++;
        }
        frame.setTitle(presenter.getTitle());
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
        if (e.getSource() == exitButton) {
            frame.dispose();
        } else if (e.getSource() == loginButton) {
            frame.dispose();
            new LoginScreen(dataBundle, serializer, false);
        } else if (e.getSource() == regButton) {
            new RegistrationScreen(dataBundle, serializer);
            frame.dispose();
        } else if (e.getSource() == creditsButton) {
            JOptionPane.showMessageDialog(frame, presenter.creditPage());
        }
    }

}
