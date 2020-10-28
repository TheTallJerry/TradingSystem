package client.guiandpresenter.adminscreen.adminundo;

import client.controllers.AdminSystem;
import client.databundle.DataSerializer;
import client.guiandpresenter.Screen;
import client.guiandpresenter.adminscreen.adminmenu.AdminMenuScreen;
import genericdatatype.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A gui class responsible for admins to undo users actions
 */
public class AdminUndoScreen extends Screen implements ActionListener {
    /* helper class for displaying the actions with a given property in a JList */
    private static class ActionWithProp {
        private final String shortDescrip;
        private final Pair<String, Object> data;

        private ActionWithProp(String shortDescrip, Pair<String, Object> data) {
            this.data = data;
            this.shortDescrip = shortDescrip;
        }

        /**
         * @return <code>shortDescrip</code> as a String representation of ActionWithProp.
         */
        public String toString() {
            return shortDescrip;
        }

        Pair<String, Object> getData() {
            return data;
        }
    }

    // list on the left of screen
    private final JList<String> properties;
    private final DefaultListModel<String> propertiesModel;
    // list on the right of screen
    private final JList<ActionWithProp> actionsWithProperty;
    private final DefaultListModel<ActionWithProp> actionsWithPropertyModel;

    private final JRadioButton byUserBtn;
    private final JRadioButton byActionTypeBtn;
    private final JLabel propertiesLbl;
    private final JLabel actionsWithProperitesLbl;
    private final JLabel instructionLbl;
    private final String[] options;

    private Map<String, Map<String, List<ActionWithProp>>> actionByUserMap;
    private Map<String, Map<String, List<ActionWithProp>>> actionByTypeMap;

    private final AdminUndoPresenter presenter;
    private final AdminSystem adminSystem;

    /**
     * Construct a <code>AdminUndoScreen</code> for the administration users to undo the user actions
     *
     * @param serializer  the serializer that will be used for initialize
     * @param adminSystem a instance of AdminSystem, which is a controller for administration users
     */
    public AdminUndoScreen(DataSerializer serializer, AdminSystem adminSystem) {
        super(serializer);
        this.adminSystem = adminSystem;
        presenter = new AdminUndoPresenter();
        frame.setTitle(presenter.getTitle());
        options = presenter.undoOption();
        propertiesModel = new DefaultListModel<>();
        actionsWithPropertyModel = new DefaultListModel<>();
        actionsWithProperty = new JList<>();
        properties = new JList<>();
        instructionLbl = new JLabel(presenter.undoInstruction());
        propertiesLbl = new JLabel(presenter.undoProperties());
        actionsWithProperitesLbl = new JLabel(presenter.undoActionProp());
        byUserBtn = new JRadioButton(presenter.undoByUser());
        byActionTypeBtn = new JRadioButton(presenter.undoByActionType());

        setup();
    }

    /*Set up the screen*/
    private void setup() {
        setupUI();
        setupListeners();

        setupData();
        showByUser();
    }

    private void setupData() {
        actionByUserMap = new HashMap<>();
        adminSystem.getFormattedRevertersByUsername().forEach((key, value) -> {
            actionByUserMap.put(key, new HashMap<>());
            value.forEach((key1, val1) -> {
                actionByUserMap.get(key).put(key1, new ArrayList<>());
                val1.forEach((pair) -> actionByUserMap.get(key).get(key1).add(new ActionWithProp(key1, pair)));
            });
        });
        actionByTypeMap = new HashMap<>();
        adminSystem.getFormattedRevertersByType().forEach((key, value) -> {
            actionByTypeMap.put(key, new HashMap<>());
            value.forEach((key1, val1) -> {
                actionByTypeMap.get(key).put(key1, new ArrayList<>());
                val1.forEach((pair) -> actionByTypeMap.get(key).get(key1).add(new ActionWithProp(key1, pair)));
            });
        });
    }

    /*Called by setup, set up the UI*/
    private void setupUI() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        final JScrollPane scrollPane1 = new JScrollPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane1, gbc);
        scrollPane1.setViewportView(properties);
        properties.setModel(propertiesModel);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.3;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane2, gbc);
        scrollPane2.setViewportView(actionsWithProperty);
        actionsWithProperty.setModel(actionsWithPropertyModel);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer4, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(instructionLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(propertiesLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(actionsWithProperitesLbl, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(byUserBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(byActionTypeBtn, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer5, gbc);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(byUserBtn);
        buttonGroup.setSelected(byUserBtn.getModel(), true);
        buttonGroup.add(byActionTypeBtn);

        frame.add(panel1);
        frame.pack();
        frame.setVisible(true);
    }

    /*Called by setup. Set up all the listeners of the screen*/
    private void setupListeners() {
        returnItem.addActionListener(this);
        byUserBtn.addActionListener(this);
        byActionTypeBtn.addActionListener(this);
        properties.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = properties.locationToIndex(e.getPoint());
                Map<String, Map<String, List<ActionWithProp>>> actionMap = byUserBtn.isSelected() ? actionByUserMap :
                        actionByTypeMap;
                actionsWithPropertyModel.removeAllElements();
                if (index >= 0) {
                    actionMap.get(propertiesModel.getElementAt(index)).values().forEach((list) ->
                            list.forEach(actionsWithPropertyModel::addElement));
                }
            }
        });
        actionsWithProperty.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = actionsWithProperty.locationToIndex(e.getPoint());
                String property = propertiesModel.getElementAt(properties.getSelectedIndex());
                if (index < 0)
                    return;
                Map<String, Map<String, List<ActionWithProp>>> actionMap = byUserBtn.isSelected() ? actionByUserMap :
                        actionByTypeMap;
                ActionWithProp actionWithProp = actionsWithPropertyModel.getElementAt(index);
                int result = JOptionPane.showOptionDialog(frame, "<html> " + actionWithProp.getData().value1 + "</html>",
                        presenter.undoAsk(),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (result == 0) {
                    String undoResult = adminSystem.undo(actionWithProp.getData().value2);
                    JOptionPane.showMessageDialog(frame, undoResult);
                    actionMap.get(property).get(actionWithProp.toString()).remove(actionWithProp);
                    actionsWithPropertyModel.remove(index);
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
        }
        if (e.getSource() == byUserBtn)
            showByUser();
        else if (e.getSource() == byActionTypeBtn)
            showByType();
    }

    /*Called by setup  and actionPerformed, showing information by user*/
    private void showByUser() {
        propertiesModel.removeAllElements();
        actionByUserMap.keySet().forEach(propertiesModel::addElement);
        properties.setSelectedIndex(0);
        actionsWithPropertyModel.removeAllElements();
    }

    /*Called by setup  and actionPerformed, showing information by type*/
    private void showByType() {
        propertiesModel.removeAllElements();
        actionByTypeMap.keySet().forEach(propertiesModel::addElement);
        properties.setSelectedIndex(0);
        actionsWithPropertyModel.removeAllElements();
    }
}