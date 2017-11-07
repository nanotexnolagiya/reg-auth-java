import model.Users;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@javax.servlet.annotation.WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirm_pass = request.getParameter("confirm_pass");
        Users users = new Users();
                List<String> errors = Users.validationRegistration(login, email, password, confirm_pass);
        if(errors.isEmpty()){
            String result = null;
            try {
                result = users.registration(login, email, password);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            if (result.length() > 2){
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(60*60*24*2);
                session.setAttribute("utoken", result);
                response.sendRedirect("/");
            }else{
                errors.add("Попробуйте войти позже!");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("WEB-INF/registration.jsp").forward(request,response);
            }
        }else{
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("WEB-INF/registration.jsp").forward(request,response);
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/registration.jsp").forward(request,response);
        Users users = new Users();
        HttpSession session = request.getSession();
        String utoken = (String) session.getAttribute("utoken");
        String login = users.authentication(utoken);
        if(login.length() > 5){
            response.sendRedirect("/");
        }
    }
}
