/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Libro;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author jnfco
 */
public class ListaLibrosController implements Initializable {

    @FXML
    private BorderPane libro_View;
    @FXML
    private Button buttonActualizarTabla;
    @FXML
    private TableView<Libro> tableViewListaLibros;
    @FXML
    private TableColumn<Libro,String> tableColumnISBN;
    @FXML
    private TableColumn<Libro,String> tableColumnTitulo;
    @FXML
    private TableColumn<Libro,String> tableColumnAutor;
    @FXML
    private TableColumn<Libro,String> tableColumnEdicion;
    @FXML
    private TableColumn<Libro,String> tableColumnAnio;
    @FXML
    private TableColumn<Libro,Button> tableColumnConfigurar;
    @FXML
    private TableColumn<Libro,Button> tableColumnDetalle;
    @FXML
    private Label labelTimer;
    
    private int tiempoMaximo = 60;
    private int tiempo;
    @FXML
    private TableColumn<Libro,String> tableColumnNCopias;
   
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        /*
        ObservableList<Libro> libros=FXCollections.observableArrayList(new Libro("19KOSPA","El principito","Saint-Exupéry","Tercera",3));
        //tableColumnConfigurar.setCellValueFactory(new PropertyValueFactory<Libro,String>("buttonConfigurar"));
        tableColumnISBN.setCellValueFactory(new PropertyValueFactory<Libro,String>("ISBN"));
        tableColumnTitulo.setCellValueFactory(new PropertyValueFactory<Libro,String>("titulo"));
        tableColumnAutor.setCellValueFactory(new PropertyValueFactory<Libro,String>("autor"));
        tableColumnNCopias.setCellValueFactory(new PropertyValueFactory<Libro,Integer>("NCopias"));
        tableColumnEdicion.setCellValueFactory(new PropertyValueFactory<Libro,String>("edicion"));
        
        tableColumnConfigurar.setCellValueFactory(new PropertyValueFactory<Libro,String>("buttonConfigurar"));
        tableColumnDetalle.setCellValueFactory(new PropertyValueFactory<Libro,String>("buttonDetalle"));
        
        tableViewListaLibros.setItems(libros);*/
        
        try {
            refrescarTabla();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.labelTimer.setText(tiempoMaximo+"");
        this.tiempo=tiempoMaximo;
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {
              if(tiempo==0){
                  tiempo=15;
                  
                   try {
                        refrescarTabla();
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ProtocolException ex) {
                        Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JSONException ex) {
                        Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  
              }
                    else{
                  --tiempo;
                  
              }
                      labelTimer.setText(tiempo+"");

          }
      }));
      fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
      fiveSecondsWonder.play();
      
    }    
    
    private void refrescarTabla() throws MalformedURLException, UnsupportedEncodingException, ProtocolException, IOException, JSONException{
        
         URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerLibroPHP);
    Map<String,Object> params = new LinkedHashMap<>();
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
    
        System.out.println("response: " + response);
    //Convierte el json enviado (decodigicado)
    JSONObject obj = new JSONObject(response);
         
    String mensaje = obj.getString("mensaje");
         //System.out.println("mensaje "+mensaje);
    if(mensaje.equals("false")){
        //System.out.println("no hay nada");
    }else{
        List<Libro> libros = new ArrayList<Libro>();
        Libro libro;
        JSONArray jsonArray = obj.getJSONArray("datos");
        for(int i = 0; i < jsonArray.length(); i++){
            String isbn = jsonArray.getJSONObject(i).getString("isbn")==null?"":jsonArray.getJSONObject(i).getString("isbn");
            String titulo=jsonArray.getJSONObject(i).getString("titulo")==null?"":jsonArray.getJSONObject(i).getString("titulo");
            
            String autor=jsonArray.getJSONObject(i).getString("autor")==null?"":jsonArray.getJSONObject(i).getString("autor");
            String edicion=jsonArray.getJSONObject(i).getString("edicion")==null?"":jsonArray.getJSONObject(i).getString("edicion");
            String anio=jsonArray.getJSONObject(i).getString("anio")==null?"":jsonArray.getJSONObject(i).getString("anio");
            String ncopias=jsonArray.getJSONObject(i).getString("ncopias")==null?"":jsonArray.getJSONObject(i).getString("ncopias");
            String estado=jsonArray.getJSONObject(i).getString("estado")==null?"":jsonArray.getJSONObject(i).getString("estado");

            
            libro  = new Libro(isbn,titulo,autor,edicion,anio,ncopias,estado);
            //System.out.println(lector.getRut());
            libros.add(libro);
        }
        setTabla(libros);
        //System.out.println(jsonMainArr.toString());
    }
    
   
     }    
     
    private void setTabla(List<Libro> libros){
        ObservableList<Libro> olibros=FXCollections.observableArrayList(libros);
        tableColumnISBN.setCellValueFactory(new PropertyValueFactory<Libro,String>("isbn"));
        tableColumnTitulo.setCellValueFactory(new PropertyValueFactory<Libro,String>("titulo"));
        tableColumnAutor.setCellValueFactory(new PropertyValueFactory<Libro,String>("autor"));
        tableColumnEdicion.setCellValueFactory(new PropertyValueFactory<Libro,String>("habilitado"));
        tableColumnAnio.setCellValueFactory(new PropertyValueFactory<Libro,String>("anio"));
        tableColumnNCopias.setCellValueFactory(new PropertyValueFactory<Libro,String>("ncopias"));
        tableColumnConfigurar.setCellValueFactory(new PropertyValueFactory<Libro,Button>("buttonConfigurar"));    
        tableColumnDetalle.setCellValueFactory(new PropertyValueFactory<Libro,Button>("buttonDetalle"));
        
        this.tableViewListaLibros.setItems(olibros);
    }
        
    @FXML
    private void onClick_buttonActualizarTabla(ActionEvent event){
        try {
            refrescarTabla();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
