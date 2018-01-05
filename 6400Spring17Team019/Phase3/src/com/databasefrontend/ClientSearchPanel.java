package com.databasefrontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by adityasahai on 25/03/17.
 */
public class ClientSearchPanel extends JPanel {
    String[] idtypes = new String[] {"driving_license", "ssn", "passport",
            "birth_certificate", "refugee_travel_document", "visa"};

    private UserClass user;
    private JRadioButton searchByName;
    private JRadioButton searchByID;
    private ButtonGroup btnGroup;
    private JTextField searchBar;
    private JComboBox idTypes;
    private JButton searchBtn;
    private JButton clearBtn;
    private JTable searchResults;


    public ClientSearchPanel(UserClass user) {
        this.user = user;

        this.searchByID = new JRadioButton("Search By ID");
        this.searchByName = new JRadioButton("Search By Name");
        this.btnGroup = new ButtonGroup();
        this.searchBar = new JTextField("Click here to Search...", 20);
        this.idTypes = new JComboBox();
        this.searchBtn = new JButton("Search");
        this.clearBtn = new JButton("Clear Search");
        this.searchResults = new JTable(4, 5);
        this.searchResults.setEnabled(false);


        this.btnGroup.add(this.searchByID);
        this.btnGroup.add(this.searchByName);

        this.idTypes = new JComboBox();
        for (String s : idtypes) {
            idTypes.addItem(toTitleCase(s));
        }

        SearchBarListener searchBarListener = new SearchBarListener(searchBar, searchBtn);

        this.searchBtn.setEnabled(false); // Disable Btn

        this.searchBar.addMouseListener(searchBarListener);

        this.searchByName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idTypes.setEnabled(false);
            }
        });

        this.searchByID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idTypes.setEnabled(true);
            }
        });

        // Default selection
        this.searchByName.setSelected(true);
        // Call action event
        for (ActionListener a : this.searchByName.getActionListeners()) {
            a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {});
        }

        this.searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBCalls dbCalls;
                if (searchByName.isSelected()) {
                    dbCalls = new DBCalls(sqlCall_enum.searchclientbyname, new String[] {searchBar.getText(),
                            searchBar.getText(), searchBar.getText(), searchBar.getText()});
                }
                else {
                    dbCalls = new DBCalls(sqlCall_enum.searchclientbyidtype, new String[] {searchBar.getText(),
                            idtypes[idTypes.getSelectedIndex()], searchBar.getText(),
                            idtypes[idTypes.getSelectedIndex()]});
                }
                TableData td = dbCalls.executeReadCall();

                displayResults(td);
            }
        });

        this.clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBarListener.reset();
                idTypes.setSelectedIndex(0);
                searchResults = new JTable(4, 5);
                searchResults.setEnabled(false);
                layoutComponents();
            }
        });

        layoutComponents();
    }

    private void displayResults(TableData td) {
        if (td == null) {
            searchResults = new JTable(4, 5);
            JOptionPane.showMessageDialog(null, "No Results Found!", "Client Search",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        else if (td.getNumRows() > 4) {
            searchResults = new JTable(4, 5);
            JOptionPane.showMessageDialog(null, "Please Enter a " +
                            "More Precise Search String", "Client Search",
                    JOptionPane.INFORMATION_MESSAGE);

        } else {
            DefaultTableModel model;
            model = new DefaultTableModel(td.getData(), td.getColumnNames());
            searchResults = new JTable(model);
            TableUtils.addButtonCol(searchResults, "View", "View client");
            TableUtils.addButtonCol(searchResults, "Edit Client Info", "Edit client");

            ActionListener viewAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    new ClientReportDialog(searchResults, table.getValueAt(row, 0),
                            table.getValueAt(row, 3), user.getSiteId());
                }
            };
            ActionListener editAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    EditClientDialog eid =  new EditClientDialog(table, row);
                    if (eid.isClientUpdated()) {
                        for (ActionListener a : searchBtn.getActionListeners()) {
                            a.actionPerformed(e);
                        }
                    }
                }
            };
            searchResults.getColumn("View client").setCellEditor(new ButtonEditorAndRenderer(viewAL));
            searchResults.getColumn("View client").setCellRenderer(new ButtonEditorAndRenderer(viewAL));
            searchResults.getColumn("Edit client").setCellEditor(new ButtonEditorAndRenderer(editAL));
            searchResults.getColumn("Edit client").setCellRenderer(new ButtonEditorAndRenderer(editAL));


        }

        searchResults.setEnabled(true);
        layoutComponents();
    }

    private static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                c = ' ';
            }
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    private void layoutComponents() {
        setSize(new Dimension(600, 50));
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        removeAll(); // Remove existing components

        // First Row

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(10, 0, 0, 10);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.searchByName, gc);

        gc.gridx = 1;
        gc.insets = new Insets(10, 10, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.searchByID, gc);

        // Second Row

        gc.gridy = 1;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(10, 0, 0, 10);
        add(this.searchBar, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(10, 10, 0, 0);
        add(this.idTypes, gc);

        // Third Row

        gc.gridy = 2;
        gc.gridx = 0;
        gc.insets = new Insets(10, 0, 0, 10);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.searchBtn, gc);

        gc.gridx = 1;
        gc.insets = new Insets(10, 10, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.clearBtn, gc);

        // Last row
        gc.weighty = 2.0;
        gc.gridwidth = 2;

        gc.gridy = 3;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(this.searchResults), gc);
    }
}

class SearchBarListener implements MouseListener {
    public boolean isSearchInProgress() {
        return searchInProgress;
    }

    public void setSearchInProgress(boolean searchInProgress) {
        this.searchInProgress = searchInProgress;
    }

    private boolean searchInProgress;
    private JTextField searchBar;
    private JButton searchBtn;

    public SearchBarListener(JTextField searchBar, JButton searchBtn) {
        this.searchBar = searchBar;
        this.searchInProgress = false;
        this.searchBtn = searchBtn;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isSearchInProgress()) {
            searchBar.setText("");
            enableSearchBtn();
            setSearchInProgress(true);
        }
    }

    public void reset() {
        setDefaultText();
        disableSearchBtn();
        setSearchInProgress(false);
    }

    private void setDefaultText() {
        searchBar.setText("Click Here to Search...");
    }

    private void disableSearchBtn() {
        searchBtn.setEnabled(false);
    }

    private void enableSearchBtn() {
        searchBtn.setEnabled(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}


