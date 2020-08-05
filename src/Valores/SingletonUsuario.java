/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Valores;

import Modelo.Usuario;

/**
 *
 * @author Gerardo
 */
public class SingletonUsuario {
    public static Usuario usuario=null;
    
    public static void getInstancia(){
        if(SingletonUsuario.usuario==null){
            SingletonUsuario.usuario= new Usuario();
        }
    }
    
    public static void setUsuario(String usuario,String tipo){
        SingletonUsuario.getInstancia();
        SingletonUsuario.usuario.setUsuario(usuario);
        SingletonUsuario.usuario.setTipoUsuario(tipo);
    }
    
    public static void eliminarUsuario(){
        SingletonUsuario.usuario = null;
        System.gc();
    }
    
    
    
}
