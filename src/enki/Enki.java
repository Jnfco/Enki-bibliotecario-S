

package enki;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 *
 * @author jnfco
 */
public class Enki extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("Vistas.MainViewController.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setResizable(false);
        stage.setTitle("Login"); 
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        verificarArchivos();
        launch(args);
    }
    
       public static void verificarArchivos(){
        File carpetaConf = new File(Valores.ValoresEstaticos.carpetaConfiguracion);
        File archivoServidor = new File(carpetaConf.getAbsolutePath()+"/"+Valores.ValoresEstaticos.archivoServidor);
        System.out.println(archivoServidor.getAbsolutePath());
        if(!carpetaConf.exists()){
            carpetaConf.mkdir();
        }
        if(!archivoServidor.exists()){
            try {
                archivoServidor.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivoServidor.getAbsolutePath()));
                writer.write("http://localhost");
                writer.newLine();
                writer.write("8000");
                writer.newLine();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Enki.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
    }
    
}
