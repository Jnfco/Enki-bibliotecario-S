/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Libro;
import Modelo.Prestamo;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author jnfco
 */
public class ListaHistorialLectorController implements Initializable {

    @FXML
    private TableColumn<Prestamo,String > tableColumnCodigoPrestamo;
    @FXML
    private TableColumn<Prestamo,String> tableColumnRut;
    @FXML
    private TableColumn<Prestamo,String> tableColumnFechaPrestamo;
    @FXML
    private TableColumn<Prestamo,String> tableColumnFechaDevolucion;
    
    private String rut;
    @FXML
    private TableView<Prestamo> tableViewPrestamos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }    
    
     private void refrescarTabla() throws MalformedURLException, UnsupportedEncodingException, ProtocolException, IOException, JSONException{
        
         URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerPrestamoLectorPHP);
    Map<String,Object> params = new LinkedHashMap<>();
     params.put("rut", rut);
         System.out.println("RUT: "+rut);
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
        List<Prestamo> prestamos = new ArrayList<Prestamo>();
        Prestamo prestamo;
        JSONArray jsonArray = obj.getJSONArray("datos");
        System.out.println("RUT: "+rut);
        for(int i = 0; i < jsonArray.length(); i++){
            String codigo = jsonArray.getJSONObject(i).getString("codigo")==null?"":jsonArray.getJSONObject(i).getString("codigo");
            String refLector=jsonArray.getJSONObject(i).getString("refLector")==null?"":jsonArray.getJSONObject(i).getString("refLector");
            
            String fechaPrestamo=jsonArray.getJSONObject(i).getString("fechaPrestamo")==null?"":jsonArray.getJSONObject(i).getString("fechaPrestamo");
            String fechaDevolucion=jsonArray.getJSONObject(i).getString("fechaDevolucion")==null?"":jsonArray.getJSONObject(i).getString("fechaDevolucion");
        
            prestamo  = new Prestamo(codigo,refLector,fechaPrestamo,fechaDevolucion);
            //System.out.println(lector.getRut());
            prestamos.add(prestamo);
        }
        setTabla(prestamos);
        //System.out.println(jsonMainArr.toString());
    }
    
   
     }    
     
    private void setTabla(List<Prestamo> prestamos){
        ObservableList<Prestamo> oprestamos=FXCollections.observableArrayList(prestamos);
        tableColumnCodigoPrestamo.setCellValueFactory(new PropertyValueFactory<Prestamo,String>("codigo"));
        tableColumnRut.setCellValueFactory(new PropertyValueFactory<Prestamo,String>("refLector"));
        tableColumnFechaPrestamo.setCellValueFactory(new PropertyValueFactory<Prestamo,String>("fechaPrestamo"));
        tableColumnFechaDevolucion.setCellValueFactory(new PropertyValueFactory<Prestamo,String>("fechaDevolucion"));
        
        this.tableViewPrestamos.setItems(oprestamos);
    }
    
    public void setRut(String rut)
    {
        try {
            this.rut=rut;
            refrescarTabla();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ListaHistorialLectorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ListaHistorialLectorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ListaHistorialLectorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ListaHistorialLectorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
