/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Valores.SingletonUsuario;
import java.io.BufferedReader;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author mati_
 */
public class OlvideContrasenaController implements Initializable {

    @FXML
    private TextField textFieldCorreo;
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
    }    

    @FXML
    private void onClick_textFieldCorreo(ActionEvent event) {
       
    }
    
    
    public void enviarEmail()throws MalformedURLException, ProtocolException, IOException, JSONException{
         URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.recuperarCorrePHP); // URL to your application
        System.out.println(url);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("email", this.textFieldCorreo.getText().trim()); // All parameters, also easy
       

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
       String mensaje = obj.getString("tipo");
       
       //System.out.println("Mensaje: "+obj);
    
     if(mensaje.contains("true")){
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Informacion");
            alerta.setContentText("Modificado con exito");
            alerta.showAndWait();
     }else{
         
         Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Informacion");
            alerta.setContentText("Ingrese un correo valido");
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

    @FXML
    private void onClick_buttonAceptar(ActionEvent event) {
         if(isValidEmailAddress(textFieldCorreo.getText().trim())){
            try {
                enviarEmail();
            } catch (ProtocolException ex) {
                Logger.getLogger(OlvideContrasenaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OlvideContrasenaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(OlvideContrasenaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Informacion");
            alerta.setContentText("No se ha podido realizar la operacion\nIngrese un correo valido");
            alerta.showAndWait();
        }
         ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void onClick_buttonCancelar(ActionEvent event) {
       ((Node)(event.getSource())).getScene().getWindow().hide();

    }

    
    
}
