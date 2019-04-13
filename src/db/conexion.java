package db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;

public class conexion {
    public Connection con;
    public ResultSet res;
    public Statement stat;
    
    public void db() {
    Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("setting.properties"));
            String driverName = properties.getProperty("driverName");
            String jdbcUrl = properties.getProperty("jdbcUrl");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String serverName = properties.getProperty("serverName");
            String port = properties.getProperty("port");
            String databaseName = properties.getProperty("databaseName");
            try {
                Class.forName(driverName);
                con=DriverManager.getConnection(jdbcUrl+serverName+":"+port+"/"+databaseName,user,password);
                stat=con.createStatement();
            } catch (Exception e) {
                
            }
            
            
        } catch (IOException e) {
             
        }
    
    }
    
    public void testKoneksi(String server, String port, String database, String username, String password){
            if ("".equals(server)||"".equals(port)||"".equals(database))
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setHeaderText(null);
                alert.setContentText("Por favor complete el servidor, puerto y base de datos !!");
                alert.showAndWait();
            }
            else{
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con=DriverManager.getConnection("jdbc:mysql://"+server+":"+port+"/"+database,username,password);
                stat=con.createStatement();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukses");
                alert.setHeaderText(null);
                alert.setContentText("Conexión exitosa !!");
                alert.showAndWait();
            } catch (Throwable e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(String.valueOf(e.getMessage()));
                alert.showAndWait();
            }
            }
    }
    public void simpanKoneksi(String server, String port, String database, String username, String password){
            if ("".equals(server)||"".equals(port)||"".equals(database))
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setHeaderText(null);
                alert.setContentText("Por favor complete el servidor, puerto y base de datos !!");
                alert.showAndWait();
            }
            else{
            try {
                Properties properties = new Properties();
                properties.setProperty("driverName", "com.mysql.jdbc.Driver");
                properties.setProperty("jdbcUrl", "jdbc:mysql://");
                properties.setProperty("user", username);
                properties.setProperty("password", password);
                properties.setProperty("serverName", server);
                properties.setProperty("port", port);
                properties.setProperty("databaseName", database);

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    con=DriverManager.getConnection("jdbc:mysql://"+server+":"+port+"/"+database,username,password);
                    stat=con.createStatement();
                    properties.store(new FileOutputStream("setting.properties"), "Configuración de conexión MySQL");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sukses");
                    alert.setHeaderText(null);
                    alert.setContentText("Conexión guardada correctamente...");
                    alert.showAndWait();
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(String.valueOf(ex));
                    alert.showAndWait();
                }
                
            } catch (Throwable e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(String.valueOf(e.getMessage()));
                alert.showAndWait();
            }
            }
    }
    
}
