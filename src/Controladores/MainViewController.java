

package Controladores;

import Valores.SingletonUsuario;
import Valores.Validaciones;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import enki.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author jnfco
 */
public class MainViewController implements Initializable {
    @FXML
    private BorderPane contenido_View;
    @FXML
    private Button buttonCrearLibroCopia;
    @FXML
    private Button buttonListaLibros;
    @FXML
    private Button buttonListarEstantes;
     @FXML
    private Button buttonLector;
    @FXML
    private Button buttonBibliotecario;
    @FXML
    private MenuItem menuItemCerrarSesion;
    @FXML
    private Label labelCerrarSesion;
    @FXML
    private Label labelCargo;
    @FXML
    private Label labelUsuario;
    @FXML
    private Button buttonListarPrestamo;
    @FXML
    private Button buttonCrearPrestamo;
    @FXML
    private Button buttonCrearEstante;
    @FXML
    private Label labelCambiarContraseña;
    @FXML
    private Button buttonDevolucionLibro;
    @FXML
    private ImageView imagen;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       labelUsuario.setStyle("-fx-font-weight: bold;");
       
        //Privilegios de bibliotecarios
        if(SingletonUsuario.usuario.getTipoUsuario().equals("bib")){
            buttonBibliotecario.setVisible(false);
            buttonCrearLibroCopia.setVisible(false);
            labelUsuario.setText(SingletonUsuario.usuario.getUsuario());
            labelCargo.setText("Bibliotecario");
            
            File file = new File("src/Img/bib.png");
            Image image = new Image(file.toURI().toString());
            imagen.setImage(image);
        }
        else{
            labelUsuario.setText(SingletonUsuario.usuario.getUsuario());
            labelCargo.setText("Administrador");
            
            File file = new File("src/Img/admi.png");
            Image image = new Image(file.toURI().toString());
            imagen.setImage(image);
        }
        
        BorderPane bp=null;
        try {
            bp = FXMLLoader.load(getClass().getResource("/enki/ListaLibros.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        contenido_View.setCenter(bp);
        
    }    

    
    @FXML
    private void crearCopia(ActionEvent event) throws IOException
    {
        
        
       BorderPane bp2 = FXMLLoader.load(getClass().getResource("/enki/CrearLibroCopia.fxml"));
       contenido_View.setCenter(bp2);
    }
    
    @FXML
    private void listaLibros(ActionEvent event)throws IOException
    {
       BorderPane bp2 = FXMLLoader.load(getClass().getResource("/enki/ListaLibros.fxml"));
        contenido_View.setCenter(bp2);
    }

    private void cerrar(ActionEvent event) {
        
        System.exit(0);
    }
    
     @FXML
     private void onClick_buttonListarEstantes(ActionEvent event){
          BorderPane bp = null;
        try {
            bp = FXMLLoader.load(getClass().getResource("/enki/ListaEstantes.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        contenido_View.setCenter(bp);
     }
     
     @FXML
     private void onClick_buttonCrearEstante(ActionEvent event){
          BorderPane bp = null;
        try {
            bp = FXMLLoader.load(getClass().getResource("/enki/CrearEstante.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        contenido_View.setCenter(bp);
     }
             
     @FXML
     private void onClick_buttonCrearPrestamo(ActionEvent event){
         BorderPane bp = null;
        try {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/enki/CrearPrestamo.fxml"));
            Parent root = loader.load();
            
            CrearPrestamoController m = (CrearPrestamoController) loader.getController();
            m.setCorreo(this.labelUsuario.getText());
            m.setPadre(contenido_View);
            System.out.println("CORREO PASADO: " + this.labelUsuario.getText());
            
            contenido_View.setCenter(root);
            System.out.println();
            
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     }

    
     @FXML
     private void onClick_buttonLector(ActionEvent event) throws IOException{
          BorderPane bp2 = FXMLLoader.load(getClass().getResource("/enki/ListaLector.fxml"));
        contenido_View.setCenter(bp2);
     }

    @FXML
    private void onActionBibliotecario(ActionEvent event) throws IOException {
        
        BorderPane bp2 = FXMLLoader.load(getClass().getResource("/enki/ListaBibliotecarios.fxml"));
       contenido_View.setCenter(bp2);
    }
            
            @FXML
     private void onClick_buttonListarPrestamo(ActionEvent event)throws MalformedURLException, UnsupportedEncodingException, ProtocolException, JSONException{
           BorderPane bp = null;
        try {
            
             //Permite pasarle la informacion a la otra ventana
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/enki/ListaPrestamos.fxml"));
            Parent root = loader.load();
            ListaPrestamosController m = loader.getController();
            m.setPadre(contenido_View);
            m.refrescarTabla();
            contenido_View.setCenter(root);
            
            
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     @FXML
     private void onClick_menuItemCerrarSesion(ActionEvent event){
         Alert alert = new Alert(AlertType.CONFIRMATION);
         alert.setTitle("Confirmación");
         alert.setHeaderText("Estas seguro que deseas salir?");
         alert.setContentText("Si aceptas, se cerrara la sesion actual.");

         Optional<ButtonType> result = alert.showAndWait();
         if (result.get() == ButtonType.OK){
            //((Node)(event.getSource())).getScene().getWindow().hide(); //Cierra la ventana actual
            System.exit(0);
         } else {
             
         }

     }

    @FXML
    private void onClick_labelCerrarSesion(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
         alert.setTitle("Confirmación");
         alert.setHeaderText("Estas seguro que deseas cerrar Sesion?");
         alert.setContentText("Si aceptas, se cerrara la sesion actual.");

         Optional<ButtonType> result = alert.showAndWait();
         if (result.get() == ButtonType.OK){
            try {
                ((Node)(event.getSource())).getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/enki/Login.fxml"));
                
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setTitle("Login"); 
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
         } else {
             
         }
        
    }

    @FXML
    private void cambiarContraseña(MouseEvent event) {
        
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/enki/CambiarContrase�a.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Cambiar contrase�a"); 
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClick_buttonDevolucionLibro(ActionEvent event) throws IOException, UnsupportedEncodingException, UnsupportedEncodingException, MalformedURLException, MalformedURLException, JSONException{
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Devolución de Material");
        dialog.setHeaderText("Devolución de Material Biblioteca");
        dialog.setContentText("Ingrese Codigo Copia:");
        
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            
            if (!validarCopia(result.get())){
                return;
            }
            
             //Permite pasarle la informacion a la otra ventana
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/enki/DevolverPrestamo.fxml"));
            Parent root = loader.load();
            DevolverPrestamoController m = loader.getController();
            m.setCodigo(result.get());
            m.setPadre(contenido_View);
            System.out.println();
            contenido_View.setCenter(root);
        }

     }
    
    private boolean validarCopia(String get) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.validarCopia); // URL to your application
        System.out.println(url);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigo", get); // All parameters, also easy

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        // Convert string to byte array, as it should be sent
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        // Connect, easy
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        // Tell server that this is POST and in which format is the data
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        // This gets the output from your server
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String response="";
        System.out.println(in.toString());
        for (int c; (c = in.read()) >= 0;){
            response=response + (char)c;
        }
        
        System.out.println(response);
        JSONObject obj = new JSONObject(response);
        String mensaje = obj.getString("mensaje");

           //System.out.println("Mensaje: "+obj);

         if(mensaje.contains("true")){
            String estado = obj.getString("estado");
            
             System.out.println("ESTADO ENCONTRADO: " + estado);
             
             if (estado.equals("Habilitado")){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Revisar la informacion");
                alert.setContentText("El libro con codigo de copia #" + get + " no se encuentra prestado.");

                alert.showAndWait();
                return false;
             }
            
            if (!estado.equals("Prestado")){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Revisar la informacion");
                alert.setContentText("El libro con codigo de copia #" + get + " \nno se encuentra disponible para prestamos.");

                alert.showAndWait();
                return false;
            }

         }  
         else {
             alertaCodigoIncorrecto(get);
             return false;
         }

        
        return true;
    }

   private void alertaCodigoIncorrecto(String codigo) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Revisar la informacion");
        alert.setContentText("El codigo de copia " + codigo + " no fue encontrado.");

        alert.showAndWait();
    }
}
     

