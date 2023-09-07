package nations;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private final static String URL = "jdbc:mysql://localhost:3306/db_nations";
    private final static String USER = "root";
    private final static String PW = "Java2023!";
    private final static String SEARCH_COUNTRIES_BY_INPUT = "SELECT c.name AS country_name, c.country_id, r.name AS region, c2.name AS continent " +
            "FROM countries c " +
            "JOIN regions r ON c.region_id = r.region_id " +
            "JOIN continents c2 ON r.continent_id = c2.continent_id " +
            "WHERE c.name LIKE ? ORDER BY c.name;";
    private final static String LANGUAGES_BY_COUNTRY_ID= "SELECT c.country_id, c.name AS country_name, l.`language` \n" +
            "FROM countries c \n" +
            "JOIN country_languages cl ON c.country_id = cl.country_id \n" +
            "JOIN languages l ON cl.language_id = l.language_id \n" +
            "WHERE c.country_id = ? ;";
    private final static String RECENT_STATS_BY_COUNTRY_ID = "SELECT cs.year, cs.population, cs.gdp\n" +
            "FROM country_stats cs\n" +
            "WHERE country_id = ? \n" +
            "ORDER BY `year` DESC;";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try(Connection connection = DriverManager.getConnection(URL, USER, PW)){
            System.out.println("Search... ");
            String search = "%"+scan.nextLine()+"%";
            System.out.println("Results for '" + search + "' ...");
            try(PreparedStatement ps = connection.prepareStatement(SEARCH_COUNTRIES_BY_INPUT)){
               ps.setString(1, search);
                try(ResultSet rs = ps.executeQuery()){
                    System.out.println(" COUNTRY | ID | REGION | CONTINENT ");
                    while(rs.next()){
                            String countryName= rs.getString("country_name");
                            int countryId = rs.getInt("country_id");
                            String region=rs.getString("region");
                            String continent=rs.getString("continent");
                            System.out.println(countryName + " | " + countryId + " | " + region + " | " + continent);
                        }
                }
            } catch (SQLException e){
                System.out.println("Error with searching engine.");
            }
            System.out.println();
            System.out.println("Enter country id to see details: ");
            int choiceId = Integer.parseInt(scan.nextLine());

            try (PreparedStatement ps = connection.prepareStatement(LANGUAGES_BY_COUNTRY_ID)){
               ps.setInt(1, choiceId);
                try (ResultSet rs = ps.executeQuery()){
                    rs.next();
                    int countryId = rs.getInt("country_id");
                    String countryName=rs.getString("country_name");
                    System.out.println("Details for: " + countryName + "   ID: " + countryId);
                    System.out.print("Languages: ");
                    String language =rs.getString("language");
                    System.out.print(language);
                    while(rs.next()){
                        System.out.print(", ");
                        language =rs.getString("language");
                        System.out.print(language);
                    }
                    System.out.println();
                }
            } catch (SQLException e){
                System.out.println("Error with getting information about languages.");
            }

            try (PreparedStatement ps = connection.prepareStatement(RECENT_STATS_BY_COUNTRY_ID)){
                ps.setInt(1,choiceId);
                System.out.println("Most recent stats: ");
                try(ResultSet rs = ps.executeQuery()){
                    rs.next();
                    int year=rs.getInt("year");
                    System.out.println("YEAR: " + year);
                    int population = rs.getInt("population");
                    System.out.println("POPULATION: " + population);
                    double gdp = rs.getDouble("gdp");
                    System.out.println("GDP: " + gdp);
                }
            } catch(SQLException e){
                System.out.println("Error in getting statistics.");
            }

        } catch (SQLException e){
            System.out.print("Connessione al database non riuscita.");
        }

        scan.close();
    }
}
