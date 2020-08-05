
package Controladores;

import Modelo.Estante;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Jnfo
 */
public class ConfigurarNivelController implements Initializable {

    @FXML
    private Button buttonGuardar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private ComboBox<String> comboBoxEstante;
    @FXML
    private ComboBox<String> comboBoxNivel;
    @FXML
    private Label codigoLabel;

    private String codigo;
    
    private List<Estante> estantes ;
    private String codigoEstante;
    private String codigoNivel;
    private String codigoCopia;
    @FXML
    private BorderPane borderPaneConfigurarNivel;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            comboBoxEstante.getSelectionModel().clearSelection();
            comboBoxEstante.getItems().clear();
            obtenerEstantes();
            this.comboBoxEstante.getSelectionModel().selectFirst();
            this.codigoEstante = comboBoxEstante.getSelectionModel().getSelectedItem().toString();
            setComboBoxs();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConfigurarNivelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ConfigurarNivelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigurarNivelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ConfigurarNivelController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }    

    @FXML
    private void onClick_buttonGuardar(ActionEvent event) {
        
        modificarCopia();
        ((Stage)borderPaneConfigurarNivel.getScene().getWindow()).close();
        
    }

    @FXML
    private void onClick_buttonCancelar(ActionEvent event) {
        ((Stage)borderPaneConfigurarNivel.getScene().getWindow()).close();  
    }
    
    public void setCodigo(String codigo){
        this.codigo = codigo;
        this.codigoLabel.setText(codigo);
        
    }
    
     public void setComboBoxs(){
        comboBoxNivel.getSelectionModel().clearSelection();
        comboBoxNivel.getItems().clear();
        obtenerNiveles(comboBoxEstante.getSelectionModel().getSelectedItem());    
        setCodigoEstante(this.codigoEstante = comboBoxEstante.getSelectionModel().getSelectedItem().toString());
        this.comboBoxNivel.getSelectionModel().selectFirst();
        this.codigoNivel = this.comboBoxNivel.getSelectionModel().getSelectedItem().toString();
        
    }
    
    public void obtenerNiveles(String codigo)
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
                //System.out.println("no hay nada");
            }else{
                
                
                JSONArray jsonArray = obj.getJSONArray("datos");
                String numero;
                for(int i = 0; i < jsonArray.length(); i++){
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

    private void onClick_comboBoxEstante(ActionEvent event) {
        
       setComboBoxs();
    }
    
      private void obtenerEstantes() throws MalformedURLException, UnsupportedEncodingException, ProtocolException, IOException, JSONException{
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

    @FXML
    private void seleccionarEstante(ActionEvent event) {
 
        setComboBoxs();
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
        
           setCodigoNivel(comboBoxNivel.getSelectionModel().getSelectedItem());
    }
    
     public void obtenerNivelesEstante(String codigo,List<Estante> estantes)
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
                //System.out.println("no hay nada");
            }else{
                
                
                JSONArray jsonArray = obj.getJSONArray("datos");
                String numero;
                for(int i = 0; i < jsonArray.length(); i++){
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
     
     
     
       private void modificarCopia(){
        //String estado = comboBoxEstado.getValue().equals("")?"":comboBoxEstado.getValue();
        //isbn=isbn.equals("")?"":isbn;
        //String  ubicacion= " ".equals("")?"":" ";
        
           codigoCopia= codigoCopia.equals("")?"":codigoCopia;
           codigoNivel=codigoNivel.equals("")?"":codigoNivel;
           codigoEstante=codigoEstante.equals("")?"":codigoEstante;
       
            
            try {
                this.modificarCopiaEnBaseDeDatos(codigoCopia,codigoNivel,codigoEstante);
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
    
    
    //TODO: Decodificar JSON
    /**
     * 
     * @param isbn
     * @param autor
     * @param anio
     * @param dewey
     * @param titulo
     * @param edicion
     */
    
    public void modificarCopiaEnBaseDeDatos(String codigoCopia,String codigoNivel,String codigoEstante) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException{
    
    URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarEstanteCopiaPHP);
    Map<String,Object> params = new LinkedHashMap<>();
        System.out.println("Codigo Copia antes de guardar: "+codigoCopia);
        System.out.println("Codigo Nivel antes de guardar: "+codigoNivel);
     params.put("codigoEstante",codigoEstante .trim());
     params.put("codigoNivel",codigoNivel .trim());
     params.put("codigo", codigoCopia.trim());
    
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
          System.out.println(response);
          //System.out.println("ISBN; "+isbn);
    JSONObject obj = new JSONObject(response);
    String mensaje = obj.getString("mensaje");
    
    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    alerta.setTitle("Modificar ubicación");
    alerta.setHeaderText(null);
    alerta.setContentText("Ubicación modificada exitosamente");
    alerta.showAndWait();
    
  
}
}
