package client.guiandpresenter.userscreen.usertrade.usertrademenuscreen;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.usermenu.UserMenuScreen;
import client.guiandpresenter.userscreen.usertrade.userrequesttradescreen.UserRequestTradeScreen;
import client.guiandpresenter.userscreen.usertrade.userviewtradescreen.UserViewTradeScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A gui class, for <code>users</code> to choose next actions related to <code>Trade</code>
 */
public class UserTradeMenuScreen extends Screen implements ActionListener {

    private final JButton viewTradeBtn, requestTradeBtn,
            threeRecentTradePartnersBtn, threeRecentTradeItemsBtn, tradeThresholdsBtn;
    private final UserTradeMenuPresenter presenter;
    private final UserSystem userSystem;

    /**
     * Construct a <code>UserTradeDirectory</code> for <code>users</code> to
     * choose next actions related to <code>Trade</code>
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserTradeMenuScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);


        this.userSystem = userSystem;
        presenter = new UserTradeMenuPresenter();

        viewTradeBtn = new JButton(presenter.viewTradeBtn());
        requestTradeBtn = new JButton(presenter.requestTradeBtn());
        threeRecentTradePartnersBtn = new JButton(presenter.threeRecentTradePartnersBtn());
        threeRecentTradeItemsBtn = new JButton(presenter.threeRecentTradeItemsBtn());
        tradeThresholdsBtn = new JButton(presenter.tradeThresholdsBtn());

        shouldSerialize = false;

        setup();
    }

    /*Set up the screen*/
    private void setup() {
        returnItem.addActionListener(this);

        frame.setFont(fontPrompt);
        frame.setTitle(presenter.getTitle());
        Container pane = frame.getContentPane();

        GridBagConstraints c = setupConstraint();

        c.fill = GridBagConstraints.CENTER;

        for (JButton btn : new JButton[]{viewTradeBtn, requestTradeBtn,
                threeRecentTradePartnersBtn, threeRecentTradeItemsBtn, tradeThresholdsBtn}) {
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
        if (e.getSource() == viewTradeBtn) {
            new UserViewTradeScreen(serializer, userSystem);
            frame.dispose();
        } else if (e.getSource() == requestTradeBtn) {
            if (userSystem.getOnVacation())
                JOptionPane.showMessageDialog(frame, presenter.cannotRequestTradeMsg());
            else {
                new UserRequestTradeScreen(serializer, userSystem);
                frame.dispose();
            }
        } else if (e.getSource() == returnItem) {
            new UserMenuScreen(serializer, userSystem);
            frame.dispose();
        } else if (e.getSource() == threeRecentTradeItemsBtn) {
            JOptionPane.showMessageDialog(frame, presenter.getThreeRecentTradeItemMsg(userSystem.getThreeRecentTradeItem()));
        } else if (e.getSource() == threeRecentTradePartnersBtn) {
            JOptionPane.showMessageDialog(frame, presenter.getTopThreeTradePartnerMsg(userSystem.getTopThreeTradePartner()));
        } else if (e.getSource() == tradeThresholdsBtn) {
            JOptionPane.showMessageDialog(frame, presenter.getTradeThresholds(userSystem.getTradeThresholds()));
        }
    }
}
