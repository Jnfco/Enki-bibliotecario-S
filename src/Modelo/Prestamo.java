/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author jnfco
 */
public class Prestamo {
    
    
   private String codigo;
   private String refLector;
   private String fechaPrestamo;
   private String fechaDevolucion;

    public Prestamo(String codigo, String refLector, String fechaPrestamo, String fechaDevolucion) {
        this.codigo = codigo;
        this.refLector = refLector;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getRefLector() {
        return refLector;
    }

    public void setRefLector(String refLector) {
        this.refLector = refLector;
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

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

   
}
