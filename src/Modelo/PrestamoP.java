/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Controladores.DetalleCopiaController;
import Controladores.DevolverPrestamoController;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONException;

/**
 *
 * @author Christian
 */
public class PrestamoP {
    
    private Integer codigo;
    private String refLector;
    private String refTrabajador;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private String estado;
    private Button detalle;
    private BorderPane padre;

    public PrestamoP(BorderPane padre, Integer codigo, String refLector, String refTrabajador, String fechaPrestamo, String fechaDevolución, String estado) {
        this.codigo = codigo;
        this.refLector = refLector;
        this.refTrabajador = refTrabajador;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolución;
        this.estado = estado;
        this.detalle = new Button("Ver detalle");
        this.padre = padre;
        
        this.detalle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/enki/DevolverPrestamo.fxml"));
                    Parent root = loader.load();
                    DevolverPrestamoController m = loader.getController();
                    m.setPrestamo(codigo);
                    m.setPadre(padre);
                    padre.setCenter(root);
                } catch (IOException ex) {
                    Logger.getLogger(Libro.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSONException ex) {
                    Logger.getLogger(PrestamoP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getRefLector() {
        return refLector;
    }

    public void setRefLector(String refLector) {
        this.refLector = refLector;
    }

    public String getRefTrabajador() {
        return refTrabajador;
    }

    public void setRefTrabajador(String refTrabajador) {
        this.refTrabajador = refTrabajador;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolución) {
        this.fechaDevolucion = fechaDevolución;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Button getDetalle() {
        return detalle;
    }

    public void setDetalle(Button detalle) {
        this.detalle = detalle;
    }

    public BorderPane getPadre() {
        return padre;
    }

    public void setPadre(BorderPane padre) {
        this.padre = padre;
    }

    
    
}
