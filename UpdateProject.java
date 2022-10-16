// Imported libraries.
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UpdateProject {
    /***
     * Lets user update or finalise project details.
     * @throws ParseException
     */
    public void update() throws SQLException {
        // Lets user select what they want to do.
        try {
            while (true) {
                // Creates a JDBC object to connect to the database.
                JDBC jdbc = new JDBC();
                Statement statement = jdbc.connection();

                Scanner sc = new Scanner(System.in);
                System.out.println("What would you like to do?" +
                        "\nF - Finalise project" +
                        "\nD - Change due-date" +
                        "\nP - Make a payment" +
                        "\nB - Back to main menu");
                String menu = sc.nextLine();

                // Displays all the projects with 'incomplete' completion status.
                if (menu.equalsIgnoreCase("F")) {
                    String printProjectStatement = "SELECT project_number, project_name, completion_status FROM projects";
                    ResultSet printProjectResults = statement.executeQuery(printProjectStatement);

                    while (printProjectResults.next()) {
                        System.out.println(
                                printProjectResults.getInt("project_number") + ", "
                                        + printProjectResults.getString("project_name") + ", "
                                        + printProjectResults.getString("completion_status")
                        );
                    }

                    // Lets user finalise a project user project number.
                    System.out.println("Enter project number you would to finalise: ");
                    int projectNumber = sc.nextInt();

                    Date now = new Date();
                    String projectStatement = "SELECT project_number, project_name, completion_status FROM projects " +
                            "WHERE project_number = '" + projectNumber + "' ";
                    ResultSet projectResults = statement.executeQuery(projectStatement);

                    if (projectResults.next()) {
                        statement.executeUpdate("UPDATE projects " +
                                "SET completion_status = 'finalised', completion_date = '" + now + "'" +
                                "WHERE project_number = '" + projectNumber + "'"
                        );

                        System.out.println("Project number: " + projectNumber +
                                " has been finalised.\n");

                        // Prints project invoice.
                        String projectStatement2 = "SELECT customer.name, customer.cell_number, projects.project_fee, projects.amount_paid" +
                                " FROM customer, projects WHERE customer.project_number = "+ projectNumber+" " +
                                "AND customer.project_number = projects.project_number";

                        ResultSet projectResults2 = statement.executeQuery(projectStatement2);

                        // Constants.
                        String line = "\n====================================================";

                        if (projectResults2.next()) {
                            double totalPlusVat = (projectResults2.getFloat("project_fee") +
                                    (projectResults2.getFloat("project_fee") * 0.15));
                            System.out.println("INVOICE." +
                                    line +
                                    "\nCompany            Poised" +
                                    "\nContact Number     074 029 2896" +
                                    "\nAddress            34 Long Street, Pretoria, 0001" +
                                    line +
                                    "\nCustomer name      "    +   projectResults2.getString("name") +
                                    "\nCustomer number    "  + projectResults2.getString("cell_number") +
                                    line +
                                    "\nProject Cost       R" + projectResults2.getFloat("project_fee") +
                                    "\nVAT                R" + (projectResults2.getFloat("project_fee") * 0.15) +
                                    "\nTotal Cost         R" + totalPlusVat +
                                    "\nAmount Paid        R" + projectResults2.getFloat("amount_paid") +
                                    line +
                                    "\nAmount Due         R" + (totalPlusVat - projectResults2.getFloat("amount_paid")) + "\n");
                        }
                    } else {
                        System.out.println("Project number doesn't exist");
                    }
                // Lets user change due date
                } else if (menu.equalsIgnoreCase("D")) {
                    String printProjectStatement = "SELECT project_number, project_name, due_date FROM projects";
                    ResultSet printProjectResults = statement.executeQuery(printProjectStatement);

                    while (printProjectResults.next()) {
                        System.out.println(
                                printProjectResults.getInt("project_number") + ", "
                                        + printProjectResults.getString("project_name") + ", "
                                        + printProjectResults.getString("due_date")
                        );
                    }

                    try {
                        // Lets user select a project by project number.
                        System.out.println("Enter project number you would to change due date: ");
                        int projectNumber = sc.nextInt();

                        String projectStatement = "SELECT * FROM projects " +
                                "WHERE project_number = '" + projectNumber + "' ";
                        ResultSet projectResults = statement.executeQuery(projectStatement);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                        dateFormat.setLenient(false);
                        System.out.println("Due date (dd/mm/yy): ");
                        String newDate = sc.next();
                        dateFormat.parse(newDate);

                        if (projectResults.next()) {
                            statement.executeUpdate("UPDATE projects " +
                                    "SET due_date = '" + newDate + "' " +
                                    "WHERE project_number = '" + projectNumber + "'"
                            );
                        }

                        // Displays new project information.
                        System.out.println("Project " + projectNumber + " due date changed to " + newDate + ".\n");
                    // Error when file name is not found.
                    } catch (ParseException e) {
                        System.out.println("Incorrect date format.");
                    }
                // Lets user pay.
                } else if (menu.equalsIgnoreCase("P")) {
                    String printProjectStatement = "SELECT * FROM projects";
                    ResultSet printProjectResults = statement.executeQuery(printProjectStatement);

                    while (printProjectResults.next()) {
                        System.out.println(
                                printProjectResults.getInt("project_number") + ", "
                                        + printProjectResults.getString("project_name") + ", "
                                        + "Amount paid: " + printProjectResults.getFloat("amount_paid")
                        );
                    }

                    // Lets user select a project by project number.
                    System.out.println("Enter project number you would to change amount paid: ");
                    int projectNumber = sc.nextInt();

                    String projectStatement = "SELECT * FROM projects WHERE project_number = '" + projectNumber + "' ";
                    ResultSet projectResults = statement.executeQuery(projectStatement);

                    if (projectResults.next()) {
                        // Lets user pay a specific amount.
                        System.out.println("Project total: " + projectResults.getFloat("project_fee"));
                        System.out.println("Amount paid so far: " + projectResults.getFloat("amount_paid"));
                        System.out.println("Amount Due: " + (projectResults.getFloat("project_fee")
                                - projectResults.getFloat("amount_paid")));
                        System.out.println("Enter amount: ");
                        double newAmount = Double.parseDouble(sc.next());
                        double newAmountDue = ((projectResults.getFloat("project_fee"))
                                - (projectResults.getFloat("amount_paid") + newAmount));

                        statement.executeUpdate("UPDATE projects " +
                                "SET amount_paid = '" + (newAmount + projectResults.getFloat("amount_paid")) + "' " +
                                "WHERE project_number = '" + projectNumber + "'"
                        );

                        // Displays new project information.
                        System.out.println(newAmount + " paid to Project " + projectNumber);
                        System.out.println("Amount Due: " + newAmountDue + ".\n");
                    }
                // Takes user back to main menu.
                } else if (menu.equalsIgnoreCase("B")) {
                    break;
                // Error when user select an invalid option.
                } else {
                    System.out.println("Invalid selection");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database not found.");
        }
    }
}

