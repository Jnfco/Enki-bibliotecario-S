
package Modelo;
import Controladores.ListaHistorialLectorController;
import Controladores.ModificarLectorController;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Gerardo
 */
public class Lector {
    String rut;
    String nombre;
    String apellidoP;
    String apellidoM;
    Button buttonConfiguraciones;
    Button buttonVerHistorial;
    CheckBox habilitado;
      
    public Lector(String rut, String nombre, String apellidoP, String apellidoM,String habilitado) {
        this.rut = rut;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.buttonConfiguraciones = new Button("Modificar");
        this.buttonVerHistorial = new Button("Ver historial");
        this.habilitado = new CheckBox();
        
        //Accion del botor modificar
        this.buttonConfiguraciones.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {     
                    
                    //Permite pasarle la informacion a la otra ventana
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/enki/ModificarLector.fxml"));
                    Parent root = loader.load();
                    ModificarLectorController m = loader.getController();
                   
                    m.setRut(Lector.this.rut);
                    
                    
                    Scene scene = new Scene(root);
                    
                    Stage stage = new Stage();
                    stage.setMinWidth(600);
                    stage.setMinHeight(449);
                    stage.setTitle("Modificar Lector");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    //((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual
                    stage.show();
                    
                    root=null;
                    loader=null;
                    scene=null;
                    stage=null;
                } catch (IOException ex) {
                    Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                    
            }
        });
        
        this.buttonVerHistorial.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                try {     
                    
                    //Permite pasarle la informacion a la otra ventana
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/enki/ListaHistorialLector.fxml"));
                    Parent root = loader.load();
                    ListaHistorialLectorController m = loader.getController();
                    System.out.println("Rut:"+rut);
                    m.setRut(rut);
                    
                    
                    Scene scene = new Scene(root);
                    
                    Stage stage = new Stage();
                    stage.setMinWidth(600);
                    stage.setMinHeight(449);
                    stage.setTitle("Historial Lector");
                    stage.setScene(scene);
                    //((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual
                    stage.show();
                    
                    root=null;
                    loader=null;
                    scene=null;
                    stage=null;
                } catch (IOException ex) {
                    Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        //this.habilitado.setSelected(true);
        //Set el checkbox, con la informacion de la BD
        if(habilitado.equals("true")){
            this.habilitado.setSelected(true);
        }else{
            this.habilitado.setSelected(false);
        }
        //evento del checkbox
        this.habilitado.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              if(Lector.this.habilitado.isSelected()){
                  //Para habilitar
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmación");
                    alert.setHeaderText(null);
                    //alert.setHeaderText("Look, a Confirmation Dialog");
                    alert.setContentText("¿Desea habilitar este lector?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        try {
                            Lector.this.habilitado.setSelected(true);
                            habilitarLector();
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JSONException ex) {
                            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                    else if(result.get() == ButtonType.CANCEL){
                        try
                        {
                            Lector.this.habilitado.setSelected(false);
                            deshabilitarLector();
                        } catch (UnsupportedEncodingException ex)
                        {
                            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex)
                        {
                            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JSONException ex)
                        {
                            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
              }
              else{
                  //Para desabilitar
                   Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmacion");
                    //alert.setHeaderText("Look, a Confirmation Dialog");
                    alert.setContentText("Deseas deshabilitar este lector?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                       try {
                           Lector.this.habilitado.setSelected(false);
                           deshabilitarLector();
                       } catch (UnsupportedEncodingException ex) {
                           Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (IOException ex) {
                           Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (JSONException ex) {
                           Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                       }
                    } else if(result.get() == ButtonType.CANCEL) {
                        
                       try
                       {
                           Lector.this.habilitado.setSelected(true);
                           habilitarLector();
                       } catch (UnsupportedEncodingException ex)
                       {
                           Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (IOException ex)
                       {
                           Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (JSONException ex)
                       {
                           Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                       }
                    }
              }
            }
        });
        
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public Button getButtonConfiguraciones() {
        return buttonConfiguraciones;
    }

    public void setButtonConfiguraciones(Button buttonConfiguraciones) {
        this.buttonConfiguraciones = buttonConfiguraciones;
    }

    public CheckBox getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(CheckBox habilitado) {
        this.habilitado = habilitado;
    }
    
    public void habilitarLector() throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
     URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.habilitarLectorPHP);
     Map<String,Object> params = new LinkedHashMap<>();
     params.put("rut", this.rut);
     
        
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
    //alerta.setTitle("Mensaje");
    alerta.setContentText(mensaje);
    alerta.showAndWait();

    }
        
    
       
    public void deshabilitarLector() throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.dehabilitarLectorPHP);
     Map<String,Object> params = new LinkedHashMap<>();
     params.put("rut", this.rut);
     
        
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
    //alerta.setTitle("Mensaje");
    alerta.setContentText(mensaje);
    alerta.showAndWait();
        
    }

    public Button getButtonVerHistorial() {
        return buttonVerHistorial;
    }

    public void setButtonVerHistorial(Button buttonVerHistorial) {
        this.buttonVerHistorial = buttonVerHistorial;
    }
    
}
