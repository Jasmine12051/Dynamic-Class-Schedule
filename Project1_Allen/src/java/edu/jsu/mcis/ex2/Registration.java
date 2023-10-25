package edu.jsu.mcis.ex2;

import java.io.PrintWriter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.ex2.dao.DAOFactory;
import edu.jsu.mcis.ex2.dao.RegistrationDAO;
import jakarta.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;

public class Registration extends HttpServlet {

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
            int termid = Integer.parseInt(request.getParameter("termid"));
            String username = request.getRemoteUser();
            
            RegistrationDAO dao = daoFactory.getRegistrationDAO();
            out.println(dao.list(termid, username));
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }            
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
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
        
        response.setContentType("application/json; charset=UTF-8");
        
        try ( PrintWriter out = response.getWriter()) {
            
            String username = request.getRemoteUser();
            int termid = Integer.parseInt(request.getParameter("termid"));
            int crn = Integer.parseInt(request.getParameter("crn"));
            
            RegistrationDAO dao = daoFactory.getRegistrationDAO();
            out.println(dao.create(username, termid, crn));
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
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
        
        BufferedReader br = null;
        response.setContentType("application/json; charset=UTF-8");
        
        try(PrintWriter out = response.getWriter()){
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String p = URLDecoder.decode(br.readLine().trim(), Charset.defaultCharset());
            
            HashMap<String, String> parameters = new HashMap<>();
            
            String[] pairs = p.trim().split("&");
            for (int i = 0; i < pairs.length; ++i) {
                String[] pair = pairs[i].split("=");
                parameters.put(pair[0], pair[1]);
            }
            
            String username = request.getRemoteUser();
            int termid = Integer.parseInt(parameters.get("termid"));
            int crn = Integer.parseInt(parameters.get("crn"));
            
            RegistrationDAO dao = daoFactory.getRegistrationDAO();
            
            out.println(dao.delete(username, termid, crn));

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