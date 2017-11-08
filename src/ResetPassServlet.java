import model.Users;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "ResetPassServlet", urlPatterns = "/reset_password")
public class ResetPassServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        List<String> errors = new ArrayList<>();
        if (Users.issetEmail(email)){
            try {
                Users.sendResetPassword(email);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }else{
            errors.add("Такой E-mail не существует");
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("WEB-INF/reset_pass.jsp").forward(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/reset_pass.jsp").forward(request,response);
    }
}
