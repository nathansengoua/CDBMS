package finaldefenseproject01;

import com.amazonaws.SdkClientException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ACCOUNT__CLOUD {
      
    // AWS credentials (for localstack, credentials can be blank)
    private static final String AWS_ACCESS_KEY = "";
    private static final String AWS_SECRET_KEY = "";
    private static final String AWS_REGION = "us-east-1"; 
    private static final String BUCKET_NAME = "defensebucket";
    private final String PRECLOUD_DIRECTORY = System.getProperty("user.home") + "/AppData/Local/CDMSCloud/precloud";
    private final String POSTCLOUD_DIRECTORY = System.getProperty("user.home") + "/AppData/Local/CDMSCloud/postcloud";
    private Connection conn;
    private String url="jdbc:mysql://localhost:3306/mta_db";
    private String user="root";
    private String pswd="";
    
 public ACCOUNT__CLOUD(){
     try {
            conn = DriverManager.getConnection( url,user,pswd);
            System.out.println("sucesfull Importdb connection");
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        configureAWS();
        createcoudfolder();
        //backupMySQLDatabase();
    }
    
   
      
    
    private void createcoudfolder() {
        // Get the home directory of the current user
       String userHome = System.getProperty("user.home")+"/AppData/Local";
        // Define the path to the Cloud folder
        String cloudFolderPath = userHome + File.separator + "CDMSCloud";

        // Create a File object representing the Cloud folder
        File cloudFolder = new File(cloudFolderPath);

        // Check if the Cloud folder already exists
        if (!cloudFolder.exists()) {
            // Attempt to create the Cloud folder
            boolean cloudCreated = cloudFolder.mkdir();

            // Check if the Cloud folder creation was successful
            if (cloudCreated) {
                System.out.println("Cloud folder created successfully.");

                // Define the path to the precloud folder within the Cloud folder
                String precloudFolderPath = cloudFolderPath + File.separator + "precloud";
                System.out.println(precloudFolderPath);
                // Create a File object representing the precloud folder
                File precloudFolder = new File(precloudFolderPath);

                // Attempt to create the precloud folder
                boolean precloudCreated = precloudFolder.mkdir();

                // Check if the precloud folder creation was successful
                if (precloudCreated) {
                    System.out.println("precloud folder created successfully.");
                } else {
                    System.err.println("Failed to create precloud folder.");
                }

                // Define the path to the postcloud folder within the Cloud folder
                String postcloudFolderPath = cloudFolderPath + File.separator + "postcloud";
                System.out.println(postcloudFolderPath);

                // Create a File object representing the postcloud folder
                File postcloudFolder = new File(postcloudFolderPath);

                // Attempt to create the postcloud folder
                boolean postcloudCreated = postcloudFolder.mkdir();

                // Check if the postcloud folder creation was successful
                if (postcloudCreated) {
                    System.out.println("postcloud folder created successfully.");
                } else {
                    System.err.println("Failed to create postcloud folder.");
                }

            } else {
                System.err.println("Failed to create Cloud folder.");
            }
        } else {
            System.out.println("Cloud folder already exists.");
        }
    }
    

    // Configure AWS SDK and return the Amazon S3 client
    private AmazonS3 configureAWS() {
        String endpointUrl = "http://127.0.0.1:4566";
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointUrl, "us-east-1"))
                .withPathStyleAccessEnabled(true)  // Enable path-style access for LocalStack
                .build();
            
            // Check if the bucket already exists
            if (!s3Client.doesBucketExistV2(BUCKET_NAME)) {
                // Create the bucket
                s3Client.createBucket(new CreateBucketRequest(BUCKET_NAME));
                System.out.println("Bucket created: " + BUCKET_NAME);
            } else {
                System.out.println("Bucket already exists: " + BUCKET_NAME);
            }
            
            return s3Client;
        } catch (SdkClientException e) {
            System.err.println("Error occurred while configuring AWS client: " + e.getMessage());
            return null;
        }
    }
   

    // Upload data to S3 bucket using the provided Amazon S3 client
    public boolean uploadData() {
        AmazonS3 s3Client=configureAWS();
        try {
            File folder = new File(PRECLOUD_DIRECTORY);
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String key = "data/" + file.getName();
                        // Check if the object already exists in the bucket
                        if (!s3Client.doesObjectExist(BUCKET_NAME, key)) {
                            // If the object does not exist, upload it
                            s3Client.putObject(new PutObjectRequest(BUCKET_NAME, key, file));
                            System.out.println("Uploaded " + file.getName() + " to S3 bucket");
                            return true;
                        } else {
                            System.out.println("File " + file.getName() + " already exists in the S3 bucket. Skipping upload.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error occurred while uploading data to S3: " + e.getMessage());
        }
        return false;
    }
    public boolean restoreFilesFromS3() {
        AmazonS3 s3Client=configureAWS();
        try {
            ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(BUCKET_NAME);
            ListObjectsV2Result result;
            do {
                result = s3Client.listObjectsV2(request);
                List<S3ObjectSummary> objects = result.getObjectSummaries();
                for (S3ObjectSummary objectSummary : objects) {
                    String key = objectSummary.getKey();
                    String restoredFileName = POSTCLOUD_DIRECTORY + File.separator + key;
                    // Get object from S3
                    S3Object s3Object = s3Client.getObject(new GetObjectRequest(BUCKET_NAME, key));
                    // Read object content from input stream
                    S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
                    byte[] objectContent = IOUtils.toByteArray(objectInputStream);
                    // Save object content to a local file
                    File restoredFile = new File(restoredFileName);
                    FileOutputStream outputStream = new FileOutputStream(restoredFile);
                    outputStream.write(objectContent);
                    outputStream.close();
                    System.out.println("Restored file from S3 bucket: " + restoredFileName);
                    return true;
                }
                request.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());
        } catch (IOException e) {
            System.err.println("Error occurred while restoring files from S3: " + e.getMessage());
            return false;
        }
        return false;
    }


//create a mysql backupfile
    public boolean backupMySQLDatabase() {
    String outputPath = PRECLOUD_DIRECTORY + File.separator + "backup.sql";

    try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tablesResultSet = metaData.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});

        while (tablesResultSet.next()) {
            String tableName = tablesResultSet.getString("TABLE_NAME");
            if (!tableName.equals("employee")) {
                exportTableData(conn, tableName, writer);
            }
        }
        
        System.out.println("Data backup successful.");
        return true;
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean restoreMSQLDatabase(){
        String sqlFilePath = POSTCLOUD_DIRECTORY+"/data/backup.sql";

        try (Statement statement = conn.createStatement()) {

            // Disable foreign key checks
            try (Statement disableFK = conn.createStatement()) {
                disableFK.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
            }

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath))) {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] sqlCommands = stringBuilder.toString().split(";");

            for (String sqlCommand : sqlCommands) {
                try {
                    if (!sqlCommand.trim().isEmpty()) {
                        statement.executeUpdate(sqlCommand);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // Enable foreign key checks
            try (Statement enableFK = conn.createStatement()) {
                enableFK.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
            }

            System.out.println("Data restore completed.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    
    }

        private void exportTableData(Connection connection, String tableName, PrintWriter writer) throws SQLException {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Export table data
                while (resultSet.next()) {
                    writer.print("INSERT INTO `" + tableName + "` VALUES (");
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = resultSet.getObject(i);
                        if (value == null) {
                            writer.print("NULL");
                        } else if (value instanceof Number) {
                            writer.print(value);
                        } else {
                            writer.print("'" + value.toString().replaceAll("'", "''") + "'");
                        }
                        if (i < columnCount) {
                            writer.print(",");
                        }
                    }
                    writer.println(");");
                }

                writer.println();
            }
        }

 public ResultSet getTableData() {
         ResultSet rs = null;
        try {
          
            Statement stmt = conn.createStatement();

          
            String query = "select * from employee";
            rs = stmt.executeQuery(query);
            System.out.println("succefully table querry display");
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }       

    void close() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public boolean ADDUSER(String username,String password,int access){
        try {
            PreparedStatement stmt = null;
            String querry= "insert into employee(usename,password,access) values(?,?,?)";
            stmt = conn.prepareStatement(querry);
            stmt.setString(1, username);
            stmt.setString(2,password);
            stmt.setInt(3,access);
            stmt.executeUpdate();
            stmt.close();
            return true;
                
        } catch (SQLException ex) {
            Logger.getLogger(IMPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

          }

          public boolean updateusers(String name, String password,int access, int id) {
        try {
            
            String query = "update transport_agency SET usename= ?, password= ?, access= ? where employee_id = ?";
            PreparedStatement stmt= conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setInt(3,access);
            stmt.setInt(4,id);
            stmt.executeUpdate();
            System.out.println("user update");
            stmt.close();
            return true;
        }        catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return false;
    }
    public void Delete(int id) {
         try {
              String query = "Delete From employee where employee_id= ?";
              PreparedStatement stmt = conn.prepareStatement(query);
              stmt.setLong(1, id);
              stmt.execute();
              stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EXPORTDBM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

