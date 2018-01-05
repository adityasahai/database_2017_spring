package com.databasefrontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by adityasahai on 25/03/17.
 */
public class ItemSearchPanel extends JPanel{
    private UserClass user;

    private JTextField searchBar, date;
    private JLabel expiry;
    private JButton searchButton;
    private JMenuBar menuBar;
    private ArrayList<JCheckBox> catCheckBox;
    private ArrayList<JCheckBox> subCatCheckBox;
    private ArrayList<JCheckBox> storageCheckBox;
    private ArrayList<JCheckBox> siteCheckBox;
    private JCheckBox showExpired;
    private JTable results;
    private boolean neverClickedOn;
    private String[] searchQuery;

    private final String[] categories = {"food", "supplies"};
    private final String[] storage = {"drygood", "refrigerated", "frozen"};
    private final int numSubCats = 10;

    public ItemSearchPanel(UserClass user) {
        this.user = user;

        // declare components
        this.searchBar = new JTextField("", 25);
        this.searchBar.setMinimumSize(new Dimension(250, 25));
        this.neverClickedOn = true;
        this.searchButton = new JButton("Search");
        this.date = new JTextField("YYYY-MM-DD", 10);
        this.date.setMinimumSize(new Dimension(100, 25));
        this.results = new JTable(10, 8);
        this.catCheckBox = new ArrayList<>();
        this.subCatCheckBox = new ArrayList<>();
        this.storageCheckBox = new ArrayList<>();
        this.siteCheckBox = new ArrayList<>();
        this.searchQuery = new String[7];
        this.expiry = new JLabel("Expiry Date:");
        this.showExpired = new JCheckBox("show expired items");

        populateMenuBar();

        configureComponents();

        layoutComponents();

        setActionListener(); // SQL query is formed here and sent

        setVisible(true);
    }

    private void setActionListener() {
        this.searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSearchQuerySet()) {
                    JOptionPane.showMessageDialog(null, "Search Query Cannot be empty",
                            "Item Search", JOptionPane.ERROR_MESSAGE);
                } else {
                    // set Expiry Date
                    searchQuery[0] = isDateSet() ? date.getText() : "9999-01-01";
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calobj = Calendar.getInstance();

                    searchQuery[1] = showexpired()?"2000-01-01":df.format(calobj.getTime());

                    // Set Categories
                    boolean catIsSet = isSet(catCheckBox);
                    ArrayList<String> cats = new ArrayList<>();
                    for (int i = 0; i < categories.length; i++) {
                        if (catIsSet) {
                            if (catCheckBox.get(i).isSelected()) {
                                cats.add("'" + categories[i] + "'");
                            }
                        } else {
                            cats.add("'" + categories[i] + "'"); // Add all
                        }
                    }

                    searchQuery[2] = "(" + String.join("," , cats) + ")";
                    System.out.println("categories - " + searchQuery[2]);

                    // Set Sub Cat
                    boolean isSubCatSet = isSet(subCatCheckBox);
                    ArrayList<String> subCats = new ArrayList<>();
                    for (int i = 0; i < numSubCats; i++) {
                        if (isSubCatSet) {
                            if (subCatCheckBox.get(i).isSelected()) {
                                subCats.add("'" + subcategory_enum.values()[i].getValue() + "'");
                            }
                        } else {
                            subCats.add("'" + subcategory_enum.values()[i].getValue() + "'"); // Add all
                        }
                    }
                    searchQuery[3] = "(" + String.join(",", subCats) + ")";
                    System.out.println("sub categories - " + searchQuery[3]);

                    // Set Storage
                    boolean isStorageSet = isSet(storageCheckBox);
                    ArrayList<String> storages = new ArrayList<>();
                    for (int i = 0; i < storage.length; i++) {
                        if (isStorageSet) {
                            if (storageCheckBox.get(i).isSelected()) {
                                storages.add("'" + storage[i] + "'");
                            }
                        } else {
                            storages.add("'" + storage[i] + "'"); // Add all
                        }
                    }
                    searchQuery[4] = "(" + String.join("," , storages) + ")";

                    DBCalls sitecount = new DBCalls(sqlCall_enum.getSitecount, null);
                    TableData number = sitecount.executeReadCall();
                    Object[][] dat = new Object[1][1];
                    dat = number.getData();
                    System.out.println("XXXXX:" + dat[0][0]);
                    searchQuery[5] = "(";
                    for (int i=1; i < Integer.parseInt(dat[0][0].toString()); i++) {
                        searchQuery[5] += Integer.toString(i);
                        searchQuery[5] += ",";
                    }
                    searchQuery[5] += dat[0][0].toString() + ")";

                    boolean isSiteset = isSet(siteCheckBox);
                    ArrayList<String> siteCheck = new ArrayList<>();
                    for (int i = 0; i < Integer.parseInt(dat[0][0].toString()); i++) {
                        if (isSiteset) {
                            if (siteCheckBox.get(i).isSelected()) {
                                siteCheck.add("'" + Integer.toString(i+1) + "'");
                            }
                        } else {
                            siteCheck.add("'" + Integer.toString(i+1) + "'"); // Add all
                        }
                    }
                    searchQuery[6] = "(" + String.join(",", siteCheck) + ")";
                    System.out.println("sites - " + searchQuery[5]);
                    // Set name
                    searchQuery[5] = searchBar.getText();



                    // Make Sql Call
                    DBCalls itemSearch = new DBCalls(sqlCall_enum.getItemsSearch, searchQuery);
                    TableData searchResults = itemSearch.executeReadCall();
                    displayResults(searchResults);
                }
            }
        });
    }

    private void displayResults(TableData searchResults) {
        if (searchResults != null) {
            DefaultTableModel model = new DefaultTableModel(searchResults.getData(), searchResults.getColumnNames());
            results = new JTable(model);
            TableUtils.addButtonCol(results, "Request/Edit", "Add Requests");
            ActionListener requestButtonAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = table.getEditingRow();
                    Object[][] dat = searchResults.getData();
                    DBCalls dbCalls_sub = new DBCalls(sqlCall_enum.getActiveStates, new String[] {
                            user.getSiteId(), user.getSiteId(), user.getSiteId()});
                    TableData tb = dbCalls_sub.executeReadCall();
                    JTable active_table = new JTable(tb.getData(), tb.getColumnNames());

                    if (user.getSiteId().equals(dat[row][7].toString())) {
                        new EditInventoryDialog(table, dat[row][0].toString(), dat[row][1].toString(),
                                dat[row][5].toString(), row);
                    } else {
                        if (dat[row][2].toString().equals("supplies") &&
                                active_table.getValueAt(0,0).toString().equals("inactive")) {
                            JOptionPane.showMessageDialog(null, "Cannot request supplies as " +
                                            "we do not have a shelter active!", "Item Search",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (dat[row][2].toString().equals("food") &&
                                active_table.getValueAt(0, 1).toString().equals("inactive") &&
                                active_table.getValueAt(0, 2).toString().equals("inactive")) {
                            JOptionPane.showMessageDialog(null, "Cannot request food as " +
                                            "we do not have a soupkitchen or pantry active!", "Item Search",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        ItemRequestDialog reqDialog = new ItemRequestDialog(user, dat[row]);
                        reqDialog.setLocationRelativeTo(table);
                        reqDialog.setVisible(true);
                    }
                }
            };
            results.getColumn("Add Requests").setCellRenderer(new ButtonEditorAndRenderer(requestButtonAL));
            results.getColumn("Add Requests").setCellEditor(new ButtonEditorAndRenderer(requestButtonAL));
            setSorting();
        } else {
            results = new JTable(4, 10);
            JOptionPane.showMessageDialog(null, "No Results Found!", "Item Search",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        layoutComponents();
    }
    private void setSorting() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(results.getModel());
        results.setRowSorter(sorter);
        sorter.setComparator(0, new TableIntComparator());
        sorter.setComparator(5, new TableIntComparator());
        sorter.setComparator(7, new TableIntComparator());
    }
    private boolean isSet(ArrayList<JCheckBox> e) {
        boolean notSet = false;
        for (JCheckBox j : e) {
            if (j.isSelected()) {
                return true;
            }
        }
        return notSet;
    }

    private boolean isDateSet() {
         return !date.getText().trim().equals("");
    }
    private boolean showexpired() {
        return showExpired.isSelected();
    }
    private boolean isSearchQuerySet() {
        // return !(neverClickedOn || searchBar.getText().trim().equals(""));
          return true;
    }

    private void configureComponents() {
        this.searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (neverClickedOn) {
                    searchBar.setText("");
                    neverClickedOn = false;
                }
            }
           });

        // Select one check box in each menu by default
        //catCheckBox.get(0).setSelected(true);
        //subCatCheckBox.get(0).setSelected(true);
        //storageCheckBox.get(0).setSelected(true);

        // Set default expiry date
        date.setText("9999-01-01");
    }

    private void populateMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBorderPainted(true);
        menuBar.setMinimumSize(new Dimension(500, 15));
        JMenu catMenu = new JMenu("Categories");
        JMenu subCatMenu = new JMenu("Sub Categories");
        JMenu storageMenu = new JMenu("Storage Type");
        JMenu sitesMenu = new JMenu("Sites");

        for (String a : categories) {
            JCheckBox tempCheckBox = new JCheckBox(toTitleCase(a));
            catCheckBox.add(tempCheckBox);
            catMenu.add(tempCheckBox);
        }

        for (String a : storage) {
            JCheckBox tempCheckBox = new JCheckBox(toTitleCase(a));
            storageCheckBox.add(tempCheckBox);
            storageMenu.add(tempCheckBox);
        }

        for (int i = 0; i < numSubCats; i++) {
            JCheckBox tempCheckBox = new JCheckBox(toTitleCase(subcategory_enum.values()[i].toString()));
            subCatCheckBox.add(tempCheckBox);
            subCatMenu.add(tempCheckBox);
        }
        DBCalls sitecount = new DBCalls(sqlCall_enum.getSitesInfo, null);
        TableData number = sitecount.executeReadCall();
        JTable sitestable =  new JTable(number.getData(), number.getColumnNames());
        int numSites = sitestable.getRowCount();
        for (int i = 0; i < numSites; i++) {
            JCheckBox tempCheckBox = new JCheckBox(sitestable.getValueAt(i, 0).toString() + "( " +
                                                   sitestable.getValueAt(i, 1).toString() + " )");
            siteCheckBox.add(tempCheckBox);
            sitesMenu.add(tempCheckBox);
        }
        menuBar.add(sitesMenu);
        menuBar.add(catMenu);
        menuBar.add(subCatMenu);
        menuBar.add(storageMenu);
    }

    private static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
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

    private void layoutComponents(){
        setSize(new Dimension(600, 50));
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // Remove all old components
        removeAll();

        // First Row
        gc.gridy = 0;
        gc.gridx = 0;
        gc.weighty = 0.1;
        gc.gridwidth = 4;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.BOTH;
        add(menuBar, gc);

        // Second Row
        gc.gridy++;
        gc.weighty = 0.1;
        gc.weightx = 0.3;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(0, 0, 0, 10);
        add(searchBar, gc);

        gc.gridx++;
        gc.weightx = 0.1;
        gc.anchor = GridBagConstraints.LINE_START;
        add(showExpired, gc);

        gc.gridx += 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 0.1;
        gc.gridwidth = 1;
        add(expiry, gc);


        gc.gridx++;
        gc.weightx = 0.1;
        gc.anchor = GridBagConstraints.LINE_START;
        add(date, gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_END;
        add(searchButton, gc);

        // Third Row
        gc.weighty = 2.0;
        gc.weightx = 2.0;
        gc.gridwidth = 5;
        gc.gridx = 0;
        gc.gridy += 2;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(results), gc);
    }

}
