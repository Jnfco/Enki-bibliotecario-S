/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Estante;
import Valores.Validaciones;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author jnfco
 */
public class CrearLibroCopiaController implements Initializable {

    @FXML
    private Button buttonGuardar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private TextField textBoxISBN;
    @FXML
    private TextField textBoxAutor;
    @FXML
    private TextField textBoxAño;
    @FXML
    private TextField textBoxDewey;
    @FXML
    private TextField textBoxTitulo;
    @FXML
    private TextField textBoxEdicion;
    @FXML
    private ComboBox<String> comboBoxEstante;
    @FXML
    private ComboBox<String> comboBoxNivel;
    
    private List<Estante> estantes ;
    private String codigoEstante;
    private String codigoNivel;
    private String codigoCopia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.textBoxISBN.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(17));
            this.textBoxAutor.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(200));
            this.textBoxAño.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionRut(4));
            this.textBoxDewey.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionRut(3));
            this.textBoxTitulo.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(200));
            this.textBoxEdicion.addEventFilter(KeyEvent.KEY_TYPED , Validaciones.ValidacionMaxString(200));
            obtenerEstante();
            this.comboBoxEstante.getSelectionModel().selectFirst();
            this.codigoEstante = comboBoxEstante.getSelectionModel().getSelectedItem().toString();
            setComboBoxs();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }    

    @FXML
    private void guardar(ActionEvent event) {
      
        //System.out.println("Datos: "+textBoxAutor.getText()+" "+textBoxA�o.getText()+" "+textBoxDewey.getText()+" "+textBoxEdicion.getText()+" "+textBoxISBN.getText()+" "+textBoxTitulo.getText());
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Confirmacion");
          alert.setHeaderText(null);
          alert.setContentText("Desea guardar los datos?");

          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK){
              if(Validaciones.validaISBN(this.textBoxISBN.getText())){
                  crearLibro();
              }else{
              alert = new Alert(AlertType.INFORMATION);
              alert.setTitle("Problemas al crear el libro");
              alert.setHeaderText(null);
              alert.setContentText("Asegúrese que el formato sea\n"+"aaa-bbb-ccc-ddd-e");

              alert.showAndWait();
              }
          } else {
              
          }
 
    }

    private void cancelar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setContentText("Estas seguro que quieres limpiar todos los campos de textos?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            textBoxEdicion.setText("");
            textBoxAutor.setText("");
            textBoxTitulo.setText("");
            textBoxAño.setText("");
            textBoxISBN.setText("");
            textBoxDewey.setText("");
        }
        //((Node)(event.getSource())).getScene().getWindow().hide(); 
    }
 
     private void crearLibro(){
        String isbn = textBoxISBN.getText().equals("")?"":textBoxISBN.getText();
        String autor=textBoxAutor.getText().equals("")?"":textBoxAutor.getText();
        String anio=textBoxAño.getText().equals("")?"":textBoxAño.getText();
        String dewey=textBoxDewey.getText().equals("")?"":textBoxDewey.getText();
        String titulo = textBoxTitulo.getText().equals("")?"":textBoxTitulo.getText();
        String  edicion= textBoxEdicion.getText().equals("")?"":textBoxEdicion.getText();
        
        // System.out.println("Datos: "+isbn+" "+autor+" "+anio+" "+dewey+" "+titulo+" "+edicion);
        
        if(!isbn.equals("")){
            
            try {
                this.crearLibroEnBaseDeDatos(isbn, autor, anio,dewey , titulo, edicion);
                //crearCopia(isbn);
            } catch (UnsupportedEncodingException ex) {
                System.out.println(ex);
               // Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println(ex);
                //Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }else{
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText("No se puede realizar esta operación.");
            alerta.setContentText("EL campo ISBN esta vacio, ingrese un ISBN valido.");
            alerta.showAndWait();
        }
        
        
    }
    
    
    public void crearLibroEnBaseDeDatos(String isbn,String autor,String anio,String dewey,
                                         String titulo,String edicion) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
    
    URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.crearLibroPHP);
    System.out.println(url);
    Map<String,Object> params = new LinkedHashMap<>();
    params.put("isbn", isbn);
    params.put("autor", autor);
    params.put("anio", anio);
    params.put("dewey", dewey);
    params.put("titulo",titulo);
    params.put("edicion", edicion);
    
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
    String tipo = obj.getString("tipo");
    if(tipo.equals("false")){
        System.out.println(mensaje);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Mensaje de la operacion");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
        crearCopia(isbn);
        
            this.textBoxISBN.setText("");
            this.textBoxAutor.setText("");
            this.textBoxAño.setText("");
            this.textBoxDewey.setText("");
            this.textBoxTitulo.setText("");
            this.textBoxEdicion.setText("");
        
        
        //crearVentanaAgregarCopia(isbn, titulo, anio, edicion);
        
    }else{
        System.out.println(mensaje);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Mensaje de la operacion");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
   // System.out.println(response);
}
    
    
    
    
    
    private void crearCopia(String isbn){
        String estado = "En exhibicion";
        isbn=isbn.equals("")?"":isbn;
            
            try {
                this.crearCopiaEnBaseDeDatos(isbn,estado);
                
            } catch (UnsupportedEncodingException ex) {
                System.out.println(ex);
               // Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println(ex);
                //Logger.getLogger(CrearLectorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(AgregarCopiaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        
        
        
        
    }
    
    
    public void crearCopiaEnBaseDeDatos(String isbn,String estado) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.crearPrimerCopiaPHP);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("isbnlibro",isbn);
        params.put("estado",estado);
        params.put("codigoEstante",codigoEstante);
        params.put("codigoNivel",codigoNivel);
        params.put("numcopia", "1");
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
//        JSONObject obj = new JSONObject(response);
        //String mensaje = obj.getString("mensaje");
       // System.out.println("Response: "+response);

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setContentText("Se ha creado copia exitosamente");
        alerta.showAndWait();
}
    

    @FXML
    private void onClick_buttonCancelar(ActionEvent event) {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
         alert.setTitle("Confirmación");
         alert.setHeaderText("Estas seguro que cancelar la operación");
         alert.setContentText("Si aceptas, se eliminarán los datos actuales.");

         Optional<ButtonType> result = alert.showAndWait();
         if (result.get() == ButtonType.OK){
            this.textBoxISBN.setText("");
            this.textBoxTitulo.setText("");
            this.textBoxAutor.setText("");
            this.textBoxAño.setText("");
            this.textBoxDewey.setText("");
           this.textBoxEdicion.setText("");
           
         } else {
             
         }
    }
    
    public void crearVentanaAgregarCopia(String isbn,String titulo,String año,String edicion)
    {
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
        } catch (IOException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    private void obtenerEstante() throws MalformedURLException, UnsupportedEncodingException, ProtocolException, IOException, JSONException{
        
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
        estantes = new ArrayList<Estante>();
        Estante estante;
        JSONArray jsonArray = obj.getJSONArray("datos");
        for(int i = 0; i < jsonArray.length(); i++){
                        String id = jsonArray.getJSONObject(i).getString("codigo")==null?"":jsonArray.getJSONObject(i).getString("codigo");
            String numero = jsonArray.getJSONObject(i).getString("numero")==null?"":jsonArray.getJSONObject(i).getString("numero");
            String niveles=jsonArray.getJSONObject(i).getString("cantidadniveles")==null?"":jsonArray.getJSONObject(i).getString("cantidadniveles");
            String codigo=jsonArray.getJSONObject(i).getString("codigo")==null?"":jsonArray.getJSONObject(i).getString("codigo");
            
            String intervaloInf=jsonArray.getJSONObject(i).getString("intervaloInf")==null?"":jsonArray.getJSONObject(i).getString("intervaloInf");
            String intervaloSup=jsonArray.getJSONObject(i).getString("intervaloSup")==null?"":jsonArray.getJSONObject(i).getString("intervaloSup");
           
              estante= new Estante(id,numero,niveles,intervaloInf,intervaloSup);
            //System.out.println(lector.getRut());
            estantes.add(estante);
         
            comboBoxEstante.getItems().addAll(id);
            //System.out.println("Codigo EStante: "+codigo);
            //System.out.println("Numero EStante: "+numero);
            
            
        }
        
        
        
        
        
}
     }
    
    public void obtenerNivelesEstante(String codigo)
    {
        
        try {
            URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.obtenerNivelPHP);
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("codigoEstante",codigo);
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
                System.out.println("no hay nada");
            }else{
                
                
                JSONArray jsonArray = obj.getJSONArray("datos");
                String numero;
                for(int i = 0; i < jsonArray.length(); i++){
                    System.out.println("nivel "+i);
                    numero = jsonArray.getJSONObject(i).getString("codigo")==null?"":jsonArray.getJSONObject(i).getString("codigo");
                    comboBoxNivel.getItems().addAll(numero);
                    
                }
                
                
            }       } catch (MalformedURLException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(CrearLibroCopiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    @FXML
    private void seleccionarEstante(ActionEvent event) {
        setComboBoxs();  
    }
    
    public void setComboBoxs(){
        comboBoxNivel.getSelectionModel().clearSelection();
        comboBoxNivel.getItems().clear();
        obtenerNivelesEstante(comboBoxEstante.getSelectionModel().getSelectedItem());    
        setCodigoEstante(this.codigoEstante = comboBoxEstante.getSelectionModel().getSelectedItem().toString());
        this.comboBoxNivel.getSelectionModel().selectFirst();
        this.codigoNivel = this.comboBoxNivel.getSelectionModel().getSelectedItem().toString();
        
    }
    
    
    public void guardarDatos() throws MalformedURLException, UnsupportedEncodingException, ProtocolException, IOException, JSONException
    {
         URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarEstanteCopiaPHP);
     Map<String,Object> params = new LinkedHashMap<>();
     params.put("codigoEstante",codigoEstante .trim());
     params.put("codigoNivel",codigoNivel .trim());
     params.put("codigoCopia", codigoCopia.trim());
     
        
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
    
    if(mensaje.equals("false")){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Problemas al modificar");
        alerta.setContentText("El rut ingresado no se ha podido modificar o no existe");
        alerta.showAndWait();
        }
    else{
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Mensaje");
        alerta.setContentText("Modificado con exito");
        alerta.showAndWait();
        }

    }   
    
    public void setCodigoEstante(String codigoEstante)
    {
        this.codigoEstante=codigoEstante;
    }
    
    public void setCodigoNivel(String codigoNivel)
    {
        this.codigoNivel=codigoNivel;
    }
    
    public void setCodigoCopia(String codigoCopia)
    {
        this.codigoCopia=codigoCopia;
    }

    @FXML
    private void seleccionarNivel(ActionEvent event) {
        
        setCodigoNivel(comboBoxNivel.getSelectionModel().getSelectedItem().toString());
        System.out.println("Estante: "+this.codigoEstante + "Nivel: "+this.codigoNivel);
    }
    
    
    }

