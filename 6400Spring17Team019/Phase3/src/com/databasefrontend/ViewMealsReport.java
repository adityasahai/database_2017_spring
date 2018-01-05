package com.databasefrontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by adityasahai on 17/03/17.
 */
public class ViewMealsReport extends JDialog{
    private JPanel mainPanel;
    private JTable valueTable;
    private boolean isTableNull;

    public ViewMealsReport(JFrame frame) {
        super(frame, "Meals Report", true);

        // Values
        valueTable = getTable();
        if (valueTable != null) {
            this.setResizable(false);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setLayout(new BorderLayout());

            // Panel
            valueTable.setEnabled(false);
            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(new JScrollPane(valueTable), BorderLayout.CENTER);
            setSize(new Dimension(600, 120));
            add(mainPanel, BorderLayout.CENTER);
        }
        else {
            setTableNull(true);
            setVisible(false);
        }
    }

    public void setTableNull(boolean tableNull) {
        isTableNull = tableNull;
    }

    public boolean isTableNull() {
        return isTableNull;
    }

    private JTable getTable() {
        DBCalls dbcalls = new DBCalls(sqlCall_enum.viewMeals2);

        TableData mp = dbcalls.executeReadCall();
        JTable mealstable =  new JTable(mp.getData(), mp.getColumnNames());
        int veggies = 0, protiens = 0, nuts = 0;
        if (mp != null) {
            for (int i=0; i < mealstable.getRowCount(); i++ ) {
                switch (mealstable.getValueAt(i, 1).toString()) {
                    case "vegetables":
                        veggies = Integer.parseInt(mealstable.getValueAt(i,0 ).toString());
                        break;
                    case "nuts/grains/beans":
                        nuts = Integer.parseInt(mealstable.getValueAt(i,0 ).toString());
                        break;
                    case "meat/seafood":
                    case "dairy/eggs":
                        protiens += Integer.parseInt(mealstable.getValueAt(i,0 ).toString());
                        break;
                    default:
                        System.out.println("Exception");
                }

            }
            String str = "";
            int min;
            min = (veggies<protiens) ? ((veggies<nuts)?veggies:nuts):((protiens<nuts)?protiens:nuts);
            boolean added_one = false;
            if (min == veggies) {
                str += "Vegetables";
                added_one = true;
            }
            if (min == protiens) {
                if (added_one) {
                    str += ", ";
                }
                str += " Meat/Seafood or Dairy/Eggs";
                added_one = true;
            }
            if (min == nuts) {
                if (added_one) {
                    str += ", ";
                }
                str += "Nuts/Grains/Beans";
            }

            DefaultTableModel model = new DefaultTableModel();
            JTable table = new JTable(model);


            model.addColumn("Max available meals");
            model.addColumn("SubCategories needed");
            model.addRow(new Object[]{Integer.toString(min), str});
            return table;

        }
        else {
            return null;
        }
    }
}
