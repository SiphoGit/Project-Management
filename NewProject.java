// Imported libraries.
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Scanner;
import java.sql.*;

public class NewProject {
    /***
     * Lets user create a new project.
     */
    public void createNewProject() {
        try {
            // Creates a JDBC object to connect to the database.
            JDBC jdbc = new JDBC();
            Statement statement = jdbc.connection();

            Scanner sc = new Scanner(System.in);
            // Lets user enter project details
            System.out.println("\nEnter project details below" +
                    "\nProject Number: ");
            String projectNumber = sc.nextLine();
            System.out.println("Project Name: ");
            String projectName = sc.nextLine();
            System.out.println("Building Type: ");
            String buildingType = sc.nextLine();
            System.out.println("Physical Address: ");
            String physicalAddress = sc.nextLine();
            System.out.println("ERF Number: ");
            String erfNumber = sc.nextLine();
            System.out.println("Project Fee(excl. VAT): ");
            float projectFee = sc.nextFloat();
            System.out.println("Amount Paid to Date: ");
            float amountPaid = sc.nextFloat();
            sc.nextLine();

            // Validates date format entered.
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            dateFormat.setLenient(false);
            System.out.println("Due date (dd/mm/yy): ");
            String deadLine = sc.nextLine();
            dateFormat.parse(deadLine);

            // Lets user enter customer's details.
            System.out.println("\nEnter customer's details below" +
                    "\nName: ");
            String customerName = sc.nextLine();
            System.out.println("Cell number: ");
            String customerNumber = sc.nextLine();
            System.out.println("Email Address: ");
            String customerEmailAddress = sc.nextLine();
            System.out.println("Physical Address: ");
            String customerPhysicalAddress = sc.nextLine();

            // Lets user enter contractor's details.
            System.out.println("\nEnter contractor's details below" +
                    "\nName: ");
            String contractorName = sc.nextLine();
            System.out.println("Cell number: ");
            String contractorNumber = sc.nextLine();
            System.out.println("Email Address: ");
            String contractorEmailAddress = sc.nextLine();
            System.out.println("Physical Address: ");
            String contractorPhysicalAddress = sc.nextLine();

            // Lets user enter architect's details.
            System.out.println("Enter architect's details below" +
                    "\nName: ");
            String architectName = sc.nextLine();
            System.out.println("Cell number: ");
            String architectNumber = sc.nextLine();
            System.out.println("Email Address: ");
            String architectEmailAddress = sc.nextLine();
            System.out.println("Physical Address: ");
            String architectPhysicalAddress = sc.nextLine();

            // Adds new project.
            // inserts project name as building type and customer name if project name is not provided.
            if (Objects.equals(projectName, "")) {
                statement.executeUpdate(
                        "INSERT into projects VALUES('" + projectNumber + "', '" + buildingType + customerName + "' , '"
                                + buildingType+ "', '" + physicalAddress + "', '" + erfNumber + "', '"
                                + (projectFee + (projectFee*0.5)) +"', '" + amountPaid +"', '"
                                + deadLine +"', 'incomplete', 'in-progress')"
                );
            } else {
                statement.executeUpdate(
                        "INSERT into projects VALUES('" + projectNumber + "', '" + projectName + "' , '"
                                + buildingType+ "', '" + physicalAddress + "', '" + erfNumber + "', '"
                                + (projectFee + (projectFee*0.5)) +"', '" + amountPaid +"', '"
                                + deadLine +"', 'incomplete', 'in-progress')"
                );
            }

            // Adds new customer.
            statement.executeUpdate(
                    "INSERT into contractor VALUES('"+ projectNumber +"', '" + customerName + "', '" + customerNumber + "' , '"
                            + customerEmailAddress + "', '" + customerPhysicalAddress + "')"
            );

            // Adds new contractor.
            statement.executeUpdate(
                    "INSERT into contractor VALUES('"+ projectNumber +"', '" + contractorName + "', '" + contractorNumber + "' , '"
                            + contractorEmailAddress + "', '" + contractorPhysicalAddress + "')"
            );

            // Adds new architect.
            statement.executeUpdate(
                    "INSERT into architect VALUES('"+ projectNumber +"', '" + architectName + "', '" + architectNumber + "' , '"
                            + architectEmailAddress + "', '" + architectPhysicalAddress + "')"
            );
        // Date format error
        // Error when database name doesn't exist.
        } catch (ParseException e){
            System.out.println("Incorrect date format.");
        } catch (SQLException e) {
            System.out.println("Data base not found.");
        }
    }
}
