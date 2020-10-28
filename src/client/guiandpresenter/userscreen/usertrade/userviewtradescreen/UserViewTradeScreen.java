package client.guiandpresenter.userscreen.usertrade.userviewtradescreen;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.usertrade.usermeetingscreen.UserMeetingScreen;
import client.guiandpresenter.userscreen.usertrade.usertrademenuscreen.UserTradeMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A gui class, for <code>users</code> to view all <code>trades</code> related
 */
public class UserViewTradeScreen extends Screen {
    private final JList<String> tradeJList;
    private final JPopupMenu tradePopupMenu;
    private final UserViewTradePresenter presenter;
    private String[][] tradeInfos;
    private final UserSystem userSystem;

    /**
     * displays all <code>trade</code>, then a JDialog to prompt for a trade id to
     * do actions on, then select view/edit <code>meeting</code> or agree/refuse
     * , validate, then takes to ViewEdit or AcceptRefuse.
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserViewTradeScreen(DataSerializer serializer,
                               UserSystem userSystem) {
        super(serializer);
        if (userSystem.isCurrUserGuest()) shouldSerialize = false;
        presenter = new UserViewTradePresenter();
        this.userSystem = userSystem;
        frame.setLayout(new BorderLayout());
        JPanel panelTrade = new JPanel();

        returnItem.addActionListener(e -> {
            new UserTradeMenuScreen(
                    serializer, userSystem);
            frame.dispose();
        });

        tradeJList = new JList<>();
        tradePopupMenu = new JPopupMenu();
        String[] menuItem = presenter.menuItem();
        JMenuItem viewOrEditMeeting = new JMenuItem(menuItem[0]);
        JMenuItem agreeToTrade = new JMenuItem(menuItem[1]);
        JMenuItem denyToTrade = new JMenuItem(menuItem[2]);
        tradePopupMenu.add(viewOrEditMeeting);
        tradePopupMenu.add(agreeToTrade);
        tradePopupMenu.add(denyToTrade);
        tradeJList.add(tradePopupMenu);

        if (userSystem.getTradesInfo().get(0).length == 0) {
            panelTrade.add(new JLabel(presenter.noTrade()));
            tradeInfos = new String[0][0];
        } else {
            panelTrade.add(new JLabel(presenter.tradeInstruction()));
            tradeInfos = new String[3][userSystem.getTradesInfo().get(0).length];
            int counter = 0;
            for (String[] s : userSystem.getTradesInfo()) {
                System.arraycopy(s, 0, tradeInfos[counter], 0, s.length);
                counter++;
            }
            tradeJList.setListData(tradeInfos[1]);
        }
        panelTrade.add(tradeJList);

        tradeJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3 && tradeJList.getSelectedIndex() != -1) {
                    tradePopupMenu.show(e.getComponent(), e.getX(), e.getY());
                } else if (e.getClickCount() == 2 && tradeJList.getSelectedIndex() != -1) {
                    JOptionPane.showMessageDialog(frame, tradeInfos[2][tradeJList.getSelectedIndex()]);
                }
            }
        });

        viewOrEditMeeting.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int tradeId = Integer.parseInt(tradeInfos[0][tradeJList.getSelectedIndex()]);
                if (userSystem.tradeNotStarted(tradeId))
                    JOptionPane.showMessageDialog(frame, presenter.tradeNotStarted());
                else {
                    new UserMeetingScreen(serializer, tradeId, userSystem);
                    frame.dispose();
                }
            }
        });

        agreeToTrade.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int tradeId = Integer.parseInt(tradeInfos[0][tradeJList.getSelectedIndex()]);
                JOptionPane.showMessageDialog(frame, presenter.agreeToTradeStatus(userSystem.agreeToTrade(tradeId)));
                setListData();
            }
        });

        denyToTrade.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int tradeId = Integer.parseInt(tradeInfos[0][tradeJList.getSelectedIndex()]);
                JOptionPane.showMessageDialog(frame, presenter.declineTradeStatus(userSystem.denyTrade(tradeId)));
                setListData();
            }
        });

        frame.setTitle(presenter.getTitle());
        frame.add(panelTrade, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private void setListData() {
        tradeInfos = new String[3][userSystem.getTradesInfo().get(0).length];
        int counter = 0;
        for (String[] s : userSystem.getTradesInfo()) {
            System.arraycopy(s, 0, tradeInfos[counter], 0, s.length);
            counter++;
        }
        tradeJList.setListData(tradeInfos[1]);
    }
}