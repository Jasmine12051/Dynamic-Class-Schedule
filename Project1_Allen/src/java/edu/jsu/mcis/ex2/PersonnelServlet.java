package edu.jsu.mcis.ex2;

import java.io.PrintWriter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PersonnelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        PersonnelDatabase db = null;

        ServletContext context = request.getServletContext();

        if (context.getAttribute("db") == null) {
            System.err.println("*** Creating New PersonnelDatabase Instance ...");
            db = new PersonnelDatabase();
            context.setAttribute("db", db);
        }
        else {
            db = (PersonnelDatabase) context.getAttribute("db");
        }

        response.setContentType("text/html;charset=UTF-8");

        try ( PrintWriter out = response.getWriter()) {

            String lastname = request.getParameter("search");

            out.println("<p>Search Parameter: " + lastname + "</p>");
            out.println(db.getPersonnelAsHTML(lastname));

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
        return "Test Servlet";
    }

}