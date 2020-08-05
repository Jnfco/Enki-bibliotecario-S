
package Controladores;

import static Controladores.CrearBibliotecarioController.isValidEmailAddress;
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
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.KeyEvent;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Gerardo Estrada
 */
public class CrearLectorController implements Initializable {

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
    private boolean isRutCorrecto = true;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     this.textBoxRut.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(12));
     this.textBoxNombre.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(250));
     this.textBoxApellidoPaterno.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(50));
     this.textBoxApellidoMaterno.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(50));
     this.textBoxDIreccion.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(250));
     this.textBoxTelefono.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionRut(50));
     this.textBoxEmail.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(256));
     
     
            
        
    }    

    @FXML
    private void onClick_buttonAceptar(ActionEvent event) {    
        if(isValidEmailAddress(textBoxEmail.getText()))
        {
            crearLector();
            if(this.isRutCorrecto){
                ((Node)(event.getSource())).getScene().getWindow().hide(); 
            }
            else{
                this.isRutCorrecto = true;
            }                       
        }else{
            Alert alert = new Alert(AlertType.NONE, "Ingrese email correctamente", ButtonType.OK);
            alert.showAndWait();
        }
         
    }

    @FXML
    private void onClick_buttonCancelar(ActionEvent event) {
         ((Node)(event.getSource())).getScene().getWindow().hide(); 
    }
    
    //TODO
    private void crearLector(){
        String rut = textBoxRut.getText().equals("")?"":textBoxRut.getText();
        String nombre=textBoxNombre.getText().equals("")?"":textBoxNombre.getText();
        String apellidoPat=textBoxApellidoPaterno.getText().equals("")?"":textBoxApellidoPaterno.getText();
        String apellidoMat=textBoxApellidoMaterno.getText().equals("")?"":textBoxApellidoMaterno.getText();
        String direccion = textBoxDIreccion.getText().equals("")?"":textBoxDIreccion.getText();
        String email = textBoxEmail.getText().equals("")?"":textBoxEmail.getText();
        String telefono = textBoxTelefono.getText().equals("")?"":textBoxTelefono.getText();
        
                      
        if(Validaciones.validaRut(rut)){
            
            try {
                this.crearLectorEnBaseDeDatos(rut, nombre, apellidoPat, apellidoMat, direccion, email, telefono);
            } catch (UnsupportedEncodingException ex) {
                System.out.println(ex);
               // Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println(ex);
                //Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
            }


        }else{
            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText("No se puede realizar esta operación.");
            alerta.setContentText("EL campo rut está incorrecto, ingrese un rut válido.");
            alerta.showAndWait();
            this.isRutCorrecto = false;
            this.textBoxRut.setText("");
        }    
    }
    
    
    
    //TODO: Decodificar JSON
    /**
     * 
     * @param rut
     * @param nombre
     * @param apellidoPat
     * @param apellidoMat
     * @param direccion
     * @param email
     * @param telefono 
     */
    
    public void crearLectorEnBaseDeDatos(String rut,String nombre,String apellidoPat,
                                         String apellidoMat,String direccion,String email,
                                         String telefono) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
    
    URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.crearLectorPHP);
    Map<String,Object> params = new LinkedHashMap<>();
    params.put("rut", rut);
    params.put("nombre", nombre);
    params.put("apellidoPaterno", apellidoPat);
    params.put("apellidoMateno", apellidoMat);
    params.put("direccion", direccion);
    params.put("telefono", telefono);
    params.put("correoElectronico", email);
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
    System.out.println(in);
    for (int c; (c = in.read()) >= 0;)
       response=response + (char)c;
    
    //Convierte el json enviado (decodigicado)
    JSONObject obj = new JSONObject(response);
    String mensaje = obj.getString("mensaje");
    
    Alert alerta = new Alert(AlertType.INFORMATION);
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

