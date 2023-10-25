package edu.jsu.mcis.ex2.dao;

import com.github.cliftonlabs.json_simple.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationDAO {
    
    private final DAOFactory daoFactory;
    
    private final String QUERY_USERNAME = "SELECT * FROM student WHERE username = ?";
    private final String QUERY_CREATE = "INSERT INTO "
            + "registration (studentid, termid, crn) VALUES (?, ?, ?)";
    private final String QUERY_DELETE = "DELETE FROM registration WHERE studentid=? AND termid=? AND crn=?";
    private final String QUERY_LIST = "SELECT registration.*, section.*, course.* " 
            + "FROM Registration " 
            + "INNER JOIN Section ON Registration.termid = Section.termid AND "
            + "Registration.crn = Section.crn " 
            + "INNER JOIN Course ON Section.subjectid = Course.subjectid AND Section.num = Course.num "
            + "WHERE registration.termid = ? and registration.studentid = ?";
    
    
    
    RegistrationDAO(DAOFactory dao) {
        this.daoFactory = dao;
    }
    
    public String list(int termid, String username){

        JsonObject results = new JsonObject();
        results.put("success", false);

        Connection conn = daoFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(QUERY_LIST);
            
            ps.setInt(1, termid);
            ps.setInt(2, findStudentID(username));
            
            boolean hasresults = ps.execute();

            if (hasresults) {
                
                results.put("success", true);
                
                JsonArray data = new JsonArray();

                rs = ps.getResultSet();
                
                while (rs.next()) {
                    
                    JsonObject json = new JsonObject();

                    json.put("subjectid", rs.getString("subjectid"));
                    json.put("scheduletypeid", rs.getString("scheduletypeid"));
                    json.put("num", rs.getString("num"));                   
                    json.put("start", rs.getString("start"));
                    json.put("end", rs.getString("end"));
                    json.put("days", rs.getString("days"));
                    json.put("termid", rs.getString("termid"));
                    json.put("description", rs.getString("description"));
                    json.put("levelid", rs.getString("levelid"));
                    json.put("section", rs.getString("section"));
                    json.put("instructor", rs.getString("instructor"));
                    json.put("credits", rs.getString("credits"));
                    json.put("where", rs.getString("where"));
                    json.put("crn", rs.getString("crn"));
                    
                    
                    data.add(json);
                    
                                        
                }
                
                results.put("data", data);
                        

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                }
                catch (Exception e) {
                    e.printStackTrace(); 
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                }
                catch (Exception e) {
                    e.printStackTrace(); 
                }
            } 
        }
        return Jsoner.serialize(results);
    }
    
    
    public Boolean create(String username, int termid, int crn) {
        Connection conn = daoFactory.getConnection();
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        
        try{
            
            ps1 = conn.prepareStatement(QUERY_CREATE);
            ps1.setInt(1, findStudentID(username));
            ps1.setInt(2, termid);
            ps1.setInt(3, crn);
            
            int rowsInserted = ps1.executeUpdate();
            return rowsInserted == 1;
            
        }
        catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as per your application's error handling approach
        }
        finally {
            if (ps1 != null) {
                try {
                    ps1.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return false;
    }
    
    public Boolean delete(String username, int termid, int crn) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = daoFactory.getConnection();
            ps = conn.prepareStatement(QUERY_DELETE);
            ps.setInt(1, findStudentID(username));
            ps.setInt(2, termid);
            ps.setInt(3, crn);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
            
        }
        catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as per your application's error handling approach
        }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return false;
    }
    
    private int findStudentID(String username) {
        
        int result = 0;

        JsonObject json = new JsonObject();
        json.put("success", false);

        Connection conn = daoFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(QUERY_USERNAME);
            ps.setString(1, username);
            
            boolean hasresults = ps.execute();

            if (hasresults) {

                rs = ps.getResultSet();
                
                if (rs.next()) {
                    
                    result = rs.getInt("id");
                                        
                }

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }

        }

        return result;

    }
 
    
}
       

    
    
