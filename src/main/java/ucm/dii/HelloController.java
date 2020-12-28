package ucm.dii;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class HelloController {

	Hashtable<String, Hashtable<String,Date>> eventos_usuarios= new Hashtable<String, Hashtable<String,Date>> ();
	Hashtable<String, Hashtable<String,Integer>> eventos_intensity= new Hashtable<String, Hashtable<String,Integer>> ();
	Hashtable<String, Date> lastAccess=new Hashtable<String, Date>();
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!! ";
    }
    
    @GetMapping("/sendheart/{event}")
    public synchronized void sendHeart(HttpServletRequest request, @RequestParam(name="user") String usuario, @PathVariable("event") String evento) {
    	if (eventos_usuarios.containsKey(evento)) {
    		Hashtable<String, Date> usuarios = eventos_usuarios.get(evento);
    		Hashtable<String, Integer> intensity = eventos_intensity.get(evento);
    	if(!usuarios.containsKey(usuario)) {
    		usuarios.put(usuario, new Date());
    		intensity.put(usuario,0);
    	} else {
    		if (new Date().getTime()-usuarios.get(usuario).getTime()<2000)
    			intensity.put(usuario,intensity.get(usuario)+1);
    		else
    			intensity.put(usuario,0);
    	}
    	
    	HashSet<String> usuariostemp = new HashSet<String>(usuarios.keySet());
    	for (String actual:usuariostemp) {
    		if (new Date().getTime()-usuarios.get(actual).getTime()>10000) {
    			usuarios.remove(actual);
    			intensity.remove(actual);
    		}
    	}
    	}
    	
    }
    
    @GetMapping("/register/{event}")
    public void getHearts(@RequestParam(name="password")  String password, @PathVariable("event") String evento) {
    	if (password=="mypassword") {
    	 eventos_usuarios.put(evento, new Hashtable<String,Date>());
    	 eventos_intensity.put(evento, new Hashtable<String,Integer>());
    	} 
    }
    
    @GetMapping("/gethearts/{event}")
    @ResponseBody
    public String getHearts(@RequestParam(name="password")  String password, @PathVariable("event") String evento) {
    	if (password=="mypassword") {
    		 return eventos_intensity.get(evento);
    	}
        
    }

}
