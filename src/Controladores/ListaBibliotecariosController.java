/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Bibliotecario;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class ListaBibliotecariosController implements Initializable {

    @FXML
    private Button buttonNuevoBibliotecario;
    @FXML
    private Button buttonBuscar;
    @FXML
    private Button buttonActualizarLista;
    @FXML
    private TextField textBoxBuscar;
    @FXML
    private TableView<Bibliotecario> tableViewBibliotecarios;
    @FXML
    private TableColumn<Bibliotecario,String> tableColumnRut;
    @FXML
    private TableColumn<Bibliotecario,String> tableColumnNombre;
    @FXML
    private TableColumn<Bibliotecario,String> tableColumnAPaterno;
    @FXML
    private TableColumn<Bibliotecario,String> tableColumnAMaterno;
    @FXML
    private TableColumn<Bibliotecario,Button> tableColumnConfig;
    @FXML
    private TableColumn<Bibliotecario,CheckBox> tableColumnHabilitado;
    @FXML
    private Label labelTimer;
    
    private int tiempoMaximo = 60;
    private int tiempo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            refrescarTabla();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
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
                        Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ProtocolException ex) {
                        Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JSONException ex) {
                        Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void onClick_buttonActualizarLista(ActionEvent event){
         try {
                        refrescarTabla();
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ProtocolException ex) {
                        Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JSONException ex) {
                        Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }

    @FXML
    private void onClick_buttonBuscar(ActionEvent event) {
    }
    
    private void refrescarTabla() throws MalformedURLException, UnsupportedEncodingException, ProtocolException, IOException, JSONException{
        
         URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerBibliotecarioPHP);
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
    
    //Convierte el json enviado (decodigicado)
    JSONObject obj = new JSONObject(response);
    String mensaje = obj.getString("mensaje");
    
    if(mensaje.equals("false")){
        //System.out.println("no hay nada");
    }else{
        List<Bibliotecario> bibliotecarios = new ArrayList<Bibliotecario>();
        Bibliotecario bibliotecario;
        JSONArray jsonArray = obj.getJSONArray("datos");
        for(int i = 0; i < jsonArray.length(); i++){
            String rut = jsonArray.getJSONObject(i).getString("rut")==null?"":jsonArray.getJSONObject(i).getString("rut");
            String nombre=jsonArray.getJSONObject(i).getString("nombre")==null?"":jsonArray.getJSONObject(i).getString("nombre");
            
            String apaterno=jsonArray.getJSONObject(i).getString("apellidoPaterno")==null?"":jsonArray.getJSONObject(i).getString("apellidoPaterno");
            String amaterno=jsonArray.getJSONObject(i).getString("apellidoMaterno")==null?"":jsonArray.getJSONObject(i).getString("apellidoMaterno");
            String habilitado=jsonArray.getJSONObject(i).getString("estado")==null?"":jsonArray.getJSONObject(i).getString("estado");

            bibliotecario  = new Bibliotecario(rut,nombre,apaterno,amaterno,habilitado);
            //System.out.println(lector.getRut());
            bibliotecarios.add(bibliotecario);
        }
        setTabla(bibliotecarios);
        //System.out.println(jsonMainArr.toString());
    }
    
   /* Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    alerta.setTitle("Mensaje");
    alerta.setContentText(mensaje);
    alerta.showAndWait();*/
   // System.out.println(response);
        
        
        
    }
    
    @FXML
    private void onClick_buttonNuevoBibliotecario(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/enki/CrearBibliotecario.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setMinWidth(600);
            stage.setMinHeight(321);
            stage.setTitle(Valores.ValoresEstaticos.enki);
            stage.setScene(scene);
          //  ((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refrescarTabla();
        } catch (IOException ex) {
            Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ListaBibliotecariosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void setTabla(List<Bibliotecario> bibliotecarios){
        ObservableList<Bibliotecario> libros=FXCollections.observableArrayList(bibliotecarios);
        tableColumnRut.setCellValueFactory(new PropertyValueFactory<Bibliotecario,String>("rut"));
        tableColumnNombre.setCellValueFactory(new PropertyValueFactory<Bibliotecario,String>("nombre"));
        tableColumnAPaterno.setCellValueFactory(new PropertyValueFactory<Bibliotecario,String>("apellidoP"));
        tableColumnAMaterno.setCellValueFactory(new PropertyValueFactory<Bibliotecario,String>("apellidoM"));
        tableColumnConfig.setCellValueFactory(new PropertyValueFactory<Bibliotecario,Button>("buttonConfiguraciones"));    
        tableColumnHabilitado.setCellValueFactory(new PropertyValueFactory<Bibliotecario,CheckBox>("habilitado"));
        
        this.tableViewBibliotecarios.setItems(libros);
    }
    
}
