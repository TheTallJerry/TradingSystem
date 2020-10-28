package client.guiandpresenter.adminscreen.adminrequest;

import client.controllers.AdminSystem;
import client.controllers.RequestType;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.adminscreen.adminmenu.AdminMenuScreen;
import genericdatatype.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Map;

/**
 * A gui class responsible for showing the requests waiting for admins to check
 */
public class AdminRequestScreen extends Screen {
    private static class Request {
        private final String shortDescrip;
        private final String longDescrip;

        Request(String shortDescrip, String longDescrip) {
            this.shortDescrip = shortDescrip;
            this.longDescrip = longDescrip;
        }

        @Override
        public String toString() {
            return getLongDescrip();
        }

        Pair<String, String> toPair() {
            return new Pair<>(getShortDescrip(), getLongDescrip());
        }

        String getLongDescrip() {
            return longDescrip;
        }

        String getShortDescrip() {
            return shortDescrip;
        }
    }

    private final JPanel panel;
    private final JList<Request> itemRequestLst;
    private final JList<Request> unfreezeRequestLst;
    private final JList<Request> reportLst;
    private final JList<Request> adminCreationLst;
    private final JLabel itemRequestLbl;
    private final JLabel unfreezeRequestLbl;
    private final JLabel userReportLbl;
    private final JLabel adminCreationLbl;
    private final JLabel instructionLbl;
    private final DefaultListModel<Request> itemModel;
    private final DefaultListModel<Request> unfreezeModel;
    private final DefaultListModel<Request> reportModel;
    private final DefaultListModel<Request> adminCreationModel;
    private final String[] options = {"Accept", "Deny", "Back"};
    private final AdminRequestPresenter presenter;
    private final AdminSystem adminSystem;

    /**
     * Construct a <code>AdminRequestScreen</code> for the administration users to accept and deny requests from users
     *
     * @param serializer  the serializer that will be used for initialize
     * @param adminSystem a instance of AdminSystem, which is a controller for administration users
     */
    public AdminRequestScreen(DataSerializer serializer, AdminSystem adminSystem) {
        super(serializer);
        this.presenter = new AdminRequestPresenter();
        this.adminSystem = adminSystem;

        panel = new JPanel();
        itemRequestLst = new JList<>();
        unfreezeRequestLst = new JList<>();
        reportLst = new JList<>();
        adminCreationLst = new JList<>();
        itemRequestLbl = new JLabel(presenter.itemRequestLbl());
        unfreezeRequestLbl = new JLabel(presenter.unfreezeRequestLbl());
        userReportLbl = new JLabel(presenter.userReportLbl());
        adminCreationLbl = new JLabel(presenter.adminCreationLbl());
        instructionLbl = new JLabel(presenter.requestInstruction());
        itemModel = new DefaultListModel<>();
        unfreezeModel = new DefaultListModel<>();
        reportModel = new DefaultListModel<>();
        adminCreationModel = new DefaultListModel<>();

        setup();
    }

    /*Set up the screen*/
    private void setup() {
        setupUI();
        fillInLists();
        setupListener(itemRequestLst, itemModel, RequestType.ITEM);
        setupListener(unfreezeRequestLst, unfreezeModel, RequestType.UNFREEZE);
        setupListener(reportLst, reportModel, RequestType.REPORT);
        setupListener(adminCreationLst, adminCreationModel, RequestType.ADMIN_CREATION);
        frame.setTitle(presenter.getTitle());
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /*Called by setup, set up the UI*/
    private void setupUI() {
        // set up UI
        itemRequestLst.setFont(fontPrompt);
        unfreezeRequestLst.setFont(fontPrompt);
        itemRequestLbl.setFont(fontPrompt);
        unfreezeRequestLbl.setFont(fontPrompt);
        reportLst.setFont(fontPrompt);
        userReportLbl.setFont(fontPrompt);
        adminCreationLbl.setFont(fontPrompt);
        adminCreationLst.setFont(fontPrompt);
        instructionLbl.setFont(fontPrompt);
        itemRequestLst.setModel(itemModel);
        unfreezeRequestLst.setModel(unfreezeModel);
        reportLst.setModel(reportModel);
        adminCreationLst.setModel(adminCreationModel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.ipadx = 100;
        gbc.ipady = 150;
        gbc.weightx = 1;
        gbc.weighty = 1;
        JScrollPane scrollPane = new JScrollPane();
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);
        scrollPane.setViewportView(itemRequestLst);
        scrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.ipadx = 100;
        gbc.ipady = 150;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);
        scrollPane.setViewportView(unfreezeRequestLst);
        scrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.ipadx = 100;
        gbc.ipady = 150;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);
        scrollPane.setViewportView(reportLst);
        scrollPane = new JScrollPane();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.ipadx = 100;
        gbc.ipady = 150;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);
        scrollPane.setViewportView(adminCreationLst);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(itemRequestLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(unfreezeRequestLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userReportLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(adminCreationLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(instructionLbl, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spacer1, gbc);

        returnItem.addActionListener(e -> {
            new AdminMenuScreen(serializer, adminSystem);
            frame.dispose();
        });
    }

    /*Called by setup, set up the lists showed on screen*/
    private void fillInLists() {
        // fill in JLists
        Map<RequestType, Collection<Pair<String, String>>> requests = adminSystem.getRequests();
        for (Map.Entry<RequestType, Collection<Pair<String, String>>> e : requests.entrySet()) {
            switch (e.getKey()) {
                case ITEM:
                    e.getValue().forEach(p -> itemModel.addElement(new Request(p.value1, p.value2)));
                    break;
                case UNFREEZE:
                    e.getValue().forEach(p -> unfreezeModel.addElement(new Request(p.value1, p.value2)));
                    break;
                case REPORT:
                    e.getValue().forEach(p -> reportModel.addElement(new Request(p.value1, p.value2)));
                    break;
                case ADMIN_CREATION:
                    e.getValue().forEach(p -> adminCreationModel.addElement(new Request(p.value1, p.value2)));
                    break;
            }
        }
    }

    /* Remove the request from screen after it is being handled */
    private void deleteItem(RequestType type, int index) {
        switch (type) {
            case ITEM:
                itemModel.remove(index);
                break;
            case UNFREEZE:
                unfreezeModel.remove(index);
                break;
            case REPORT:
                reportModel.remove(index);
                break;
            case ADMIN_CREATION:
                adminCreationModel.remove(index);
                break;
        }
    }


    /*Called by setup. Set up all the listeners of the screen*/
    private void setupListener(JList<Request> jList, DefaultListModel<Request> model, RequestType type) {
        // reference: http://www.java2s.com/Code/JavaAPI/javax.swing/JListaddMouseListenerMouseListenerlis.htm
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                int index = jList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    Request r = model.getElementAt(index);
                    int result = JOptionPane.showOptionDialog(frame,
                            presenter.detailedRequest(r.getLongDescrip()),
                            presenter.detailedRequestTitle(),
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (result == 0 || result == 1) {
                        JOptionPane.showMessageDialog(frame, presenter.requestProcess(
                                adminSystem.handleRequest(type, r.toPair(), result == 0)));
                        deleteItem(type, index);
                    }
                }
            }
        });
    }
}
