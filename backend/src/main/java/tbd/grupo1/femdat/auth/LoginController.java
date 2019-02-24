package tbd.grupo1.femdat.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.configurationprocessor.json.JSONStringer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@CrossOrigin(origins = "*")
@ComponentScan(basePackages="tbd.grupo1.femdat")
@RestController
@EnableAutoConfiguration
@RequestMapping("/login")
public class LoginController {
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public boolean login(@RequestBody User user){
        if(this.validate(user.getUsername(), user.getPassword())){
            CrearSesion sesion = new CrearSesion(user);
            return true;
        }
        return false;
    }

    private boolean validate(String username, String password){
        return (username.equals("admin") && password.equals("admin"));
    }
}
