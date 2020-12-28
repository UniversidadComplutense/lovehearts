package ucm.dii;

import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Hashtable;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloCoDateller {

	Hashtable<String,Date> usuarios=new Hashtable<String,Date>();
	Hashtable<String,Integer> intensity=new Hashtable<String,Integer>();
	int contador=0;
	
    @RequestMapping("/")
    public String index() {
    	contador++;
        return "Greetings from Spring Boot!! "+contador;
    }
    
    
    public String sendHeart(String usuario) {
    	if(usuarios.containsKey(usuario))
    		usuarios.put(usuario, new Date());
        return "Greetings from Spring Boot!!";
    }
    
    public String getHearts() {
        return "Greetings from Spring Boot!!";
    }

}
