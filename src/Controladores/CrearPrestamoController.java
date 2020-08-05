/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Valores.SingletonUsuario;
import Valores.Validaciones;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author mati_
 */
public class CrearPrestamoController implements Initializable {

    @FXML
    private TextField textFieldRut;
    @FXML
    private Button buttonBuscar;
    @FXML
    private TextField textFieldCodigo1;
    @FXML
    private Button buttonAgregarPrestamo1;
    @FXML
    private TextField textFieldCodigo2;
    @FXML
    private Button buttonAgregarPrestamo2;
    @FXML
    private TextField textFieldCodigo3;
    @FXML
    private Button buttonAceptar;
    @FXML
    private Label labelPrestamo2;
    @FXML
    private Label labelName;
    @FXML
    private Label labelRut;
    @FXML
    private Label labelName1;
    @FXML
    private Label labelRut1;
    @FXML
    private Label labelPrestamo3;
    @FXML
    private Button buttonQuitarPrestamo2;
    @FXML
    private Button buttonQuitarPrestamo3;
    
    private String correo;
    private BorderPane padre;
    private String rutLector;
    @FXML
    private Button buttonLimpiar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
        
        textFieldCodigo2.setVisible(false);
        buttonAgregarPrestamo2.setVisible(false);
        labelPrestamo2.setVisible(false);
        buttonQuitarPrestamo2.setVisible(false);
        
        textFieldCodigo3.setVisible(false);
        labelPrestamo3.setVisible(false);
        buttonQuitarPrestamo3.setVisible(false);
        
        
    }    
    
    @FXML
    private void cargarLector(ActionEvent event) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        
        if (this.textFieldRut.getText().isEmpty()){
            this.buttonAgregarPrestamo1.setDisable(false);
            this.textFieldCodigo1.setDisable(false);
            return;
        }
        
        if(Validaciones.validaRut(this.textFieldRut.getText())){
            this.buttonAgregarPrestamo1.setDisable(false);
            this.textFieldCodigo1.setDisable(false);
                
            if (!validarCopias(this.textFieldRut.getText())){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setHeaderText("Excede máximo de copias prestadas");
                alert.setContentText("Intente finalizando sus prestamos actuales!");

                alert.showAndWait();

                this.textFieldRut.setText("");
                return;
            }
            
            this.textFieldRut.setDisable(true);

            boolean flag=false;
            URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.getLectorPHP); // URL to your application
            //System.out.println(url);
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("rut", this.textFieldRut.getText().trim()); // All parameters, also easy
            this.rutLector = this.textFieldRut.getText().trim();

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
            //System.out.println(in.toString());
            for (int c; (c = in.read()) >= 0;){
                response=response + (char)c;
            }
               //System.out.println(response);
               JSONObject obj = new JSONObject(response);
               String mensaje = obj.getString("mensaje");

               //System.out.println("Mensaje: "+obj);

             if(mensaje.contains("true")){
                 String nombre = obj.getString("nombre");
                 String ap = obj.getString("ap");
                 String am = obj.getString("am");
                 String dir = obj.getString("dir");
                 String cor = obj.getString("cor");
                 this.labelName.setText(nombre + " " +  ap + " " +  am);
                 this.labelRut.setText(this.textFieldRut.getText());
                 this.labelName1.setText(dir);
                 this.labelRut1.setText(cor);
             }  

             System.out.println("CARGADO LECTOR Y CORREO DEL TRABAJADOR ES: " + this.correo);
            //return flag;
            
        }else{
            this.buttonAgregarPrestamo1.setDisable(true);
            this.textFieldCodigo1.setDisable(true);
            
            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText("No se puede realizar esta operación.");
            alerta.setContentText("EL campo rut está incorrecto, ingrese un rut válido.");
            alerta.showAndWait();
            this.textFieldRut.setText("");
        }    
        
        
        
    
    }


    @FXML
    private void agregarPrestamo1(ActionEvent event) {
        
        textFieldCodigo2.setVisible(true);
        buttonAgregarPrestamo2.setVisible(true);
        labelPrestamo2.setVisible(true);
        buttonAgregarPrestamo1.setVisible(false);
        buttonQuitarPrestamo2.setVisible(true);
    }

    @FXML
    private void agregarPrestamo2(ActionEvent event) {
        
        textFieldCodigo3.setVisible(true);
        labelPrestamo3.setVisible(true);
        buttonAgregarPrestamo2.setVisible(false);
        buttonQuitarPrestamo3.setVisible(true);
        buttonQuitarPrestamo2.setVisible(false);
    }


    @FXML
    private void aceptar(ActionEvent event) throws UnsupportedEncodingException, IOException, MalformedURLException, JSONException {
        
        boolean resultado = false;
        
        if (textFieldRut.getText().isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Revisar la información");
            alert.setContentText("¡Ingrese el rut del lector!");
            alert.showAndWait();
            return;
        }
        
        if (textFieldCodigo1.isVisible() && textFieldCodigo2.isVisible()==false){
            System.out.println("SOLO UNA COPIA");
            
            if (textFieldCodigo1.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Revisar la información");
                alert.setContentText("¡Ingrese el codigo de la copia!");
                alert.showAndWait();
                return;
            }
            
            String codigo1 = textFieldCodigo1.getText();
            
            
            if (validarCopiaDisponible(codigo1)){
                System.out.println("Paso aqui");
                solicitarCopia(codigo1);
                String codigoPrestamo = crearPrestamo();
                crearPrestamoCopia(codigoPrestamo, codigo1);
            }
            
        }
        else if (textFieldCodigo1.isVisible() && textFieldCodigo2.isVisible() && textFieldCodigo3.isVisible()==false){
            System.out.println("DOS COPIAS");
            
            if (textFieldCodigo1.getText().isEmpty() || textFieldCodigo2.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Revisar la información");
                alert.setContentText("¡Ingrese ambos codigos de las copias!");
                alert.showAndWait();
                return;
            }
            
            String codigo1 = textFieldCodigo1.getText();
            String codigo2 = textFieldCodigo2.getText();
            

            
            if (validarCopiaDisponible(codigo1)){
                if (validarCopiaDisponible(codigo2)){
                    solicitarCopia(codigo1);
                    solicitarCopia(codigo2);
                    String codigoPrestamo = crearPrestamo();
                    //crearPrestamo();
                    crearPrestamoCopia(codigoPrestamo, codigo1);
                    crearPrestamoCopia(codigoPrestamo, codigo2);
                }
            }
        }
        else if (textFieldCodigo1.isVisible() && textFieldCodigo2.isVisible() && textFieldCodigo3.isVisible()){
            System.out.println("TRES COPIAS");
            
            if (textFieldCodigo1.getText().isEmpty() || textFieldCodigo2.getText().isEmpty() || textFieldCodigo3.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Revisar la información");
                alert.setContentText("¡Ingrese todos los codigos de las copias!");
                alert.showAndWait();
                return;
            }
            
            String codigo1 = textFieldCodigo1.getText();
            String codigo2 = textFieldCodigo2.getText();
            String codigo3 = textFieldCodigo3.getText();
            

            
            if (validarCopiaDisponible(codigo1)){
                if (validarCopiaDisponible(codigo2)){
                    if (validarCopiaDisponible(codigo3)){
                        solicitarCopia(codigo1);
                        solicitarCopia(codigo2);
                        solicitarCopia(codigo3);
                        String codigoPrestamo = crearPrestamo();
                        crearPrestamoCopia(codigoPrestamo, codigo1);
                        crearPrestamoCopia(codigoPrestamo, codigo2);
                        crearPrestamoCopia(codigoPrestamo, codigo3);
                        //CREAR PRESTAMO
                    }
                }
            }
        }
        
        if (resultado){
            buttonAgregarPrestamo1.setVisible(true);
        
            textFieldCodigo2.setVisible(false);
            buttonAgregarPrestamo2.setVisible(false);
            labelPrestamo2.setVisible(false);

            textFieldCodigo3.setVisible(false);
            labelPrestamo3.setVisible(false);

            textFieldCodigo1.setText("");
            textFieldCodigo2.setText("");
            textFieldCodigo3.setText("");
            textFieldRut.setText("");

            buttonQuitarPrestamo3.setVisible(false);
            buttonQuitarPrestamo2.setVisible(false);
        }
        
        
       
    }


    @FXML
    private void quitarPrestamo2(ActionEvent event) {
        
        buttonQuitarPrestamo2.setVisible(false);
        labelPrestamo2.setVisible(false);
        textFieldCodigo2.setVisible(false);
        buttonAgregarPrestamo2.setVisible(false);
        buttonAgregarPrestamo1.setVisible(true);
       
    }

    @FXML
    private void quitarPrestam3(ActionEvent event) {
        buttonQuitarPrestamo3.setVisible(false);
        labelPrestamo3.setVisible(false);
        textFieldCodigo3.setVisible(false);
        buttonAgregarPrestamo2.setVisible(true);
        buttonQuitarPrestamo2.setVisible(true);
    }

    private boolean validarCopiaDisponible(String codigo1) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        
        if (codigo1==null){
            return false;
        }
        
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.validarCopia); // URL to your application
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigo", codigo1.trim()); // All parameters, also easy

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
        for (int c; (c = in.read()) >= 0;){
            response=response + (char)c;
        }
        
        JSONObject obj = new JSONObject(response);
        String mensaje = obj.getString("mensaje");


         if(mensaje.contains("true")){
            String estado = obj.getString("estado");
            
            if (!estado.equals("Habilitado")){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Revisar la informacion");
                alert.setContentText("El libro con codigo de copia #" + codigo1 + " no se encuentra disponible.");

                alert.showAndWait();
                return false;
            }

         }  
         else {
             alertaCodigoIncorrecto(codigo1);
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

    private void solicitarCopia(String codigo1) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        boolean flag=false;
        
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.modificarEstadoCopia); // URL to your application
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigo", codigo1.trim()); // All parameters, also easy
        params.put("estado", "Prestado"); // All parameters, also easy

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
        for (int c; (c = in.read()) >= 0;){
            response=response + (char)c;
        }
        
        JSONObject obj = new JSONObject(response);
        
        String mensaje = obj.getString("mensaje");



        //return flag;
        
    }

    private String crearPrestamo() throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        Calendar fecha1 = Calendar.getInstance();
        Calendar fecha2 = Calendar.getInstance();
        fecha2.add(Calendar.DAY_OF_MONTH, 3);
        
        String dia1 = String.valueOf(fecha1.getTime().getDate());
        String mes1 = String.valueOf(fecha1.getTime().getMonth()+1);
        String año1 = String.valueOf(fecha1.getTime().getYear()+1900);
        
        if (Integer.valueOf(dia1)<10){
            dia1 = "0"+dia1;
        }
        
        if (Integer.valueOf(mes1)<10){
            mes1 = "0"+mes1;
        }
        
        String fechaFinal1 = dia1+"/"+mes1+"/"+año1;
        
        String dia = String.valueOf(fecha2.getTime().getDate());
        String mes = String.valueOf(fecha2.getTime().getMonth()+1);
        String año = String.valueOf(fecha2.getTime().getYear()+1900);
        
        if (Integer.valueOf(dia)<10){
            dia = "0"+dia;
        }
        
        if (Integer.valueOf(mes)<10){
            mes = "0"+mes;
        }
        
        String fechaFinal2 = dia+"/"+mes+"/"+año;
        
        boolean flag=false;
        
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.crearPrestamo); // URL to your application
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("refLector", this.rutLector); // All parameters, also easy
        params.put("refTrabajador", SingletonUsuario.usuario.getUsuario()); // All parameters, also easy
        params.put("fechaDevolucion", fechaFinal2); // All parameters, also easy
        params.put("fechaPrestamo", fechaFinal1); // All parameters, also easy
        
        //System.out.println("Datos: "+this.rutLector+" "+SingletonUsuario.usuario.getUsuario()+" ");
        
        
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
        for (int c; (c = in.read()) >= 0;){
            response=response + (char)c;
        }
        
        JSONObject obj = new JSONObject(response);
        String mensaje = obj.getString("mensaje");
        String codigo = obj.getString("codigo");
        //System.out.println("Codigo"+codigo);
        //System.out.println("Codigo"+codigo);
       

         if(mensaje.equals("true")){
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
            //System.out.println("Codigo devuelto: "+obj.getInt("codigo"));  
            return codigo;
         }  
         else {
        }

        //return flag;
        return null;
    }

    void setCorreo(String text) {
        this.correo = text;
    }

    void setPadre(BorderPane contenido_View) {
        this.padre = contenido_View;
    }    

    private void crearPrestamoCopia(String codigoPrestamo, String codigoCopia) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.crearPrestamoCopia); // URL to your application
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("codigoPrestamo", codigoPrestamo); // All parameters, also easy
        params.put("codigoCopia", codigoCopia); // All parameters, also easy
        params.put("estado", "Pendiente"); // All parameters, also easy
        //System.out.println(codigoPrestamo+" "+codigoCopia);
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
        for (int c; (c = in.read()) >= 0;){
            response=response + (char)c;
        }
        
        JSONObject obj = new JSONObject(response);
        String mensaje = obj.getString("mensaje");
       // String ma = obj.getString("mensaje");

         if(mensaje.contains("true")){
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
         else {
            /* Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Mensaje");
            alerta.setContentText(ma);
            alerta.showAndWait();*/
        }

        //return flag;
    }

    private boolean validarCopias(String text) throws MalformedURLException, UnsupportedEncodingException, IOException, JSONException {
        URL url = new URL(Valores.SingletonServidor.getInstancia().getServidor()+"/"+Valores.ValoresEstaticos.prestamosPorRut); // URL to your application
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("rut", text); // All parameters, also easy
        

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
        for (int c; (c = in.read()) >= 0;){
            response=response + (char)c;
        }
        
        JSONObject obj = new JSONObject(response);
        String mensaje = obj.getString("mensaje");


         if(mensaje.contains("true")){
             return true;
            
         }  
         else {
             return false;
        }
    }
    
    private String getRut(){
        String aux="";
        
        return aux;
    }

    @FXML
    private void limpiarButton(ActionEvent event) {
        this.textFieldCodigo1.setText("");
        this.textFieldCodigo2.setText("");
        this.textFieldCodigo3.setText("");
        
        this.textFieldRut.setDisable(false);
        this.textFieldRut.setText("");
        
        this.labelPrestamo2.setVisible(false);
        this.buttonAgregarPrestamo1.setVisible(true);
        
        this.labelName.setText("");
        this.labelName1.setText("");
        this.labelRut.setText("");
        this.labelRut1.setText("");
        
        this.textFieldCodigo1.setDisable(true);
        this.textFieldCodigo2.setVisible(false);
        this.textFieldCodigo3.setVisible(false);
        
        this.buttonAgregarPrestamo1.setDisable(true);
        this.buttonAgregarPrestamo2.setVisible(false);
        this.buttonQuitarPrestamo2.setVisible(false);
        this.buttonQuitarPrestamo3.setVisible(false);
        
    }

    
}
