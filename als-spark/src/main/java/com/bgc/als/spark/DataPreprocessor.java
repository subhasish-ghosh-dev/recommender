/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgc.als.spark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author subhasish
 */
public class DataPreprocessor {
    
    
    public static void main(String[] args){
        BufferedReader reader=null;
        //BufferedReader reader2=null;
        //BufferedWriter writer=null;
        try {
            File file=new File("ratings_Books.csv");
            //File file2 =new File("ratings_Books_1.csv");
            reader = new BufferedReader(new FileReader(file));
            //writer = new BufferedWriter(new FileWriter(file2));
            String line=null;
            int userid=1200;
            int movieid=1001;
            int count=1;
            DataAccess da=new DataAccess();
            System.out.println("HASH STARTED");
            while((line=reader.readLine())!=null){
                //movieid=movieid+1;
                userid=userid+1;
                //count=count+1;
                String[] splits=line.split(",");
                //if(userid==3500000)break;
                //System.out.println(m.get(splits[0]));
                //System.out.println(movieid);
                //if(movieid>142723)
                //da.insertpRecords(splits[0],movieid);
                if(userid>340867)
                da.insertsRecords(splits[1],userid);
                //if(count>22713)
                //da.insertRecords(splits[1], splits[0], Float.parseFloat(splits[2]), Integer.parseInt(splits[3]));
            }
            /*System.out.println("WRITE STARTED");
            int count=1;
            line=null;
            reader2 = new BufferedReader(new FileReader(file));
            while((line=reader2.readLine())!=null){
                //if(count++==3500000)break;
                StringBuilder str=new StringBuilder();
                String[] splits=line.split(",");
                str.append(m.get(splits[1])).append(",");
                str.append(p.get(splits[0])).append(",");
                str.append(splits[2]).append(",");
                str.append(splits[3]).append("\r\n");
                
                //System.out.println(m.get(splits[0]));
                writer.write(str.toString());
            }*/
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                
            }
        }
        
    }
    
}
