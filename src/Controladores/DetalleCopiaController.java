/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Copia;
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
import java.util.Random;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author jnfco
 */
public class DetalleCopiaController implements Initializable {

    @FXML
    private BorderPane lector_View;
    @FXML
    private TableColumn<Copia,String> tableColumnTitulo;
    @FXML
    private TableColumn<Copia,String> tableColumnAutor;
    @FXML
    private Button buttonAgregarCopia;
    @FXML
    private TableView<Copia> tableViewListaCopias;
    @FXML
    private TableColumn<Copia,String> tableColumnICodigo;
    @FXML
    private TableColumn<Copia,ComboBox> tableColumnEstado;
    @FXML
    private TableColumn<Copia,String> tableColumnUbicacion;
    @FXML
    private TableColumn<Copia,Button> tableColumnModificar;
    @FXML
    private Label labelTimer;
    
    @FXML
    private Button buttonActaulizarTabla;
    
    private int tiempoMaximo = 60;
    private int tiempo;

    private String isbn;
    private String autor;
    private String titulo;
    private String año;
    private String edicion;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
            this.labelTimer.setText(tiempoMaximo+"");
            this.tiempo=tiempoMaximo;
            Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                
                @Override
                public void handle(ActionEvent event) {
                    if(tiempo==0){
                        tiempo=tiempoMaximo;
                        
                        try {
                            refrescarTabla();
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ProtocolException ex) {
                            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JSONException ex) {
                            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
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

    @FXML
    private void agregarCopia(ActionEvent event) {
        
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/enki/AgregarCopia.fxml"));
            Scene scene = null;
            
            Parent principalParent = fxmlLoader.load();
            
            AgregarCopiaController controller= fxmlLoader.getController();
            controller.setIsbn(isbn);
            controller.setTitulo(titulo);
            controller.setAño(año);
            controller.setEdicion(edicion);
          
            
            
           
            scene = new Scene(principalParent);
            Stage agregarCopia = new Stage();
            agregarCopia.setMinWidth(600);
            agregarCopia.setMinHeight(367);
            agregarCopia.setTitle("Agregar Copia");
            agregarCopia.setScene(scene);
            agregarCopia.initModality(Modality.APPLICATION_MODAL);
            agregarCopia.showAndWait();
            refrescarTabla();
        } catch (IOException ex) {
        Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
    
    
    
     private void refrescarTabla() throws MalformedURLException, UnsupportedEncodingException, ProtocolException, IOException, JSONException{
        
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
         
    String mensaje = obj.getString("mensaje");
         System.out.println("mensaje "+mensaje);
    if(mensaje.equals("false")){
        //System.out.println("no hay nada");
    }else{
        List<Copia> copias = new ArrayList<Copia>();
        Copia copia;
        JSONArray jsonArray = obj.getJSONArray("datos");
        for(int i = 0; i < jsonArray.length(); i++){
            String isbnlibro = jsonArray.getJSONObject(i).getString("isbnlibro")==null?"":jsonArray.getJSONObject(i).getString("isbnlibro");
            String codigo=jsonArray.getJSONObject(i).getString("codigo")==null?"":jsonArray.getJSONObject(i).getString("codigo");
            
//            String ubicacion=jsonArray.getJSONObject(i).getString("ubicacion")==null?"":jsonArray.getJSONObject(i).getString("ubicacion");
            String estado=jsonArray.getJSONObject(i).getString("estado")==null?"":jsonArray.getJSONObject(i).getString("estado");
            String estante=jsonArray.getJSONObject(i).getString("refEstante")==null?"":jsonArray.getJSONObject(i).getString("refEstante");
            String nivel=jsonArray.getJSONObject(i).getString("refNivel")==null?"":jsonArray.getJSONObject(i).getString("refNivel");
            
            System.out.println("Estado de copia: "+estado);
            
            String ubicacion="Estante: "+estante+" Nivel: "+nivel;
            copia  = new Copia(codigo,titulo,estado,ubicacion,autor,isbn);
            //System.out.println(lector.getRut());
            copias.add(copia);
        }
        setTabla(copias);
        //System.out.println(jsonMainArr.toString());
    }
    
    } 
     
     private void setTabla(List<Copia> copias){
        ObservableList<Copia> ocopias=FXCollections.observableArrayList(copias);
        tableColumnICodigo.setCellValueFactory(new PropertyValueFactory<Copia,String>("codigo"));
        tableColumnTitulo.setCellValueFactory(new PropertyValueFactory<Copia,String>("titulo"));
        tableColumnAutor.setCellValueFactory(new PropertyValueFactory<Copia,String>("autor"));
        tableColumnEstado.setCellValueFactory(new PropertyValueFactory<Copia,ComboBox>("estadoCopia"));
        tableColumnUbicacion.setCellValueFactory(new PropertyValueFactory<Copia,String>("ubicacion"));
        tableColumnModificar.setCellValueFactory(new PropertyValueFactory<Copia,Button>("configurarUbicacionButton"));
        
        this.tableViewListaCopias.setItems(ocopias);
    }
    @FXML
    public void onClick_buttonActaulizarTabla(ActionEvent event){
        try {
            refrescarTabla();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void showTabla(){
        try {
            refrescarTabla();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(DetalleCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setIsbn(String isbn)
    {
        this.isbn=isbn;
    }
    
    public void setTitulo(String titulo)
    {
        this.titulo=titulo;
    }
    
    public void setAutor(String autor)
    {
        this.autor=autor;
    }
    
     public void setAño(String año)
    {
        this.año=año;
    }
     
      public void setEdicion(String edicion)
    {
        this.edicion=edicion;
    }
      
      
        
    
    }
    



