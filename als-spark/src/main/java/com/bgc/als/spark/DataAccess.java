/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgc.als.spark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author subhasish
 */
public class DataAccess {
    Connection con=null;
    static ArrayList<String> userids=new ArrayList<String>();
    //static int id=1010;
    public void insertpRecords(String ids,int id){
        
        con=DBConnection.getConnection();
        if(con!=null&&Collections.binarySearch(userids, ids)<0){
            userids.add(ids);
            String sql="insert into productids(id, product_id) values(?,?)";
            try {
                PreparedStatement pstmt=con.prepareStatement(sql);
                //id=id+1;
                pstmt.setInt(1,id);
                pstmt.setString(2, ids);
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            finally{
                try{
                    if(con!=null){
                        DBConnection.getConnection().close();
                        DBConnection.closeConnection();
                        con.close();
                        con=null;                    
                    }                    
                }
                catch(SQLException  s){
                    s.printStackTrace();
                }                
            }
            
        }
    }
    
    public void insertsRecords(String ids, int id){
        
        con=DBConnection.getConnection();
        if(con!=null&&Collections.binarySearch(userids, ids)<0){
            userids.add(ids);
            String sql="insert into systemids(id, customer) values(?,?)";
            try {
                PreparedStatement pstmt=con.prepareStatement(sql);
                //id=id+1;
                pstmt.setInt(1,id);
                pstmt.setString(2, ids);
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            finally{
                try{
                    if(con!=null){
                        DBConnection.getConnection().close();
                        DBConnection.closeConnection();
                        con.close();
                        con=null;                    
                    }                    
                }
                catch(SQLException  s){
                    s.printStackTrace();
                }                
            }
            
        }

    }
    
    public void insertRecords(String uid, String pid, float r, int t){
        con=DBConnection.getConnection();
        if(con!=null){
            String s1="SELECT id from systemids where customer=?";
            String s2="SELECT id from productids where product_id=?";
            int product_id=0;
            int customer=0;
            float rate=r;
            int timestamp=t;
            try {
                PreparedStatement pstmt=con.prepareStatement(s1);                
                pstmt.setString(1,uid);
                ResultSet rst = pstmt.executeQuery();
                while(rst.next()){
                    customer = rst.getInt(1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
             try {
                PreparedStatement pstmt=con.prepareStatement(s2);                
                pstmt.setString(1,pid);
                ResultSet rst = pstmt.executeQuery();
                while(rst.next()){
                    product_id = rst.getInt(1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            String sql="insert into rating(id, product_id, rating, timestamp) values(?,?,?,?)";
            try {
                PreparedStatement pstmt=con.prepareStatement(sql);
                
                pstmt.setInt(1,customer);
                pstmt.setInt(2,product_id);
                pstmt.setFloat(3, rate);
                pstmt.setInt(4, timestamp);
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            finally{
                try{
                    if(con!=null){
                        DBConnection.getConnection().close();
                        DBConnection.closeConnection();
                        con.close();
                        con=null;                    
                    }                    
                }
                catch(SQLException  s){
                    s.printStackTrace();
                }                
            }
            
        }


    }
    
}
