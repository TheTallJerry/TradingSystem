package client.guiandpresenter.userscreen.useraccount.useraccountmenu;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.useraccount.*;
import client.guiandpresenter.userscreen.useraccount.userrequestitemscreens.UserRequestItemScreen;
import client.guiandpresenter.userscreen.useraccount.usersetcity.UserSetCityScreen;
import client.guiandpresenter.userscreen.usermenu.UserMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class, the first screen showed to user when log in, giving options for user to choose next action.
 */
public class UserAccountMenuScreen extends Screen implements ActionListener {

    private final JButton requestItemCreationBtn, modifyWishlistBtn, changePwButton, getAcctInfoBtn,
            changeOnVacationStatusBtn, modifyBlocklistBtn, setCityBtn, requestUnfreezeBtn, reportBtn;
    private final UserSystem userSystem;

    /**
     * Construct a <code>UserAccountDirectory</code> for users and admins registration
     * from this screen
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of  <code>UserSystem</code>, which is a controller for users
     */
    public UserAccountMenuScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        this.userSystem = userSystem;
        UserAccountMenuPresenter presenter = new UserAccountMenuPresenter();

        requestItemCreationBtn = new JButton(presenter.requestItemCreationBtnMsg());
        modifyWishlistBtn = new JButton(presenter.modifyToWishlistBtnMsg());
        changePwButton = new JButton(presenter.changePwButtonMsg());
        getAcctInfoBtn = new JButton(presenter.getAcctInfoBtn());
        changeOnVacationStatusBtn = new JButton(presenter.changeOnVacationStatusBtn());
        modifyBlocklistBtn = new JButton(presenter.modifyBlocklistBtn());
        setCityBtn = new JButton(presenter.setCityBtn());
        requestUnfreezeBtn = new JButton(presenter.requestUnfreezeBtn());
        reportBtn = new JButton(presenter.reportBtn());

        shouldSerialize = false;

        setup();
    }

    /**
     * Set up the screen
     */
    private void setup() {
        frame.setFont(fontPrompt);
        Container pane = frame.getContentPane();

        GridBagConstraints c = setupConstraint();
        c.fill = GridBagConstraints.CENTER;

        returnItem.addActionListener(this);

        for (JButton btn : new JButton[]{requestItemCreationBtn, modifyWishlistBtn,
                changePwButton, getAcctInfoBtn, changeOnVacationStatusBtn, modifyBlocklistBtn,
                setCityBtn, requestUnfreezeBtn, reportBtn}) {
            btn.setFont(fontPrompt);
            btn.setPreferredSize(btnDim);
            btn.addActionListener(this);
            pane.add(btn, c);
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
        if (e.getSource() == requestItemCreationBtn) {
            UserRequestItemScreen u = new UserRequestItemScreen(serializer, userSystem);
            u.setup();
        } else if (e.getSource() == modifyWishlistBtn) {
            new UserModifyWishlistScreen(serializer, userSystem);
        } else if (e.getSource() == changePwButton) {
            new UserChangePwScreen(serializer, userSystem);
        } else if (e.getSource() == getAcctInfoBtn) {
            new UserGetAccountInfoScreen(serializer, userSystem);
        } else if (e.getSource() == changeOnVacationStatusBtn) {
            new UserChangeOnVacationScreen(serializer, userSystem);
        } else if (e.getSource() == modifyBlocklistBtn) {
            new UserModifyBlockListScreen(serializer, userSystem);
        } else if (e.getSource() == setCityBtn) {
            new UserSetCityScreen(serializer, userSystem);
        } else if (e.getSource() == requestUnfreezeBtn) {
            new UserRequestUnfreezeScreen(serializer, userSystem);
        } else if (e.getSource() == reportBtn) {
            new UserReportScreen(serializer, userSystem);
        } else if (e.getSource() == returnItem) {
            new UserMenuScreen(serializer, userSystem);
        }
        frame.dispose();
    }
}