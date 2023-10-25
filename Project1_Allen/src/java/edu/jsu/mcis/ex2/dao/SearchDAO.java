package edu.jsu.mcis.ex2.dao;

import com.github.cliftonlabs.json_simple.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchDAO {
    
    private final DAOFactory daoFactory;
    private static final String QUERY_FIND = "SELECT course.*, section.*, "
    + "term.name AS termname, term.`start` AS termstart, term.`end` AS termend, "
    + "scheduletype.description as scheduletype, `level`.description as `level` "
    + "FROM ((((section JOIN scheduletype ON section.scheduletypeid = scheduletype.id) "
    + "JOIN course ON section.subjectid = course.subjectid AND section.num = course.num) "
    + "JOIN `level` ON course.levelid = `level`.id) "
    + "JOIN term ON section.termid = term.id) "
    + "WHERE ((? IS NULL OR course.subjectid = ?) "    // subjectid (parameters 1 & 2)
    + "AND (? IS NULL OR course.num = ?) "             // num (parameters 3 & 4)
    + "AND (? IS NULL OR `level`.id = ?) "             // levelid (parameters 5 & 6)
    + "AND (? IS NULL OR section.scheduletypeid = ?) " // scheduletypeid (parameters 7 & 8)
    + "AND (? IS NULL OR section.`start` >= ?) "       // start as LocalTime (parameters 9 & 10)
    + "AND (? IS NULL OR section.`end` <= ?) "         // end as LocalTime (parameters 11 & 12)
    + "AND (? IS NULL OR section.days REGEXP ?) "      // days (ex: "M|W|F") (parameters 13 & 14)
    + "AND (section.termid = ?)) "                     // termid (parameter 15)
    + "ORDER BY course.num, section";
    
    
    SearchDAO(DAOFactory dao) {
        this.daoFactory = dao;
    }
    
    
    public String find(Map<String, String[]> params){

        JsonObject results = new JsonObject();
        results.put("success", false);

        Connection conn = daoFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(QUERY_FIND);
            
            String subjectid = (params.get("subjectid") != null ? params.get("subjectid")[0] : null);
            String num = (params.get("num") != null ? params.get("num")[0] : null);
            String levelid = (params.get("levelid") != null ? params.get("levelid")[0] : null);
            String scheduletypeid = (params.get("scheduletypeid") != null ? params.get("scheduletypeid")[0] : null);
            String start = (params.get("start") != null ? params.get("start")[0] : null);
            String end = (params.get("end") != null ? params.get("end")[0] : null);
            String days = (params.get("days") != null ? String.join("|", (params.get("days")[0]).split("")) : null);
            String termid = (params.get("termid") != null ? params.get("termid")[0] : null);
           
          
            ps.setString(1, subjectid);
            ps.setString(2, subjectid);
            ps.setString(3, num);
            ps.setString(4, num);
            ps.setString(5, levelid);
            ps.setString(6, levelid);
            ps.setString(7, scheduletypeid);
            ps.setString(8, scheduletypeid);
            ps.setString(9, start);
            ps.setString(10, start);
            ps.setString(11, end);
            ps.setString(12, end);
            ps.setString(13, days);
            ps.setString(14, days);
            ps.setString(15, termid);
            
            System.err.println(ps.toString());

            
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
                    json.put("levelid", rs.getString("levelid"));                    
                    json.put("start", rs.getString("start"));
                    json.put("end", rs.getString("end"));
                    json.put("days", rs.getString("days"));
                    json.put("termid", rs.getString("termid"));
                    json.put("description", rs.getString("description"));
                    json.put("scheduletype", rs.getString("scheduletype"));
                    json.put("level", rs.getString("level"));
                    json.put("section", rs.getString("section"));
                    json.put("termend", rs.getString("termend"));
                    json.put("termname", rs.getString("termname"));
                    json.put("instructor", rs.getString("instructor"));
                    json.put("credits", rs.getString("credits"));
                    json.put("where", rs.getString("where"));
                    json.put("termstart", rs.getString("termstart"));
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
}
       

    
    
