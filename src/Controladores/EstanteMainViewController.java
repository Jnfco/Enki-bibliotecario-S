
package Controladores;

import Modelo.Estante;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Matias
 */
public class EstanteMainViewController implements Initializable {

    @FXML
    private TableColumn<Estante,String> tableColumnNumero;
    @FXML
    private TableColumn<Estante,String> tableColumnNiveles;
    @FXML
    private TableColumn<Estante,Button> tableColumnConfigurar;
    @FXML
    private TableColumn<Estante,String> tableColumnDeweyInf;
    @FXML
    private TableColumn<Estante,String> tableColumnDeweySup;
    
   
    @FXML
    private Label labelTimer;
    
     private int tiempoMaximo = 2;
    private int tiempo;
    @FXML
    private TableView<Estante> tableViewEstantes;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        /*ObservableList<Estante> estantes=FXCollections.observableArrayList(new Estante(1,5,200,299));
        //tableColumnConfigurar.setCellValueFactory(new PropertyValueFactory<Libro,String>("buttonConfigurar"));
        tableColumnNumero.setCellValueFactory(new PropertyValueFactory<Estante,Integer>("numero"));
        tableColumnNiveles.setCellValueFactory(new PropertyValueFactory<Estante,Integer>("niveles"));
        tableColumnRangoInferior.setCellValueFactory(new PropertyValueFactory<Estante,Integer>("rangoInferior"));
        tableColumnRangoSuperior.setCellValueFactory(new PropertyValueFactory<Estante,Integer>("rangoSuperior"));        
        
        tableColumnConfigurar.setCellValueFactory(new PropertyValueFactory<Estante,String>("buttonConfigurar"));        
        
        tableViewListaEstantes.setItems(estantes);*/
        
        
        try {
            refrescarTabla();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EstanteMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(EstanteMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EstanteMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(EstanteMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.labelTimer.setText(tiempoMaximo+"");
        this.tiempo=tiempoMaximo;
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {
              if(tiempo==0){
                  tiempo=2;
                  
                   try {
                        refrescarTabla();
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(EstanteMainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ProtocolException ex) {
                        Logger.getLogger(EstanteMainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(EstanteMainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JSONException ex) {
                        Logger.getLogger(EstanteMainViewController.class.getName()).log(Level.SEVERE, null, ex);
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
        
         URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerEstantePHP);
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
         System.out.println("mensaje "+mensaje);
    if(mensaje.equals("false")){
        //System.out.println("no hay nada");
    }else{
        List<Estante> estantes = new ArrayList<Estante>();
        Estante estante;
        JSONArray jsonArray = obj.getJSONArray("datos");
        for(int i = 0; i < jsonArray.length(); i++){
            String codigo = jsonArray.getJSONObject(i).getString("codigo")==null?"":jsonArray.getJSONObject(i).getString("codigo");
            String numero = jsonArray.getJSONObject(i).getString("numero")==null?"":jsonArray.getJSONObject(i).getString("numero");
            String niveles=jsonArray.getJSONObject(i).getString("cantidadniveles")==null?"":jsonArray.getJSONObject(i).getString("cantidadniveles");           
            String intervaloInf=jsonArray.getJSONObject(i).getString("intervaloInf")==null?"":jsonArray.getJSONObject(i).getString("intervaloInf");
            String intervaloSup=jsonArray.getJSONObject(i).getString("intervaloSup")==null?"":jsonArray.getJSONObject(i).getString("intervaloSup");
           
              estante= new Estante(codigo,numero,niveles,intervaloInf,intervaloSup);
            //System.out.println(lector.getRut());
            estantes.add(estante);
        }
        setTabla(estantes);
    
}
     }
     
      private void setTabla(List<Estante> estantes){
        ObservableList<Estante> oestantes=FXCollections.observableArrayList(estantes);
        tableColumnNumero.setCellValueFactory(new PropertyValueFactory<Estante,String>("codigo"));
        tableColumnNiveles.setCellValueFactory(new PropertyValueFactory<Estante,String>("cantidadniveles"));
        tableColumnDeweyInf.setCellValueFactory(new PropertyValueFactory<Estante,String>("intervaloinf"));
        tableColumnDeweySup.setCellValueFactory(new PropertyValueFactory<Estante,String>("intervalosup"));
        tableColumnConfigurar.setCellValueFactory(new PropertyValueFactory<Estante,Button>("buttonConfigurar"));  
        this.tableViewEstantes.setItems(oestantes);
    }
}
