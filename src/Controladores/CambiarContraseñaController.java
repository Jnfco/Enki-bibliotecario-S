/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Valores.Validaciones;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author jnfco
 */
public class CambiarContraseñaController implements Initializable {

    @FXML
    private TextField textFieldCorreo;
    @FXML
    private PasswordField textFieldContraseñaActual;
    @FXML
    private TextField textFieldContraseñaNueva;
    @FXML
    private Button buttonAceptar;
    @FXML
    private Button buttonCancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.textFieldCorreo.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(200));
        this.textFieldContraseñaActual.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(200));
        this.textFieldContraseñaActual.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(200));
    }    

    @FXML
    private void aceptar(ActionEvent event) {
        
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setContentText("Estas seguro que quieres modificar?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
             try {
                 
                 if(isValidEmailAddress(textFieldCorreo.getText())== true)
                 {
                    guardarDatos(); 
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                 }
                 else
                 {
                     Alert alert2 = new Alert(Alert.AlertType.NONE, "Ingrese email correctamente", ButtonType.OK);
                    alert2.showAndWait();
                 }
                 
                 //Cierra la ventana
                  //Cierra la ventana actual
             } catch (UnsupportedEncodingException ex) {
                 Logger.getLogger(CambiarContraseñaController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (ProtocolException ex) {
                 Logger.getLogger(CambiarContraseñaController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
                 Logger.getLogger(CambiarContraseñaController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (JSONException ex) {
                 Logger.getLogger(CambiarContraseñaController.class.getName()).log(Level.SEVERE, null, ex);
             }

        } else {
            ((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual

       }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();        
    }
    
    public void guardarDatos() throws MalformedURLException, UnsupportedEncodingException, ProtocolException, IOException, JSONException
    {
         URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarContraseñaPHP);
     Map<String,Object> params = new LinkedHashMap<>();
     params.put("correoElectronico", this.textFieldCorreo.getText().trim());
     params.put("contrasenaNueva", this.textFieldContraseñaNueva.getText().trim());
     params.put("contrasena", this.textFieldContraseñaActual.getText().trim());
     
        System.out.println("Enmail: "+textFieldCorreo.getText());
        System.out.println("Contrasena nueva: "+textFieldContraseñaNueva.getText());
        System.out.println("ContrasenaActual "+textFieldContraseñaActual.getText());
     StringBuilder postData = new StringBuilder();
     for (Map.Entry<String,Object> param : params.entrySet()) {
        if (postData.length() != 0) postData.append('&');
        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
        postData.append('=');
        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
    }

    // Convierte el array, para ser enviendo
    byte[] postDataBytes = postData.toString().getBytes("UTF-8");

    // Conectar al server
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    
    // Configura
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
    conn.setDoOutput(true);
    conn.getOutputStream().write(postDataBytes);

    // Obtiene la respuesta del servidor
    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    
    String response="";
    //System.out.println(in);
    for (int c; (c = in.read()) >= 0;)
       response=response + (char)c;
    
    //Convierte el json enviado (decodigicado)
    JSONObject obj = new JSONObject(response);
    String mensaje = obj.getString("mensaje");
    
    if(mensaje.equals("false")){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Problemas al modificar");
        alerta.setContentText("El rut ingresado no se ha podido modificar o no existe");
        alerta.showAndWait();
        }
    else{
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Mensaje");
        alerta.setContentText("Modificado con exito");
        alerta.showAndWait();
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
    }
    

