package tbd.grupo1.femdat.auth;

import org.springframework.beans.factory.annotation.Autowired;


public class LoginService {

    protected boolean validate(String user, String password){
        return (user.equals("admin") && password.equals("admin"));
    }
}
