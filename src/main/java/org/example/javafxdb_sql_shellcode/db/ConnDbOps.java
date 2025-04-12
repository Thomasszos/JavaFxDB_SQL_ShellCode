/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.javafxdb_sql_shellcode.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.javafxdb_sql_shellcode.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author MoaathAlrajab
 */
public class ConnDbOps {
    final String DB_URL = "jdbc:mysql://csc311thomasszostak2.mysql.database.azure.com:3306/dbname?serverTimezone=UTC&useSSL=true&requireSSL=false";

    //    final String MYSQL_SERVER_URL = "jdbc:mysql://csc311thomasszostak2.mysql.database.azure.com:3306";
//    final String DB_URL = "jdbc:mysql://csc311thomasszostak2.mysql.database.azure.com/csc311thomasszostak2";
    final String USERNAME = "thomasszostak";
    final String PASSWORD = "FARM123$";

    public boolean connectToDatabase() {
        boolean hasRegisteredStudents = false;

        //DriverManager.getConnection(url, "thomasszostak", {your_password});
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS students ("
                    + "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "firstName VARCHAR(200) NOT NULL,"
                    + "lastName VARCHAR(200) NOT NULL,"
                    + "dept VARCHAR(200) NOT NULL,"
                    + "major VARCHAR(200) NOT NULL"
                    + ")";
            statement.executeUpdate(sql);

            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM students");
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                hasRegisteredStudents = true;
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasRegisteredStudents;
    }
    
//    public  boolean connectToDatabase() {
//        boolean hasRegistredUsers = false;
//
//
//        //Class.forName("com.mysql.jdbc.Driver");
//        try {
//            //First, connect to MYSQL server and create the database if not created
//            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
//            Statement statement = conn.createStatement();
//            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS DBname");
//            statement.close();
//            conn.close();
//
//            //Second, connect to the database and create the table "users" if cot created
//            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
//            statement = conn.createStatement();
//            String sql = "CREATE TABLE IF NOT EXISTS users ("
//                    + "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
//                    + "name VARCHAR(200) NOT NULL,"
//                    + "email VARCHAR(200) NOT NULL UNIQUE,"
//                    + "phone VARCHAR(200),"
//                    + "address VARCHAR(200),"
//                    + "password VARCHAR(200) NOT NULL"
//                    + ")";
//            statement.executeUpdate(sql);
//
//            //check if we have users in the table users
//            statement = conn.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
//
//            if (resultSet.next()) {
//                int numUsers = resultSet.getInt(1);
//                if (numUsers > 0) {
//                    hasRegistredUsers = true;
//                }
//            }
//
//            statement.close();
//            conn.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return hasRegistredUsers;
//    }

    public  void queryUserByName(String firstName) {


        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM students WHERE firstname = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, firstName);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String lastName = resultSet.getString("lastname");
                String dept = resultSet.getString("department");
                String major = resultSet.getString("major");
                System.out.println("ID: " + id + ", First Name: " + firstName + ", Last Name: " + lastName + ", Department: " + dept + ", Major: " + major);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void listAllUsers() {



        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM students ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String dept = resultSet.getString("department");
                String major = resultSet.getString("major");
                System.out.println("ID: " + id + ", First Name: " + firstname + ", Last Name: " + lastname + ", Department: " + dept + ", Major: " + major);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void insertUser(String firstName, String lastName, String dept, String major, String photo) {




        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO students (firstname, lastName, department, major, photo) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, dept);
            preparedStatement.setString(4, major);
            preparedStatement.setString(5, photo);

            int row = preparedStatement.executeUpdate();

            if (row > 0) {
                System.out.println("A new student was inserted successfully.");
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editStudent(int id, String newFirstName, String newLastName, String newDept, String newMajor, String newPhoto) {
        String updateSql = "UPDATE students SET firstName = ?, lastName = ?, department = ?, major = ?, photo = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            updateStmt.setString(1, newFirstName);
            updateStmt.setString(2, newLastName);
            updateStmt.setString(3, newDept);
            updateStmt.setString(4, newMajor);
            updateStmt.setString(5, newPhoto);
            updateStmt.setInt(6, id);

            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Student with ID " + id + " updated successfully.");
            } else {
                System.out.println("⚠️ No student found with ID: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void deleteStudent(String firstName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String selectSql = "DELETE FROM students WHERE firstName = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSql);
            preparedStatement.setString(1, firstName);
            //ResultSet rs = preparedStatement.executeQuery();
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Student(s) with first name " + firstName + " was successfully deleted.");
            } else {
                System.out.println("⚠️ No student found with ID: " + firstName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Person> loadStudentsFromDB() {
        ObservableList<Person> students = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            while (rs.next()) {
                Person np = new Person(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("department"),
                        rs.getString("major")
                );
                np.setPhoto(rs.getString("photo"));
                students.add(np);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }





}
