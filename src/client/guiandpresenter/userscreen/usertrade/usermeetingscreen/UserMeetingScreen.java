package client.guiandpresenter.userscreen.usertrade.usermeetingscreen;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.usertrade.userviewtradescreen.UserViewTradeScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A gui class, for {@code users} to view and edit {@code meeting}
 */
public class UserMeetingScreen extends Screen implements ActionListener {

    private final JButton getMeetingInfoBtn, createMeetingBtn, editMeetingBtn,
            confirmMeetingArrangementBtn, confirmMeetingOccurredBtn;
    private final UserMeetingPresenter presenter;
    private final int tradeId;
    private final UserSystem userSystem;
    private final JDialog editMeetingDialog, createMeetingDialog;
    private final JLabel label;

    /**
     * Construct a <code>UserViewOrEditMeetingScreen</code> for <code>users</code> to view and edit <code>Meeting</code>.
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     * @param tradeId    id of the trade related to the <code>Meeting</code> that is going to be browsed.
     */
    public UserMeetingScreen(DataSerializer serializer, int tradeId, UserSystem userSystem) {
        super(serializer);

        this.tradeId = tradeId;

        presenter = new UserMeetingPresenter();
        this.userSystem = userSystem;

        getMeetingInfoBtn = new JButton(presenter.getMeetingInfoBtn());
        createMeetingBtn = new JButton(presenter.createEditBtn()[0]);
        editMeetingBtn = new JButton(presenter.createEditBtn()[1]);
        confirmMeetingArrangementBtn = new JButton(presenter.confirmMeetingArrangementBtn());
        confirmMeetingOccurredBtn = new JButton(presenter.confirmMeetingOccurredBtn());
        label = new JLabel(presenter.getMeetingThresholds(userSystem.getMeetingThreshold()));

        editMeetingDialog = new JDialog();
        createMeetingDialog = new JDialog();

        setupLog(editMeetingDialog, editMeetingBtn.getText());
        setupLog(createMeetingDialog, createMeetingBtn.getText());

        if (userSystem.isCurrUserGuest())
            shouldSerialize = false;

        setup();
    }

    /*Set up the screen*/
    private void setup() {
        returnItem.addActionListener(this);
        Container pane = frame.getContentPane();

        GridBagConstraints c = setupConstraint();

        label.setFont(fontPrompt);
        pane.add(label, c);
        c.gridy++;

        for (JButton btn : new JButton[]{getMeetingInfoBtn, createMeetingBtn, editMeetingBtn,
                confirmMeetingArrangementBtn, confirmMeetingOccurredBtn}) {
            btn.addActionListener(this);
            btn.setFont(fontPrompt);
            btn.setPreferredSize(btnDim);
            pane.add(btn, c);
            c.gridy++;
        }

        frame.pack();
        frame.setVisible(true);
    }

    /*Called by actionPerformed, take in a button message in String*/
    private void setupLog(JDialog jDialog, String btnMsg) {
        jDialog.setLayout(new GridBagLayout());
        jDialog.setBounds(logX, logY, logWidth, logHeight);
        GridBagConstraints constraint = setupConstraint();

        JTextField timeField = new JTextField(intTextFieldSize),
                locField = new JTextField(intTextFieldSize);

        JLabel meetingTimeLbl = new JLabel(presenter.meetingTimeLbl()),
                meetingLocLbl = new JLabel(presenter.meetingLocLbl());
        JButton btn = new JButton(btnMsg);

        meetingTimeLbl.setFont(fontPrompt);
        constraint.anchor = GridBagConstraints.EAST;
        constraint.ipadx = 70;
        jDialog.add(meetingTimeLbl, constraint);

        constraint.gridy++;
        meetingLocLbl.setFont(fontPrompt);
        jDialog.add(meetingLocLbl, constraint);

        constraint.gridx = 1;
        constraint.gridy = 0;
        timeField.setFont(fontPrompt);
        constraint.anchor = GridBagConstraints.WEST;
        jDialog.add(timeField, constraint);

        constraint.gridy++;
        locField.setFont(fontPrompt);
        jDialog.add(locField, constraint);

        constraint.gridy++;
        btn.setText(btnMsg);
        btn.addActionListener(e -> {
            if (btn.getText().equals(presenter.createEditBtn()[0])) {
                JOptionPane.showMessageDialog(frame, presenter.meetingCreateStatus(userSystem.createMeeting(tradeId,
                        locField.getText(), timeField.getText())));
                createMeetingDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, presenter.meetingEditStatus(userSystem.editMeeting(tradeId,
                        locField.getText(), timeField.getText())));
            }
            locField.setText("");
            timeField.setText("");
        });
        jDialog.add(btn, constraint);

        jDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                locField.setText("");
                timeField.setText("");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                locField.setText("");
                timeField.setText("");
            }
        });
        jDialog.pack();
    }

    /**
     * Listen to the actions performed and response to these actions
     *
     * @param e action performed by the admin who is using the system
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (createMeetingDialog.isVisible())
            createMeetingDialog.dispose();
        if (editMeetingDialog.isVisible())
            editMeetingDialog.dispose();
        if (e.getSource() == getMeetingInfoBtn) {
            JOptionPane.showMessageDialog(frame, presenter.getMeetingInfo(
                    userSystem.getMeetingInfo(Integer.toString(tradeId))));
        } else if (e.getSource() == createMeetingBtn) {
            createMeetingDialog.setVisible(true);
        } else if (e.getSource() == editMeetingBtn) {
            if (userSystem.getMeetingInfo(Integer.toString(tradeId)).equals(""))
                JOptionPane.showMessageDialog(frame, presenter.getMeetingInfo(""));
            else {
                editMeetingDialog.setVisible(true);
            }
        } else if (e.getSource() == confirmMeetingArrangementBtn) {
            JOptionPane.showMessageDialog(frame, presenter.displayMeetingArrStatus(
                    userSystem.confirmMeetingArrangement(tradeId)));
        } else if (e.getSource() == confirmMeetingOccurredBtn) {
            JOptionPane.showMessageDialog(frame, presenter.displayMeetingOccStatus(
                    userSystem.confirmMeetingOccurred(tradeId)));
        } else if (e.getSource() == returnItem) {
            new UserViewTradeScreen(serializer, userSystem);
            frame.dispose();
        }
    }
}
