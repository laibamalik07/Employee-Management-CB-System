package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeManager {

    private static final String URL = "jdbc:mysql://localhost/employeedb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Employee Management System");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addEmployee(scanner);
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    updateEmployee(scanner);
                    break;
                case 4:
                    deleteEmployee(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addEmployee(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter position: ");
        String position = scanner.nextLine();
        System.out.print("Enter salary: ");
        double salary = scanner.nextDouble();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, position);
            statement.setDouble(3, salary);
            statement.executeUpdate();
            System.out.println("Employee added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewEmployees() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employees")) {

            List<Employee> employees = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String position = resultSet.getString("position");
                double salary = resultSet.getDouble("salary");
                employees.add(new Employee(id, name, position, salary));
            }

            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateEmployee(Scanner scanner) {
        System.out.print("Enter employee ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new position: ");
        String position = scanner.nextLine();
        System.out.print("Enter new salary: ");
        double salary = scanner.nextDouble();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("UPDATE employees SET name = ?, position = ?, salary = ? WHERE id = ?")) {
            statement.setString(1, name);
            statement.setString(2, position);
            statement.setDouble(3, salary);
            statement.setInt(4, id);
            statement.executeUpdate();
            System.out.println("Employee updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteEmployee(Scanner scanner) {
        System.out.print("Enter employee ID to delete: ");
        int id = scanner.nextInt();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM employees WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Employee deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
