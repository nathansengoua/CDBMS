/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finaldefenseproject01;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SENGOUA NATHAN
 */
public class ReportDBM {
     private Connection conn;
    private String url="jdbc:mysql://localhost:3306/mta_db";
    private String user="root";
    private String pswd="";
    public ReportDBM(){
        try {
            conn = DriverManager.getConnection( url,user,pswd);
            System.out.println("sucesfull Exportdb connection");
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void close(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   public ResultSet displayexportreporttable(int year, int month){
          ResultSet rs = null;  
       try {
             PreparedStatement stmt = null;
          
             String query= "SELECT count(destination_country) as frequency, jorney.destination_country FROM product "
                     + "JOIN customer ON product.customer_id=customer.customer_id JOIN reciever ON product.reciever_id = reciever.reciever_id "
                     + "JOIN transport_agency ON product.agency_id=transport_agency.LTA_BL "
                     + "JOIN jorney on transport_agency.jorney_id=jorney.flight_vehicle_num "
                     + "WHERE type=\"export\" and YEAR(product.invoice_date) = ? AND month(product.invoice_date) = ? "
                     + "GROUP by destination_country;" ;
             stmt= conn.prepareStatement(query);
             stmt.setInt(1, year);
             stmt.setInt(2, month);
             rs = stmt.executeQuery();
             System.out.println("succesfll querry");
         } catch (SQLException ex) {
             Logger.getLogger(ReportDBM.class.getName()).log(Level.SEVERE, null, ex);
         }
       return rs;
       
   } 
    
    public ArrayList<String> retrieveyears() {
        ArrayList<String> records = new ArrayList<>(); 
        try {
         // Create and execute a query to retrieve records from the database table
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT YEAR(invoice_date) AS YEAR FROM product");

      
          // Iterate through the result set and add records to the list
          while (resultSet.next()) {
              String record = resultSet.getString("YEAR");
              records.add(record);
            }
          resultSet.close();
          statement.close();
      } catch (SQLException ex) {
          Logger.getLogger(DBManagement.class.getName()).log(Level.SEVERE, null, ex);
      }
         return records;
             
   }
    
    
}
    
    

