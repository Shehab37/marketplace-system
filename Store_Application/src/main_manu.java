/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package my_package;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.plugins.tiff.ExifTIFFTagSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import static my_package.Connect.*;
import static my_package.Login.user;

/**
 * author@Shady
 */
public class main_menu extends javax.swing.JFrame {

    /**
     * **** GUI attributes *****
     */
    public boolean transfer = false;
    JPanel prev;
    JPanel current;

    // this variable is used to get the id of the selected product in table pro_table
    String target_name = null, target_name2 = null;

//    int pro_id = 0;
//    String pro_name = "";
//    int pro_amount = 0;
//    int pro_price = 0;
    private void add_item() {
        try {
            create_socket();
            serverOutputStream.writeUTF("add_item");

            serverOutputStream.writeInt(Integer.parseInt(id.getText()));
            serverOutputStream.writeUTF(name.getText());
            serverOutputStream.writeInt(Integer.parseInt(price.getText()));
            serverOutputStream.writeInt(Integer.parseInt(amount.getText()));
            serverOutputStream.writeUTF(cat.getText());

            int res = clientReadSource.readInt();
            if (res != 1) {
                JOptionPane.showMessageDialog(null, "adding failed,check data and try again");
            }

            client_socket.close();
            display_admin_store();
        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void update_item() {
        try {
            create_socket();
            serverOutputStream.writeUTF("update_item");

            serverOutputStream.writeInt(Integer.parseInt(id.getText()));
            serverOutputStream.writeUTF(name.getText());
            serverOutputStream.writeInt(Integer.parseInt(price.getText()));
            serverOutputStream.writeInt(Integer.parseInt(amount.getText()));
            serverOutputStream.writeUTF(cat.getText());
            int res = clientReadSource.readInt();
            if (res != 1) {
                JOptionPane.showMessageDialog(null, "updating failed,check data and try again");
            }

            client_socket.close();
            display_admin_store();
        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void Store_buttonMouseExited(java.awt.event.MouseEvent evt) {                                         
        if (current != Store_button)
            Store_button.setBackground(new Color(64, 43, 100));
    } 
    private void Store_buttonMouseEntered(java.awt.event.MouseEvent evt) {                                          
        Store_button.setBackground(new Color(85, 65, 118));
    }
       private void my_cart_panelMouseEntered(java.awt.event.MouseEvent evt) {                                           
        my_cart_panel.setBackground(new Color(85, 65, 118));
    } 
    
    
private void display_history() {
     try {
            create_socket();

            DefaultTableModel model = (DefaultTableModel) table_history.getModel();
            model.setRowCount(0);
            String[] s = {"", "", "", ""};
            
            serverOutputStream.writeUTF("display_history");
            int count = clientReadSource.readInt();    
            while (count != 0) {

               s[0] = clientReadSource.readUTF();
               s[1] = clientReadSource.readUTF();
               s[2] = clientReadSource.readUTF();
               s[3] = clientReadSource.readUTF();
                count--;
                model.addRow(s);
            }
        client_socket.close();

    }
     catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void delete_item() {
        try {
            create_socket();
            serverOutputStream.writeUTF("delete_item");

            serverOutputStream.writeInt(Integer.parseInt(id.getText()));

            int res = clientReadSource.readInt();
            if (res != 1) {
                JOptionPane.showMessageDialog(null, "deletion failed,check data and try again");
            }

            client_socket.close();
            display_admin_store();
        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     private void my_cart_panelMouseExited(java.awt.event.MouseEvent evt) {                                          
        if (current != my_cart_panel)
            my_cart_panel.setBackground(new Color(64, 43, 100));
    } 
    
    
    
    private void my_cart_panelMouseClicked(java.awt.event.MouseEvent evt) {

        //gui stuff
        store.setVisible(false);
        cart.setVisible(true);
        store_admin.setVisible(false);
        deposit.setVisible(false);
        acc_info.setVisible(false);
        //adjust color
        adjustcolor(my_cart_panel);
        current = my_cart_panel;

        //database stuff
        if (check_admin() == false) {
            display_cart();
        } else {
            cart.setVisible(false);
            admin_view_history.setVisible(true);
            display_history();

        }

    }
 private void cat_barMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if ("Search By Category..".equals(cat_bar.getText()))
            cat_bar.setText("");
    }
    void display_cart() {
        total_price_label.setText(String.valueOf(get_total_price()) + "$");

        try {
            create_socket();

            DefaultTableModel model = (DefaultTableModel) pro_table3.getModel();
            model.setRowCount(0);
            String[] s = {"", "", "", ""};

            serverOutputStream.writeUTF("get_cart_products");
            serverOutputStream.writeUTF(Login.user);
            int count = clientReadSource.readInt();

            while (count != 0) {

                s[0] = clientReadSource.readUTF();
                s[1] = clientReadSource.readUTF();
                s[2] = clientReadSource.readUTF();
                s[3] = clientReadSource.readUTF();
                count--;
                model.addRow(s);
            }

            client_socket.close();

        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private boolean check_admin() {
        boolean flag = false;
        try {
            create_socket();
            serverOutputStream.writeUTF("check_admin");
            serverOutputStream.writeUTF(Login.user);
            int res = clientReadSource.readInt();

            if (res == 1) {
                flag = true;
            }
            client_socket.close();
        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            create_socket();

            serverOutputStream.writeUTF("remove_all_cart");
            serverOutputStream.writeUTF(Login.user);
            int res = clientReadSource.readInt();

            client_socket.close();
            display_cart();

        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }

//        String query = "DELETE FROM ORDERS WHERE FORGUSERNAME= '"+Login.user+"' " ;
//        Connection con = javaconnect.connectlogin();
//        javaconnect.connectlogin();
//        PreparedStatement pss;
//        try {
//            pss = con.prepareStatement(query);
//            pss.executeUpdate();
//            display_cart();

    }
            
            
///

 private void store_button() {
        // TODO add your handling code here:
        // TODO add your handling code here:
        //turn on Store button color only

        //check if it's an admin
        boolean flagg = check_admin();
//
        if (flagg) {
            store_admin.setVisible(true);
            store.setVisible(false);

        } else {
            store.setVisible(true);
            store_admin.setVisible(false);
        }

        cart.setVisible(false);
        deposit.setVisible(false);
        acc_info.setVisible(false);
        adjustcolor(Store_button);
        current = Store_button;
        admin_view_history.setVisible(false);
        display_store();

    }
    
 private void deposit_panelMouseClicked(java.awt.event.MouseEvent evt) {                                           
        // TODO add your handling code here:
        if(!check_admin()){
        store.setVisible(false);
        store_admin.setVisible(false);
        cart.setVisible(false);
        deposit.setVisible(true);
        acc_info.setVisible(false);
        //adjust color
        adjustcolor(deposit_panel);
        current = deposit_panel;
        admin_view_history.setVisible(false);
        
        
 
        String s_cash = "";
        try {
            create_socket();
            serverOutputStream.writeUTF("deposit_panel");
            serverOutputStream.writeUTF(Login.user);
            try {
                s_cash = String.valueOf(clientReadSource.readInt());
            } catch (IOException ex) {
                Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
            }

            show_price.setText(s_cash + " $");
            client_socket.close();
        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }          
    


String get_E_wallet() {
        String e_wallet = "-1";
        String query6 = "SELECT E_WALLET  FROM PERSON WHERE USERNAME='" + Login.user + "' ";
        Connection con = javaconnect.connectlogin();
        javaconnect.connectlogin();
        try {
            PreparedStatement pr = con.prepareStatement(query6);
            ResultSet rs = pr.executeQuery();

            if (rs.next()) {
                e_wallet = rs.getString(1);
                show_price.setText(rs.getString(1) + " $");

            }
            pr.close();

        } catch (SQLException ex) {
            Logger.getLogger(Create_account.class.getName()).log(Level.SEVERE, null, ex);

        }

        return e_wallet;
    }

    private void Proceed_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                               
// TODO add your handling code h

        int amount = Integer.parseInt(amount_dep.getText());
        int cvvNumber = Integer.parseInt(cvv.getText());
        System.out.println(amount + " " + cvvNumber);

        int bank_cash = 0;
        String trans = null;
        boolean trans_check = false;
        int e_cash = 0;
        try {
            create_socket(); 
            System.out.println("socket created");
            //returning ewallet

            serverOutputStream.writeUTF("deposit");
            serverOutputStream.writeUTF(Login.user);
            System.out.println(Login.user);
            e_cash = clientReadSource.readInt();
            System.out.println(e_cash);

            //// if check is selected 
            int flag_trans = 0;
            if (check.isSelected()) {
                flag_trans = 1;
            }

            serverOutputStream.writeInt(flag_trans);
            if (check.isSelected()) {
                trans = trans_acc.getText();
                serverOutputStream.writeUTF(trans);
                if (clientReadSource.readInt() <= 0) {
                    trans_check = true;
                    JOptionPane.showMessageDialog(null, "The username you are trying to transfer to doesn't exist");
                } else { // <<<<<<<<<<<<<<
                    trans = null;
                    trans_acc.setText(null);
                }

            }


            // check if it a valid account 
            int valid_account = 1;

            //if you want to transfer to an account 
            // first check that the current account exists and has valid amount
            // second query to update the requested account
            //get the cash amount in bank and use it in queries
//        String query4 = "SELECT CASH FROM BANKACCOUNT WHERE ForgUser='" + Login.user + "' and NUMBER=" + cvvNumber + "";
//            java.sql.Statement ps2 = con.createStatement();
//        ps=javaconnect.connectlogin().prepareStatement(query);
//            ResultSet rs = ps2.executeQuery(query4);
            if (clientReadSource.readInt() > 0) {
                bank_cash = clientReadSource.readInt();
            } else {
                valid_account = 0;
                JOptionPane.showMessageDialog(null, "Invalid CVV");
            }

            //execute queries 
            serverOutputStream.writeInt(valid_account);
            serverOutputStream.writeInt(amount);
            if (valid_account > 0) {
                if (trans != null && !trans_check) {
                    if (amount > e_cash) {
                        JOptionPane.showMessageDialog(null, "E_wallet not sufficient");
                    } else {
                        JOptionPane.showMessageDialog(null, "Amount Transfered Successfully");
                    }
                } else if (trans == null) {

                    if (amount <= bank_cash) {
                        JOptionPane.showMessageDialog(null, "Amount Deposited Successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient amount in bank");
                    }
                }

            }
            String s_cash = "";
            try {
                s_cash = String.valueOf(clientReadSource.readInt());
            } catch (IOException ex) {
                Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(s_cash);
            show_price.setText(s_cash + " $");
            client_socket.close();

        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }

//        show_price.setText(rs.getString(1) + " $");
        trans_acc.setText(null);// reset the field to null
        

    }
    
    public void adjustcolor(JPanel target) {
        target.setBackground(new Color(85, 65, 118));
        target.setForeground(Color.WHITE);
        if (prev != target) {
            prev.setBackground(new Color(64, 43, 100));
            prev.setForeground(new Color(64, 43, 100));
        }
        prev = target;
    }
    private int get_total_price() {
        int price = 2;
        try {
            create_socket();
            serverOutputStream.writeUTF("get_total_price");

            serverOutputStream.writeUTF(user);

            price = clientReadSource.readInt();
            client_socket.close();

        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return price;
    }
    String get_E_wallet() {
        String e_wallet = "-1";
        String query6 = "SELECT E_WALLET  FROM PERSON WHERE USERNAME='" + Login.user + "' ";
        Connection con = javaconnect.connectlogin();
        javaconnect.connectlogin();
        try {
            PreparedStatement pr = con.prepareStatement(query6);
            ResultSet rs = pr.executeQuery();

            if (rs.next()) {
                e_wallet = rs.getString(1);
                show_price.setText(rs.getString(1) + " $");

            }
            pr.close();

        } catch (SQLException ex) {
            Logger.getLogger(Create_account.class.getName()).log(Level.SEVERE, null, ex);

        }

        return e_wallet;
    }
    private void search_barMouseClicked(java.awt.event.MouseEvent evt) {                                        
        if ("Search Record..".equals(search_bar.getText()))
            search_bar.setText("");
    }
 private void my_cart_panelMouseClicked(java.awt.event.MouseEvent evt) {                                           

        //gui stuff
        store.setVisible(false);
        cart.setVisible(true);
        store_admin.setVisible(false);
        deposit.setVisible(false);
        acc_info.setVisible(false);
        //adjust color
        adjustcolor(my_cart_panel);
        current = my_cart_panel;

        //database stuff
        if (check_admin() == false) {
            display_cart();
        } else {
            cart.setVisible(false);
            admin_view_history.setVisible(true);
            display_history();

        }

    }      


    
     private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        //code to add to cart : 
        // target_name
        // Login.user()
        String s = amount_text.getText();
        int act_amount = Integer.parseInt(s);
        boolean valid = true;

        try {

            create_socket();

            serverOutputStream.writeUTF("get_amount");
            serverOutputStream.writeUTF(target_name);

            int temp = clientReadSource.readInt();

            if (act_amount <= 0 || temp < act_amount) {
                valid = false;
                JOptionPane.showMessageDialog(null, "Please enter a positive valid amount");
            }

            client_socket.close();

        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (valid) {
            try {
                create_socket();

                serverOutputStream.writeUTF("insert_to_cart");
                serverOutputStream.writeUTF(Login.user);
                serverOutputStream.writeUTF(target_name);
                serverOutputStream.writeInt(Integer.parseInt(amount_text.getText()));
                int res = clientReadSource.readInt();

                client_socket.close();
                if (res == 1) {
                    JOptionPane.showMessageDialog(null, "Item has been added to cart");
                } else {
                    JOptionPane.showMessageDialog(null, "error");
                }

            } catch (IOException ex) {
                Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        private void view_infoMouseClicked(java.awt.event.MouseEvent evt) {                                       
        /**
         * ******* GUI STUFF ******
         */
        store.setVisible(false);
        admin_view_history.setVisible(false);
        cart.setVisible(false);
        store_admin.setVisible(false);
        deposit.setVisible(false);
        acc_info.setVisible(true);
        if(check_admin()){
        client_history_table.setVisible(false);
        jScrollPane5.setVisible(false);
        jLabel44.setVisible(false);
        e_label.setVisible(false);
        e_text.setVisible(false);
        }
        //adjust color
        adjustcolor(view_info);
        current = view_info;

        /**
         * **** DATABASE STUFF ******
         */
        try {
            create_socket();
            serverOutputStream.writeUTF("view_account");
            serverOutputStream.writeUTF(Login.user);

            first_name.setText(clientReadSource.readUTF());
            last_name.setText(clientReadSource.readUTF());
            user_name.setText(clientReadSource.readUTF());
            email_address.setText(clientReadSource.readUTF());
            password.setText(clientReadSource.readUTF());
            e_text.setText(clientReadSource.readUTF());
            client_socket.close();
        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }

        // client history table --> client code
        if (!check_admin()) {
            try {
            create_socket();

            DefaultTableModel model = (DefaultTableModel) client_history_table.getModel();
            model.setRowCount(0);
            String[] s = {"", "", "",""};
            
            serverOutputStream.writeUTF("display_client_history");
            serverOutputStream.writeUTF(Login.user);
            int count = clientReadSource.readInt();    
            while (count != 0) {

               s[0] = clientReadSource.readUTF();
               s[1] = clientReadSource.readUTF();
               s[2] = clientReadSource.readUTF();
               s[3] = clientReadSource.readUTF();
                count--;
                model.addRow(s);
            }
        client_socket.close();

    }
     catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }

        }
private void pro_table2MouseClicked(java.awt.event.MouseEvent evt) {                                        
        int row = pro_table2.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) pro_table2.getModel();
        id.setText(model.getValueAt(row, 0).toString());
        name.setText(model.getValueAt(row, 1).toString());
        price.setText(model.getValueAt(row, 2).toString());
        amount.setText(model.getValueAt(row, 3).toString());
        cat.setText(model.getValueAt(row, 4).toString());
    }

    } 
         
         private void searchActionPerformed(java.awt.event.ActionEvent evt) {                                       

        try {
            create_socket();
            String look = search_bar.getText();
            String cat_look = cat_bar.getText();
            System.out.println(look + " " + cat_look);
            serverOutputStream.writeUTF("search_item");
            // setting up the flags
            if (look == " ") {
                System.out.println("treu");
                serverOutputStream.writeInt(0);
            } else {
                System.out.println("false");
                serverOutputStream.writeInt(1);
            }
            if (cat_look == " ") {
                System.out.println("treu");
                serverOutputStream.writeInt(0);
            } else {
                System.out.println("false");
                serverOutputStream.writeInt(1);
            }

            serverOutputStream.writeUTF(look);
            serverOutputStream.writeUTF(cat_look);

            DefaultTableModel model = (DefaultTableModel) pro_table.getModel();
            model.setRowCount(0);
            String[] s = {"", "", "", ""};

            int count = clientReadSource.readInt();

            while (count != 0) {

                s[0] = clientReadSource.readUTF();
                s[1] = clientReadSource.readUTF();
                s[2] = clientReadSource.readUTF();
                s[3] = clientReadSource.readUTF();
                count--;
                model.addRow(s);
            }
            client_socket.close();
        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }

    } 
          private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {                                      

        try {
            String s = amount_update.getText();
            int act_amount = Integer.parseInt(s);
            boolean valid = true;

            create_socket();

            serverOutputStream.writeUTF("get_amount");
            serverOutputStream.writeUTF(target_name2);

            int temp = clientReadSource.readInt();

            if (act_amount <= 0 || temp < act_amount) {
                valid = false;
                JOptionPane.showMessageDialog(null, "Please enter a positive valid amount");
            }

            client_socket.close();

            if (valid) {
                create_socket();

                serverOutputStream.writeUTF("change_amount");
                serverOutputStream.writeUTF(Login.user);
                serverOutputStream.writeUTF(target_name2);
                serverOutputStream.writeInt(Integer.parseInt(amount_update.getText()));
                int res = clientReadSource.readInt();

                client_socket.close();
                display_cart();
                if (res == 1) {
                    JOptionPane.showMessageDialog(null, "amount has been updated");
                }
            }
  private void Store_buttonMouseClicked(java.awt.event.MouseEvent evt) {                                          
        store_button();
    } 
//        String query = "UPDATE ORDERS SET SELECTED_AMOUNT=? WHERE FORGUSERNAME= '"+Login.user+"' and PRODUCT_NAME='"+target_name2+"'" ;
//        Connection con = javaconnect.connectlogin();
//        javaconnect.connectlogin();
//        PreparedStatement pss;
//        try {
//            pss = con.prepareStatement(query);
//            pss.setInt(1, temp2);
//            pss.executeUpdate();
//            display_cart();
//
//} catch (SQLException ex) {
//            Logger.getLogger(main_menu.class  
//
//.getName()).log(Level.SEVERE, null, ex);
//        }        
        } catch (IOException ex) {
            Logger.getLogger(main_menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    
