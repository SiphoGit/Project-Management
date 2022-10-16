// Imported libraries.
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) throws SQLException {
        while (true) {
            // User main menu.
            Scanner sc = new Scanner(System.in);
            System.out.println(
                    "\nMain menu." +
                    "\nN - New project" +
                    "\nU - Update details or finalise a project" +
                    "\nVA - View all projects" +
                    "\nV - View in-progress projects" +
                    "\nO - View over-due projects" +
                    "\nS - Search project" +
                    "\nE - Exit");
            String mainMenu = sc.next();

            // Lets user create a new project.
            if (mainMenu.equalsIgnoreCase("N")) {
                // Instantiates 'NewProject' object.
                NewProject newProject = new NewProject();
                newProject.createNewProject();
            // Lets user update or finalise project details.
            } else if (mainMenu.equalsIgnoreCase("U")) {
                // Instantiates 'UpdateProject' object.
                UpdateProject updateData = new UpdateProject();
                updateData.update();
            // Lets user view in-progress projects.
            } else if (mainMenu.equalsIgnoreCase("V")) {
                 // Calls 'incompleteProjects' method
                incompleteProjects();
            // Lets user view all projects.
            }else if (mainMenu.equalsIgnoreCase("VA")) {
                // Calls 'viewAllProjects' method
                viewAllProjects();
            // Lets user view overdue projects.
            } else if (mainMenu.equalsIgnoreCase("O")) {
                    // Calls 'overdueProjects' method
                    overdueProjects();
            // Lets user search project by project number or name.
            } else if (mainMenu.equalsIgnoreCase("S")) {
                // Instantiates 'UpdateProject' object.
                SearchProject searchProject = new SearchProject();
                searchProject.searchProject();
            } else if (mainMenu.equalsIgnoreCase("E")) {
                System.out.println("Goodbye!");
                break;
            // Error when user select an invalid option.
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }

    /***
     * Lets user view all projects.
     */
    public static void viewAllProjects() {
        try {
            // Creates a JDBC object to connect to the database.
            JDBC jdbc = new JDBC();
            Statement statement = jdbc.connection();

            // Select fields in projects table.
            String printProjectStatement = "SELECT project_number, project_name FROM projects";
            ResultSet printProjectResults = statement.executeQuery(printProjectStatement);

            while (printProjectResults.next()) {
                System.out.println(
                        printProjectResults.getInt("project_number") + ", "
                                + printProjectResults.getString("project_name")
                );
            }

            while (true) {
                // Lets user enter input.
                Scanner sc = new Scanner(System.in);
                System.out.println("\nWould you like to view a specific project?" +
                        "\nY - To view a specific project" +
                        "\nB - To go back to main menu");
                String menu = sc.next();

                // Lets user view a project using a project number.
                if (menu.equalsIgnoreCase("Y")) {
                    System.out.println("Enter project number: ");
                    int projectNumber = sc.nextInt();

                    // Select fields from projects, customer, contractor, and architect tables.
                    String projectStatement = "SELECT customer.name, customer.cell_number, customer.email_address, customer.physical_address, " +
                            "projects.project_number, projects.project_name, projects.building_type, projects.physical_address,  projects.ERF_number, " +
                            "projects.project_fee, projects.amount_paid, projects.due_date, projects.completion_status, projects.completion_date," +
                            "contractor.name, contractor.cell_number, contractor.email_address, contractor.physical_address," +
                            "architect.name, architect.cell_number, architect.email_address, architect.physical_address" +
                            " FROM customer, projects, contractor, architect WHERE customer.project_number = "+ projectNumber+" " +
                            "AND projects.project_number= "+ projectNumber+" AND contractor.project_number = "+ projectNumber+" " +
                            "AND architect.project_number = "+ projectNumber+"";
                    ResultSet projectResults = statement.executeQuery(projectStatement);

                    // Displays project information.
                    if (projectResults.next()) {
                        System.out.println(
                                "\nProject number: " + projectResults.getInt("project_number")
                                        + "\nProject name: " + projectResults.getString("project_name")
                                        + "\nBuilding type: " + projectResults.getString("building_type")
                                        + "\nPhysical address: " + projectResults.getString("projects.physical_address")
                                        + "\nERF number: " + projectResults.getString("ERF_number")
                                        + "\nProject fee: " + projectResults.getFloat("project_fee")
                                        + "\nAmount paid: " + projectResults.getFloat("amount_paid")
                                        + "\nDue date: " + projectResults.getString("due_date")
                                        + "\nCompletion status: " + projectResults.getString("completion_status")
                                        + "\nCompletion date: " + projectResults.getString("completion_date")
                                        + "\n\nCustomer"
                                        + "\nName: " + projectResults.getString("customer.name")
                                        + "\nCell number: " + projectResults.getString("customer.cell_number")
                                        + "\nEmail address: " + projectResults.getString("customer.email_address")
                                        + "\nPhysical address: " + projectResults.getString("customer.physical_address")
                                        + "\n\nContractor"
                                        + "\nName: " + projectResults.getString("contractor.name")
                                        + "\nCell number: " + projectResults.getString("customer.cell_number")
                                        + "\nEmail address: " + projectResults.getString("customer.email_address")
                                        + "\nPhysical address: " + projectResults.getString("customer.physical_address")
                                        + "\n\nArchitect"
                                        + "\nName: " + projectResults.getString("architect.name")
                                        + "\nCell number: " + projectResults.getString("architect.cell_number")
                                        + "\nEmail address: " + projectResults.getString("architect.email_address")
                                        + "\nPhysical address: " + projectResults.getString("architect.physical_address")
                        );
                    }
                    // Takes user back to main menu.
                } else if (menu.equalsIgnoreCase("B")) {
                    break;
                    // Error when user select an invalid option.
                } else {
                    System.out.println("Invalid selection.");
                }
            }
            // Error when database name doesn't exist.
        } catch (SQLException e) {
            System.out.println("Database not found.");
        }
    }

    /***
     * prints out overdue project
     */
    public static void overdueProjects() {
        System.out.println("overdue projects");
        try {
            // Creates a JDBC object to connect to the database.
            JDBC jdbc = new JDBC();
            Statement statement = jdbc.connection();

            // Selects all fields in projects table.
            String projectStatement = "SELECT * FROM projects";
            ResultSet projectResults = statement.executeQuery(projectStatement);

            if (projectResults.next()) {
                // Parses string date to data datatype
                java.util.Date dueDate = new SimpleDateFormat("dd/MM/yy").parse(projectResults.getString("due_date"));
                java.util.Date now = new Date();

                // Selects fields in projects table.
                String projectStatement2 = "SELECT * FROM projects " +
                        "WHERE completion_status = 'incomplete' " +
                        "AND '"+dueDate+"' < '"+now+"' ";
                ResultSet projectResults2 = statement.executeQuery(projectStatement2);

                // Displays overdue project details.
                while (projectResults2.next()) {
                    System.out.println(
                            "\nProject number: " + projectResults2.getInt("project_number")
                                    + "\nProject name: " + projectResults2.getString("project_name")
                                    + "\nBuilding type: " + projectResults2.getString("building_type")
                                    + "\nPhysical address: " + projectResults2.getString("physical_address")
                                    + "\nERF number: " + projectResults2.getString("ERF_number")
                                    + "\nProject fee: " + projectResults2.getFloat("project_fee")
                                    + "\nAmount paid: " + projectResults2.getFloat("amount_paid")
                                    + "\nDue date: " + projectResults2.getString("due_date")
                                    + "\nCompletion status: " + projectResults2.getString("completion_status")
                                    + "\nCompletion date: " + projectResults2.getString("completion_date")
                    );
                }
            }
            // Date parse error.
            // Error when database name doesn't exist.
        }  catch (SQLException e) {
            System.out.println("Data base not found.");

        } catch (ParseException e) {
            System.out.println("Incorrect date format.");
        }
    }

    /***
     * Displays projects have not been finalised.
     */
    public static void incompleteProjects() {
        try {
            System.out.println("In-progress projects");

            // Creates a JDBC object to connect to the database.
            JDBC jdbc = new JDBC();
            Statement statement = jdbc.connection();

            // Displays project information.
            String projectStatement = "SELECT * FROM projects WHERE completion_status = 'incomplete' ";
            ResultSet projectResults = statement.executeQuery(projectStatement);

            // Prints project information.
            while (projectResults.next()) {
                System.out.println(
                        "\nProject number: " + projectResults.getInt("project_number")
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
            }
            // Error when database name doesn't exist.
        } catch (SQLException e) {
            System.out.println("Data base not found.");
        }
    }
}
