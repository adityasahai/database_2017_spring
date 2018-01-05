package com.databasefrontend;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chandrika on 15/04/17.
 */
public class ServiceEditDialog extends JDialog {

    private JLabel description;
    private JLabel conditions;
    private JLabel starttime;
    private JLabel endtime;
    private JLabel maleBunks;
    private JLabel femaleBunks;
    private JLabel mixedBunks;
    private JLabel roomsAvailable;
    private JLabel seatsAvailable;


    private JTextField descriptionEd;
    private JTextField conditionsEd;
    private JTextField starttimeEd;
    private JTextField endtimeEd;
    private JTextField maleBunksEd;
    private JTextField femaleBunksEd;
    private JTextField mixedBunksEd;
    private JTextField seatsAvailableEd;
    private JTextField roomsAvailableEd;
    Boolean desNull = false;
    Boolean condNull = false;
    Boolean startNull = false;
    Boolean endNull = false;
    Boolean mbunksnull = false;
    Boolean fbunksnull = false;
    Boolean mixbunksnull = false;
    Boolean seatsnull = false;
    Boolean roomsnull = false;
    private JButton save, cancel;

    public ServiceEditDialog(JTable parent,JTable table, int row , String siteid) {
        super();
        setModalityType(ModalityType.APPLICATION_MODAL);
        this.description = new JLabel("Description: ");
        this.conditions = new JLabel("Conditions: ");
        this.starttime = new JLabel("Start Time: ");
        this.endtime = new JLabel("End Time: ");
        this.maleBunks = new JLabel("Male bunks: ");
        this.femaleBunks = new JLabel("Female Bunks: ");
        this.mixedBunks = new JLabel("Mixed Bunks: ");
        this.roomsAvailable = new JLabel("Rooms Available: ");
        this.seatsAvailable = new JLabel("Seats Available: ");



        if(table.getValueAt(0, 2) == null){
            this.descriptionEd = new JTextField(5);
            desNull = true;
        }else{
            this.descriptionEd = new JTextField(table.getValueAt(0, 2).toString());
        }
        if(table.getValueAt(0, 3) == null){
            this.conditionsEd = new JTextField(5);
            condNull = true;
        }else{
            this.conditionsEd = new JTextField(table.getValueAt(0, 3).toString());
        }
        if(table.getValueAt(0, 4) == null){
            this.starttimeEd = new JTextField(5);
            startNull = true;
        }else{
            this.starttimeEd = new JTextField(table.getValueAt(0, 4).toString());
        }
        if(table.getValueAt(0, 5) == null){
            this.endtimeEd = new JTextField(5);
            endNull = true;
        }else{
            this.endtimeEd = new JTextField(table.getValueAt(0, 5).toString());
        }

        if(row == 2){
            if(table.getValueAt(0, 6) == null){
                this.maleBunksEd = new JTextField(5);
                mbunksnull = true;
            }else{
                this.maleBunksEd = new JTextField(table.getValueAt(0, 6).toString());
            }
            if(table.getValueAt(0, 7) == null){
              this.femaleBunksEd = new JTextField(5);
              fbunksnull = true;
            }else{
                this.femaleBunksEd = new JTextField(table.getValueAt(0, 7).toString());
            }
            if(table.getValueAt(0, 8) == null){
                this.mixedBunksEd = new JTextField(5);
                mixbunksnull = true;
            }else{
                this.mixedBunksEd = new JTextField(table.getValueAt(0, 8).toString());
            }
            if(table.getValueAt(0, 9) == null){
                this.roomsAvailableEd = new JTextField(5);
                roomsnull = true;
            }else{
                this.roomsAvailableEd = new JTextField(table.getValueAt(0, 9).toString());
            }
        }
        if(row == 1){
            if(table.getValueAt(0, 6) == null){
              this.seatsAvailableEd = new JTextField(5);
              seatsnull = true;
            }else{
                this.seatsAvailableEd = new JTextField(table.getValueAt(0, 6).toString());
            }
        }


        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str_des = descriptionEd.getText();
                String str_cond = conditionsEd.getText();
                String str_starttime = starttimeEd.getText();
                String str_endtime = endtimeEd.getText();

                boolean something_changed = false;
                String str_male = "";
                String str_female = "";
                String str_mixed = "";
                String str_rooms = "";
                String str_seats = "";
                String log = "";
                if(row == 2){
                    str_male = maleBunksEd.getText();
                    str_female = femaleBunksEd.getText();
                    str_mixed = mixedBunksEd.getText();
                    str_rooms = roomsAvailableEd.getText();
                    if(mbunksnull== true && !str_male.equals("")){
                        something_changed = true;
                    }else{
                        if (!table.getValueAt(0, 6).toString().equals(str_male)) {
                            log += " Male bunks changed from " + table.getValueAt(0, 6).toString() + " to " + str_male;
                            something_changed = true;
                        }
                    }
                    if(fbunksnull == true && !str_female.equals("")){
                        something_changed = true;
                    }else{
                        if (!table.getValueAt(0, 7).toString().equals(str_female)) {
                            log += " Female bunks changed from " + table.getValueAt(0, 7).toString() + " to " + str_female;
                            something_changed = true;
                        }
                    }
                    if(mixbunksnull == true && !str_mixed.equals("")){
                        something_changed = true;
                    }else{
                        if (!table.getValueAt(0, 8).toString().equals(str_mixed)) {
                            log += " Mixed bunks changed from " + table.getValueAt(0, 8).toString() + " to " + str_mixed;
                            something_changed = true;
                        }
                    }
                    if(roomsnull == true && !str_rooms.equals("")){
                        something_changed = true;
                    }else{
                        if (!table.getValueAt(0, 9).toString().equals(str_rooms)) {
                            log += " Rooms Available changed from " + table.getValueAt(0, 9).toString() + " to " + str_rooms;
                            something_changed = true;
                        }
                    }

                }
                if(row == 1){
                    str_seats = seatsAvailableEd.getText();
                    if(seatsnull == true && !str_seats.equals("")){
                        something_changed = true;
                    }else{
                        if (!table.getValueAt(0, 6).toString().equals(str_seats)) {
                            log += " Seats Available changed from " + table.getValueAt(0, 6).toString() + " to " + str_seats;
                            something_changed = true;
                        }
                    }

                }

                if(desNull == true && !str_des.equals("")){
                    something_changed = true;
                }else{
                    if (!table.getValueAt(0, 2).toString().equals(str_des)) {
                        log += " Description changed from " + table.getValueAt(0, 2).toString() + " to " + str_des;
                        something_changed = true;
                    }
                }

                if(condNull == true && !str_cond.equals("")){
                    something_changed = true;
                }else{
                    if (!table.getValueAt(0, 3).toString().equals(str_cond)) {
                        log += " Conditions changed from " + table.getValueAt(0, 3).toString() + " to " + str_cond;
                        something_changed = true;
                    }
                }

                if(startNull == true && !str_starttime.equals("")){
                    something_changed = true;
                }if(str_starttime.equals("")){
                    str_starttime = "00:00";
                }
                else{
                    if (startNull == false && !table.getValueAt(0, 4).toString().equals(str_starttime)) {
                        log += " starttime changed from " + table.getValueAt(0, 4).toString() + " to " + str_starttime;
                        something_changed = true;
                    }
                }

                if(endNull == true && !str_endtime.equals("")){
                    something_changed = true;
                }if(str_endtime.equals("")){
                    str_endtime = "00:00";
                }
                else{
                    if (endNull == false && !table.getValueAt(0, 5).toString().equals(str_endtime)) {
                        log += " EndTime changed from " + table.getValueAt(0, 5).toString() + " to " + str_endtime;
                        something_changed = true;
                    }
                }



                DBCalls dbcalls = null;
                if(row == 1){
                    dbcalls = new DBCalls(sqlCall_enum.updateservicesoupkitchen, new String[]{
                            str_des,str_cond, str_starttime , str_endtime, str_seats,siteid
                    });
                }
                if(row == 2){
                    dbcalls = new DBCalls(sqlCall_enum.updateserviceshelter, new String[]{
                            str_des,str_cond, str_starttime , str_endtime, str_male,str_female,str_mixed,str_rooms,siteid
                    });
                }
                if(row == 3){
                    dbcalls = new DBCalls(sqlCall_enum.updateservicefoodpantry, new String[]{
                            str_des,str_cond, str_starttime , str_endtime,siteid});
                }

                if (something_changed && dbcalls.executeWriteCall() == 0) {
                    JOptionPane.showMessageDialog(parent, "Update Successful!", "Update",
                            JOptionPane.INFORMATION_MESSAGE);

                }
                else if (something_changed)  {
                    JOptionPane.showMessageDialog(parent, "Update Failed!", "Update",
                            JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });

        // Layout Componenets
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.weighty = 0.1;
        gc.insets = new Insets(0, 5, 0, 0);
        add(this.description, gc);

        gc.gridx++;
        this.descriptionEd.setPreferredSize(new Dimension(180,20));
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.descriptionEd, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.insets = new Insets(0, 5, 0, 0);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.conditions, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        this.conditionsEd.setPreferredSize(new Dimension(180,20));
        add(this.conditionsEd, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        this.starttimeEd.setPreferredSize(new Dimension(80,20));
        gc.insets = new Insets(0, 5, 0, 0);
        add(this.starttime, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.starttimeEd, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.insets = new Insets(0, 5, 0, 0);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.endtime, gc);

        gc.gridx++;
        this.endtimeEd.setPreferredSize(new Dimension(80,20));
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.endtimeEd, gc);

        gc.gridx = 0;
        gc.gridy++;
        if(row == 1){
            gc.insets = new Insets(0, 5, 0, 0);
            gc.anchor = GridBagConstraints.LINE_END;
            add(this.seatsAvailable, gc);

            gc.gridx++;
            this.seatsAvailableEd.setPreferredSize(new Dimension(50,20));
            gc.anchor = GridBagConstraints.LINE_START;
            add(this.seatsAvailableEd, gc);

            gc.gridx = 0;
            gc.gridy++;
        }
        if(row == 2){
            gc.weighty = 0.1;
            gc.weightx = 0.5;
            gc.insets = new Insets(0, 5, 0, 0);
            gc.anchor = GridBagConstraints.LINE_END;
            add(this.maleBunks, gc);

            gc.gridx++;
            this.maleBunksEd.setPreferredSize(new Dimension(50,20));
            gc.anchor = GridBagConstraints.LINE_START;
            add(this.maleBunksEd, gc);

            gc.gridx = 0;
            gc.gridy++;
            gc.insets = new Insets(0, 5, 0, 0);
            gc.anchor = GridBagConstraints.LINE_END;
            add(this.femaleBunks, gc);

            gc.gridx++;
            this.femaleBunksEd.setPreferredSize(new Dimension(50,20));
            gc.anchor = GridBagConstraints.LINE_START;
            add(this.femaleBunksEd, gc);

            gc.gridx = 0;
            gc.gridy++;
            gc.insets = new Insets(0, 5, 0, 0);
            gc.anchor = GridBagConstraints.LINE_END;
            add(this.mixedBunks, gc);

            gc.gridx ++;
            this.mixedBunksEd.setPreferredSize(new Dimension(50,20));
            gc.anchor = GridBagConstraints.LINE_START;
            add(this.mixedBunksEd, gc);

            gc.gridx = 0;
            gc.gridy++;
            gc.insets = new Insets(0, 5, 0, 0);
            gc.anchor = GridBagConstraints.LINE_END;
            add(this.roomsAvailable, gc);

            gc.gridx ++;
            this.roomsAvailableEd.setPreferredSize(new Dimension(50,20));
            gc.anchor = GridBagConstraints.LINE_START;
            add(this.roomsAvailableEd, gc);

            gc.gridx = 0;
            gc.gridy++;
        }

        gc.weighty = 0.8;
        gc.insets = new Insets(0, 0, 0, 10);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        add(save, gc);

        gc.insets = new Insets(0, 10, 0, 0);
        gc.gridx++;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(cancel, gc);


        setTitle("Edit Service ");
        setSize(new Dimension(400, 400));
        setResizable(false);
        setLocationRelativeTo(parent);

        setVisible(true);
    }

}
