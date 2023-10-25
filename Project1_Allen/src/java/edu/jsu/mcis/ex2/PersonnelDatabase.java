package edu.jsu.mcis.ex2;

import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class PersonnelDatabase {

    private DataSource ds = null;

    private final String QUERY_SELECT_BY_NAME = "SELECT * FROM people WHERE lastname = ?";

    public PersonnelDatabase() {

        try {

            Context envContext = new InitialContext();
            Context initContext = (Context) envContext.lookup("java:/comp/env");
            ds = (DataSource) initContext.lookup("jdbc/db_pool");

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getPersonnelAsHTML(String lastname) {

        String html = null;

        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(QUERY_SELECT_BY_NAME);
            ps.setString(1, lastname);

            if (ps.execute()) {

                rs = ps.getResultSet();
                html = getResultSetAsTable(rs);

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
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }

        }

        return html;

    }

    private String getResultSetAsTable(ResultSet resultset) {

        StringBuilder table = new StringBuilder();

        try {

            StringBuilder tableheading = new StringBuilder();
            StringBuilder tablerow;

            ResultSetMetaData metadata = resultset.getMetaData();

            int numberOfColumns = metadata.getColumnCount();

            table.append("<table border=\"1\">");
            tableheading.append("<tr>");

            for (int i = 1; i <= numberOfColumns; i++) {

                String key = metadata.getColumnLabel(i);
                tableheading.append("<th>").append(key).append("</th>");

            }

            tableheading.append("</tr>");

            table.append(tableheading.toString());

            while (resultset.next()) {

                tablerow = new StringBuilder();
                tablerow.append("<tr>");

                for (int i = 1; i <= numberOfColumns; i++) {

                    String value = resultset.getString(i);

                    if (resultset.wasNull()) {
                        tablerow.append("<td></td>");
                    }
                    else {
                        tablerow.append("<td>").append(value).append("</td>");
                    }

                }

                tablerow.append("</tr>");

                table.append(tablerow.toString());

            }

            table.append("</table><br />");

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return table.toString();

    }

    private Connection getConnection() {

        Connection c = null;

        try {

            if (ds != null) {
                c = ds.getConnection();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return c;

    }

}