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
public class EXPORTDBM {
    private Connection conn;
    private String url="jdbc:mysql://localhost:3306/mta_db";
    private String user="root";
    private String pswd="";
    public EXPORTDBM(){
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
    
    public ResultSet exportlogin(String username,String password){
        ResultSet rs=null;
        try {
            PreparedStatement stmt = null;
            String querry= "select usename,password,access from employee where usename= ? AND password = ?";
            stmt = conn.prepareStatement(querry);
            stmt.setString(1, username);
            stmt.setString(2,password);
            rs = stmt.executeQuery();
                
            
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
         
        }
        return rs;
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
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("reciever id = "+ id);
        return id;
     }
     
    
    
    public boolean insertagency(long LTA,String name,Date shipping_date,String jorneyID ){
        try {
            PreparedStatement stmt=null;
            String querry = "insert into transport_agency(LTA_BL,name,shipping_date,jorney_id) values(?,?,?,?)";
            stmt = conn.prepareStatement(querry);
            stmt.setLong(1, LTA);
            stmt.setString(2, name);
            stmt.setDate(3,shipping_date);
            stmt.setString(4, jorneyID);
            //stmt.setDate(5, arrdate);
            stmt.executeUpdate();
            System.out.println("agency inserted");
            stmt.close();
            return true;
        }        catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return false;
    }

    public boolean insertjorney(String jorneyID,String descountry,String destown,String means){
        try {
            PreparedStatement stmt=null;
            String querry = "insert into jorney(flight_vehicle_num,destination_country,destination_town,means) values(?,?,?,?)";
            stmt = conn.prepareStatement(querry);
            stmt.setString(1, jorneyID);
            stmt.setString(2, descountry);
            stmt.setString(3,destown);
            stmt.setString(4, means);
            //stmt.setDate(5, arrdate);
            stmt.executeUpdate();
            System.out.println("agency inserted");
            stmt.close();
            return true;
        }        catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return false;
    }
    
    public void inserproduct(String type,long ID,String NATURE,int QUANTITY,int WEIGHT,String DESCRIPTION,Date INVOICEDATE,Date ARRIVALDATE, Date DELIVERYDATE,String FOLDERNAME,long AMOUNT,long COUID,long AGENID,long EMPID){
        try {
            PreparedStatement stmt = null;
            String query = "insert into product(type,pro_id,nature,quantity,weight,Description,invoice_date,arrival_date,delivery_date,folder_name,amount,customer_id,reciever_id,agency_id,employee_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, type);
            stmt.setLong(2, ID);
            stmt.setString(3, NATURE);
            stmt.setInt(4, QUANTITY);
            stmt.setLong(5, WEIGHT);
            stmt.setString(6, DESCRIPTION);
            stmt.setDate(7, INVOICEDATE);
            stmt.setDate(8, ARRIVALDATE);
            stmt.setDate(9, DELIVERYDATE);
            stmt.setString(10, FOLDERNAME);
            stmt.setLong(11, AMOUNT);
            stmt.setLong(12, COUID);
            stmt.setLong(13, this.getrecieverid());
            stmt.setLong(14, AGENID);
            stmt.setLong(15, EMPID);
           
            stmt.executeUpdate();
            System.out.println("product inserted");
            stmt.close();
          
                       
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet getTableData() {
         ResultSet rs = null;
        try {
          
            Statement stmt = conn.createStatement();
         
            String query = "SELECT `pro_id`,`nature`,`quantity`,`weight`,`Description`,`arrival_date`,`invoice_date`,`delivery_date`,`folder_name`,`amount`,customer.*,reciever.name as reciever_name, reciever.address as reciever_address, reciever.sex as reciever_sex,transport_agency.*, jorney.*  FROM product JOIN customer ON product.customer_id=customer.customer_id JOIN reciever ON product.reciever_id = reciever.reciever_id JOIN transport_agency ON product.agency_id=transport_agency.LTA_BL JOIN jorney on transport_agency.jorney_id=jorney.flight_vehicle_num where type='EXPORT'";
            rs = stmt.executeQuery(query);
            System.out.println("succefully table querry display");
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
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
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    

   public void insertEXPORTRow(long GOODSID, String NATURE, int QUANTITY, int WEIGHT, String DESCRIPTION, java.util.Date ARRIVALDATE, java.util.Date INVOICEDATE, java.util.Date DELIVERYDATE, String FOLDERNAME, String IMPNAME, String IMPADDRESS, String IMPSEX, long IMPID, String EXPNAME, String EXPADDRESS, String EXPSEX, long TRANID, String MEANS, String AGENCY, String COUNTRY, String TOWN, java.util.Date SHIPPINGDATE, String JORNEYID, Long AMOUNT) {
    try {
        String customerQuery = "INSERT INTO customer (customer_id, name, address, sex) VALUES (?, ?, ?, ?)";
        String receiverQuery = "INSERT INTO reciever (name, address, sex) VALUES (?, ?, ?)";
        String transportAgencyQuery = "INSERT INTO transport_agency (LTA_BL, name, shipping_date, jorney_id) VALUES (?, ?, ?, ?)";
        String jorneyQuery = "INSERT INTO jorney (flight_vehicle_num, destination_country, destination_town, means) VALUES (?, ?, ?, ?)";
        String productQuery = "INSERT INTO product (type, pro_id, nature, quantity, weight, Description, invoice_date, arrival_date, delivery_date, folder_name, amount, customer_id, reciever_id, agency_id, employee_id) VALUES ('EXPORT', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

    public void Delete(long id) {
         try {
              String query = "Delete From product where product.pro_id= ?";
              PreparedStatement stmt = conn.prepareStatement(query);
              stmt.setLong(1, id);
              stmt.execute();
              stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean updatejorney(String jorney_id, String country, String town, String means) {
        try {
            PreparedStatement stmt=null;
            String querry = "update jorney set destination_country = ?, destination_town = ?, means = ? where flight_vehicle_num = ?";
            stmt = conn.prepareStatement(querry);
            
            stmt.setString(1, country);
            stmt.setString(2,town);
            stmt.setString(3, means);
            stmt.setString(4, jorney_id);
            stmt.executeUpdate();
            System.out.println("agency updated");
            stmt.close();
            return true;
        }        catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return false;
    }
    public String getjourneyFromtransport(Long ID){
        String id= null;
        try {
            String query= "select jorney_id from transport_agency where LTA_BL = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1,ID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                id =rs.getString("jorney_id");
                System.out.println(id +":" +ID);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public void updateproduct(String typ, long goodid, String nature, int qty, int weight, String description, Date finalinvdate, Date finalarrdate, Date finaldeldate, String fdname, long fairs, long idnum, long transportid, long employee_id) {
       try {
            PreparedStatement stmt = null;
            String query = "update product set type= ?, nature = ?, quantity = ?, weight = ?, Description = ?, invoice_date = ?, arrival_date = ?, delivery_date = ?, folder_name = ?, amount = ?, customer_id = ?, agency_id = ?, employee_id = ? where pro_id= ? ";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, typ);
            
            stmt.setString(2, nature);
            stmt.setInt(3, qty);
            stmt.setLong(4, weight);
            stmt.setString(5, description);
            stmt.setDate(6, finalinvdate);
            stmt.setDate(7, finalarrdate);
            stmt.setDate(8, finaldeldate);
            stmt.setString(9, fdname);
            stmt.setLong(10, fairs);
            stmt.setLong(11, idnum);
            stmt.setLong(12, transportid);
            stmt.setLong(13, employee_id);
            stmt.setLong(14, goodid);
            
            stmt.executeUpdate();
            System.out.println("product inserted");
            stmt.close();
          
                       
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean updatecustomer(long idnum, String ipname, String ipaddress, String ipsex) {
         try {
            PreparedStatement stmt = null;
            String querry = "update customer set name= ?, address= ?, sex=? where customer_id= ?";
            stmt = conn.prepareStatement(querry);
            stmt.setLong(4, idnum);
            stmt.setString(1,ipname);
            stmt.setString(2, ipaddress);
            stmt.setString(3,ipsex);
            stmt.executeUpdate();
            System.out.println("customer updated");
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public int getrecieverFromproduct(long ID){
        int id= 0;
        try {
            String query= "select reciever_id from product where pro_id= ? ";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1,ID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                id=rs.getInt("reciever_id");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    public boolean updatereciever(String epname, String epaddress, String epsex, int id) {
       try {
            PreparedStatement stmt = null;
            String querry = "Update reciever SET name= ? , address = ?, sex = ? where reciever_id= ?";
            stmt = conn.prepareStatement(querry);
            stmt.setString(1, epname);
            stmt.setString(2, epaddress);
            stmt.setString(3, epsex);
            stmt.setInt(4,id);
            stmt.executeUpdate();
            System.out.println("reciever update");
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean updateagency(long transportid, String agency, Date finalshipdate) {
        try {
            PreparedStatement stmt=null;
            String query = "update transport_agency SET name= ?, shipping_date= ? where LTA_BL= ?";
            stmt = conn.prepareStatement(query);
            stmt.setLong(3, transportid);
            stmt.setString(1, agency);
            stmt.setDate(2,finalshipdate);
            stmt.executeUpdate();
            System.out.println("agency update");
            stmt.close();
            return true;
        }        catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return false;
    }
}
