package nations;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private final static String URL = "jdbc:mysql://localhost:3306/db_nations";
    private final static String USER = "root";
    private final static String PW = "Java2023!";
    private final static String QUERY = "SELECT c.name AS country_name, c.country_id, r.name AS region, c2.name AS continent " +
            "FROM countries c " +
            "JOIN regions r ON c.region_id = r.region_id " +
            "JOIN continents c2 ON r.continent_id = c2.continent_id " +
            "WHERE c.name LIKE ? ORDER BY c.name;";
    private final static String ID_QUERY= "SELECT *\n" +
            "FROM countries c \n" +
            "JOIN country_languages cl ON c.country_id = cl.country_id \n" +
            "JOIN languages l ON cl.language_id = l.language_id \n" +
            "JOIN country_stats cs ON c.country_id =cs.country_id \n" +
            "WHERE c.country_id = ? ;";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try(Connection connection = DriverManager.getConnection(URL, USER, PW)){
            System.out.println("Search... ");
            String search = "%"+scan.nextLine()+"%";
            System.out.println("Results for '" + search + "' ...");
            try(PreparedStatement ps = connection.prepareStatement(QUERY)){
               ps.setString(1, search);
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                            String countryName= rs.getString("country_name");
                            int countryId = rs.getInt("country_id");
                            String region=rs.getString("region");
                            String continent=rs.getString("continent");
                            System.out.println(countryName + " | " + countryId + " | " + region + " | " + continent);
                        }
                }
            }
            System.out.println("Enter country id to see details: ");
            int choiceId = Integer.parseInt(scan.nextLine());
            try (PreparedStatement ps = connection.prepareStatement(ID_QUERY)){
                ps.setInt(1, choiceId);
                try (ResultSet rs = ps.executeQuery()){
                    rs.next();
                        int countryId = rs.getInt("country_id");
                        String countryName=rs.getString("name");
                        System.out.println("Details for: " + countryName);
                        System.out.println(countryId);
                    System.out.print("Languages: ");
                    ArrayList<String> languages = new ArrayList<String>();

                        while (rs.next()){
                            String language = rs.getString("language");
                            languages.add(language);
                        }
                    System.out.println("Languages:" +languages);

                }
            }
        } catch (SQLException e){
            System.out.print("Connessione al database non riuscita");
        }

        scan.close();
    }
}
