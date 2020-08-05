/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;


import Controladores.DetalleCopiaController;
import Controladores.ConfigurarEstanteController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author mati_
 */
public class Estante {
    
    
   String id; 
   String codigo;
   String cantidadniveles;
   String intervaloinf;
   String intervalosup;
    Button buttonConfigurar;

    public Estante(String id, String codigo,String cantidadniveles,String intervaloinf, String intervalosup) {
        this.codigo = codigo;
        this.cantidadniveles = cantidadniveles;
        this.intervaloinf = intervaloinf;
        this.intervalosup = intervalosup;
        this.buttonConfigurar = new Button("Modificar");
        if(id.equals("-1"))
        {
            //System.out.println("Codigo: "+codigo);
            this.codigo = "Generado por sistema";
            this.buttonConfigurar.setVisible(false);
        }
        
        this.buttonConfigurar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/enki/ConfigurarEstante.fxml"));
                    
                    Parent principalParent = fxmlLoader.load();
                    
                    ConfigurarEstanteController controller = fxmlLoader.getController();
                    controller.setCodigoEstante(id);
                    //controller.setCantidadNiveles(cantidadniveles, codigo);
                    
                    Scene scene = null;
                    
                    scene = new Scene(principalParent);
                    Stage configurarEstante = new Stage();
                    configurarEstante.setMinWidth(400);
                    configurarEstante.setMinHeight(150);
                    configurarEstante.setMaxWidth(600);
                    configurarEstante.setMaxHeight(300);
                    
                    configurarEstante.setTitle("Configurar Estante");
                    configurarEstante.setScene(scene);
                    configurarEstante.initModality(Modality.APPLICATION_MODAL);
                    controller.setCantidadNiveles(cantidadniveles,codigo);
                    configurarEstante.show();
                } catch (IOException ex) {
                    Logger.getLogger(Estante.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCantidadniveles() {
        return cantidadniveles;
    }

    public void setCantidadniveles(String cantidadniveles) {
        this.cantidadniveles = cantidadniveles;
    }

    public String getIntervaloinf() {
        return intervaloinf;
    }

    public void setIntervaloinf(String intervaloinf) {
        this.intervaloinf = intervaloinf;
    }

    public String getIntervalosup() {
        return intervalosup;
    }

    public void setIntervalosup(String intervalosup) {
        this.intervalosup = intervalosup;
    }
 
    public Button getButtonConfigurar() {
        return buttonConfigurar;
    }

    public void setButtonConfigurar(Button buttonConfigurar) {
        this.buttonConfigurar = buttonConfigurar;
    }
    
    
    
    
}
