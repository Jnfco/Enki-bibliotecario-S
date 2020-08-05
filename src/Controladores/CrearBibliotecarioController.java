
package Controladores;

import Valores.Validaciones;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
public class CrearBibliotecarioController implements Initializable {

    @FXML
    private Button buttonAceptar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private TextField textBoxRut;
    @FXML
    private TextField textBoxNombre;
    @FXML
    private TextField textBoxApellidoPaterno;
    @FXML
    private TextField textBoxApellidoMaterno;
    @FXML
    private TextField textBoxEmail;
    @FXML
    private TextField textBoxTelefono;
    @FXML
    private TextField textBoxDIreccion;
    private TextField textBoxContactoEmergencia;
    @FXML
    private TextField textBoxContraseña;
    @FXML
    private TextField textBoxNombreContactoEmergencia;
    @FXML
    private TextField textBoxTelefonoContactoEmergencia;
    private boolean isRutCorrecto = true;
    private boolean isCorreoCorrecto = true;
     @Override
    public void initialize(URL url, ResourceBundle rb) {
     this.textBoxRut.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(12));
     this.textBoxNombre.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(250));
     this.textBoxApellidoPaterno.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(50));
     this.textBoxApellidoMaterno.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(50));
     this.textBoxDIreccion.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(250));
     this.textBoxTelefono.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionRut(50));
     this.textBoxEmail.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(256));
     this.textBoxTelefonoContactoEmergencia.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionRut(50));
        
        
    }    

    @FXML
    private void onClick_buttonAceptar(ActionEvent event) {        
        crearBibliotecario();
        if(this.isCorreoCorrecto && this.isRutCorrecto){
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        else{
            this.isCorreoCorrecto = true;
            this.isRutCorrecto = true;
        }
    }

    @FXML
    private void onClick_buttonCancelar(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide(); 
         
    }
    
    //TODO
    private void crearBibliotecario(){
        String rut = textBoxRut.getText().equals("")?"":textBoxRut.getText();
        String nombre=textBoxNombre.getText().equals("")?"":textBoxNombre.getText();
        String apellidoPat=textBoxApellidoPaterno.getText().equals("")?"":textBoxApellidoPaterno.getText();
        String apellidoMat=textBoxApellidoMaterno.getText().equals("")?"":textBoxApellidoMaterno.getText();
        String direccion = textBoxDIreccion.getText().equals("")?"":textBoxDIreccion.getText();
        String email = textBoxEmail.getText().equals("")?"":textBoxEmail.getText();
        String telefono = textBoxTelefono.getText().equals("")?"":textBoxTelefono.getText();
        String contactoEmergenciaNombre = textBoxNombreContactoEmergencia.getText().equals("")?"":textBoxNombreContactoEmergencia.getText();
        String contactoEmergenciaTelefono = textBoxTelefonoContactoEmergencia.getText().equals("")?"":textBoxTelefonoContactoEmergencia.getText();
        String contrasena = textBoxContraseña.getText().equals("")?"":textBoxContraseña.getText();
        
        if(Validaciones.validaRut(rut)){
            
            if(isValidEmailAddress(email)){
                try {
                   this.crearBibliotecarioEnBaseDeDatos(rut, nombre, apellidoPat, apellidoMat, direccion, email, telefono,contactoEmergenciaNombre,contactoEmergenciaTelefono,contrasena);
               } catch (UnsupportedEncodingException ex) {
                   System.out.println(ex);
                  // Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
               } catch (IOException ex) {
                   System.out.println(ex);
                   //Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
               } catch (JSONException ex) {
                   Logger.getLogger(CrearBibliotecarioController.class.getName()).log(Level.SEVERE, null, ex);
               }
             }
            else{
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Advertencia");
                alerta.setHeaderText("No se ha podido realizar la operación.");
                alerta.setContentText("El correo electrónico no es válido.");
                alerta.showAndWait();
                this.isCorreoCorrecto = false;
                this.textBoxEmail.setText("");                
            }          
            
        }else{
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText("El RUT no es valido para realizar la operación.");
            alerta.setContentText("EL campo rut esta vacío, ingrese un rut válido.");
            alerta.showAndWait();
            this.isRutCorrecto = false;
            this.textBoxRut.setText("");
        }  
    }
    
    /**
     * 
     * @param rut
     * @param nombre
     * @param apellidoPat
     * @param apellidoMat
     * @param direccion
     * @param email
     * @param telefono 
     * @param contactoEmergenciaNombre 
     * @param contactoEmergenciaTelefono 
     * @param contrasena 
     */
    
    public void crearBibliotecarioEnBaseDeDatos(String rut,String nombre,String apellidoPat,
                                         String apellidoMat,String direccion,String email,
                                         String telefono,String contactoEmergenciaNombre,String contactoEmergenciaTelefono,String contrasena) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
    
    URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.crearBibliotecarioPHP);
    Map<String,Object> params = new LinkedHashMap<>();
    params.put("rut", rut);
    params.put("nombre", nombre);
    params.put("apellidoPaterno", apellidoPat);
    params.put("apellidoMateno", apellidoMat);
    params.put("direccion", direccion);
    params.put("telefono", telefono);
    params.put("correoElectronico", email);
    params.put("contactoEmergenciaNombre", contactoEmergenciaNombre);
    params.put("contactoEmergenciaTelefono", contactoEmergenciaTelefono);
    params.put("contrasena", contrasena);
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
    
    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    alerta.setTitle("Mensaje");
    alerta.setContentText(mensaje);
    alerta.showAndWait();
   // System.out.println(response);
         
       
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
