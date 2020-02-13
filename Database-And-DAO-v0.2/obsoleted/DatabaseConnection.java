package main;


import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DatabaseConnection {
    private Connection c = null;

    DatabaseConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:DataBase.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    void CheckConnection() throws SQLException {
        if (c != null) {
            System.out.println("Test SQL results as below...");
            Statement stmt = null;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User;");
            while (rs.next()) {
                int userid = rs.getInt("Userid");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String full_name = rs.getString("Full_Name");
                String email_address = rs.getString("Email_Address");
                String account_status = rs.getString("Account_Status");
                String date_created = rs.getString("Date_Created");

                System.out.println("ID = " + userid);
                System.out.println("NAME = " + username);
                System.out.println("PASSWORD = " + password);
                System.out.println("FULL NAME = " + full_name);
                System.out.println("EMAIL = " + email_address);
                System.out.println("STATUS = " + account_status);
                System.out.println("DATE CREATED = " + date_created);
                System.out.println();
            }
            rs.close();
            stmt.close();
        }
    }


    //DAO Of User

    void AddUserInUser(String username, String password) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "INSERT INTO User (Username,Password) VALUES ('" + username + "','" + password + "');";
        stmt.execute(query);
        stmt.close();

    }

    void UpdateOneFeatureOfUserInUser(Integer userid, String featureToUpdate, String updateTo) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "UPDATE User SET " + featureToUpdate + "='" + updateTo + "' WHERE Userid = " + userid + ";";
        stmt.execute(query);
        stmt.close();
    }

    void UpdateUserInUser(Integer userid, String username, String password, String full_name, String email_address,
                          String account_status, String date_created) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "UPDATE User " +
                "SET Username='" + username + "', " +
                "Password='" + password + "', " +
                "Full_Name='" + full_name + "', " +
                "Email_Address='" + email_address + "', " +
                "Account_Status='" + account_status + "', " +
                "Date_Created='" + date_created + "' " +
                "WHERE Userid = " + userid + ";";
        stmt.execute(query);
        stmt.close();
    }

    HashMap<String, String> SelectUserInUser(Integer userid) throws SQLException {
        HashMap<String, String> result = new HashMap<String, String>();
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "SELECT * FROM USER WHERE Userid = " + userid + ";";
        ResultSet rs = stmt.executeQuery(query);

        result.put("Userid", userid.toString());
        String username = rs.getString("Username");
        result.put("Username", username);
        String password = rs.getString("Password");
        result.put("Password", password);
        String full_name = rs.getString("Full_Name");
        result.put("Full_Name", full_name);
        String email_address = rs.getString("Email_Address");
        result.put("Email_Address", email_address);
        String account_status = rs.getString("Account_Status");
        result.put("Account_Status", account_status);
        String date_created = rs.getString("Date_Created");
        result.put("Date_Created", date_created);

        rs.close();
        stmt.close();

        return result;
    }

    void DeleteUserInUser(Integer userid) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "DELETE FROM User " + "WHERE Userid = " + userid + ";";
        stmt.execute(query);
        stmt.close();
    }


    //DAO of Class

    void AddClassInClass(Integer userid, String class_name, String offer_status) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query ="INSERT INTO Class (Teacherid, Class_Name, Offer_Status, Number_Of_Offering) VALUES ("
                + userid + ",'" + class_name + "','" + offer_status + "',0);";
        stmt.execute(query);
        stmt.close();
    }

    void UpdateClassInClass(Integer classid, String class_name, String offer_status) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "UPDATE Class " +
                "SET Class_Name='" + class_name + "', " +
                "Offer_status='" + offer_status + "' " +
                "WHERE Classid = " + classid + ";";
        stmt.execute(query);
        stmt.close();
    }

    HashMap<String, String> SelectClassInClass(Integer classid) throws SQLException {
        HashMap<String, String> result = new HashMap<String, String>();
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "SELECT * FROM Class WHERE classid = " + classid + ";";
        ResultSet rs = stmt.executeQuery(query);

        result.put("Classid", classid.toString());
        Integer teacherid = rs.getInt("Teacherid");
        result.put("Teacherid", teacherid.toString());
        String class_name = rs.getString("Class_Name");
        result.put("Class_Name", class_name);
        String offer_status = rs.getString("Offer_Status");
        result.put("Offer_Status", offer_status);
        Integer number_of_offering = rs.getInt("Number_Of_Offering");
        result.put("Number_Of_Offering", number_of_offering.toString());

        rs.close();
        stmt.close();

        return result;
    }

    void DeleteClassInClass(Integer classid) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "DELETE FROM Class " + "WHERE Classid = " + classid + ";";
        stmt.execute(query);
        stmt.close();
    }


    // DAO of ClassOffering


    void AddClassOffering(Integer classid, String date_time_offering, Integer Number_Of_Student, String classroom)
            throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "INSERT INTO ClassOffering (Classid, Date_Time_Offering, Number_Of_Student, Classroom) VALUES ("
                + classid + ",'" + date_time_offering + "'," + Number_Of_Student + ",'" + classroom + "');";
        stmt.execute(query);
        stmt.close();

        //TODO Update ClassOffering in class
    }

    void UpdateClassOffering(Integer class_offering_id, Integer classid, String date_time_offering, Integer number_of_student, String classroom)
            throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "UPDATE ClassOffering " +
                "SET Classid='" + classid + "', " +
                "Date_Time_Offering='" + date_time_offering + "', " +
                "Number_Of_Student='" + number_of_student + "', " +
                "Classroom='" + classroom + "' " +
                "WHERE Class_Offering_ID = " + class_offering_id + ";";
        stmt.execute(query);
        stmt.close();
    }

    HashMap<String, String> SelectClassOffering(Integer class_offering_id) throws SQLException {
        HashMap<String, String> result = new HashMap<String, String>();
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "SELECT * FROM ClassOffering WHERE Class_Offering_id = " + class_offering_id + ";";
        ResultSet rs = stmt.executeQuery(query);

        result.put("Class_Offering_ID", class_offering_id.toString());
        Integer classid = rs.getInt("Classid");
        result.put("Classid", classid.toString());
        String date_time_offering = rs.getString("Date_Time_Offering");
        result.put("Date_Time_Offering", date_time_offering);
        Integer number_of_student = rs.getInt("Number_Of_Student");
        result.put("Number_Of_Student", number_of_student.toString());
        String classroom = rs.getString("Classroom");
        result.put("Classroom", classroom);

        return result;
    }

    void DeleteClassOffering(Integer class_offering_id) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "DELETE FROM ClassOffering " + "WHERE Class_Offering_ID = " + class_offering_id + ";";
        stmt.execute(query);
        stmt.close();
    }


    //DAO of Student


    void AddStudentInStudent(Integer studentid, String student_name, String student_email, String student_status,
                             String payment_info) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();

        String query = "INSERT INTO Student (Studentid, Student_Name, Student_Email, Student_Status, Payment_Info)" +
                " VALUES (" + studentid + ",'" + student_name + "','" + student_email + "','"
                + student_status + "','" + payment_info + "');";
        stmt.execute(query);
        stmt.close();
    }

    void UpdateStudentInStudent(Integer studentid, String student_name, String student_email, String student_status,
                                String payment_info) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query= "UPDATE Student " +
                "SET Student_Name='" + student_name + "', " +
                "Student_Email='" + student_email + "', " +
                "Student_Status='" + student_status + "', " +
                "Payment_Info='" + payment_info + "'" +
                "WHERE Studentid = " + studentid + ";";
        stmt.execute(query);
        stmt.close();
    }

    void DeleteStudentInStudent(Integer studentid) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "DELETE FROM Student " + "WHERE Studentid = " + studentid + ";";
        stmt.execute(query);
        stmt.close();
    }

    HashMap<String, String> SelectStudentInStudent(Integer studentid) throws SQLException {
        HashMap<String, String> result = new HashMap<String, String>();
        Statement stmt = null;
        stmt = c.createStatement();
        String query = "SELECT * FROM Student WHERE Studentid = " + studentid + ";";
        ResultSet rs = stmt.executeQuery(query);

        result.put("Studentid", studentid.toString());
        String student_name = rs.getString("Student_Name");
        result.put("Student_Name", student_name);
        String student_email = rs.getString("Student_Email");
        result.put("Student_Email", student_email);
        String student_status = rs.getString("Student_Status");
        result.put("Student_Status", student_status);
        String payment_info = rs.getString("Payment_Info");
        result.put("Payment_Info", payment_info);

        rs.close();
        stmt.close();
        return result;
    }


    //DAO of Student-Class
    void AddClassForStudent(Integer studentid, Integer class_offering_id) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query="INSERT INTO ClassChosenByStudent (Studentid, Offeringid) VALUES (" + studentid + "," + class_offering_id + ");";
        stmt.execute(query);
        stmt.close();

    }

    void DeleteClassForStudent(Integer studentid, Integer class_offering_id) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String query="DELETE FROM ClassChosenByStudent WHERE Studentid=" + studentid + " AND Offeringid=" + class_offering_id + ";";
        stmt.execute(query);
        stmt.close();
    }
//TODO Test The below functions briefly.

    void CalculateClassForStudent() {
    }

    void SelectClassOfStudent() {
    }

    //DAO of Attendance Record

    void AddAttendanceRecordForStudent() {
    }

    void ModifyAttendanceRecordForStudent() {
    }

    void CalculateAttendanceRecordForStudent() {
    }

    void AddStudentInClassOffering() {
    }

}
