import model.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Users users = new Users();
        List<String> errors = Users.validationAuthorization(email, password);
        if(errors.isEmpty()){
            String result = users.authorization(email, password);
            if(result.equals("error")){
                errors.add("E-mail или пароль неверно введен");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("WEB-INF/login.jsp").forward(request,response);
            }else if (result.length() > 5){
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(60*60*24*2);
                session.setAttribute("utoken", result);
                response.sendRedirect("/");
            }else{
                errors.add("Попробуйте войти позже!");
            }
        }else{
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("WEB-INF/login.jsp").forward(request,response);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/login.jsp").forward(request,response);
        Users users = new Users();
        HttpSession session = request.getSession();
        String utoken = (String) session.getAttribute("utoken");
        String login = users.authentication(utoken);
        if(login.length() > 5){
            response.sendRedirect("/");
        }
    }
}
