package edu.jsu.mcis.ex2;

import java.io.PrintWriter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.github.cliftonlabs.json_simple.*;

public class PublicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        
        response.setContentType("application/json;charset=UTF-8");
        
        try ( PrintWriter out = response.getWriter()) {
            
            JsonObject json = new JsonObject();
            json.put("success", true);
            json.put("message", "This servlet is available to unauthenticated users!");
            out.println(Jsoner.serialize(json));

        }
        catch (Exception e) {
            e.printStackTrace();
        }            
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Public Servlet";
    }

}