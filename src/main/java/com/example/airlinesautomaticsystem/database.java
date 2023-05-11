package com.example.airlinesautomaticsystem;

import java.sql.Connection;
import java.sql.DriverManager;


public class database {
    
    public static Connection connectDb(){
        
        try{
            
            Class.forName("com.mysql.jdbc.Driver");

            return DriverManager.getConnection("jdbc:mysql://localhost/employee", "root", "");
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
    
}
