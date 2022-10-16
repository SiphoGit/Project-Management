// Imported libraries.
import java.sql.*;
import java.util.Scanner;

public class SearchProject {
    /***
     * Lets user search a project by name or number.
     */
    public void searchProject() throws SQLException {
        try {
            // User input.
            Scanner sc = new Scanner(System.in);
            System.out.println("Search project by project name or number: ");

            while (true) {
                // Lets user elect an option.
                System.out.println("\nNumber - Search project number" +
                        "\nName - Search project by name" +
                        "\nB - Back to main menu");
                String menu = sc.next();

                // Creates a JDBC object to connect to the database.
                JDBC jdbc = new JDBC();
                Statement statement = jdbc.connection();

                // Lets user search a project by project number.
                if (menu.equalsIgnoreCase("number")) {
                    System.out.println("Enter project number: ");
                    int projectNumber = sc.nextInt();

                    // Selects fields from projects table.
                    String projectStatement = "SELECT * FROM projects WHERE project_number = '" + projectNumber + "' ";
                    ResultSet projectResults = statement.executeQuery(projectStatement);

                    // Prints selected project information.
                    if (projectResults.next()) {
                        System.out.println(
                                "Project number: " + projectResults.getInt("project_number")
                                        + "\nProject name: " + projectResults.getString("project_name")
                                        + "\nBuilding type: " + projectResults.getString("building_type")
                                        + "\nPhysical address: " + projectResults.getString("physical_address")
                                        + "\nERF number: " + projectResults.getString("ERF_number")
                                        + "\nProject fee: " + projectResults.getFloat("project_fee")
                                        + "\nAmount paid: " + projectResults.getFloat("amount_paid")
                                        + "\nDue date: " + projectResults.getString("due_date")
                                        + "\nCompletion status: " + projectResults.getString("completion_status")
                                        + "\nCompletion date: " + projectResults.getString("completion_date")
                        );
                    // Displayed when book id entered doesn't exist.
                    } else {
                        System.out.println("Project number doesn't exist.");
                    }
                // Lets user search a project by project name.
                } else if (menu.equalsIgnoreCase("name")) {
                    System.out.println("Enter project name: ");
                    String projectName = sc.next();

                    String projectStatement = "SELECT * FROM projects WHERE project_name = '" + projectName + "' ";
                    ResultSet projectResults = statement.executeQuery(projectStatement);

                    if (projectResults.next()) {
                        System.out.println(
                                "Project number: " + projectResults.getInt("project_number")
                                        + "\nProject name: " + projectResults.getString("project_name")
                                        + "\nBuilding type: " + projectResults.getString("building_type")
                                        + "\nPhysical address: " + projectResults.getString("physical_address")
                                        + "\nERF number: " + projectResults.getString("ERF_number")
                                        + "\nProject fee: " + projectResults.getFloat("project_fee")
                                        + "\nAmount paid: " + projectResults.getFloat("amount_paid")
                                        + "\nDue date: " + projectResults.getString("due_date")
                                        + "\nCompletion status: " + projectResults.getString("completion_status")
                                        + "\nCompletion date: " + projectResults.getString("completion_date")
                        );
                    // Displayed when book id entered doesn't exist.
                    } else {
                        System.out.println("Project name doesn't exist.");
                    }
                // Takes user back to main menu.
                } else if (menu.equalsIgnoreCase("B")) {
                    break;
                // Error message when user enter an invalid option.
                } else {
                    System.out.println("Invalid selection.");
                }
            }
        // Error when database name doesn't exist.
        } catch (SQLException e) {
            System.out.println("Data base not found.");
        }
    }
}


