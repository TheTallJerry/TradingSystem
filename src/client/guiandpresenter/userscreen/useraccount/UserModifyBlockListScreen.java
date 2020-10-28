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
 * A gui class, for <code>users</code> to modify the <code>BlockList</code>
 */
public class UserModifyBlockListScreen extends Screen implements ActionListener {

    private final JButton addBtn, removeBtn;
    private final JLabel usernameLbl, blockListLbl;
    private final JTextField usernameInput;
    private final UserSystem userSystem;
    private final UserAccountPresenter presenter;

    /**
     * Construct a <code>UserModifyBlockListScreen</code> for <code>users</code> to modify the <code>BlockList</code>
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserModifyBlockListScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        presenter = new UserAccountPresenter();
        this.userSystem = userSystem;
        blockListLbl = new JLabel(presenter.displayBlockList(userSystem.getBlockList()));

        addBtn = new JButton(presenter.addBlockListBtn());
        removeBtn = new JButton(presenter.removeBlockListBtn());

        usernameInput = new JTextField(10);

        usernameLbl = new JLabel(presenter.getUsernameLblMsg());

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

        blockListLbl.setFont(fontPrompt);
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;
        pane.add(blockListLbl, c);

        c.gridy++;
        usernameLbl.setFont(fontPrompt);
        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = 1;
        pane.add(usernameLbl, c);

        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        pane.add(usernameInput, c);

        addBtn.addActionListener(this);
        addBtn.setFont(fontPrompt);
        addBtn.setPreferredSize(btnDim);
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(addBtn, c);

        removeBtn.addActionListener(this);
        removeBtn.setFont(fontPrompt);
        removeBtn.setPreferredSize(btnDim);
        c.gridx++;
        pane.add(removeBtn, c);

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
            new UserAccountMenuScreen(serializer, userSystem);
            frame.dispose();
        } else {
            if (e.getSource() == addBtn) {
                JOptionPane.showMessageDialog(frame, presenter.displayAddToBlocklistStatus(
                        userSystem.addToBlockList(usernameInput.getText())));
            } else {
                JOptionPane.showMessageDialog(frame, presenter.displayRemoveFromBlocklistStatus(
                        userSystem.removeFromBlockList(usernameInput.getText())));
            }
            blockListLbl.setText(presenter.displayBlockList(userSystem.getBlockList()));
            usernameInput.setText("");
        }
    }
}
