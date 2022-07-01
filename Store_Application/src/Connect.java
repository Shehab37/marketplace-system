/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my_package;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Shehab
 */
public class Connect {
    // remember to change the ip address for local network
//    public static final String SERVER_IP = "127.0.0.1";
    public static  String SERVER_IP = get_server();
    public static final int PORT = 22000;         // port num
    public static Socket client_socket;
    public static DataInputStream clientReadSource;
    public static DataOutputStream serverOutputStream;

    public static void create_socket() {
        try {
            System.out.println(SERVER_IP);
            client_socket = new Socket(SERVER_IP, PORT);
            clientReadSource = new DataInputStream(client_socket.getInputStream());
            serverOutputStream = new DataOutputStream(client_socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
public static String get_server()
  {
      try {
       FileReader   file = new FileReader("config.properties");
          Properties Properties = new Properties();
          Properties.load(file);

            String ip = Properties.getProperty("server_ip_address");
           System.out.println(ip);
          
          return ip;
      } catch (FileNotFoundException ex) {
              return "127.0.0.1";
              
          } catch (IOException ex) {
            return "127.0.0.1";
      }}     

}
