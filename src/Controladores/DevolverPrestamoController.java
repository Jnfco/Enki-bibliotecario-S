/* 
 * To change this license header, choose License Headers in Project Properties. 
 * To change this template file, choose Tools | Templates 
 * and open the template in the editor. 
 */ 
package Controladores; 
 
import Modelo.Copia;
import Modelo.CopiaP;
import Modelo.Libro;
import Modelo.PrestamoP;
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
import java.util.ResourceBundle; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; 
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class DevolverPrestamoController implements Initializable { 

 
    /** 
     * Initializes the controller class. 
     */ 
    
    @FXML
    private Button more, less, buttonFinalizar, buttonRegistrar;
    @FXML
    private Label prestamo;
    @FXML
    private Label fecha1;
    @FXML
    private Label fecha2, estadoLabel;
    @FXML
    private TableView table1;
    @FXML
    private TableView table2;
    @FXML
    private TableColumn col1;
    @FXML
    private TableColumn col2;
    @FXML
    private TableColumn col3;
    @FXML
    private TableColumn col4;
    @FXML
    private TextField tfCode;
    
    
    private ObservableList<CopiaP> listCopias1;
    private ObservableList<CopiaP> listCopias2;
    ArrayList<CopiaP> copias1;
    ArrayList<CopiaP> copias2;
    private String codigoCopia;
    private BorderPane padre;
    @FXML
    private Button prestamos;
   
    @Override 
    public void initialize(URL url, ResourceBundle rb) { 
        
        
        copias1 = new ArrayList<>();
        copias2 = new ArrayList<>();
        
        tfCode.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
        
            @Override
            public void handle(KeyEvent event) {
                if (!tfCode.getText().isEmpty()){
                    //PRIMERO VALIDAD QUE SE ENCUENTRE EN "NO DEVUELTOS"

                    for (int i=0; i<copias1.size(); i++){
                        if (copias1.get(i).getCodigo().equals(tfCode.getText())){
                            
                            
                            
                            CopiaP c = copias1.get(i);
                            c.setEstado("Habilitado");
                            copias2.add(c);
                            copias1.remove(copias1.get(i));
                            createUsersTableForm();
                            tfCode.setText("");
                            return;
                        }
                    }


                    createUsersTableForm();
                }
        
        }});
        
        
        createUsersTableForm();
        
    }     
    
    public void createUsersTableForm() {
        
        col1.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        col2.setCellValueFactory(new PropertyValueFactory<>("nombreLibro"));
        col3.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        col4.setCellValueFactory(new PropertyValueFactory<>("nombreLibro"));
        
        if (this.codigoCopia != null){
            this.more.setDisable(false);
            this.less.setDisable(false);
            this.buttonFinalizar.setDisable(false);
            this.buttonRegistrar.setVisible(false);
        }
        
        this.loadUsersInformation();
    }

    private void loadUsersInformation() {
        this.listCopias1 = FXCollections.observableArrayList();
        this.listCopias2 = FXCollections.observableArrayList();
        
        if (copias1.size()>0){
            for (CopiaP  copia: copias1){
                this.listCopias1.add(copia);
            }
            this.more.setDisable(false);
        }
        else {
            this.more.setDisable(true);
        }
        this.table1.setItems(listCopias1);
        
        if (copias2.size()>0){
            for (CopiaP  copia: copias2){
                this.listCopias2.add(copia);
            }
            this.less.setDisable(false);
        }
        else {
            this.less.setDisable(true);
        }
        this.table2.setItems(listCopias2);
    }
    
    @FXML
    private void onClick_buttonMore(ActionEvent event) {
        
        if (!table1.getSelectionModel().isEmpty()){
            CopiaP c = (CopiaP) table1.getSelectionModel().getSelectedItem() ;
            copias1.remove((CopiaP) table1.getSelectionModel().getSelectedItem() );
            c.setEstado("Habilitado");
            copias2.add(c);
            
            this.createUsersTableForm();
        }
    }
    
    
    
    @FXML
    private void onClick_buttonLess(ActionEvent event) {
        
        if (!table2.getSelectionModel().isEmpty()){
            CopiaP c = (CopiaP) table2.getSelectionModel().getSelectedItem() ;
            copias2.remove((CopiaP) table2.getSelectionModel().getSelectedItem() );
            c.setEstado("Prestado");
            copias1.add(c);
            
            this.createUsersTableForm();
        }
        
    }
    
    @FXML
    private void onClick_buttonFinalizar(ActionEvent event) throws Throwable {
        

        
        if (copias1.isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("¡Éxito!");
            alert.setHeaderText("Prestamo finalizado correctamente");
            alert.setContentText("¡Todas las copias fueron devueltas!");
            alert.showAndWait();
            
            //ACTUALIZAR BD
            
            actualizarTodos();
            
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/enki/ListaPrestamos.fxml"));
                Parent root = loader.load();
                ListaPrestamosController m = loader.getController();
                m.setPadre(this.padre);
                m.refrescarTabla();
                this.padre.setCenter(root);
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.finalize();
        }
        else{
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Precaución");
            alert.setHeaderText("Existen copias por devolver");
            alert.setContentText("¡El prestamo quedará pendiente mientras se realice la devolución de todas las copias!!");
            alert.showAndWait();
            
            //ACTUALIZAR BD
            actualizarTemporal();
            
            
            
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/enki/ListaPrestamos.fxml"));
                Parent root = loader.load();
                ListaPrestamosController m = loader.getController();
                m.setPadre(this.padre);
                m.refrescarTabla();
                this.padre.setCenter(root);
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
    }
    
    @FXML
    private void onClick_buttonVolver(ActionEvent event) throws Throwable {
        BorderPane bp = null;
        try {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/enki/ListaPrestamos.fxml"));
            Parent root = loader.load();
            ListaPrestamosController m = loader.getController();
            m.setPadre(this.padre);
            m.refrescarTabla();
            padre.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @FXML
    private void onClick_buttonRegistrar(ActionEvent event) throws Throwable {
        
        if (!tfCode.getText().isEmpty()){
            //PRIMERO VALIDAD QUE SE ENCUENTRE EN "NO DEVUELTOS"
            
            for (int i=0; i<this.copias1.size(); i++){
                if (this.copias1.get(i).getCodigo().equals(tfCode.getText())){
                    copias2.add(this.copias1.get(i));
                    copias1.remove(this.copias1.get(i));
                    this.createUsersTableForm();
                    this.tfCode.setText("");
                    return;
                }
            }
            
            
            this.createUsersTableForm();
        }
        
    }
    

    public String getCodigoCopia() {
        return codigoCopia;
    }

    public void setCodigoCopia(String codigoCopia) {
        this.codigoCopia = codigoCopia;
    }

    public void setCodigo(String codigoCopia) throws UnsupportedEncodingException, IOException, MalformedURLException, JSONException {
        this.codigoCopia = codigoCopia;
        
        this.buttonFinalizar.setDisable(false);
        this.buttonRegistrar.setDisable(false);
        this.more.setDisable(false);
        this.less.setDisable(false);
        this.prestamos.setDisable(false);
                
        
        tfCode.requestFocus();
        
        PrestamoP prestamo = buscarPrestamoPorCopia(codigoCopia);
        
        if (prestamo==null){
            return;
        }
        
        
        //Buscar el prestamo en bd
        //Prestamo prestamo = bd.buscarPrestamo(codigoCopia);
        this.prestamo.setText(String.valueOf(prestamo.getCodigo()));
        this.fecha1.setText(prestamo.getFechaPrestamo());
        this.fecha2.setText(prestamo.getFechaDevolucion());
        this.estadoLabel.setText(prestamo.getEstado());
        
        //en copias1 dejar las que aun no se devuelven
        
        this.setearCopiasPendientes(String.valueOf(prestamo.getCodigo()));
        
        //en copias2 dejar las copias ya devueltas
        
        this.setearCopiasDevueltas(String.valueOf(prestamo.getCodigo()));
        
        CopiaP more = new CopiaP("","");
        
        for (int i=0; i<this.copias1.size(); i++){
            if (this.copias1.get(i).getCodigo().equals(codigoCopia)){
                more.setAll(this.copias1.get(i));
                this.copias1.remove(i);
            }
        }
        
        more.setEstado("Habilitado");
        this.copias2.add(more);
        this.createUsersTableForm();
    }

    public void setPadre(BorderPane contenido_View) {
        this.padre = contenido_View;
    }


    private void setearCopiasPendientes(String codigoPrestamo) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerCopiasPendientes);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigo", codigoPrestamo);
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
        for (int c; (c = in.read()) >= 0;)
           response=response + (char)c;

        //Convierte el json enviado (decodigicado)
        JSONObject obj = new JSONObject(response);

        String mensaje = obj.getString("mensaje");
        if(mensaje.equals("false")){
        }
        else{
            List<CopiaP> copias = new ArrayList<CopiaP>();
            JSONArray jsonArray = obj.getJSONArray("datos");
            for(int i = 0; i < jsonArray.length(); i++){
                String codigoCopia = jsonArray.getJSONObject(i).getString("codigoCopia")==null?"":jsonArray.getJSONObject(i).getString("codigoCopia");
                String titulo = jsonArray.getJSONObject(i).getString("titulo")==null?"":jsonArray.getJSONObject(i).getString("titulo");
                

                CopiaP c  = new CopiaP(codigoCopia,titulo);
                c.setEstado("Prestado");
                copias.add(c);
            }
            this.copias1.addAll(copias);
        }
        
        
    }


     private void setearCopiasDevueltas(String codigoPrestamo) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerCopiasDevueltas);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigo", codigoPrestamo);
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
        for (int c; (c = in.read()) >= 0;)
           response=response + (char)c;

        //Convierte el json enviado (decodigicado)
        JSONObject obj = new JSONObject(response);

        String mensaje = obj.getString("mensaje");
        if(mensaje.equals("false")){
        }
        else{
            List<CopiaP> copias = new ArrayList<CopiaP>();
            JSONArray jsonArray = obj.getJSONArray("datos");
            for(int i = 0; i < jsonArray.length(); i++){
                String codigoCopia = jsonArray.getJSONObject(i).getString("codigoCopia")==null?"":jsonArray.getJSONObject(i).getString("codigoCopia");
                String titulo = jsonArray.getJSONObject(i).getString("titulo")==null?"":jsonArray.getJSONObject(i).getString("titulo");
                

                CopiaP c  = new CopiaP(codigoCopia,titulo);
                c.setEstado("Habilitado");
                copias.add(c);
            }
            this.copias2.addAll(copias);
        }
        
        
    }

    private void actualizarTodos()  throws UnsupportedEncodingException, IOException, MalformedURLException, JSONException {
        for (CopiaP c: copias2){
            actualizarEstadoCopia(c);
            actualizarEstadoPrestamoCopia(c,this.prestamo.getText());
        }
        actualizarPrestamo("Finalizado");
        
    }

    private void actualizarTemporal() throws UnsupportedEncodingException, IOException, MalformedURLException, JSONException {
        for (CopiaP c1: copias1){
            actualizarEstadoCopia(c1);
            actualizarEstadoPrestamoCopia(c1,this.prestamo.getText());
        }
        for (CopiaP c2: copias2){
            actualizarEstadoCopia(c2);
            actualizarEstadoPrestamoCopia(c2,this.prestamo.getText());
        }
        actualizarPrestamo("Pendiente");
    }

    private void actualizarEstadoCopia(CopiaP copia) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarEstadoCopia);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigo", copia.getCodigo().trim());
        params.put("estado", copia.getEstado().trim());
        

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
       for (int c; (c = in.read()) >= 0;)
          response=response + (char)c;

       JSONObject obj = new JSONObject(response);
       String mensaje = obj.getString("mensaje");

       if(mensaje.equals("false")){
            
       }
       else{
       }

    }

    private void actualizarPrestamo(String estado) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarEstadoPrestamo);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigoPrestamo", this.prestamo.getText().trim());
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
       Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

       String response="";
       for (int c; (c = in.read()) >= 0;)
          response=response + (char)c;

       JSONObject obj = new JSONObject(response);
       String mensaje = obj.getString("mensaje");

       if(mensaje.equals("false")){
            
       }
       else{
       }
    }

    private void actualizarEstadoPrestamoCopia(CopiaP c1, String prestamo) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarEstadoPrestamoCopia);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigoPrestamo", prestamo.trim());
        params.put("codigoCopia", c1.getCodigo().trim());
        
        
        
        
        if (c1.getEstado().equals("Habilitado")){
            params.put("estado", "Finalizado");
        }
        else {
            params.put("estado", "Pendiente");
        }
        
        

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
       for (int c; (c = in.read()) >= 0;)
          response=response + (char)c;

       JSONObject obj = new JSONObject(response);
       String mensaje = obj.getString("mensaje");

       if(mensaje.equals("false")){
            
       }
       else{
       }
    }


    public void setPrestamo(Integer codigo) throws UnsupportedEncodingException, IOException, MalformedURLException, JSONException {
        
        
        PrestamoP prestamo = buscarPrestamoPorPrestamo(String.valueOf(codigo));
        
        if (prestamo==null){
            return;
        }
        
        this.estadoLabel.setText(prestamo.getEstado());
        
        
        if (prestamo.getEstado().equals("Finalizado")){
            this.buttonFinalizar.setDisable(true);
            this.buttonRegistrar.setDisable(true);
            this.prestamo.setDisable(true);
            this.more.setDisable(true);
            this.less.setDisable(true);
            this.prestamos.setDisable(false);
        }
        else {
            this.buttonFinalizar.setDisable(false);
            this.buttonRegistrar.setDisable(false);
            this.more.setDisable(false);
            this.less.setDisable(false);
            this.prestamos.setDisable(false);
        }
        
        
        //Buscar el prestamo en bd
        //Prestamo prestamo = bd.buscarPrestamo(codigoCopia);
        this.prestamo.setText(String.valueOf(prestamo.getCodigo()));
        this.fecha1.setText(prestamo.getFechaPrestamo());
        this.fecha2.setText(prestamo.getFechaDevolucion());
        
        //en copias1 dejar las que aun no se devuelven
        
        this.setearCopiasPendientes(String.valueOf(prestamo.getCodigo()));
        
        //en copias2 dejar las copias ya devueltas
        
        this.setearCopiasDevueltas(String.valueOf(prestamo.getCodigo()));
        
        this.createUsersTableForm();
        
        if (prestamo.getEstado().equals("Finalizado")){
            this.less.setDisable(true);
            this.tfCode.setDisable(true);
        }
    }


    private PrestamoP buscarPrestamoPorPrestamo(String codigoCopia) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
         
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerCopiaSinRestricciones);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigo", codigoCopia);
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
        for (int c; (c = in.read()) >= 0;)
           response=response + (char)c;

        //Convierte el json enviado (decodigicado)
        JSONObject obj = new JSONObject(response);
        
        System.out.println("response : " + response);

        String mensaje = obj.getString("mensaje");
        
        if(mensaje.equals("false")){
            
        }
        else{
            String numPrestamo = obj.getString("prestamo");
            String fechaP = obj.getString("fechaP");
            String fechaD = obj.getString("fechaD");
            String refLector = obj.getString("refLector");
            String refTrabajador = obj.getString("refTrabajador");
            String estado = obj.getString("estado");
            
            PrestamoP p = new PrestamoP(this.padre,Integer.valueOf(numPrestamo), refLector, refTrabajador, fechaP, fechaD, estado);
            return p;
        }
        return null;
    }
    
    
    private PrestamoP buscarPrestamoPorCopia(String codigoCopia) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
         
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerCopia);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigo", codigoCopia);
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
        for (int c; (c = in.read()) >= 0;)
           response=response + (char)c;

        //Convierte el json enviado (decodigicado)
        JSONObject obj = new JSONObject(response);
        
        System.out.println("response : " + response);

        String mensaje = obj.getString("mensaje");
        
        if(mensaje.equals("false")){
            
        }
        else{
            String numPrestamo = obj.getString("prestamo");
            String fechaP = obj.getString("fechaP");
            String fechaD = obj.getString("fechaD");
            String refLector = obj.getString("refLector");
            String refTrabajador = obj.getString("refTrabajador");
            String estado = obj.getString("estado");
            
            PrestamoP p = new PrestamoP(this.padre,Integer.valueOf(numPrestamo), refLector, refTrabajador, fechaP, fechaD, estado);
            return p;
        }
        return null;
    }
  
     
} 