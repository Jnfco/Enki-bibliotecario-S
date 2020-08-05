
package Controladores;

import static Controladores.CrearBibliotecarioController.isValidEmailAddress;
import Modelo.Bibliotecario;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javafx.scene.paint.Color;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Mn_go
 */
public class ModificarBibliotecarioController implements Initializable{

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
    @FXML
    private TextField textBoxNombreContactoEmergencia;
    @FXML
    private TextField textBoxTelefonoContactoEmergencia;
    @FXML
    private TextField textBoxContraseña;

    @FXML
    private void onClick_buttonAceptar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setContentText("Estás seguro que quieres modificar?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            if(isValidEmailAddress(textBoxEmail.getText().equals("")?"":textBoxEmail.getText())){
                try {
                    guardarDatos();
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ModificarBibliotecarioController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ModificarBibliotecarioController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSONException ex) {
                    Logger.getLogger(ModificarBibliotecarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Cierra la ventana
                ((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual
            }
            else{
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Advertencia");
                alerta.setHeaderText("No se ha podido realizar la operación.");
                alerta.setContentText("El correo electrónico no es válido.");
                alerta.showAndWait();
                this.textBoxEmail.setText(""); 
            }            
        } else {
            ((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual

       }
    }

    @FXML
    private void onClick_buttonCancelar(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual
    }
    
     @Override
     public void initialize(URL location, ResourceBundle resources) {
        this.textBoxRut.setEditable(false);
     this.textBoxRut.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionRut(9));
     this.textBoxNombre.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(250));
     this.textBoxApellidoPaterno.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(50));
     this.textBoxApellidoMaterno.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(50));
     this.textBoxDIreccion.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(250));
     this.textBoxTelefono.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionRut(50));
     this.textBoxEmail.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(256)); 
     this.textBoxNombreContactoEmergencia.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(256));
     this.textBoxTelefonoContactoEmergencia.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(256));
     this.textBoxContraseña.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(256));
     
    }
    
        /**
     * 
     * @param Rrut Rut a consultar de la base de datos. 
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws JSONException 
     */
    private void obtenerBibliotecario(String Rrut) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
     URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.BibliotecarioPHP);
     Map<String,Object> params = new LinkedHashMap<>();
     params.put("rut", Rrut.trim()); // All parameters, also easy
        
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
        //System.out.println("no hay nada");
    }else{
        List<Bibliotecario> bibliotecarios = new ArrayList<Bibliotecario>();
        Bibliotecario bibliotecario;
        JSONArray jsonArray = obj.getJSONArray("datos");
            String rut = jsonArray.getJSONObject(0).getString("rut")==null?"":jsonArray.getJSONObject(0).getString("rut");
            String nombre=jsonArray.getJSONObject(0).getString("nombre")==null?"":jsonArray.getJSONObject(0).getString("nombre");
            String apaterno=jsonArray.getJSONObject(0).getString("apellidoPaterno")==null?"":jsonArray.getJSONObject(0).getString("apellidoPaterno");
            String amaterno=jsonArray.getJSONObject(0).getString("apellidoMaterno")==null?"":jsonArray.getJSONObject(0).getString("apellidoMaterno");
            String email = jsonArray.getJSONObject(0).getString("correoElectronico")==null?"":jsonArray.getJSONObject(0).getString("correoElectronico");
            String direc = jsonArray.getJSONObject(0).getString("direccion")==null?"":jsonArray.getJSONObject(0).getString("direccion");
            String telefono = jsonArray.getJSONObject(0).getString("telefono")==null?"":jsonArray.getJSONObject(0).getString("telefono");
            String nombreEmergencia = jsonArray.getJSONObject(0).getString("contactoEmergenciaNombre")==null?"":jsonArray.getJSONObject(0).getString("contactoEmergenciaNombre");
            String telefonoEmergencia = jsonArray.getJSONObject(0).getString("contactoEmergenciaTelefono")==null?"":jsonArray.getJSONObject(0).getString("contactoEmergenciaTelefono");
            String contrasena = jsonArray.getJSONObject(0).getString("contrasena")==null?"":jsonArray.getJSONObject(0).getString("contrasena");
                                         
            //Edita
            
            this.textBoxRut.setText(rut);
            this.textBoxNombre.setText(nombre);
            this.textBoxApellidoPaterno.setText(apaterno);
            this.textBoxApellidoMaterno.setText(amaterno);
            this.textBoxEmail.setText(email);
            this.textBoxTelefono.setText(telefono);
            this.textBoxDIreccion.setText(direc);
            this.textBoxNombreContactoEmergencia.setText(nombreEmergencia);
            this.textBoxTelefonoContactoEmergencia.setText(telefonoEmergencia);
            this.textBoxContraseña.setText(contrasena);
            
            
            /*
            if(estado.equals("true")){
                this.labelEstado.setText("Habilitado");
                this.labelEstado.setTextFill(Color.BLUE);
            }else{
                this.labelEstado.setText("Desabilitado");
                this.labelEstado.setTextFill(Color.RED); 
            }    
            */
    }
        
    }
    
    /**
     * 
     * @param rut a consultar en la base de datos.
     */
    public void setRut(String rut) {
        try {
            obtenerBibliotecario(rut);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ModificarBibliotecarioController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ModificarBibliotecarioController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ModificarBibliotecarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardarDatos() throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
     URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarBibliotecarioPHP);
     Map<String,Object> params = new LinkedHashMap<>();
     params.put("rut", this.textBoxRut.getText().trim());
     params.put("nombre", this.textBoxNombre.getText().trim());
     params.put("apellidoP", this.textBoxApellidoPaterno.getText().trim());
     params.put("apellidoM", this.textBoxApellidoMaterno.getText().trim());
     params.put("correo", this.textBoxEmail.getText().trim());
     params.put("direccion", this.textBoxDIreccion.getText().trim());
     params.put("telefono", this.textBoxTelefono.getText().trim());
     params.put("nombreEmergencia", this.textBoxNombreContactoEmergencia.getText().trim());
     params.put("telefonoEmergencia", this.textBoxTelefonoContactoEmergencia.getText().trim());
     params.put("contrasena", this.textBoxContraseña.getText().trim());
     
        
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
        alerta.setContentText("Modificado con éxito");
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
