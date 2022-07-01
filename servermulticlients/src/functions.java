/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servermulticlients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 *
 * @author Shehab
 */
public class Functions {

//used to get the time of money transfers
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    
      public static void login(DataInputStream input, DataOutputStream output) throws SQLException, IOException, InterruptedException {

        String user_name = input.readUTF();
        String password = input.readUTF();

        String login = "SELECT USERNAME,PASSWORD FROM APP.PERSON WHERE username=? AND password=? ";
        PreparedStatement ps = ServerMultiClients.con.prepareStatement(login);
        ps.setString(1, user_name);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
        try (output) {
            if (rs.next()) {
                output.writeInt(1);
            } else {
                output.writeInt(-1);
            }

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            String methodName = new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName();
            System.out.println("client excuted function:  " + methodName);

        }
    }

      public static void check_admin(DataInputStream input, DataOutputStream output) throws SQLException, IOException {

        String user = input.readUTF();
        String query = "SELECT ISADMIN FROM PERSON WHERE USERNAME='" + user + "' ";
        PreparedStatement ps = ServerMultiClients.con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            if (rs.getString(1) == "true") {
                output.writeInt(1);
            }
        } else {
            output.writeInt(2);
        }
        System.out.println("adminity: " + "true".equals(rs.getString(1)));
        output.close();
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        System.out.println("client excuted function:  " + methodName);

    }
  
 public static synchronized void sign_up(DataInputStream input, DataOutputStream output) throws SQLException, IOException {
        int cvv = 0;
        String uname="";
        String query = "INSERT INTO PERSON (FNAME , LNAME , USERNAME , PASSWORD , EMAIL_ADDRESS,CVV_NUMBER) VALUES(?,?,?,?,?,?)";

        PreparedStatement ps = ServerMultiClients.con.prepareStatement(query);
        ps.setString(1, input.readUTF());
        ps.setString(2, input.readUTF());
        uname = input.readUTF();
        ps.setString(3,uname );
        ps.setString(4, input.readUTF());
        ps.setString(5, input.readUTF());
        cvv = input.readInt();
        ps.setInt(6,cvv );

        try {
            if (ps.executeUpdate() > 0) {
                output.writeInt(1);
                
                String query2 = "UPDATE  BANKACCOUNT SET FORGUSER='"+uname+"'  WHERE NUMBER =?  ";
             PreparedStatement ps2 = ServerMultiClients.con.prepareStatement(query2);
              ps2.setInt(1, cvv);
//           ps2.executeQuery();
           if(ps2.executeUpdate() >0 ){}
            
            ps2.close();
   
            
            } else {
                output.writeInt(-1);
            }
        } catch (SQLException ex) {
            output.writeInt(-1);

        }
        finally {
            output.close();
            String methodName = new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName();
            System.out.println("client excuted function:  " + methodName);
        }
        
             
             
    } 
  
  
    public static synchronized void check_out_order(DataInputStream input, DataOutputStream output) throws SQLException, IOException {
//        1-update main_orders table >>> user_n , date , total price 
//        2-update orders_details table >>> order_id  , p_name , amount

//        3-update products table
//        4-update persons table (decrease from e-wallet balance and if not show error)
        try {
        String user_name = input.readUTF();
        int order_price = input.readInt();

//        1:
        String query = "insert into main_orders(user_n , date_ , total_price) values(?,?,?)";
        PreparedStatement ps = ServerMultiClients.con.prepareStatement(query);
        ps.setString(1, user_name);
        ps.setString(2, now());
        ps.setInt(3, order_price);

        System.out.println(now());
//        try {
        if (ps.executeUpdate() > 0) {
            System.out.println("done");
        } else {
            System.out.println("err");
        }

        PreparedStatement ps_2 = ServerMultiClients.con.prepareStatement("select max(order_ID) from main_orders");

        int order_num = 0;
        ResultSet rs = ps_2.executeQuery();
        if (rs.next()) {
            order_num = rs.getInt(1);
        }
        //        System.out.println("order num = " + order_num);

        //        2:
        PreparedStatement ps_3 = ServerMultiClients.con.prepareStatement("insert into orders_details \n"
                + "select\n"
                + "? ,product_name,selected_amount \n"
                + "from app.CART \n"
                + "where forgusername = ?");
        ps_3.setInt(1, order_num);
        ps_3.setString(2, user_name);

        ps_3.executeUpdate();

//        3-update products table


       String cur_product = "";
       int cur_amount = 0;

        PreparedStatement cart_ps = ServerMultiClients.con.prepareStatement("select product_name ,selected_amount from cart where forgusername = ?");
        PreparedStatement products_ps =  ServerMultiClients.con.prepareStatement("update products set amount_in_stock  = amount_in_stock - ? where(product_name = ? )");

        cart_ps.setString(1, user_name);
        ResultSet cart_rs = cart_ps.executeQuery();

        while (cart_rs.next()) {
            cur_product = cart_rs.getString(1);
            cur_amount = cart_rs.getInt(2);
            
//            System.out.println(cur_amount);
//            System.out.println(cur_product);
            
            products_ps.setString(2, cur_product);
            products_ps.setInt(1, cur_amount);
            products_ps.executeUpdate();
        }



//                4:update persons table (decrease from e-wallet balance)
//                update person
       PreparedStatement update_person =  ServerMultiClients.con.prepareStatement("update person set e_wallet = e_wallet - ? where username = ? ");
        update_person.setInt(1, order_price);
        update_person.setString(2, user_name);
        if( update_person.executeUpdate() > 0){
            output.writeInt(1);
        }
                        
                } catch (SQLException ex) {
                       System.out.println("sqlerr");
        
                } finally {
                    output.close();
                    String methodName = new Object() {
                    }
                            .getClass()
                            .getEnclosingMethod()
                            .getName();
                    System.out.println("client excuted function:  " + methodName);
                }
        
        
        
    }
  public static synchronized void insert_to_cart(DataInputStream input, DataOutputStream output) throws SQLException, IOException {

        try {

            String user = input.readUTF();
            String product = input.readUTF();
            int amount = input.readInt();

//        check if there is user and product in CART table if then >> increase amount in table by amount
//else : excute query
            int count = -1;
            String query_1 = "SELECT COUNT(*) FROM CART WHERE FORGUSERNAME = ? AND PRODUCT_NAME = ? ";
            PreparedStatement ps = ServerMultiClients.con.prepareStatement(query_1);
            ps.setString(1, user);
            ps.setString(2, product);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                String query_3 = "UPDATE CART SET SELECTED_AMOUNT=SELECTED_AMOUNT +? WHERE FORGUSERNAME= '" + user + "' and PRODUCT_NAME='" + product + "'";
                PreparedStatement ps_3 = ServerMultiClients.con.prepareStatement(query_3);
                ps_3.setInt(1, amount);
                if (ps_3.executeUpdate() > 0) {
                    output.writeInt(1);
                } else {
                    output.writeInt(-1);
                }

            } else {
                String query_2 = "Insert into CART(FORGUSERNAME,PRODUCT_NAME,SELECTED_AMOUNT) values(?,?,?)";

                PreparedStatement ps_2 = ServerMultiClients.con.prepareStatement(query_2);
                ps_2.setString(1, user);
                ps_2.setString(2, product);
                ps_2.setInt(3, amount);

                if (ps_2.executeUpdate() > 0) {
                    output.writeInt(1);
                } else {
                    output.writeInt(-1);
                }
            }
        } catch (SQLException ex) {
            output.writeInt(-1);

        } finally {
            output.close();
            String methodName = new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName();
            System.out.println("client excuted function:  " + methodName);
        }

    }
     public static void validate_cvv(DataInputStream input, DataOutputStream output) throws SQLException, IOException {
        int cvv = 0;
        cvv = input.readInt();
//         System.out.println(cvv);
        String query = "SELECT COUNT(*) FROM BANKACCOUNT  WHERE NUMBER ="+cvv+"  ";
         PreparedStatement ps2 = ServerMultiClients.con.prepareStatement(query);
        ResultSet rs = ps2.executeQuery();
        rs.next();
 
        String query2 = "SELECT FORGUSER FROM BANKACCOUNT  WHERE NUMBER ="+cvv+"  ";
         PreparedStatement ps3 = ServerMultiClients.con.prepareStatement(query2);
        ResultSet rs3 = ps3.executeQuery();
        rs3.next();

         System.out.println(rs3.getString(1));
        if(rs.getInt(1) <= 0 || rs3.getString(1) != null ){
             output.writeInt(-1);
 
        }
        else{
             output.writeInt(1);

        }
        ps3.close();
         ps2.close();

         String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        System.out.println("client excuted function:  " + methodName);


    }
    
    public static synchronized void deposit(DataInputStream input, DataOutputStream output) throws SQLException, IOException {
//         String trans = "";
        try (output) {
            //         String trans = "";
            int e_cash = 0; //wallet 
            int bank_cash = 0;
            boolean trans_check = false;
            //returning ewallet
            String uname = input.readUTF();
            String query = "SELECT E_WALLET,CVV_NUMBER FROM PERSON WHERE USERNAME = '" + uname + "'";
            PreparedStatement ps = ServerMultiClients.con.prepareStatement(query);
//            

//            System.out.println(uname);
            System.out.println("7agaaa");
            ResultSet rs = ps.executeQuery();
            rs.next();
            e_cash = rs.getInt(1);
            System.out.println(rs.getInt(1));

            output.writeInt(e_cash);

            int cvv = rs.getInt(2); // granted

            // if check is selected
            int flag_trans = input.readInt();
            System.out.println(flag_trans);
            String trans = null;
            if (flag_trans >= 1) {
                trans = input.readUTF();

                String query2 = "SELECT COUNT(USERNAME) AS C FROM PERSON WHERE USERNAME='" + trans + "'";
                PreparedStatement ps2 = ServerMultiClients.con.prepareStatement(query2);

                ResultSet rs2 = ps2.executeQuery();
                rs2.next();
//                System.out.println("ss"+ rs2.getInt(1));
                if (rs2.getInt(1) > 0) {
                    output.writeInt(1);
                } else {
                    output.writeInt(0);
                    trans_check = true;
                }
            }
            // check if it a valid account
            String query3 = "SELECT CASH FROM BANKACCOUNT WHERE ForgUser='" + uname + "' and NUMBER=" + cvv + "";
            PreparedStatement ps3 = ServerMultiClients.con.prepareStatement(query3);
            ResultSet rs3 = ps3.executeQuery();

            if (rs3.next()) {
                output.writeInt(1);
                bank_cash = rs3.getInt(1);
                output.writeInt(bank_cash);
            } else {
                output.writeInt(0);
            }
            int valid_account = input.readInt();
            String query4 = null, query5 = null;
            PreparedStatement ps4;
            int amount = input.readInt();
            if (valid_account > 0) {
                if (trans != null && !trans_check) {
                    if (amount > e_cash) {

                    } else {
                        query4 = "UPDATE PERSON SET E_WALLET=E_WALLET+" + amount + " WHERE  USERNAME='" + trans + "' ";
                        query5 = "UPDATE PERSON SET E_WALLET= E_WALLET-" + amount + "WHERE USERNAME='" + uname + "'";
                    }
                } else if (trans == null) {
                    if (amount <= bank_cash) {
                        query4 = "UPDATE PERSON SET E_WALLET=E_WALLET+" + amount + " WHERE CVV_NUMBER=" + cvv + " and USERNAME='" + uname + "' ";
                        query5 = "UPDATE BANKACCOUNT SET CASH= CASH-" + amount + "WHERE NUMBER=" + cvv + "";
                    } else {

                    }

                }
            }
            if (query4 != null) {
                ps4 = javaconnect.connectlogin().prepareStatement(query4);
                ps4.executeUpdate();
                ps4.close();
            }
            if (query5 != null) {
                ps4 = javaconnect.connectlogin().prepareStatement(query5);
                ps4.executeUpdate();
                ps4.close();
            }
            String query6 = "SELECT E_WALLET,CVV_NUMBER FROM PERSON WHERE USERNAME = '" + uname + "'";
            PreparedStatement ps6 = ServerMultiClients.con.prepareStatement(query6);
            ResultSet rs6 = ps6.executeQuery();
            rs6.next();
            e_cash = rs6.getInt(1);
            output.writeInt(e_cash);
        } //wallet
        finally {
            String methodName = new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName();
            System.out.println("client excuted function:  " + methodName);

        }

    }
    
     public static synchronized void deposit_panel(DataInputStream input, DataOutputStream output) throws SQLException, IOException {
        String uname = input.readUTF();
        String query6 = "SELECT E_WALLET,CVV_NUMBER FROM PERSON WHERE USERNAME = '" + uname + "'";
        PreparedStatement ps6 = ServerMultiClients.con.prepareStatement(query6);
        ResultSet rs6 = ps6.executeQuery();
        rs6.next();
        int e_cash = rs6.getInt(1);
        output.writeInt(e_cash);
    }
    public static void display_history (DataInputStream input, DataOutputStream output) throws SQLException, IOException {

     String query = "SELECT ORDER_ID,USERN,DATE,TOTAL_PRICE FROM MAIN_ORDERS";

     String count_query = "SELECT count(*) as record_count FROM APP.MAIN_ORDERS";
        PreparedStatement count_ps = ServerMultiClients.con.prepareStatement(count_query);
        ResultSet count_rs = count_ps.executeQuery();
        count_rs.next();
        int c = count_rs.getInt("record_count");
        output.writeInt(c);


     PreparedStatement ps4 = null;
     ps4 = ServerMultiClients.con.prepareStatement(query);
     ResultSet rs = ps4.executeQuery();

     while (rs.next()) {
            output.writeUTF(rs.getString(1));
            output.writeUTF(rs.getString(2));
            output.writeUTF(rs.getString(3));
            output.writeUTF(rs.getString(4));
        }
        output.close();
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        System.out.println("client excuted function:  " + methodName);


    }
    
  public static void display_client_history (DataInputStream input, DataOutputStream output) throws SQLException, IOException {
     
     String query = "SELECT O.P_NAME,P.price,O.AMOUNT,M.DATE_,M.TOTAL_PRICE,O.ORDER_ID FROM MAIN_ORDERS AS M, ORDERS_DETAILS AS O , PRODUCTS AS P WHERE O.ORDER_ID=M.ORDER_ID and M.USER_N="
                    + "? and P.product_name=O.P_NAME ";     
     String count_query = "SELECT count(*) as record_count FROM  APP.MAIN_ORDERS AS M, APP.ORDERS_DETAILS AS O ,"
             + " APP.PRODUCTS AS P WHERE O.ORDER_ID=M.ORDER_ID and M.USER_N=? and P.product_name=O.P_NAME ";
        String user= input.readUTF();   
        PreparedStatement count_ps = ServerMultiClients.con.prepareStatement(count_query);
        
        count_ps.setString(1,user );
        ResultSet count_rs = count_ps.executeQuery();
        count_rs.next();
        int c = count_rs.getInt("record_count");
        output.writeInt(c);
    

     PreparedStatement ps4 = null;
     ps4 = ServerMultiClients.con.prepareStatement(query);
     ps4.setString(1,user );
     ResultSet rs = ps4.executeQuery();
        
     while (rs.next()) {
            output.writeUTF(rs.getString(1));
            output.writeUTF(rs.getString(2));
            output.writeUTF(rs.getString(4));
            output.writeUTF(rs.getString(3));
        }
        output.close();
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        System.out.println("client excuted function:  " + methodName);

       }
  
  
  
  
//   end of file 
  }
  
  
  
  
  
  
