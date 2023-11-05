import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "LogoutDashboardServlet", urlPatterns = "/_dashboard/logout")
public class LogoutDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;



    // Create a dataSource which registered in web.
    private DataSource dataSource;



    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //logout of session
        request.getSession().invalidate();
        //test redirect
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendRedirect("login.html");
        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }
}
