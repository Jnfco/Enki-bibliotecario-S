/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Meinster
 */
public class Servidor {
    private String servidor;
    private String puerto;
/**
 * 
 * @param servidor direccion del servidor ejemplo: https://yourAddress.com
 * @param puerto  puerto de conexion.
 */
    public Servidor(String servidor, String puerto) {
        this.servidor = servidor;
        this.puerto = puerto;
    }

    public Servidor(){}
    
    /**
     * 
     * @return direccion del servidor
     */
    public String getServidor() {
        return servidor;
    }

    /**
     * 
     * @param servidor direccion del servidor
     */
    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    /**
     * 
     * @return  puerto de conexion 
     */
    public String getPuerto() {
        return puerto;
    }

    /**
     * 
     * @param puerto de conexion 
     */
    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }
    
    public String getDireccion(){
        return this.servidor+":"+this.puerto;
    }
  
}
