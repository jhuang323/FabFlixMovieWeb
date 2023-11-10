package _dashboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

@WebServlet(name = "EmployeeDashServlet", urlPatterns = "/_dashboard/api/EmployeeDash")
public class EmployeeDashServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private DataSource dataSource;
    public static final String metaDataQuery = "SELECT TABLE_NAME, COLUMN_NAME, COLUMN_TYPE\n" +
            "FROM information_schema.columns \n" +
            "WHERE table_schema = \'moviedb\' \n " +
            "ORDER BY TABLE_NAME;";

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement metaDataStatement = connection.prepareStatement(metaDataQuery);
            ResultSet metaDataResultSet = metaDataStatement.executeQuery();

            String table_name= "";
            String column_name = "";
            String column_type = "";
            HashMap<String, Table> metaDataMap = new HashMap<String, Table>();
            while(metaDataResultSet.next()){
                table_name = metaDataResultSet.getString("TABLE_NAME");
                column_name = metaDataResultSet.getString("COLUMN_NAME");
                column_type = metaDataResultSet.getString("COLUMN_TYPE");
                if(metaDataMap.get(table_name) != null){
                    metaDataMap.get(table_name).addColumn(column_name, column_type);
                }
                else{
                    metaDataMap.put(metaDataResultSet.getString("TABLE_NAME"),
                            new Table(table_name, column_name, column_type));
                    metaDataMap.get(table_name).addColumn(column_name, column_type);
                }
            }

            JsonArray metaData = new JsonArray();

            for (HashMap.Entry<String, Table> entrySet : metaDataMap.entrySet()) {

                JsonObject tableObject = new JsonObject();
                tableObject.addProperty("table_name", entrySet.getKey());

                JsonArray columnsArray = new JsonArray();
                HashMap<String, String> columnData = entrySet.getValue().getColumns();

                for(HashMap.Entry<String, String> entrySetColumns : columnData.entrySet()){
                    JsonObject columnInfo = new JsonObject();
                    columnInfo.addProperty("column_name", entrySetColumns.getKey());
                    columnInfo.addProperty("column_type", entrySetColumns.getValue());
                    columnsArray.add(columnInfo);
                }
                tableObject.add("column_info", columnsArray);
                metaData.add(tableObject);
            }
            out.write(metaData.toString());
            response.setStatus(200);

        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            request.getServletContext().log("Error:", e);
            response.setStatus(500);
        } finally {
            out.close();
        }

    }

}
