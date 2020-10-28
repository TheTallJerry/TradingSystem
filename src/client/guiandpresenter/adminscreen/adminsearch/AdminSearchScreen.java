package client.guiandpresenter.adminscreen.adminsearch;

import client.controllers.AdminSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.adminscreen.adminmenu.AdminMenuScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * A gui class responsible for admins to search information by username, trade and meetings
 */
public class AdminSearchScreen extends Screen implements ActionListener {
    private final JButton search;
    private Map<String, String> searchResult;
    private JList<String> result;
    private final DefaultListModel<String> model;
    private final JLabel searchLbl;
    private final JTextField searchFld;
    private final JSplitPane splitPane;
    private final AdminSystem adminSystem;
    private final AdminSearchPresenters presenter;

    /**
     * Construct a <code>AdminSearchScreen</code> for the administration users to search different information
     * by username, trade and meeting
     *
     * @param serializer  the serializer that will be used for initialize
     * @param adminSystem a instance of AdminSystem, which is a controller for administration users
     */
    public AdminSearchScreen(DataSerializer serializer, AdminSystem adminSystem) {
        super(serializer);
        this.adminSystem = adminSystem;
        this.presenter = new AdminSearchPresenters();

        result = new JList<>();
        model = new DefaultListModel<>();
        search = new JButton(presenter.textSearchInfo());

        searchLbl = new JLabel(presenter.enterDirection());
        searchFld = new JTextField(stringTextFieldSize);
        splitPane = new JSplitPane();

        setupGUI();
        setupListener();
    }

    /*Set up the screen*/
    private void setupGUI() {
        search.setFont(fontPrompt);
        searchLbl.setFont(fontPrompt);
        searchFld.setFont(fontInput);
        frame.setFont(fontPrompt);
        frame.setTitle(presenter.getTitle());
        result = new JList<>();
        result.setModel(model);
        JScrollPane scrollPane = new JScrollPane(result);
        scrollPane.setMaximumSize(new Dimension(200, 200));
        scrollPane.setMinimumSize(new Dimension(200, 200));
        splitPane.setLeftComponent(scrollPane);
        result.setVisibleRowCount(6);
        result.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = setupConstraint();
        c.fill = GridBagConstraints.CENTER;
        c.gridy++;
        c.gridy++;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        pane.add(searchLbl, c);

        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.gridy++;
        pane.add(searchFld, c);

        c.gridy++;
        c.gridwidth = 2;
        JScrollPane scroll = new JScrollPane(result);
        scroll.setFont(fontPrompt);
        pane.add(scroll, c);

        search.setPreferredSize(btnDim);
        c.anchor = GridBagConstraints.SOUTH;
        c.gridy++;
        pane.add(search, c);

        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
    }

    private void setupListener() {
        search.addActionListener(this);
        returnItem.addActionListener(this);
        result.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = result.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        JOptionPane.showMessageDialog(frame, searchResult.get(model.getElementAt(index)));
                    }
                }
            }
        });
    }

    /**
     * Listen to the actions performed and response to these actions
     *
     * @param e action performed by the admin who is using the system
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnItem) {
            new AdminMenuScreen(serializer, adminSystem);
            frame.dispose();
        } else if (e.getSource() == search) {
            search();
        }
    }

    private void search() {
        String user = searchFld.getText();
        searchResult = adminSystem.searchByUsername(user);
        model.removeAllElements();
        for (Map.Entry<String, String> e : searchResult.entrySet())
            model.addElement(e.getKey());
        result.getSelectionModel().addListSelectionListener(e1 -> {
        });
    }
}
