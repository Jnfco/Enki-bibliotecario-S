/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Controladores.ConfigurarLibrosController;
import Controladores.DetalleCopiaController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author jnfco
 */
public class CopiaP {
    
    private String codigo;
    private String estado;
    private String numeroCopia;
    private String isbn;
    private Integer refEstante;
    private Integer refNivel;
    private String nombreLibro;

    public CopiaP(String codigo, String estado, String numeroCopia, String isbn, Integer refEstante, 
            Integer refNivel, String nombreLibro) {
        this.codigo = codigo;
        this.estado = estado;
        this.numeroCopia = numeroCopia;
        this.isbn = isbn;
        this.refEstante = refEstante;
        this.refNivel = refNivel;
        this.nombreLibro = nombreLibro;
    }
    
    public CopiaP(String codigo, String nombreLibro){
        this.codigo = codigo;
        this.nombreLibro = nombreLibro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumeroCopia() {
        return numeroCopia;
    }

    public void setNumeroCopia(String numeroCopia) {
        this.numeroCopia = numeroCopia;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getRefEstante() {
        return refEstante;
    }

    public void setRefEstante(Integer refEstante) {
        this.refEstante = refEstante;
    }

    public Integer getRefNivel() {
        return refNivel;
    }

    public void setRefNivel(Integer refNivel) {
        this.refNivel = refNivel;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public void setAll(CopiaP get) {
        this.codigo = get.codigo;
        this.estado = get.estado;
        this.isbn = get.isbn;
        this.nombreLibro = get.nombreLibro;
        this.numeroCopia = get.numeroCopia;
        this.refEstante = get.refEstante;
        this.refNivel = get.refEstante;
    }

    

    
    
}
