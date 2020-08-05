/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Servidor;
import enki.Enki;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Gerardo Estrada
 */
public class ConexionServidorController implements Initializable {

    @FXML
    private Button buttonProbarConexion;
    @FXML
    private Button buttonAceptar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private TextField textBoxDIreccion;
    @FXML
    private TextField textBoxPuerto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Servidor server = Valores.SingletonServidor.getInstancia();
        this.textBoxDIreccion.setText(server.getServidor());
        this.textBoxPuerto.setText(server.getPuerto());
    }    

    @FXML
    private void OnCLick_ButtonProbarConexion(ActionEvent event) {
        try {
            System.out.println(this.pingHost());
        } catch (MalformedURLException ex) {
            System.out.println("Problemas de conexion");
        } catch (IOException ex) {
             System.out.println("Problemas de conexion");
        }
    }

    //TODO : preguntar confirmacion
    @FXML
    private void OnCLick_ButtonAceptar(ActionEvent event) {
        this.guardarConfiguracion();
        Servidor servidor = Valores.SingletonServidor.getInstancia();
        servidor.setServidor(this.textBoxDIreccion.getText().trim());
        servidor.setPuerto(this.textBoxPuerto.getText().trim());
         ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void OnCLick_ButtonCancelar(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    /**
     * 
     * @return true : si se conecta al host.
     * @throws ProtocolException
     * @throws MalformedURLException
     * @throws IOException 
     */
    private boolean pingHost() throws ProtocolException, MalformedURLException, IOException {
     HttpURLConnection connection = (HttpURLConnection) new URL(this.textBoxDIreccion.getText().trim()+":"+this.textBoxPuerto.getText().trim()).openConnection();
    connection.setRequestMethod("HEAD");
    int responseCode = connection.getResponseCode();
    if (responseCode != 200) {
      return false;
    }
    return true;
}
    
    /**
     * 
     * @return true si se creo el archivo
     */
    public boolean guardarConfiguracion(){
         File archivoServidor = new File(Valores.ValoresEstaticos.carpetaConfiguracion +"/"+Valores.ValoresEstaticos.archivoServidor);
        //System.out.println(archivoServidor.getAbsolutePath());
        if(archivoServidor.exists())
        {
            archivoServidor.delete();
        }  
         try {
                archivoServidor.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivoServidor.getAbsolutePath()));
                writer.write(this.textBoxDIreccion.getText().trim());
                writer.newLine();
                writer.write(this.textBoxPuerto.getText().trim());
                writer.newLine();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Enki.class.getName()).log(Level.SEVERE, null, ex);
            }   
        
        return true;
    }
    
}
