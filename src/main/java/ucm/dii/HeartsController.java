package ucm.dii;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class HeartsController {

	Hashtable<String, Hashtable<String,Date>> eventos_usuarios= new Hashtable<String, Hashtable<String,Date>> ();
	Hashtable<String, Hashtable<String,Integer>> eventos_intensity= new Hashtable<String, Hashtable<String,Integer>> ();
	Hashtable<String, Date> lastAccess=new Hashtable<String, Date>();

	private static int MAX_HEARTS=3;
	private static long INTENSITY_WAIT=1000;
	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!! ";
	}

	@GetMapping("/sendheart/{event}/{user}")
	public synchronized void sendHeart(HttpServletRequest request, 
			@PathVariable(name="user") String usuario, 
			@PathVariable("event") String evento) {
		if (eventos_usuarios.containsKey(evento)) {
			Hashtable<String, Date> usuarios = eventos_usuarios.get(evento);
			Hashtable<String, Integer> intensity = eventos_intensity.get(evento);
			if(!usuarios.containsKey(usuario)) {
				usuarios.put(usuario, new Date());
				intensity.put(usuario,0);
			} else {
				usuarios.put(usuario, new Date());
				if (intensity.get(usuario)<MAX_HEARTS) // to not create too many balloons
					intensity.put(usuario,intensity.get(usuario)+1);    		
			}    	
		}
	}


	@GetMapping("/removeheart/{event}/{user}")
	public synchronized void removeHeart(HttpServletRequest request,
			@PathVariable("user") String usuario,
			@PathVariable("event") String evento) {
		if (eventos_usuarios.containsKey(evento)) {
			Hashtable<String, Date> usuarios = eventos_usuarios.get(evento);
			Hashtable<String, Integer> intensity = eventos_intensity.get(evento);
			if(usuarios.containsKey(usuario)) {
				intensity.put(usuario,intensity.get(usuario)-1);
			}    	
		}
	}

	@GetMapping("/register/{event}")
	public void registerEvent(
			@RequestParam(name="password")  String password, 
			@PathVariable("event") String evento) {
		if (password.equals("mypassword") && !eventos_usuarios.containsKey(evento)) {
			eventos_usuarios.put(evento, new Hashtable<String,Date>());
			eventos_intensity.put(evento, new Hashtable<String,Integer>());
		} 
	}

	Hashtable<String, Date> lastCheck=new Hashtable<String, Date>(); 
	@GetMapping("/gethearts/{event}")
	@ResponseBody
	public synchronized Map<String,Integer> getHearts(@RequestParam(name="password")  String password, @PathVariable("event") String evento) {
		if (password.equals("mypassword")  && eventos_usuarios.containsKey(evento)) {
			Hashtable<String, Date> usuarios = eventos_usuarios.get(evento);
			Hashtable<String, Integer> intensity = eventos_intensity.get(evento);
			HashSet<String> usuariostemp = new HashSet<String>(usuarios.keySet());

			for (String actual:usuariostemp) {        		
				long delay=new Date().getTime()-usuarios.get(actual).getTime();
				if (delay>INTENSITY_WAIT*MAX_HEARTS) {        			
					usuarios.remove(actual);
					intensity.remove(actual);
				} else { 
					if (lastCheck.containsKey(actual)) {
						if ((new Date().getTime()-lastCheck.get(actual).getTime())>INTENSITY_WAIT && intensity.get(actual)>0) {
							lastCheck.put(actual, new Date());
							intensity.put(actual,intensity.get(actual)-1);
						}
					}	else {
						lastCheck.put(actual, new Date());
					}

				}
			}
			return eventos_intensity.get(evento);
		}
		return new Hashtable<String,Integer>();

	}

}
