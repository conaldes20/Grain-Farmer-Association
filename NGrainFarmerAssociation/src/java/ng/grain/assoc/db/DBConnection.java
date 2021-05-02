/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.db;

/**
 *
 * @author CONALDES
 */;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnection {  
    private static String DBNAME = "grfmassocdb";
    
    public static Connection getDSBasedConnection() { //throws SQLException, NamingException {         
        InitialContext ctx = null;
        DataSource ds;
        Connection conn = null;
        try {
            ctx = new InitialContext();
            ds = (DataSource)ctx.lookup("java:app/mysqlDatasource");
            conn = ds.getConnection("conaldes", "conaDAO123");
        } catch (NamingException | SQLException ex) {
            System.err.println("getDSBasedConnection() => SQLException: " + ex.getMessage()); 
        }
         return conn;
    }        
        
    public static Connection getMSQLConnection() {
        Connection dbconn = null;              
        Properties properties = new java.util.Properties();
        properties.put("user", "conaldes");
    	properties.put("password", "conaDAO123");
        String MySQLURL = "jdbc:mysql://localhost:3306/grfmassocdb?zeroDateTimeBehavior=convertToNull&autoReconnect=true&useSSL=false";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbconn = DriverManager.getConnection(MySQLURL, properties); 
            System.err.println("Remote server is connected."); 
         } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("getMSQLConnection() => SQLException: " + ex.getMessage()); 
        }
        return dbconn;
    }
}
