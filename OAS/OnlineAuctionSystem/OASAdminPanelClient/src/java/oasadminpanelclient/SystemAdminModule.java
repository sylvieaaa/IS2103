/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadminpanelclient;

import ejb.session.stateless.EmployeeControllerRemote;
import entity.EmployeeEntity;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRight;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidAccessRightException;
import util.exception.StaffNotFoundException;


public class SystemAdminModule {

    private EmployeeControllerRemote employeeControllerRemote;

    //private EmployeeEntity currentEmployee;

    public SystemAdminModule() {
    }

    public SystemAdminModule(EmployeeControllerRemote employeeControllerRemote) {
        this.employeeControllerRemote = employeeControllerRemote;
    }

    public void menuOperation(EmployeeEntity currentEmployee) throws InvalidAccessRightException {
        if (! currentEmployee.getAccessRight().equals(EmployeeAccessRight.SYSTEMADMIN)) {
            throw new InvalidAccessRightException("You don't have SYSTEMADMIN rights to access the system administration module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("* Crazy Auction :: System Administrator's Admin Panel *\n");
            System.out.println("1: Create Employee");
            System.out.println("2: View Employee Details");
            System.out.println("3: View All Employees");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    createNewEmployee();
                } else if (response == 2) {
                    viewEmployeeDetails();
                } else if (response == 3) {
                    viewAllEmployees();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    public void createNewEmployee() {
      try{
        Scanner scanner = new Scanner(System.in);
        EmployeeEntity newEmployee = new EmployeeEntity();

        System.out.println("* Crazy Auction :: System Administration :: Create New Employee *\n");
        System.out.print("Enter First Name> ");
        newEmployee.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newEmployee.setLastName(scanner.nextLine().trim());

        while (true) {
            System.out.println("Select Access Right (1: Sales Staff, 2: Finance Staff, 3: System Administrator)> ");
            Integer accessRightInt = scanner.nextInt();

            if (accessRightInt >= 1 && accessRightInt <= 3) {
                newEmployee.setAccessRight(EmployeeAccessRight.values()[accessRightInt - 1]);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        scanner.nextLine();
       // while (true) {
        System.out.print("Enter Username> ");
        newEmployee.setUserName(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newEmployee.setPassword(scanner.nextLine().trim());
        newEmployee = employeeControllerRemote.createNewEmployee(newEmployee);
        
        System.out.println("New staff created successfully!: " + "\n");
      }
      catch (EmployeeExistException | GeneralException ex)
      {
          System.out.println("An unexpected error has occurred: " + ex.getMessage());
      }

    }

    public void viewEmployeeDetails() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("* Crazy Auction :: System Administration :: View Employee Details *\n");
        System.out.print("Enter Employee ID> ");
        Long employeeId = scanner.nextLong();

        try {
            EmployeeEntity employee = employeeControllerRemote.retrieveEmployeeByEmployeeId(employeeId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username", "Password");
            System.out.printf("%8s%20s%20s%18s%20s%20s\n", employee.getEmployeeId().toString(), employee.getFirstName(), employee.getLastName(), employee.getAccessRight().toString(), employee.getUserName(), employee.getPassword());
            System.out.println("------------------------");
            System.out.println("1: Update Staff");
            System.out.println("2: Delete Staff");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                updateStaff(employee);
            } else if (response == 2) {
                deleteEmployee(employee);
            }
        } catch (EmployeeNotFoundException ex) {
            System.out.println("An error has occurred while retrieving employee " + ex.getMessage() + "\n");
        }
    }

    public void updateStaff(EmployeeEntity employee) {

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("* Crazy Auction :: System Administration :: View Staff Details :: Update Staff *\n");
        System.out.print("Enter First Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            employee.setFirstName(input);
        }

        System.out.print("Enter Last Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            employee.setLastName(input);
        }

        while (true) {
            System.out.print("Select Access Right (0: No Change, 1: Cashier, 2: Manager)> ");
            Integer accessRightInt = scanner.nextInt();

            if (accessRightInt >= 1 && accessRightInt <= 2) {
                employee.setAccessRight(EmployeeAccessRight.values()[accessRightInt - 1]);
                break;
            } else if (accessRightInt == 0) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        scanner.nextLine();
        System.out.print("Enter Username (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            employee.setUserName(input);
        }

        System.out.print("Enter Password (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            employee.setPassword(input);
        }

        employeeControllerRemote.updateEmployee(employee);
        System.out.println("Employee updated successfully!\n");
    }

    public void deleteEmployee(EmployeeEntity employee) {
        
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("* Crazy Auction :: System Administration :: View Employee Details ::Delete Employee *\n");
        System.out.printf("Confirm Delete Employee %s %s ? (Employee ID: %d) (Enter 'Y' to Delete)> ", employee.getFirstName(), employee.getLastName(), employee.getEmployeeId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                employeeControllerRemote.deleteEmployee(employee.getEmployeeId());
                System.out.println("Employee deleted successfully!\n");
            } 
            catch (EmployeeNotFoundException ex) 
            {
                System.out.println("An error has occurred while deleting employee: " + ex.getMessage() + "\n");
            }            
        }
        else
        {
            System.out.println("Employee NOT deleted!\n");
        }
    }

    

    public void viewAllEmployees() {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("* Crazy Auction :: System Administration :: View All Employee ***\n");
        
        List<EmployeeEntity> employeeEntities = employeeControllerRemote.retrieveAllEmployee();
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username", "Password");

        for(EmployeeEntity employee:employeeEntities)
        {
            System.out.printf("%8s%20s%20s%18s%20s%20s\n", employee.getEmployeeId().toString(), employee.getFirstName(), employee.getLastName(), employee.getAccessRight().toString(), employee.getUserName(), employee.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();

    }
}