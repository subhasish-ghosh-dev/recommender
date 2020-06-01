/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgc.als.spark;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author subhasish
 */
public class DBConnection {
    
    private static Connection con=null;
    
    private DBConnection(){}
    
    public static Connection getConnection(){
        if(con!=null){
            return con;
        }
        else{
            try{
                Class.forName("com.mysql.jdbc.Driver");
                con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sm","root","12345678");
                
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                
            }
            return con;
        }
    }
    
    public static void closeConnection(){
        con=null;
    }
    
    
}
