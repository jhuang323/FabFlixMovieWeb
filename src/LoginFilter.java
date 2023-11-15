import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    private final ArrayList<String> allowedURIs = new ArrayList<>();

    private final ArrayList<String> allowedURIsEmployee = new ArrayList<>();

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("LoginFilter: " + httpRequest.getRequestURI());

        // Check if this URL is allowed to access without logging in
        if (this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI()) || true) //for debugging must disable later
        {
            // Keep default action: pass along the filter chain
            chain.doFilter(request, response);
            return;
        }

        System.out.println(httpRequest.getRequestURI() + " " + this.isUrlEmployeeSection(httpRequest.getRequestURI()));

        if(this.isUrlEmployeeSection(httpRequest.getRequestURI())){
            //the employee section

            //check if employee object exists
            if(httpRequest.getSession().getAttribute("employee") == null){
                if(httpRequest.getRequestURI().endsWith("_dashboard")){

                    httpResponse.sendRedirect("_dashboard/login.html");

                }
                else{

                    //redirect to employee login
                    httpResponse.sendRedirect("login.html");

                }


            }
            else {
                chain.doFilter(request, response);
            }

        }
        else{
            // Redirect to login page if the "user" attribute doesn't exist in session
            if (httpRequest.getSession().getAttribute("user") == null) {
                httpResponse.sendRedirect("login.html");
            } else {

                chain.doFilter(request, response);

            }
        }


    }



    private boolean isUrlAllowedWithoutLogin(String requestURI) {
        /*
         Setup your own rules here to allow accessing some resources without logging in
         Always allow your own login related requests(html, js, servlet, etc..)
         You might also want to allow some CSS files, etc..
         */
        return allowedURIs.stream().anyMatch(requestURI.toLowerCase()::endsWith);
    }

    private boolean isUrlEmployeeSection(String requestURI) {
        /*
         Setup your own rules here to allow accessing some resources without logging in
         Always allow your own login related requests(html, js, servlet, etc..)
         You might also want to allow some CSS files, etc..
         */
        return allowedURIsEmployee.stream().anyMatch(requestURI.toLowerCase()::contains);
    }

    public void init(FilterConfig fConfig) {
        //whitelist of urls that allows for no login
        allowedURIs.add("login.html");
        allowedURIs.add("login.js");
        allowedURIs.add("login.css");
        allowedURIs.add("api/login");

        //dashboard allowed urls
        allowedURIs.add("_dashboard/login.html");
        allowedURIs.add("_dashboard/login.js");
        allowedURIs.add("_dashboard/login.css");
        allowedURIs.add("_dashboard/api/loginemployee");


        //add urls that are part of employee section
        allowedURIsEmployee.add("_dashboard");
    }

    public void destroy() {
        // ignored.
    }

}
