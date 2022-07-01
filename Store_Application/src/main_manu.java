








///


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


    }     
    
    
