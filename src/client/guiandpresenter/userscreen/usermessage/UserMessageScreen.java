package client.guiandpresenter.userscreen.usermessage;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.usermenu.UserMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A gui class, for <code>users</code> to view and send messages
 */
public class UserMessageScreen extends Screen implements ActionListener {
    private final UserSystem userSystem;
    private final JButton viewReceivedBtn, viewSentBtn, sendBtn, confirmBtn;
    private final JDialog log;
    private final JTextField usernameField, messageField;
    private final UserMessagePresenter presenter;

    /**
     * Construct a <code>UserMessageScreen</code> for <code>users</code> to view and send messages
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserMessageScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        this.userSystem = userSystem;
        presenter = new UserMessagePresenter();

        viewReceivedBtn = new JButton(presenter.viewReceivedBtn());
        viewSentBtn = new JButton(presenter.viewSentBtn());
        sendBtn = new JButton(presenter.sendBtn());
        confirmBtn = new JButton(presenter.getConfirmBtnMsg());

        log = new JDialog();

        usernameField = new JTextField(stringTextFieldSize);
        messageField = new JTextField(stringTextFieldSize);

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

        for (JButton btn : new JButton[]{viewReceivedBtn, viewSentBtn, sendBtn}) {
            btn.setPreferredSize(btnDim);
            btn.setFont(fontPrompt);
            btn.addActionListener(this);
            pane.add(btn, c);
            c.gridy++;
        }
        setupLog();

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Set up the dialog screen that called by actionPerformed
     */
    private void setupLog() {
        log.setLayout(new GridBagLayout());
        log.setBounds(logX, logY, logWidth, logHeight);

        GridBagConstraints c = setupConstraint();

        JLabel usernameLbl = new JLabel(presenter.getUsernameLblMsg());
        usernameLbl.setFont(fontPrompt);
        c.anchor = GridBagConstraints.EAST;
        log.add(usernameLbl, c);

        c.gridy++;
        JLabel messageLbl = new JLabel(presenter.messageLbl());
        messageLbl.setFont(fontPrompt);
        log.add(messageLbl, c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        log.add(usernameField, c);

        c.gridy++;
        log.add(messageField, c);

        c.gridy++;
        confirmBtn.setFont(fontPrompt);
        confirmBtn.addActionListener(this);
        c.anchor = GridBagConstraints.CENTER;
        log.add(confirmBtn, c);

        log.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                usernameField.setText("");
                messageField.setText("");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                usernameField.setText("");
                messageField.setText("");
            }
        });

        log.pack();
    }

    /**
     * Listen to the actions performed and response to these actions
     *
     * @param e action performed by the admin who is using the system
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(log.isVisible())
            log.dispose();
        if (e.getSource() == returnItem) {
            new UserMenuScreen(serializer, userSystem);
            frame.dispose();
        } else if (e.getSource() == viewReceivedBtn) {
            JOptionPane.showMessageDialog(frame, presenter.messageReceived(userSystem.getMessageReceived()));
        } else if (e.getSource() == viewSentBtn) {
            JOptionPane.showMessageDialog(frame, presenter.messageSent(userSystem.getMessageSent()));
        } else if (e.getSource() == sendBtn) {
            log.setVisible(true);
        } else if (e.getSource() == confirmBtn) {
            JOptionPane.showMessageDialog(frame, presenter.messageSentStatus(
                    userSystem.sendPrivateMessage(usernameField.getText(), messageField.getText())));
            log.dispose();
        }
    }
}
