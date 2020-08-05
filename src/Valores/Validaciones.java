package Valores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author jnfo
 */
public class Validaciones {
    public static EventHandler<KeyEvent> ValidacionRut(final Integer max_Lengh) {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            TextField txt_TextField = (TextField) e.getSource();                
            if (txt_TextField.getText().length() >= max_Lengh) {                    
                e.consume();
            }
            if(e.getCharacter().matches("[0-9]")){ 
                if(txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")){
                    e.consume();
                }else if(txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
                    e.consume(); 

                }
            }else{
                e.consume();
            }
        }
    };
}        
    
    public static EventHandler<KeyEvent> ValidacionMaxString(final Integer max_Lengh) {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            TextField txt_TextField = (TextField) e.getSource();                
            if (txt_TextField.getText().length() >= max_Lengh) {                    
                e.consume();
            }else{
                //e.consume();
            }
        }
    };
    
    
}        
    
        public static EventHandler<KeyEvent> ValidacionMaxNumero(final Integer max_Lengh) {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            TextField txt_TextField = (TextField) e.getSource(); 
            int pos = txt_TextField.getText().length()-1;
            char ultimo = txt_TextField.getText().charAt(pos);
            if (!Character.isDigit(ultimo)){
                e.consume();
                return;
            }
            if (txt_TextField.getText().length() >= max_Lengh) {                    
                e.consume();
            }else{
                //e.consume();
            }
        }
    };
    
    
}     
    
   public static boolean isValidEmailAddress(String email) {
   boolean result = true;
   try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
   } catch (AddressException ex) {
      result = false;
   }
   return result;
}
    
    public static Boolean validaRut ( String rut ) {
       Pattern pattern = Pattern.compile("([0-9]{2}|[0-9]{1})+.[0-9]{3}+.[0-9]{3}+-[0-9kK]{1}$");                
       Matcher matcher = pattern.matcher(rut);
       if ( matcher.matches() == false ) 
           return false;
       String[] stringRut = rut.split("-");
       System.out.println(stringRut[0].replace(".", ""));
       return stringRut[1].toLowerCase().equals(dv(stringRut[0].replace(".", "")));
   }
        public static String dv ( String rut ) {
            Integer M=0,S=1,T=Integer.parseInt(rut);
            for (;T!=0;T=(int) Math.floor(T/=10))
            S=(S+T%10*(9-M++%6))%11;
            return ( S > 0 ) ? String.valueOf(S-1) : "k";		
    }
        
        public static Boolean validaISBN ( String isbn ) {
       Pattern pattern = Pattern.compile("[0-9]{3}+-[0-9]{3}+-[0-9]{3}+-[0-9]{3}+-[0-9]{1}$");                
       Matcher matcher = pattern.matcher(isbn);
       if ( matcher.matches() == false ) {
           return false;
       }
       else{
           return true;
       }
   }
    
}
