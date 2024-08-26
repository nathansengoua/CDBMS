/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finaldefenseproject01;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SENGOUA NATHAN
 */
    
    



public class DBManagement {
    private Connection conn;
    private String url="jdbc:mysql://localhost:3306/defenseproj_mta";
    private String user="root";
    private String pswd="";

    public DBManagement(){
     //constructor   
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(url, user, pswd);
            System.out.println("succefully DB Connection");
        } catch (SQLException ex) {
            Logger.getLogger(DBManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //verify login 
    public boolean verifyLogin(String username, String password) {
        try {
            String query="SELECT * FROM login WHERE username= ? AND password= ?";
            PreparedStatement stmt;
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
               return true; // Login successful
            }
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return false; // Login failed
    }
    public boolean checkid(long GOODID) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("select goodid from importdb where goodid = ?");
            stmt.setLong(1, GOODID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
  public boolean insertimportdetails(int GOODSID,String NATURE,int QUANTITY,int WEIGHT,String DESCRIPTION,String IMPNAME, String IMPADDRESS,String IMPSEX,int IMPID,String EXPNAME,String EXPADDRESS,String EXPSEX,int TRANID, String MEANS,String AGENCY,String COUNTRY,String TOWN,Date SHIPPINGDATE, Date INVOICEDATE){
      PreparedStatement stmt;
      try{
          stmt= conn.prepareStatement("INSERT INTO importdb (goodid,nature,quantity,weight,description,impname,impsex,impaddress,impid,recname,recaddress,recsex,transmeans,transagency,transref,importer_country,importer_town,shippingdate,invoicedate) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
          stmt.setInt(1, GOODSID);
          stmt.setString(2, NATURE);
          stmt.setInt(3, QUANTITY);
          stmt.setInt(4, WEIGHT);
          stmt.setString(5, DESCRIPTION);
          stmt.setString(6,IMPNAME);
          stmt.setString(7,IMPSEX);
          stmt.setString(8,IMPADDRESS);
          stmt.setInt(9,IMPID);
          stmt.setString(10, EXPNAME);
          stmt.setString(11, EXPADDRESS);
          stmt.setString(12,EXPSEX);
          stmt.setString(13,MEANS);
          stmt.setString(14,AGENCY);
          stmt.setInt(15,TRANID);
          stmt.setString(16, COUNTRY);
          stmt.setString(17, TOWN);
          stmt.setDate(18,SHIPPINGDATE);
          stmt.setDate(19,INVOICEDATE);
          //stmt.setDate(20,ARRIVALDATE);
          stmt.execute();
          System.out.println("goods details inserted");
          stmt.close();
          return true;
      }catch(SQLException ex){
          ex.printStackTrace();
          return false;
      }
  } 
  public ResultSet getTableData() {
         ResultSet rs = null;
        try {
          
            Statement stmt = conn.createStatement();

          
            String query = "SELECT * FROM importdb";
            rs = stmt.executeQuery(query);
            System.out.println("succefully table querry display");
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
  
 /* public void insertRow(long GOODSID,String NATURE,int QUANTITY,int WEIGHT,String DESCRIPTION,Date ARRIVALDATE,Date INVOICEDATE,Date DELIVERYDATE, String IMPNAME, String IMPADDRESS,String IMPSEX,long IMPID,String EXPNAME,String EXPADDRESS,String EXPSEX,long TRANID, String MEANS,String AGENCY,String COUNTRY,String TOWN,java.util.Date SHIPPINGDATE, java.util.Date INVOICEDATE, java.util.Date ARRIVALDATE){
      PreparedStatement stmt;
      try{
                                                                   
          stmt= conn.prepareStatement("INSERT INTO importdb (pro_id,nature,quantity,weight,Description,arrival_date,invoice_date,delivery_date,folder_name,amount,customer.name,customer.customer_id,customer.address,customer.sex,reciever.name, reciever.address,reciever.sex,transport_agency.LTA_BL,transport_agency.name,transport_agency.shipping_date,flight_vehicle_num,jorney.destination_country,jorney.destination_town,jorney.means) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
          //stmt= conn.prepareStatement("INSERT INTO importdb (goodid,nature,quantity,weight,description,impname,impsex,impaddress,impid,recname,recaddress,recsex,transmeans,transagency,transref,importer_country,importer_town,shippingdate,invoicedate,arrivaldate) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
          
          stmt.setLong(1, GOODSID);
          stmt.setString(2, NATURE);
          stmt.setInt(3, QUANTITY);
          stmt.setInt(4, WEIGHT);
          stmt.setString(5, DESCRIPTION);
          stmt.setString(6,IMPNAME);
          stmt.setString(7,IMPSEX);
          stmt.setString(8,IMPADDRESS);
          stmt.setLong(9,IMPID);
          stmt.setString(10, EXPNAME);
          stmt.setString(11, EXPADDRESS);
          stmt.setString(12,EXPSEX);
          stmt.setString(13,MEANS);
          stmt.setString(14,AGENCY);
          stmt.setLong(15,TRANID);
          stmt.setString(16, COUNTRY);
          stmt.setString(17, TOWN);
          stmt.setDate(18, new java.sql.Date(SHIPPINGDATE.getTime()));
          stmt.setDate(19, new java.sql.Date(INVOICEDATE.getTime()));
          stmt.setDate(20, new java.sql.Date(ARRIVALDATE.getTime()));
          
          stmt.executeUpdate();
          System.out.println("EXCELL DOC SUCEESFULLY inserted");
          stmt.close();
          
      }catch(SQLException ex){
          ex.printStackTrace();
          
      }
    }
  
  public void closeconnection(){
       try{
           conn.close();
       }catch(SQLException ex){
           ex.printStackTrace();
       }
   }
   
   */
}
    
