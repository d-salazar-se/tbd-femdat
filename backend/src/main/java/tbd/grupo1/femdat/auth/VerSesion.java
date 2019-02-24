package tbd.grupo1.femdat.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/VerSession")
public class VerSesion extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @RequestMapping(value = "/verSession", method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession misession = (HttpSession) request.getSession();
        User user = (User) misession.getAttribute("user");

        System.out.println(user);

    }

    @RequestMapping(value = "/getSesion", method = RequestMethod.GET)
    protected User getSesion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession misession = (HttpSession) request.getSession();
        User user = (User) misession.getAttribute("user");
        return user;

    }
}
