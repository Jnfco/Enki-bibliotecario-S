
package Modelo;


import Controladores.ConfigurarNivelController;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author jnfco
 */
public class Copia {
    
    private String codigo;
    private String titulo;
    private String estado;
    private String ubicacion;
    private String autor;
    private String isbn;
    private ComboBox estadoCopia;
    private Button configurarUbicacionButton;

    public Copia(String codigo, String titulo, String estado, String ubicacion, String autor,String isbn) {
      
            this.codigo = codigo;
            this.titulo = titulo;
            this.estado = estado;
            this.ubicacion = ubicacion;
            this.autor = autor;
            this.isbn = isbn;
            //System.out.println("Estado de copia en copia: "+estado);
            //System.out.println("codigo copia: "+codigo);
            estadoCopia = new ComboBox<String>();
            estadoCopia.getItems().addAll("En exhibicion","Habilitado","Deshabilitado","Prestado");
            if(estado.equals("En exhibicion"))
            {
                
                estadoCopia.getSelectionModel().select("En exhibicion");
            }
            
            if(estado.equals("Habilitado"))
            {
                estadoCopia.getSelectionModel().select("Habilitado");
            }
                        
            if(estado.equals("Deshabilitado"))
            {
                estadoCopia.getSelectionModel().select("Deshabilitado");
            }
            
            if(estado.equals("Prestado"))
            {
                estadoCopia.getSelectionModel().select("Prestado");
            }
            
           
            this.estadoCopia.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event)
                {
                                       
                    modificarEstadoCopia(estadoCopia.getSelectionModel().getSelectedItem().toString(),codigo);
                }

               
            });
            
            this.configurarUbicacionButton = new Button("Click aquí");
            this.configurarUbicacionButton.setMaxWidth(Double.MAX_VALUE);
            this.configurarUbicacionButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    
                     try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/enki/configurarNivel.fxml"));
                    
                    Parent principalParent = fxmlLoader.load();
                    
                    ConfigurarNivelController controller = fxmlLoader.getController();
                    controller.setCodigoCopia(codigo);
                    controller.setCodigo(codigo);
                    //controller.setCantidadNiveles(cantidadniveles, codigo);
                    
                    Scene scene = null;
                    
                    scene = new Scene(principalParent);
                    Stage configurarEstante = new Stage();
                    configurarEstante.setMinWidth(436);
                    configurarEstante.setMinHeight(207);
                    configurarEstante.setMaxWidth(600);
                    configurarEstante.setMaxHeight(300);
                    
                    configurarEstante.setTitle("Modificar Ubicacion");
                    configurarEstante.setScene(scene);
                    configurarEstante.initModality(Modality.APPLICATION_MODAL);
                    configurarEstante.showAndWait();
                } catch (IOException ex) {
                    Logger.getLogger(Estante.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
                }
            });
         
        
    }
    
     public void modificarEstadoCopia(String estado,String codigo) 
     {
         
         estado=estado.equals("")?"":estado;
         codigo=codigo.equals("")?"":codigo;
            
            try {
                this.modificarEstadoCopiaEnBaseDeDatos(estado,codigo);
            } catch (UnsupportedEncodingException ex) {
                System.out.println(ex);
               // Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println(ex);
                //Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(Copia.class.getName()).log(Level.SEVERE, null, ex);
            }
  }
     
     public void modificarEstadoCopiaEnBaseDeDatos(String estado,String codigo) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
    
    URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarEstadoCopiaPHP);
    Map<String,Object> params = new LinkedHashMap<>();
         //System.out.println("Codigo antes de mandar:"+codigo);
         //System.out.println("Estado antes de mandar:"+estado);
    params.put("codigo", codigo);
    params.put("estado", estado);
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
    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
    
    String response="";
    //System.out.println(in);
    for (int c; (c = in.read()) >= 0;)
       response=response + (char)c;
    
    //Convierte el json enviado (decodigicado)
    //System.out.println(response);
    //System.out.println("ISBN; "+isbn);
    JSONObject obj = new JSONObject(response);
    String mensaje = obj.getString("mensaje");
    
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Modificar estado");
    alert.setHeaderText(null);
    alert.setContentText("Estado modificado exitosamente");
    alert.showAndWait();

   // System.out.println(response);
}

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public ComboBox getEstadoCopia() {
        return estadoCopia;
    }

    public void setEstadoCopia(ComboBox estadoCopia) {
        this.estadoCopia = estadoCopia;
    }

    public Button getConfigurarUbicacionButton() {
        return configurarUbicacionButton;
    }

    public void setConfigurarUbicacionButton(Button configurarUbicacionButton) {
        this.configurarUbicacionButton = configurarUbicacionButton;
    }
    
    
    
}
