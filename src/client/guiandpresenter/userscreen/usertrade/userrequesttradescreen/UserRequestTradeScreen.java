package client.guiandpresenter.userscreen.usertrade.userrequesttradescreen;

import client.controllers.UserSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.userscreen.usertrade.usertrademenuscreen.UserTradeMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A gui class, for <code>users</code> to request <code>Trade</code> with other <code>users</code>
 */
public class UserRequestTradeScreen extends Screen implements ActionListener {
    private final JButton nextBtn, withIdSuggestionBtn, mostBorrowTypeBtn;
    private final JTextField optionalLendItemField;
    private final JLabel optionalLendItemLbl, isPermanentLbl;
    private final JComboBox<String> isPermanentTrade;
    private final JDialog log;
    private final UserSystem userSystem;
    private final UserRequestTradePresenter presenter;
    private final DefaultListModel<String> itemsModel, wishlistModel, itemsAvailableListModel;
    private JList<String> itemsList;
    private JList<String> itemsAvailableList;
    private JList<String> wishlistList;
    private final String[] options;
    private String selected;

    /**
     * Construct a <code>UserRequestTradeScreen</code> for <code>users</code> to
     * request <code>Trade</code> with other <code>users</code>
     *
     * @param serializer the serializer that will be used for initialize
     * @param userSystem a instance of <code>UserSystem</code>, which is a controller for users
     */
    public UserRequestTradeScreen(DataSerializer serializer, UserSystem userSystem) {
        super(serializer);

        this.userSystem = userSystem;

        presenter = new UserRequestTradePresenter();

        options = presenter.getOptions();
        optionalLendItemField = new JTextField(intTextFieldSize);

        isPermanentTrade = new JComboBox<>(presenter.tradeTypes());

        isPermanentLbl = new JLabel(presenter.tradeTypeMsg());
        optionalLendItemLbl = new JLabel(presenter.optionalLendItemLblMsg());

        nextBtn = new JButton(presenter.nextBtnMsg());
        withIdSuggestionBtn = new JButton(presenter.withIdSuggestionBtn());
        mostBorrowTypeBtn = new JButton(presenter.mostBorrowTypeBtn());

        itemsModel = new DefaultListModel<>();
        wishlistModel = new DefaultListModel<>();
        itemsAvailableListModel = new DefaultListModel<>();

        if (userSystem.isCurrUserGuest())
            shouldSerialize = false;

        log = new JDialog();
        setup();
    }

    /*Set up the screen*/
    private void setup() {
        returnItem.addActionListener(this);

        GridBagConstraints c = setupConstraint();
        JPanel pane = new JPanel();

        //GridBagConstraints c = setupConstraint();
        //Container pane = frame.getContentPane();

        JScrollPane scrollPane = new JScrollPane();

        itemsList = new JList<>();
        itemsList.setModel(itemsModel);
        userSystem.getInventoryForTrade().forEach(itemsModel::addElement);
        itemsList.setFont(fontPrompt);
        itemsAvailableList = new JList<>();
        itemsAvailableList.setModel(itemsAvailableListModel);
        userSystem.getUserItemsAvailable().forEach(itemsAvailableListModel::addElement);
        itemsAvailableList.setFont(fontPrompt);
        wishlistList = new JList<>();
        wishlistList.setModel(wishlistModel);
        userSystem.getUserWishlist().forEach(wishlistModel::addElement);
        wishlistList.setFont(fontPrompt);

        c.gridx = 3;
        c.gridy++;
        c.ipadx = 0;
        c.ipady = 0;

        mostBorrowTypeBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                presenter.mostBorrowedType(userSystem.createBorrowingSuggestion())));
        mostBorrowTypeBtn.setFont(fontPrompt);
        mostBorrowTypeBtn.setPreferredSize(btnDim);
        pane.add(mostBorrowTypeBtn, c);

        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.ipadx = frame.getPreferredSize().width / 5;
        gbc.ipady = frame.getPreferredSize().height / 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        pane.add(scrollPane, gbc);
        scrollPane.setViewportView(itemsList);

        scrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.ipadx = frame.getPreferredSize().width / 5;
        gbc.ipady = frame.getPreferredSize().height / 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        pane.add(scrollPane, gbc);
        scrollPane.setViewportView(itemsAvailableList);

        scrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.ipadx = frame.getPreferredSize().width / 5;
        gbc.ipady = frame.getPreferredSize().height / 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        pane.add(scrollPane, gbc);
        scrollPane.setViewportView(wishlistList);
        String[] buttonList = presenter.boxTitle();
        for (int i = 1; i < 4; i++) {
            gbc = new GridBagConstraints();
            gbc.gridx = i;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.anchor = GridBagConstraints.WEST;
            pane.add(new JLabel(buttonList[i - 1]), gbc);
        }
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        pane.add(new JLabel(presenter.instructionLabel()), gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pane.add(spacer1, gbc);

        frame.pack();
        frame.setTitle(presenter.getTitle());
        frame.add(pane);
        frame.setVisible(true);
        setupPopupDialog();
        setupLog();
    }

    /*Called by setup*/
    private void setupLog() {
        log.setLayout(new GridBagLayout());
        log.setBounds(logX, logY, logWidth, logHeight);

        GridBagConstraints c = setupConstraint();
        c.weighty = 0.1;

        //optionalLendItemLbl and optionalLendItemField
        c.gridx = 0;
        optionalLendItemLbl.setFont(fontPrompt);
        optionalLendItemField.setFont(fontInput);
        log.add(optionalLendItemLbl, c);

        c.gridx++;
        log.add(optionalLendItemField, c);

        c.gridy++;
        c.gridx = 0;

        //isPermanentLbl and isPermanentTrade
        isPermanentLbl.setFont(fontPrompt);
        isPermanentTrade.setFont(fontInput);
        log.add(isPermanentLbl, c);

        c.gridx++;
        log.add(isPermanentTrade, c);

        c.gridy++;
        c.gridx = 0;
        withIdSuggestionBtn.addActionListener(this);
        withIdSuggestionBtn.setFont(fontPrompt);
        log.add(withIdSuggestionBtn, c);

        c.gridx++;
        nextBtn.addActionListener(this);
        nextBtn.setFont(fontPrompt);
        log.add(nextBtn, c);

        log.pack();
    }

    private void setupPopupDialog() {
// reference: http://www.java2s.com/Code/JavaAPI/javax.swing/JListaddMouseListenerMouseListenerlis.htm
        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (e.getSource() == itemsList) {
                        int index = itemsList.locationToIndex(e.getPoint());
                        if (index >= 0) {
                            selected = itemsModel.getElementAt(index);
                            int result = JOptionPane.showOptionDialog(frame, presenter.itemDetails(selected),
                                    presenter.requestTradePrompt(), JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                            if (result == 0) log.setVisible(true);
                        }
                    } else if (e.getSource() == wishlistList) {
                        int index = wishlistList.locationToIndex(e.getPoint());
                        if (index >= 0) {
                            selected = wishlistModel.getElementAt(index);
                            JOptionPane.showMessageDialog(frame, presenter.itemDetails(selected));
                        }
                    } else if (e.getSource() == itemsAvailableList) {
                        int index = itemsAvailableList.locationToIndex(e.getPoint());
                        if (index >= 0) {
                            selected = itemsAvailableListModel.getElementAt(index);
                            JOptionPane.showMessageDialog(frame, presenter.itemDetails(selected));
                        }
                    }
                }
            }
        };
        itemsList.addMouseListener(mouseListener);
        wishlistList.addMouseListener(mouseListener);
        itemsAvailableList.addMouseListener(mouseListener);
    }

    /**
     * Listen to the actions performed and response to these actions
     *
     * @param e action performed by the admin who is using the system
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextBtn) {
            log.dispose();
            JOptionPane.showMessageDialog(frame, presenter.displayTradeRequestStatus(userSystem.requestTrade(
                    selected, optionalLendItemField.getText(), String.valueOf(isPermanentTrade.getSelectedItem()))));
        } else if (e.getSource() == withIdSuggestionBtn) {
            JOptionPane.showMessageDialog(frame, presenter.lendingSuggestion(
                    userSystem.createLendingSuggestion(selected)));
        } else if (e.getSource() == returnItem) {
            if (log.isVisible()) log.dispose();
            new UserTradeMenuScreen(serializer, userSystem);
            frame.dispose();
        }
    }
}
