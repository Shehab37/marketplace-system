/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servermulticlients;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class javaconnect {
    public static Connection connectlogin(){
        Connection con = null;
        try {
            con= (Connection) DriverManager.getConnection("jdbc:derby://localhost:1527/myloginfff");
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(javaconnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
