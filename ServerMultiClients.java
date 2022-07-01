/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package servermulticlients;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author phelp
 */
public class ServerMultiClients {

    /**
     * @param args the command line arguments
     */
    static Connection con = javaconnect.connectlogin();  // connecting to db

    private static final int PORT = 22000;         // port num
    private static final int N_THREADS = 10;         // port num
//    private static final ArrayList<ClientHandler> clients = new ArrayList<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);

    public static void main(String[] args) {

        System.out.println("connected to db");

        ServerSocket server_socket = null;
        try {
            server_socket = new ServerSocket(PORT);
        } catch (IOException ex) {
            Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
        }
        int clientCounter = 0;
        while (true) {
            try {
                Socket client_socket = server_socket.accept();
                System.out.println("function No  " + (clientCounter + 1) + " has CONNECTED to server");

                ClientHandler client_thread = new ClientHandler(client_socket);
//                clients.add(client_thread);
                pool.execute(client_thread);

            } catch (IOException ex) {
                Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
            }
            clientCounter++;
        }

    }

}
