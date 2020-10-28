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
 * GUI class of the screen for <code>User</code>s to report other the <code>User</code>s.
 */
public class UserReportScreen extends Screen implements ActionListener {
    private final UserSystem userSystem;
    private final JLabel nameLbl, reasonLbl;
    private final JButton confirmBtn;
    private final JTextField usernameField, reasonField;
    private final UserAccountPresenter presenter;

    /**
     * Construct a <code>UserReportScreen</code> for <code>User</code> to report other the <code>User</code>s.
     *
     * @param serializer the serializer that will be used for initialize.
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users.
     */
    public UserReportScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);
        this.userSystem = userSystem;

        presenter = new UserAccountPresenter();

        nameLbl = new JLabel(presenter.reportNameLbl());
        reasonLbl = new JLabel(presenter.reportReasonLbl());
        confirmBtn = new JButton(presenter.getConfirmBtnMsg());
        usernameField = new JTextField(10);
        reasonField = new JTextField(10);

        if (userSystem.isCurrUserGuest())
            shouldSerialize = false;

        setup();
    }

    /*Set up the screen*/
    private void setup() {
        returnItem.addActionListener(this);
        GridBagConstraints c = setupConstraint();

        Container pane = frame.getContentPane();

        c.anchor = GridBagConstraints.EAST;
        nameLbl.setFont(fontPrompt);
        pane.add(nameLbl, c);

        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        usernameField.setFont(fontInput);
        pane.add(usernameField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        reasonLbl.setFont(nameLbl.getFont());
        pane.add(reasonLbl, c);

        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        reasonField.setFont(usernameField.getFont());
        pane.add(reasonField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.CENTER;
        confirmBtn.addActionListener(this);
        confirmBtn.setFont(nameLbl.getFont());
        confirmBtn.setPreferredSize(btnDim);
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
            JOptionPane.showMessageDialog(frame, presenter.reportUserStatus(
                    userSystem.reportUser(usernameField.getText(), reasonField.getText())));
            usernameField.setText("");
            reasonField.setText("");
        } else {
            new UserAccountMenuScreen(serializer, userSystem);
            frame.dispose();
        }
    }
}
