package nations;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private final static String URL = "jdbc:mysql://localhost:3306/db_nations";
    private final static String USER = "root";
    private final static String PW = "Java2023!";
    private final static String QUERY = "SELECT c.name AS country_name, c.country_id, r.name AS region, c2.name AS continent \n" +
        "FROM countries c\n" +
        "JOIN regions r ON c.region_id = r.region_id \n" +
        "JOIN continents c2 ON r.continent_id = c2.continent_id \n" +
        "ORDER BY c.name;";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try(Connection connection = DriverManager.getConnection(URL, USER, PW)){
            try(PreparedStatement ps = connection.prepareStatement(QUERY)){
                try(ResultSet rs = ps.executeQuery(QUERY)){
                    while(rs.next()){
                        String countryName= rs.getString("country_name");
                        int countryId = rs.getInt("country_id");
                        String region=rs.getString("region");
                        String continent=rs.getString("continent");
                        System.out.println(countryName + " | " + countryId + " | " + region + " | " + continent);
                    }
                }
            }

        } catch (SQLException e){
            System.out.print("Connessione al database non riuscita");
        }

        scan.close();
    }
}
