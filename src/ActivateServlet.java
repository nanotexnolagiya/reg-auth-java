import model.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "ActivateServlet", urlPatterns = "/activate")
public class ActivateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("token") != null){
            String token = request.getParameter("token");
            Users users = new Users();
            if (users.activateUser(token)){
                response.sendRedirect("/");
            }else{
                response.sendRedirect("/registration");
            }
        }else{
            response.sendRedirect("/registration");
        }
    }
}
