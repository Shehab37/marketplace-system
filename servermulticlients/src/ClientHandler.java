package servermulticlients;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author phelp
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {

    Socket client_socket;
    DataInputStream input;
    DataOutputStream output;

    ClientHandler(Socket client) throws IOException {
        this.client_socket = client;

        input = new DataInputStream(client_socket.getInputStream());
        output = new DataOutputStream(client_socket.getOutputStream());

    }

    @Override
    public void run() {
        try {
            while (true) {
                String i = input.readUTF();

                if (i.equals("login")) {
                    Functions.login(input, output);

                }
//                if (i.equals("logout")) {
//                    logout();
//                }
                if (i.equals("sign_up")) {
                    Functions.sign_up(input, output);

                }
                if (i.equals("view_account")) {
                    Functions.view_account(input, output);
                }
                if (i.equals("get_products_user")) {
                    Functions.get_products_user(input, output);
                }
                if (i.equals("get_cart_products")) {
                    Functions.get_cart_products(input, output);
                }
                if (i.equals("get_products_admin")) {
                    Functions.get_products_admin(input, output);
                }
                if (i.equals("check_admin")) {
                    Functions.check_admin(input, output);
                }
                if (i.equals("add_item")) {
                    Functions.add_item(input, output);
                }
                if (i.equals("update_item")) {
                    Functions.update_item(input, output);
                }
                if (i.equals("delete_item")) {
                    Functions.delete_item(input, output);
                }
                if (i.equals("get_amount")) {
                    Functions.get_amount(input, output);
                }
                if (i.equals("change_amount")) {
                    Functions.change_amount(input, output);
                }
                if (i.equals("insert_to_cart")) {
                    Functions.insert_to_cart(input, output);
                }
                if (i.equals("remove_all_cart")) {
                    Functions.remove_all_cart(input, output);
                }
                if (i.equals("remove_cart_item")) {
                    Functions.remove_cart_item(input, output);
                }
                if (i.equals("search_item")) {
                    Functions.search_item(input, output);
                }
//                if (i.equals("edit_item_amount")) {
//                   Functions.edit_item_amount(input ,output);
//                }
//                if (i.equals("check_out")) {
//                   Functions.check_out(input ,output);
//                }
                if (i.equals("get_total_price")) {
                    Functions.get_total_price(input, output);
                }
                if (i.equals("check_out_order")) {
                    Functions.check_out_order(input, output);
                }
                if (i.equals("check_price_with_wallet")) {
                    Functions.check_price_with_wallet(input, output);
                }

                if (i.equals("search_item_Admin")) {
                    Functions.search_item_Admin(input, output);
                }
                if (i.equals("validate_cvv")) {
                    Functions.validate_cvv(input, output);
                }
                if (i.equals("deposit")) {
                    Functions.deposit(input, output);
                }
                if (i.equals("deposit_panel")) {
                    Functions.deposit_panel(input, output);
                }
                if(i.equals("display_history")){
                    Functions.display_history(input, output);
                }
                if(i.equals("display_client_history")){
                    Functions.display_client_history(input, output);
                }
                if (i.equals("empty_cart")) {
                    Functions.empty_cart(input, output);
                }
            }

        } catch (IOException | SQLException e) {
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
                System.out.println("connection closed");

            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
