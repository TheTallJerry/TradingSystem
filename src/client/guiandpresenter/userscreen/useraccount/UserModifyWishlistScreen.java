package client.guiandpresenter.userscreen.useraccount;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.useraccount.useraccountmenu.UserAccountMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A gui class, for <code>users</code> to modify the <code>wishlist</code>
 */
public class UserModifyWishlistScreen extends Screen {
    private final JLabel instructionLbl;
    private final UserSystem userSystem;
    private final UserAccountPresenter presenter;
    private final DefaultListModel<String> itemsModel, wishlistModel;
    private JList<String> itemsList, wishlistList;
    private final String[] options, options2;

    /**
     * Construct a <code>UserModifyWishlistScreen</code> for <code>users</code> to modify the <code>wishlist</code>
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserModifyWishlistScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        presenter = new UserAccountPresenter();
        this.userSystem = userSystem;

        itemsModel = new DefaultListModel<>();
        wishlistModel = new DefaultListModel<>();
        options = presenter.addOptions();
        options2 = presenter.removeOptions();
        instructionLbl = new JLabel(presenter.modifyWishlistInstruction());
        if (userSystem.isCurrUserGuest())
            shouldSerialize = false;
        setup();
    }

    /**
     * Set up the screen
     */
    private void setup() {
        GridBagConstraints c = setupConstraint();
        Container pane = frame.getContentPane();


        JScrollPane scrollPane = new JScrollPane();
        JScrollPane wishlistPane = new JScrollPane();

        itemsList = new JList<>();
        itemsList.setModel(itemsModel);
        userSystem.getInventoryForWishlist().forEach(itemsModel::addElement);
        itemsList.setFont(fontPrompt);

        scrollPane.setViewportView(itemsList);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(stringTextFieldSize, stringTextFieldSize));
        scrollPane.getHorizontalScrollBar().setPreferredSize(scrollPane.getVerticalScrollBar().getPreferredSize());
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.5;
        c.ipadx = frame.getPreferredSize().width / 4;
        c.ipady = frame.getPreferredSize().height / 3;
        setupPopupDialog();
        pane.add(scrollPane, c);

        c = new GridBagConstraints();
        wishlistList = new JList<>();
        wishlistList.setModel(wishlistModel);
        userSystem.getUserWishlist().forEach(wishlistModel::addElement);
        wishlistList.setFont(fontPrompt);
        c.ipadx = frame.getPreferredSize().width / 4;
        c.ipady = frame.getPreferredSize().height / 3;
        c.gridx = 4;
        c.weightx = 0.5;
        c.gridy = 2;
        wishlistPane.setViewportView(wishlistList);
        wishlistPane.getVerticalScrollBar().setPreferredSize(new Dimension(stringTextFieldSize, stringTextFieldSize));
        wishlistPane.getHorizontalScrollBar().setPreferredSize(scrollPane.getVerticalScrollBar().getPreferredSize());
        setupWishlistDialog();
        pane.add(wishlistPane, c);

        instructionLbl.setFont(fontPrompt);
        c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 2;
        c.weightx = 5.0;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(instructionLbl, c);


        returnItem.addActionListener(e -> {
            new UserAccountMenuScreen(serializer, userSystem);
            frame.dispose();
        });

        frame.pack();
        frame.setVisible(true);
    }

    private void setupPopupDialog() {
        // reference: http://www.java2s.com/Code/JavaAPI/javax.swing/JListaddMouseListenerMouseListenerlis.htm
        itemsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                int index = itemsList.locationToIndex(e.getPoint());
                if (index < 0)
                    return;
                String selected = itemsModel.getElementAt(index);
                int result = JOptionPane.showOptionDialog(frame, presenter.itemDetails(selected),
                        presenter.wishlistPrompt(), JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (result == 0) {
                    JOptionPane.showMessageDialog(frame, presenter.addToWishlistStatus(
                            userSystem.addToUserWishList(selected)));
                    itemsModel.removeElement(selected);
                    wishlistModel.removeAllElements();
                    userSystem.getUserWishlist().forEach(wishlistModel::addElement);
                }
            }
        });
    }

    private void setupWishlistDialog() {
        // reference: http://www.java2s.com/Code/JavaAPI/javax.swing/JListaddMouseListenerMouseListenerlis.htm
        wishlistList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                int index = wishlistList.locationToIndex(e.getPoint());
                if (index < 0)
                    return;
                String selected = wishlistModel.getElementAt(index);
                int result = JOptionPane.showOptionDialog(frame, presenter.itemDetails(selected),
                        presenter.wishlistPrompt(), JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
                if (result == 0) {
                    JOptionPane.showMessageDialog(frame, presenter.removeFromWishlistStatus(
                            userSystem.removeFromUserWishList(selected)));
                    wishlistModel.removeElement(selected);
                    itemsModel.removeAllElements();
                    userSystem.getInventoryForWishlist().forEach(itemsModel::addElement);
                }
            }
        });
    }
}
