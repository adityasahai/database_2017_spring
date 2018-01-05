package com.databasefrontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 25/03/17.
 */
public class AddItemPanel extends JPanel {
    private final String[] categories = {"food", "supplies"};
    private final String[] storage = {"drygood", "refrigerated", "frozen"};
    private final int numSubCats = 10;

    private UserClass user;
    private JLabel nameLabel, categoryLabel, subcategoryLabel, storageTypeLabel, numUnitsLabel, expiryDateLabel;
    private JTextField nameText, numUnitsText, expiryText;
    private JComboBox categoryList, subCatList, storageTypeList;
    private JButton addBtn;
    private JButton clearBtn;

    public AddItemPanel(UserClass user) {
        this.user = user;
        this.nameLabel = new JLabel(" Enter Item Name here");
        this.categoryLabel = new JLabel("Choose Category");
        this.subcategoryLabel = new JLabel("Choose SubCategory");
        this.storageTypeLabel = new JLabel("Choose Storage Type");
        this.numUnitsLabel = new JLabel("No of Units");
        this.expiryDateLabel = new JLabel("Expiry Date(YYYY-MM-DD)");

        this.categoryList = new JComboBox();
        this.subCatList = new JComboBox();
        this.storageTypeList = new JComboBox();

        this.nameText = new JTextField(10);
        this.numUnitsText = new JTextField(3);
        this.expiryText = new JTextField(8);

        this.addBtn = new JButton("Add");
        this.clearBtn = new JButton("Clear");

        // Populate Combo Boxes
        // Category
        for (String s : categories) {
            this.categoryList.addItem(toTitleCase(s));
        }

        // SubCategory
        for (int i = 0; i < numSubCats; i++) {
            this.subCatList.addItem(toTitleCase(subcategory_enum.values()[i].toString()));
        }

        // Storage type
        for (String s : storage) {
            this.storageTypeList.addItem(toTitleCase(s));
        }

        setButtons();

        clear();

        layoutComponents();
    }

    private void setButtons() {
        this.clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        this.addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBCalls dbCalls;
                try {
                    dbCalls = new DBCalls(sqlCall_enum.checkifitemexists, new String[] {nameText.getText(), subcategory_enum.values()[subCatList.getSelectedIndex()].toString(),
                            storage[storageTypeList.getSelectedIndex()], expiryText.getText(), user.getSiteId()});

                    TableData td = dbCalls.executeReadCall();
                    if (td == null) {
                        throw new ItemNotFoundException("Item not found");
                    }
                    dbCalls = new DBCalls(sqlCall_enum.addItemtryupdate, new String[]{
                            numUnitsText.getText(), nameText.getText(), subcategory_enum.values()[subCatList.getSelectedIndex()].toString(),
                            storage[storageTypeList.getSelectedIndex()], expiryText.getText(), user.getSiteId()
                    });
                    System.out.println("coming here");
                    if (dbCalls.executeWriteCall() == 0) {
                        JOptionPane.showMessageDialog(null, "Item Added Successfully", "Add Item",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Item Addition failed", "Add Item",
                                JOptionPane.ERROR_MESSAGE);
                    }


                } catch (ItemNotFoundException f) {
                    dbCalls = new DBCalls(sqlCall_enum.addItem, new String[]{nameText.getText(),
                            categories[categoryList.getSelectedIndex()],
                            subcategory_enum.values()[subCatList.getSelectedIndex()].toString(),
                            storage[storageTypeList.getSelectedIndex()], numUnitsText.getText(), expiryText.getText(),
                            user.getSiteId()});
                    if (dbCalls.executeWriteCall() == 0) {
                        JOptionPane.showMessageDialog(null, "Item Added Successfully", "Add Item",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Item Addition failed", "Add Item",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void clear() {
        this.nameText.setText("");
        this.numUnitsText.setText("");
        this.expiryText.setText("");
        this.categoryList.setSelectedIndex(0);
        this.subCatList.setSelectedIndex(0);
        this.storageTypeList.setSelectedIndex(0);
        this.expiryText.setText("9999-01-01");
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();


        //////////// First row ///////////////////////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        add(this.nameLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.nameText, gc);

        ////////////Second row ///////////////////////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridy = 1;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.categoryLabel, gc);

        gc.gridy = 1;
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.categoryList, gc);

        ////////////Third row ///////////////////////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridy = 2;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.subcategoryLabel, gc);

        gc.gridy = 2;
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.subCatList, gc);

        ////////////Fourth row ///////////////////////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridy = 3;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.storageTypeLabel, gc);

        gc.gridy = 3;
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.storageTypeList, gc);

        ////////////Fifth row ///////////////////////////////////
        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridy = 4;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.numUnitsLabel, gc);

        gc.gridy = 4;
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.numUnitsText, gc);

        ////////////Sixth row ///////////////////////////////////
        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridy = 5;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.expiryDateLabel, gc);

        gc.gridy = 5;
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.expiryText, gc);

        ////////////Seventh row ///////////////////////////////////
        gc.weighty = 2.0;

        gc.gridy = 6;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(10, 0, 0, 10);
        add(this.addBtn, gc);

        gc.gridx++;
        gc.insets = new Insets(10, 10, 0, 0);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(this.clearBtn, gc);

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
}
