
package Valores;

import Modelo.Servidor;

/**
 *
 * @author Meinster
 */
public class SingletonServidor {
    private static Servidor server=null;
    protected SingletonServidor(){
    }
    /**
     * 
     * @return instancia servidor
     */
    public static Servidor getInstancia(){
        if(server==null){
            server =new Servidor();
        }
        return server;
    }
    
    /**
     * 
     */
    public static void destruirInstancia(){
        server = null;
    }
}
