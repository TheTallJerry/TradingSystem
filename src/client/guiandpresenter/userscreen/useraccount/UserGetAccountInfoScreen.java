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
 * A gui class, for <code>users</code> view their own information
 */
public class UserGetAccountInfoScreen extends Screen implements ActionListener {
    private final JLabel accountInfoLbl;
    private final JButton viewItemAvailable, viewWishList, viewBlockList;
    private final UserSystem userSystem;
    private final String[] infoList;

    /**
     * Construct a <code>UserGetAccountInfoScreen</code> for <code>users</code> to view their own information
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserGetAccountInfoScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);
        this.userSystem = userSystem;
        this.infoList = userSystem.getUserAccountSpecifics();
        accountInfoLbl = new JLabel(infoList[0]);
        viewItemAvailable = new JButton("Items You Can Lend");
        viewWishList = new JButton("Your WishList");
        viewBlockList = new JButton("Your BlockList");
        shouldSerialize = false;
        setup();
    }

    /**
     * Set up the screen
     */
    private void setup() {
        returnItem.addActionListener(this);

        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        accountInfoLbl.setFont(fontPrompt);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 1;
        pane.add(accountInfoLbl, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        viewItemAvailable.setFont(fontPrompt);
        viewItemAvailable.setSize(btnDim);
        viewItemAvailable.addActionListener(this);
        pane.add(viewItemAvailable, c);
        c.gridy++;

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 5;
        viewBlockList.setFont(fontPrompt);
        viewBlockList.setSize(btnDim);
        viewBlockList.addActionListener(this);
        pane.add(viewBlockList, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        viewWishList.setFont(fontPrompt);
        viewWishList.setSize(btnDim);
        viewWishList.addActionListener(this);
        pane.add(viewWishList, c);
        c.gridy++;

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
            new UserAccountMenuScreen(serializer, userSystem);
            frame.dispose();
        } else if (e.getSource() == viewItemAvailable) {
            JOptionPane.showMessageDialog(frame, infoList[1]);
        } else if (e.getSource() == viewWishList) {
            JOptionPane.showMessageDialog(frame, infoList[2]);
        } else if (e.getSource() == viewBlockList) {
            JOptionPane.showMessageDialog(frame, infoList[3]);
        }
    }
}
