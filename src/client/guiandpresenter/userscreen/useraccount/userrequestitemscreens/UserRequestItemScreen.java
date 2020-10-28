package client.guiandpresenter.userscreen.useraccount.userrequestitemscreens;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.useraccount.useraccountmenu.UserAccountMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class, for <code>users</code> to create new <code>item</code>
 */
public class UserRequestItemScreen extends Screen implements ActionListener {

    private final JTextField itemNameField, itemTypeField, itemDescriptionField;
    private final JLabel itemNameLbl, itemTypeLbl, itemDescriptionLbl;
    private final JButton confirmBtn;
    private final UserRequestItemPresenter presenter;
    private final UserSystem userSystem;

    /**
     * Construct a <code>UserRequestCreateItemScreen</code> for <code>users</code> to create new <code>item</code>
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserRequestItemScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        this.userSystem = userSystem;
        presenter = new UserRequestItemPresenter();


        itemNameField = new JTextField(stringTextFieldSize);
        itemTypeField = new JTextField(stringTextFieldSize);
        itemDescriptionField = new JTextField(stringTextFieldSize + 2);

        itemNameLbl = new JLabel(presenter.itemNameLblMsg());
        itemTypeLbl = new JLabel(presenter.itemTypeLblMsg());
        itemDescriptionLbl = new JLabel(presenter.itemDescriptionLblMsg());
        confirmBtn = new JButton(presenter.getConfirmBtnMsg());

        if (userSystem.isCurrUserGuest())
            shouldSerialize = false;

    }

    /**
     * Set up the screen
     */
    public void setup() {
        returnItem.addActionListener(this);

        Container pane = frame.getContentPane();
        GridBagConstraints c = setupConstraint();

        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        for (JLabel lbl : new JLabel[]{itemNameLbl, itemTypeLbl, itemDescriptionLbl}) {
            lbl.setFont(fontPrompt);
            pane.add(lbl, c);
            c.gridy++;
        }

        c.anchor = GridBagConstraints.CENTER;
        confirmBtn.setPreferredSize(btnDim);
        confirmBtn.setFont(fontPrompt);
        confirmBtn.addActionListener(this);
        c.gridy++;
        pane.add(confirmBtn, c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        for (JTextField field : new JTextField[]{itemNameField, itemTypeField, itemDescriptionField}) {
            field.setFont(fontInput);
            pane.add(field, c);
            c.gridy++;
        }

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
            JOptionPane.showMessageDialog(frame, presenter.getItemRequestedMsg(
                    userSystem.createItemAndRequest(itemTypeField.getText(),
                            itemNameField.getText(), itemDescriptionField.getText())));
            for (JTextField field : new JTextField[]{itemNameField, itemTypeField, itemDescriptionField}) {
                field.setText("");
            }
            //return iff the user chooses to return.
        } else if (e.getSource() == returnItem) {
            new UserAccountMenuScreen(serializer, userSystem);
            frame.dispose();
        }

    }
}
