/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Libro;
import Modelo.PrestamoP;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author mati_
 */
public class ListaPrestamosController implements Initializable {

    @FXML
    private Button buscarButton;
    @FXML
    private TextField rut;
    @FXML
    private TableView<PrestamoP> table;
    @FXML
    private TableColumn<PrestamoP, Integer> codigo;
    @FXML
    private TableColumn<PrestamoP, String> rutLector;
    @FXML
    private TableColumn<PrestamoP, String> estado;
    @FXML
    private TableColumn<PrestamoP, String> fechaPrestamo;
    @FXML
    private TableColumn<PrestamoP, String> fechaDevolucion;
    @FXML
    private TableColumn<PrestamoP, Button> detalles;
    
    private ArrayList<PrestamoP> prestamosTabla;
    
    private BorderPane padre;

    /**
     * Initializes the controller class.
     */
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.prestamosTabla = new ArrayList<>();
        
        /*try {
            System.out.println("");
            //refrescarTabla();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ListaLibrosController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }    

    @FXML
    private void actionBuscarButton(ActionEvent event) {
        
    }
    
    public void refrescarTabla() throws MalformedURLException, UnsupportedEncodingException, ProtocolException, IOException, JSONException{
        
         URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerPrestamos);
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
         //System.out.println("mensaje "+mensaje);
    if(mensaje.equals("false")){
        //System.out.println("no hay nada");
    }else{
        List<PrestamoP> prestamos = new ArrayList<PrestamoP>();
        JSONArray jsonArray = obj.getJSONArray("datos");
        for(int i = 0; i < jsonArray.length(); i++){
            String codigo = jsonArray.getJSONObject(i).getString("codigo")==null?"":jsonArray.getJSONObject(i).getString("codigo");
            String refLector=jsonArray.getJSONObject(i).getString("refLector")==null?"":jsonArray.getJSONObject(i).getString("refLector");
            
            String refTrabajador=jsonArray.getJSONObject(i).getString("refTrabajador")==null?"":jsonArray.getJSONObject(i).getString("refTrabajador");
            String fechaPrestamo=jsonArray.getJSONObject(i).getString("fechaPrestamo")==null?"":jsonArray.getJSONObject(i).getString("fechaPrestamo");
            String fechaDevolucion=jsonArray.getJSONObject(i).getString("fechaDevolucion")==null?"":jsonArray.getJSONObject(i).getString("fechaDevolucion");
            String estado=jsonArray.getJSONObject(i).getString("estado")==null?"":jsonArray.getJSONObject(i).getString("estado");
            
            
            System.out.println("PADRE EN LISTA PRESTAMOS: " + this.padre);
            PrestamoP prestamo  = new PrestamoP(this.padre, Integer.valueOf(codigo),refLector,refTrabajador,fechaPrestamo,fechaDevolucion,estado);
            //System.out.println(lector.getRut());
            prestamos.add(prestamo);
        }
        setTabla(prestamos);
        //System.out.println(jsonMainArr.toString());
    }
    
   
     }    
     
    private void setTabla(List<PrestamoP> prestamos){
        ObservableList<PrestamoP> olibros=FXCollections.observableArrayList(prestamos);
        codigo.setCellValueFactory(new PropertyValueFactory<PrestamoP,Integer>("codigo"));
        rutLector.setCellValueFactory(new PropertyValueFactory<PrestamoP,String>("refLector"));
        estado.setCellValueFactory(new PropertyValueFactory<PrestamoP,String>("estado"));
        fechaPrestamo.setCellValueFactory(new PropertyValueFactory<PrestamoP,String>("fechaPrestamo"));
        fechaDevolucion.setCellValueFactory(new PropertyValueFactory<PrestamoP,String>("fechaDevolucion"));
        detalles.setCellValueFactory(new PropertyValueFactory<PrestamoP,Button>("detalle"));
        
        this.table.setItems(olibros);
        this.prestamosTabla.addAll(olibros);
    }


    @FXML
    private void actionRut(KeyEvent event) throws UnsupportedEncodingException, ProtocolException, IOException, MalformedURLException, JSONException {
        
        this.table.getItems().clear();
        
        ArrayList<PrestamoP> nuevo = new ArrayList<>();
        
        if (Character.isDigit(event.getCharacter().charAt(0))){
            System.out.println("D I G I T O");
            for (PrestamoP prestamo : this.prestamosTabla) {
                if (prestamo.getRefLector().contains(rut.getText()+String.valueOf(event.getCharacter()))){
                    nuevo.add(prestamo);
                }
            }
            this.prestamosTabla.clear();
            
            this.setTabla(nuevo);
        }
        else {
            this.prestamosTabla.clear();
            this.rut.setText("");
            this.refrescarTabla();
        }
    
        
        
        
    }

    @FXML
    private void releasedRut(KeyEvent event) {
    }

    void setPadre(BorderPane contenido_View) {
        this.padre = contenido_View;
    }

    
    
    
}
