
package Controladores;

import Modelo.Servidor;
import Valores.SingletonUsuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * FXML Controller class
 *
 * @author Meinster
 */
public class LoginController implements Initializable {

    @FXML
    private MenuItem menuItemServidor;
    @FXML
    private MenuItem menuItemSalir;
    @FXML
    private MenuItem menuItemAcercaDe;
    @FXML
    private Button buttonOlvideMiContasena;
    @FXML
    private TextField textBoxCorreo;
    @FXML
   private PasswordField passwordFieldContrasena;
    @FXML
    private Button buttonIngresar;
    @FXML
    private Button buttonSalir;
    @FXML
    private MenuItem menuItemCambiarContraseña;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       getServidor();
    }    

    @FXML
    private void onClick_menuItemServidor(ActionEvent event) {
         Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/enki/ConexionServidor.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Config. de conexion de servidor"); 
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClick_menuItemSalir(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onClick_buttonOlvideMiContasena(ActionEvent event) {        
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/enki/OlvideContrasena.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setMinWidth(400);
            stage.setMinHeight(150);
            stage.setTitle(Valores.ValoresEstaticos.enki);
            stage.setScene(scene);
            stage.setResizable(false);
          //  ((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ListaLectorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    @FXML
    private void onClick_menuItemAcercaDe (ActionEvent event) {   
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/enki/AcercaDe.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Acerca de"); 
            stage.setMinWidth(250);
            stage.setMinHeight(250);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    //TODO: Valirdar ingreso
    @FXML
    private void onClick_buttonIngresar(ActionEvent event) {
        Parent root;
         try {
             if(isValidEmailAddress(textBoxCorreo.getText())) {
                 if(usuarioCorrecto()){
                           root = FXMLLoader.load(getClass().getResource("/enki/MainView.fxml"));
                           Scene scene = new Scene(root);
                           Stage stage = new Stage();
                           stage.setMinWidth(1000);
                           stage.setTitle(Valores.ValoresEstaticos.enki); 
                           stage.setScene(scene);
                          ((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual
                           stage.show();
                 }else{
                     Alert alert = new Alert(AlertType.INFORMATION);
                     alert.setTitle("Informacion");
                     alert.setHeaderText(null);
                     alert.setContentText("Usuario Incorrecto");
                     alert.showAndWait();
                 }
             }
             else
             {
                 Alert alert = new Alert(AlertType.NONE, "Ingrese email correctamente", ButtonType.OK);
alert.showAndWait();
             }
            
            
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (JSONException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    @FXML
    private void onClick_buttonSalir(ActionEvent event) {
        System.exit(0);
    }
    
    private void getServidor(){
         BufferedReader br= null;
         Servidor servidor= Valores.SingletonServidor.getInstancia();
          File archivo = new File(Valores.ValoresEstaticos.carpetaConfiguracion+"/"+Valores.ValoresEstaticos.archivoServidor);
        try {
             br = new BufferedReader(new FileReader(archivo.getAbsolutePath()));
            String linea;
            boolean bandera = true;
            while ((linea = br.readLine()) != null) {
                if(bandera){
                    servidor.setServidor(linea);
                    bandera=false;
                }else{
                    servidor.setPuerto(linea);
                }
	//System.out.println(linea);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException e) {
              e.printStackTrace();
         } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
   public static boolean isValidEmailAddress(String email) {
   boolean result = true;
   try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
   } catch (AddressException ex) {
      result = false;
   }
   return result;
}

   public boolean usuarioCorrecto() throws MalformedURLException, ProtocolException, IOException, JSONException {
       boolean flag=false;
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.loginPHP); // URL to your application
        System.out.println(url);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("correo", this.textBoxCorreo.getText().trim()); // All parameters, also easy
        params.put("contasena", this.passwordFieldContrasena.getText().trim());

    StringBuilder postData = new StringBuilder();
    for (Map.Entry<String,Object> param : params.entrySet()) {
        if (postData.length() != 0) postData.append('&');
        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
        postData.append('=');
        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
    }

    // Convert string to byte array, as it should be sent
    byte[] postDataBytes = postData.toString().getBytes("UTF-8");

    // Connect, easy
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    // Tell server that this is POST and in which format is the data
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
    conn.setDoOutput(true);
    conn.getOutputStream().write(postDataBytes);

    // This gets the output from your server
    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    String response="";
    System.out.println(in.toString());
    for (int c; (c = in.read()) >= 0;){
        response=response + (char)c;
    }
       System.out.println(response);
       JSONObject obj = new JSONObject(response);
       String mensaje = obj.getString("mensaje");
       
       //System.out.println("Mensaje: "+obj);
    
     if(mensaje.contains("true")){
         String tipo = obj.getString("tipo");
         SingletonUsuario.setUsuario(this.textBoxCorreo.getText().trim(), tipo);
         //System.out.println(obj.get("tipo"));
         return true;
     }  
     
       
       return flag;
   }  

    @FXML
    private void cambiarContraseña(ActionEvent event) {
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/enki/CambiarContraseña.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setMinWidth(400);
            stage.setMinHeight(150);
            stage.setResizable(false);
            stage.setTitle("Cambiar contrase�a"); 
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
