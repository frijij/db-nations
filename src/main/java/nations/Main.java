package nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private final static String URL = "jdbc:mysql://localhost:3306/db_nations";
    private final static String USER = "root";
    private final static String PW = "Java2023!";


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try(Connection connection = DriverManager.getConnection(URL, USER, PW)){
            System.out.println("Connessione riuscita");
        } catch (SQLException e){
            System.out.print("Connessione al database non riuscita");
        }

        scan.close();
    }
}
