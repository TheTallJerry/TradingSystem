package client.guiandpresenter.userscreen.useraccount.usersetcity;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.useraccount.useraccountmenu.UserAccountMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class, for <code>users</code> to set the city they located
 */
public class UserSetCityScreen extends Screen implements ActionListener {

    private final JLabel nowCityLbl;
    private final JButton setNewCityBtn, deleteCityBtn;
    private final UserSystem userSystem;
    private final UserSetCityPresenter presenter;
    private final JTextField newCityField;

    /**
     * Construct a <code>UserSetCityScreen</code> for <code>users</code> to set the city they located
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserSetCityScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);
        presenter = new UserSetCityPresenter();
        this.userSystem = userSystem;
        setNewCityBtn = new JButton(presenter.setNewCityButton());
        deleteCityBtn = new JButton(presenter.deleteCityButton());
        nowCityLbl = new JLabel(presenter.getCityMsg(userSystem.getCity()));
        newCityField = new JTextField(stringTextFieldSize);
        if (userSystem.isCurrUserGuest()) shouldSerialize = false;
        setup();
    }

    /**
     * Set up the screen
     */
    private void setup() {
        GridBagConstraints c = setupConstraint();
        Container pane = frame.getContentPane();

        nowCityLbl.setFont(fontPrompt);
        pane.add(nowCityLbl, c);

        newCityField.setFont(fontPrompt);
        c.gridx++;
        pane.add(newCityField, c);

        c.gridx = 0;
        c.gridy = 1;
        for (JButton btn : new JButton[]{setNewCityBtn, deleteCityBtn}) {
            btn.setFont(fontPrompt);
            btn.addActionListener(this);
            btn.setPreferredSize(btnDim);
            pane.add(btn, c);
            c.gridx++;
        }

        returnItem.addActionListener(this);

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
        if (e.getSource() == setNewCityBtn) {
            if (!newCityField.getText().equals("")) {
                JOptionPane.showMessageDialog(frame, presenter.updateCityAction(userSystem.setCity(newCityField.getText())));
            } else {
                JOptionPane.showMessageDialog(frame, presenter.WrongEnter());
            }
            newCityField.setText("");
            nowCityLbl.setText(presenter.getCityMsg(userSystem.getCity()));
        } else if (e.getSource() == deleteCityBtn) {
            userSystem.setCity("");
            JOptionPane.showMessageDialog(frame, presenter.updateCityAction(true));
            nowCityLbl.setText(presenter.getCityMsg(userSystem.getCity()));
        } else if (e.getSource() == returnItem) {
            new UserAccountMenuScreen(serializer, userSystem);
            frame.dispose();
        }
    }
}