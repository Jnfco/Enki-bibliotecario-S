package Controladores;

import Modelo.Lector;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
 * @author Gerardo
 */
public class ModificarLectorController implements Initializable{
    
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
    private Label labelEstado;
    @FXML
    private TextField textBoxEmail;
    @FXML
    private TextField textBoxTelefono;
    @FXML
    private TextField textBoxDIreccion;
    @FXML
    private TextArea textBoxComentario;
    
    @FXML
    private void onClick_buttonAceptar(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setContentText("Estás seguro que quieres modificar?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                if(Validaciones.isValidEmailAddress(textBoxEmail.getText())){
                  guardarDatos();
                  ((Node)(event.getSource())).getScene().getWindow().hide();
                }else{
                     alert = new Alert(AlertType.NONE, "Ingrese email correctamente", ButtonType.OK);
                     alert.showAndWait();
                }            
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ModificarLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ModificarLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(ModificarLectorController.class.getName()).log(Level.SEVERE, null, ex);
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
     this.textBoxEmail.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(200));       
     
    }
    
    /**
     * 
     * @param Rrut Rut a consultar de la base de datos. 
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws JSONException 
     */
    private void obtenerLector(String Rrut) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
     URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.datosLectorPHP);
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
        List<Lector> lectores = new ArrayList<Lector>();
        Lector lector;
        JSONArray jsonArray = obj.getJSONArray("datos");
            String rut = jsonArray.getJSONObject(0).getString("rut")==null?"":jsonArray.getJSONObject(0).getString("rut");
            String nombre=jsonArray.getJSONObject(0).getString("nombre")==null?"":jsonArray.getJSONObject(0).getString("nombre");
            String apaterno=jsonArray.getJSONObject(0).getString("apellidoPaterno")==null?"":jsonArray.getJSONObject(0).getString("apellidoPaterno");
            String amaterno=jsonArray.getJSONObject(0).getString("apellidoMaterno")==null?"":jsonArray.getJSONObject(0).getString("apellidoMaterno");
            String email = jsonArray.getJSONObject(0).getString("correoElectronico")==null?"":jsonArray.getJSONObject(0).getString("correoElectronico");
            String obs = jsonArray.getJSONObject(0).getString("observacion")==null?"":jsonArray.getJSONObject(0).getString("observacion");
            String direc = jsonArray.getJSONObject(0).getString("direccion")==null?"":jsonArray.getJSONObject(0).getString("direccion");
            String telefono = jsonArray.getJSONObject(0).getString("telefono")==null?"":jsonArray.getJSONObject(0).getString("telefono");
            String estado = jsonArray.getJSONObject(0).getString("estado")==null?"":jsonArray.getJSONObject(0).getString("estado");
            
            System.out.println(rut);
            //Editar
            this.textBoxRut.setText(rut);
            this.textBoxNombre.setText(nombre);
            this.textBoxApellidoPaterno.setText(apaterno);
            this.textBoxApellidoMaterno.setText(amaterno);
            this.textBoxEmail.setText(email);
            this.textBoxComentario.setText(obs);
            this.textBoxDIreccion.setText(direc);
            this.textBoxTelefono.setText(telefono);
            
            if(estado.equals("true")){
                this.labelEstado.setText("Habilitado");
                this.labelEstado.setTextFill(Color.BLUE);
            }else{
                this.labelEstado.setText("Desabilitado");
                this.labelEstado.setTextFill(Color.RED); 
            }          
    }
        
    }
    
    /**
     * 
     * @param rut a consultar en la base de datos.
     */
    public void setRut(String rut) {
        try {
            obtenerLector(rut);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ModificarLectorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ModificarLectorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ModificarLectorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void guardarDatos() throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
     URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarLectorPHP);
     Map<String,Object> params = new LinkedHashMap<>();
     params.put("rut", this.textBoxRut.getText().trim());
     params.put("nombre", this.textBoxNombre.getText().trim());
     params.put("apellidoP", this.textBoxApellidoPaterno.getText().trim());
     params.put("apellidoM", this.textBoxApellidoMaterno.getText().trim());
     params.put("correo", this.textBoxEmail.getText().trim());
     params.put("direccion", this.textBoxDIreccion.getText().trim());
     params.put("telefono", this.textBoxTelefono.getText().trim());
     params.put("comentario", this.textBoxComentario.getText().trim());
        
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
    
    JSONObject obj = new JSONObject(response);
    String mensaje = obj.getString("mensaje");
    
    if(mensaje.equals("false")){
       Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    alerta.setTitle("Problemas al modificar");
    alerta.setContentText("El rut ingresado no se ha podido modifiacar o no existe");
    alerta.showAndWait();
    }else{
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    alerta.setTitle("Mensaje");
    alerta.setContentText("Modificado con éxito");
    alerta.showAndWait();
    }

    }
    
}
