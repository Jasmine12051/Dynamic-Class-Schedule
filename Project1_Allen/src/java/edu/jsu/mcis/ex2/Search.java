package edu.jsu.mcis.ex2;

import java.io.PrintWriter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.ex2.dao.DAOFactory;
import edu.jsu.mcis.ex2.dao.RegistrationDAO;
import edu.jsu.mcis.ex2.dao.SearchDAO;
import jakarta.servlet.ServletContext;
import java.time.LocalTime;
import java.util.Map;

public class Search extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        
        DAOFactory daoFactory = null;

        ServletContext context = request.getServletContext();

        if (context.getAttribute("daoFactory") == null) {
            System.err.println("*** Creating new DAOFactory ...");
            daoFactory = new DAOFactory();
            context.setAttribute("daoFactory", daoFactory);
        }
        else {
            daoFactory = (DAOFactory) context.getAttribute("daoFactory");
        }
        
        response.setContentType("application/json;charset=UTF-8"); 
        
        
        try ( PrintWriter out = response.getWriter()) {
            
            Map<String, String[]> params = request.getParameterMap();
            
            SearchDAO dao = daoFactory.getSearchDAO();
            out.println(dao.find(params));
            

        }
        catch (Exception e) {
            e.printStackTrace();
        }            
        
    }

    @Override
    public String getServletInfo() {
        return "Public Servlet";
    }

}