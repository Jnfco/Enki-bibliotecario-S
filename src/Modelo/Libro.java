/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Controladores.ConfigurarLibrosController;
import Controladores.DetalleCopiaController;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 *
 * @author jnfco
 */
public class Libro 
{
    private String isbn;
    private String titulo;
    private String autor;
    private String edicion;
    private String anio;
    private String ncopias;
    private Button buttonDetalle;
    private Button buttonConfigurar;
    private CheckBox habilitado;
    private String estado;

    public Libro(String isbn, String titulo, String autor, String edicion,String anio,String ncopias, String estado) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.edicion = edicion;
        this.anio = anio;
        this.ncopias = ncopias;
        this.buttonDetalle = new Button("Ver detalle");
        this.buttonConfigurar = new Button("Modificar");
        this.habilitado = new CheckBox();
        this.estado = estado;
        
        this.buttonConfigurar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/enki/ConfigurarLibros.fxml"));
                    
                    Parent principalParent = fxmlLoader.load();
                    
                    ConfigurarLibrosController controller = fxmlLoader.getController();      
                    
                    //System.out.println("ISBN; "+isbn);
                    Scene scene = null;
                    scene = new Scene(principalParent);
                  
                    Stage configurarLibro = new Stage();
                    configurarLibro.setMinWidth(600);
                    configurarLibro.setMinHeight(325);
                    configurarLibro.setTitle("Modificar Libro");
                    configurarLibro.setScene(scene);
                    configurarLibro.initModality(Modality.APPLICATION_MODAL);
                    controller.setIsbn(isbn);
                    
                    //controller.
                    configurarLibro.show();
                } catch (IOException ex) {
                    Logger.getLogger(Libro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        this.buttonDetalle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader2 = new FXMLLoader();
                    fxmlLoader2.setLocation(getClass().getResource("/enki/DetalleCopia.fxml"));
                    
                    Parent principalParent = fxmlLoader2.load();
                    
                    DetalleCopiaController controller = fxmlLoader2.getController();
                    //System.out.println("ISSBN: "+isbn);
                    
                    Scene scene2 = null;
                    scene2 = new Scene(principalParent);
                    Stage detalleCopia = new Stage();
                    detalleCopia.setMinWidth(695);
                    detalleCopia.setMinHeight(500);
                    detalleCopia.setTitle("Detalle de la copia");
                    detalleCopia.setScene(scene2);
                    detalleCopia.initModality(Modality.APPLICATION_MODAL);
                    controller.setIsbn(isbn);
                    controller.setTitulo(titulo);
                    controller.setAutor(autor);
                    controller.setAño(anio);
                    controller.setEdicion(anio);
                    controller.showTabla();
                    detalleCopia.show();
                } catch (IOException ex) {
                    Logger.getLogger(Libro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        if (this.estado.equals("Habilitado")){
            this.buttonDetalle.setDisable(false);
            this.habilitado.setSelected(true);
        }
        else {
            this.buttonDetalle.setDisable(true);
            this.habilitado.setSelected(false);
        }
        
        
        //evento del checkbox
        this.habilitado.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              if(Libro.this.habilitado.isSelected()){
                  //Para habilitar
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmación");
                    alert.setHeaderText(null);
                    //alert.setHeaderText("Look, a Confirmation Dialog");
                    alert.setContentText("¿Desea habilitar este libro?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK ){
                        try {
                            if (verificarCopias2("Habilitado")){
                                try {
                                    Libro.this.buttonDetalle.setDisable(false);
                                    Libro.this.habilitado.setSelected(true);
                                    habilitarLibro();
                                    
                                } catch (UnsupportedEncodingException ex) {
                                    Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (JSONException ex) {
                                    Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Libro.this.habilitado.setSelected(true);
                            }
                            else {
                                Alert alert2 = new Alert(AlertType.ERROR);
                                alert2.setTitle("Error");
                                alert2.setContentText("El libro no puede ser habilitado!");

                                alert2.showAndWait();
                                
                                Libro.this.habilitado.setSelected(false);
                            }
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(Libro.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Libro.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JSONException ex) {
                            Logger.getLogger(Libro.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                    else if(result.get() == ButtonType.CANCEL){
                        Libro.this.habilitado.setSelected(false);
                    }
              }
              else{
                  //Para desabilitar
                   Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmacion");
                    //alert.setHeaderText("Look, a Confirmation Dialog");
                    alert.setContentText("Deseas deshabilitar este libro?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        
                       try {
                            if (verificarCopias("Deshabilitado")){
                                try {
                                Libro.this.buttonDetalle.setDisable(true);
                                Libro.this.habilitado.setSelected(false);
                                deshabilitarLibro();
                                 } catch (UnsupportedEncodingException ex) {
                                     Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                                 } catch (IOException ex) {
                                     Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                                 } catch (JSONException ex) {
                                     Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
                                 }
                                Libro.this.habilitado.setSelected(false);
                            }
                            else {
                                Alert alert2 = new Alert(AlertType.ERROR);
                                alert2.setTitle("Error");
                                alert2.setContentText("El libro tiene copias en prestamo o exhibicion!");

                                alert2.showAndWait();
                                
                                Libro.this.habilitado.setSelected(true);
                            }
                       } catch (UnsupportedEncodingException ex) {
                           Logger.getLogger(Libro.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (IOException ex) {
                           Logger.getLogger(Libro.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (JSONException ex) {
                           Logger.getLogger(Libro.class.getName()).log(Level.SEVERE, null, ex);
                       }
                        
                       
                    } else if(result.get() == ButtonType.CANCEL) {
                        Libro.this.habilitado.setSelected(true);
                    }
              }
            }


           
        });
        
        
    }

    

    public Button getButtonConfigurar() {
        return buttonConfigurar;
    }

    public void setButtonConfigurar(Button buttonConfigurar) {
        this.buttonConfigurar = buttonConfigurar;
    }

    public String getisbn() {
        return isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
       
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    
    public Button getButtonDetalle() {
        return buttonDetalle;
    }

    public void setButtonDetalle(Button buttonDetalle) {
        this.buttonDetalle = buttonDetalle;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getNcopias() {
        return ncopias;
    }

    public void setNcopias(String ncopias) {
        this.ncopias = ncopias;
    }

  
    public void habilitarLibro() throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
     URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.habilitarLibroPHP);
     Map<String,Object> params = new LinkedHashMap<>();
     params.put("isbn", this.isbn);
     
        
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
        
    
       
    public void deshabilitarLibro() throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.dehabilitarLibroPHP);
     Map<String,Object> params = new LinkedHashMap<>();
     params.put("isbn", this.isbn);
     
        
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

    public CheckBox getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(CheckBox habilitado) {
        this.habilitado = habilitado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public boolean verificarCopias(String estadoNuevo) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerCopiaPHP);
        Map<String,Object> params = new LinkedHashMap<>();
             System.out.println("ISBN; "+isbn);
          params.put("isbnlibro", isbn);
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
        List<Copia> copias = new ArrayList<Copia>();

        String mensaje = obj.getString("mensaje");
             System.out.println("mensaje "+mensaje);
        if(mensaje.equals("false")){
            return false;
        }
        
        else{
            
            Copia copia;
            JSONArray jsonArray = obj.getJSONArray("datos");
            for(int i = 0; i < jsonArray.length(); i++){
                String isbnlibro = jsonArray.getJSONObject(i).getString("isbnlibro")==null?"":jsonArray.getJSONObject(i).getString("isbnlibro");
                String codigo=jsonArray.getJSONObject(i).getString("codigo")==null?"":jsonArray.getJSONObject(i).getString("codigo");

    //            String ubicacion=jsonArray.getJSONObject(i).getString("ubicacion")==null?"":jsonArray.getJSONObject(i).getString("ubicacion");
                String estado=jsonArray.getJSONObject(i).getString("estado")==null?"":jsonArray.getJSONObject(i).getString("estado");
                String estante=jsonArray.getJSONObject(i).getString("refEstante")==null?"":jsonArray.getJSONObject(i).getString("refEstante");
                String nivel=jsonArray.getJSONObject(i).getString("refNivel")==null?"":jsonArray.getJSONObject(i).getString("refNivel");


                String ubicacion="Estante: "+estante+" Nivel: "+nivel;
                copia  = new Copia(codigo,titulo,estado,ubicacion,autor,isbn);
                //System.out.println(lector.getRut());
                copias.add(copia);
            }
            
            for (Copia c: copias){
                if (!c.getEstado().equals("Habilitado")){
                    return false;
                }
            }
        }
        
        for (Copia c: copias){
            modificarEstadoCopiaEnBaseDeDatos(estadoNuevo,c.getCodigo());
        }
        
        return true;
    }
    
    public boolean verificarCopias2(String estadoNuevo) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerCopiaPHP);
        Map<String,Object> params = new LinkedHashMap<>();
             System.out.println("ISBN; "+isbn);
          params.put("isbnlibro", isbn);
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
        List<Copia> copias = new ArrayList<Copia>();

        String mensaje = obj.getString("mensaje");
             System.out.println("mensaje "+mensaje);
        if(mensaje.equals("false")){
            return false;
        }
        
        else{
            
            Copia copia;
            JSONArray jsonArray = obj.getJSONArray("datos");
            for(int i = 0; i < jsonArray.length(); i++){
                String isbnlibro = jsonArray.getJSONObject(i).getString("isbnlibro")==null?"":jsonArray.getJSONObject(i).getString("isbnlibro");
                String codigo=jsonArray.getJSONObject(i).getString("codigo")==null?"":jsonArray.getJSONObject(i).getString("codigo");

    //            String ubicacion=jsonArray.getJSONObject(i).getString("ubicacion")==null?"":jsonArray.getJSONObject(i).getString("ubicacion");
                String estado=jsonArray.getJSONObject(i).getString("estado")==null?"":jsonArray.getJSONObject(i).getString("estado");
                String estante=jsonArray.getJSONObject(i).getString("refEstante")==null?"":jsonArray.getJSONObject(i).getString("refEstante");
                String nivel=jsonArray.getJSONObject(i).getString("refNivel")==null?"":jsonArray.getJSONObject(i).getString("refNivel");


                String ubicacion="Estante: "+estante+" Nivel: "+nivel;
                copia  = new Copia(codigo,titulo,estado,ubicacion,autor,isbn);
                //System.out.println(lector.getRut());
                copias.add(copia);
            }
            
            for (Copia c: copias){
                modificarEstadoCopiaEnBaseDeDatos(estadoNuevo,c.getCodigo());
            }
        }
        
        
        
        return true;
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

        

       // System.out.println(response);
    }
}
