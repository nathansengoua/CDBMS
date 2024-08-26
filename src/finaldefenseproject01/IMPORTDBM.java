/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finaldefenseproject01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SENGOUA NATHAN
 */
public class IMPORTDBM {
 private Connection conn;
    private String url="jdbc:mysql://localhost:3306/mta_db";
    private String user="root";
    private String pswd="";
    public IMPORTDBM(){
        try {
            conn = DriverManager.getConnection( url,user,pswd);
            System.out.println("sucesfull Importdb connection");
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void close(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean exportlogin(String username,String password){
        try {
            PreparedStatement stmt = null;
            String querry= "select usename,password from employee where usename= ? AND password = ?";
            stmt = conn.prepareStatement(querry);
            stmt.setString(1, username);
            stmt.setString(2,password);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                return true;
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
     return false;
    }
    
    public boolean insertcustomer(long id, String name, String address,String gender){
        try {
            PreparedStatement stmt = null;
            String querry = "insert into customer(customer_id,name,address,sex) values(?,?,?,?)";
            stmt = conn.prepareStatement(querry);
            stmt.setLong(1, id);
            stmt.setString(2,name);
            stmt.setString(3, address);
            stmt.setString(4,gender);
            stmt.executeUpdate();
            System.out.println("customer inserted");
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean insertreciever(String name ,String address ,String gender ){
        try {
            PreparedStatement stmt = null;
            String querry = "insert into reciever(name,address,sex) values(?,?,?)";
            stmt = conn.prepareStatement(querry);
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, gender);
            stmt.executeUpdate();
            System.out.println("reciever inserted");
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public int getemployeeid(String username){
        int id=0;
        try {
            PreparedStatement stmt = null;
            String querry = "SELECT employee_id FROM employee where usename = ?";
            
            stmt= conn.prepareStatement(querry);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                id = rs.getInt(1);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("employee id = "+ id);
        return id;
    }
    
     public long getrecieverid() {
        int id=0;
        try {
            PreparedStatement stmt = null;
            String querry = "SELECT MAX(reciever_id) FROM reciever";
            stmt= conn.prepareStatement(querry);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                id = rs.getInt(1);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("reciever id = "+ id);
        return id;
     }
    
    public boolean insertagency(long LTA,String name,String country,String town){
        try {
            PreparedStatement stmt=null;
            String querry = "insert into transport_agency(LTA_BL,name,country,town) values(?,?,?,?)";
            stmt = conn.prepareStatement(querry);
            stmt.setLong(1, LTA);
            stmt.setString(2, name);
            stmt.setString(3,country);
            stmt.setString(4, town);
            //stmt.setDate(5, arrdate);
            stmt.executeUpdate();
            System.out.println("agency inserted");
            stmt.close();
            return true;
        }        catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return false;
    }

    public void inserproduct(long ID,String NATURE,int QUANTITY,int WEIGHT,String DESCRIPTION,Date INVOICEDATE,Date SHIPPINGDATE,String TRANSMEANS,String FOLDERNAME,long AMOUNT,long COUID,long AGENID,long EMPID){
        try {
            PreparedStatement stmt = null;
            String query = "insert into product(pro_id,nature,quantity,weight,Description,depot_date,shipping_date,trans_means,folder_name,amount,customer_id,reciever_id,agency_id,employee_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(query);
            stmt.setLong(1, ID);
            stmt.setString(2, NATURE);
            stmt.setInt(3, QUANTITY);
            stmt.setLong(4, WEIGHT);
            stmt.setString(5, DESCRIPTION);
            stmt.setDate(6, INVOICEDATE);
            stmt.setDate(7, SHIPPINGDATE);
            //stmt.setDate(, DELIVERYDATE);
            stmt.setString(8, TRANSMEANS);
            stmt.setString(9, FOLDERNAME);
            stmt.setLong(10, AMOUNT);
            stmt.setLong(11, COUID);
            stmt.setLong(12, this.getrecieverid());
            stmt.setLong(13, AGENID);
            stmt.setLong(14, EMPID);
            stmt.executeUpdate();
            System.out.println("product inserted");
            stmt.close();
          
                       
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet getTableData() {
         ResultSet rs = null;
        try {
          
            Statement stmt = conn.createStatement();

          
            String query = "SELECT `pro_id`,`nature`,`quantity`,`weight`,`Description`,`arrival_date`,`invoice_date`,`delivery_date`,`folder_name`,`amount`,customer.*,reciever.name as reciever_name, reciever.address as reciever_address, reciever.sex as reciever_sex,transport_agency.*, jorney.*  FROM product JOIN customer ON product.customer_id=customer.customer_id JOIN reciever ON product.reciever_id = reciever.reciever_id JOIN transport_agency ON product.agency_id=transport_agency.LTA_BL JOIN jorney on transport_agency.jorney_id=jorney.flight_vehicle_num where type='IMPORT'";
            rs = stmt.executeQuery(query);
            System.out.println("succefully table querry display");
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
   
public void insertEXPORTRow(long GOODSID, String NATURE, int QUANTITY, int WEIGHT, String DESCRIPTION, java.util.Date ARRIVALDATE, java.util.Date INVOICEDATE, java.util.Date DELIVERYDATE, String FOLDERNAME, String IMPNAME, String IMPADDRESS, String IMPSEX, long IMPID, String EXPNAME, String EXPADDRESS, String EXPSEX, long TRANID, String MEANS, String AGENCY, String COUNTRY, String TOWN, java.util.Date SHIPPINGDATE, String JORNEYID, Long AMOUNT) {
    try {
        String customerQuery = "INSERT INTO customer (customer_id, name, address, sex) VALUES (?, ?, ?, ?)";
        String receiverQuery = "INSERT INTO reciever (name, address, sex) VALUES (?, ?, ?)";
        String transportAgencyQuery = "INSERT INTO transport_agency (LTA_BL, name, shipping_date, jorney_id) VALUES (?, ?, ?, ?)";
        String jorneyQuery = "INSERT INTO jorney (flight_vehicle_num, destination_country, destination_town, means) VALUES (?, ?, ?, ?)";
        String productQuery = "INSERT INTO product (type, pro_id, nature, quantity, weight, Description, invoice_date, arrival_date, delivery_date, folder_name, amount, customer_id, reciever_id, agency_id, employee_id) VALUES ('IMPORT', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement customerStmt = conn.prepareStatement(customerQuery);
        customerStmt.setLong(1, IMPID);
        customerStmt.setString(2, IMPNAME);
        customerStmt.setString(3, IMPADDRESS);
        customerStmt.setString(4, IMPSEX);
        customerStmt.executeUpdate();
        customerStmt.close();

        PreparedStatement receiverStmt = conn.prepareStatement(receiverQuery);
        receiverStmt.setString(1, EXPNAME);
        receiverStmt.setString(2, EXPADDRESS);
        receiverStmt.setString(3, EXPSEX);
        receiverStmt.executeUpdate();
        receiverStmt.close();

        PreparedStatement jorneyStmt = conn.prepareStatement(jorneyQuery);
        jorneyStmt.setString(1, JORNEYID);
        jorneyStmt.setString(2, COUNTRY);
        jorneyStmt.setString(3, TOWN);
        jorneyStmt.setString(4, MEANS);
        jorneyStmt.executeUpdate();
        jorneyStmt.close();        
                
        PreparedStatement transportAgencyStmt = conn.prepareStatement(transportAgencyQuery);
        transportAgencyStmt.setLong(1, TRANID);
        transportAgencyStmt.setString(2, AGENCY);
        transportAgencyStmt.setDate(3, new java.sql.Date(SHIPPINGDATE.getTime()));
        transportAgencyStmt.setString(4, JORNEYID);
        transportAgencyStmt.executeUpdate();
        transportAgencyStmt.close();        

        PreparedStatement productStmt = conn.prepareStatement(productQuery);
        productStmt.setLong(1, GOODSID);
        productStmt.setString(2, NATURE);
        productStmt.setInt(3, QUANTITY);
        productStmt.setInt(4, WEIGHT);
        productStmt.setString(5, DESCRIPTION);
        productStmt.setDate(6, new java.sql.Date(INVOICEDATE.getTime()));
        productStmt.setDate(7, new java.sql.Date(ARRIVALDATE.getTime()));
        productStmt.setDate(8, new java.sql.Date(DELIVERYDATE.getTime()));
        productStmt.setString(9, FOLDERNAME);
        productStmt.setLong(10, AMOUNT);
        productStmt.setLong(11, IMPID);
        productStmt.setLong(12, this.getrecieverid());
        productStmt.setLong(13, TRANID);
        productStmt.setInt(14, 3);
        productStmt.executeUpdate();
        productStmt.close();

        System.out.println("EXCEL DOC SUCCESSFULLY inserted");
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

public boolean checkid(long GOODID) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("select pro_id from product where pro_id = ?");
            stmt.setLong(1, GOODID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}

