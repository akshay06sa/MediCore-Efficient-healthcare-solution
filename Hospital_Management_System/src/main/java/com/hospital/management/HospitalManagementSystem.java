package com.hospital.management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HospitalManagementSystem {

	private static final String url = ""; 
	
	private static final String username = ""; 
	
	private static final String password = ""; 
	
	private static boolean showMenu = true;
	
	public static void main(String[] args) {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Scanner scanner = new Scanner(System.in);
		
		try {
			Connection connection = DriverManager.getConnection(url,username,password);
			Patient patient = new Patient(connection,scanner);
			Doctor doctor = new Doctor(connection);
			
			while(showMenu) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patients");			
				System.out.println("3. View Doctors");		
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
				System.out.println("Enter your choice: ");
				int choice = scanner.nextInt();
				
				switch(choice) {
				
				// Add patient
				case 1: {
					patient.addPatient();
					System.out.println();
					break;
				}
				
				// View all patients
				case 2:{
					patient.viewPatient();
					System.out.println();
					break;
				}
			
				// View doctors
				case 3:{
					doctor.viewDoctors();
					System.out.println();
					break;
				}
				
				// Book appointment
				case 4:{
					bookAppointment(patient,doctor,connection,scanner);
					System.out.println();
					break;
				}
					
				// Exit
				case 5:{
					System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM");
					return;
				}
				default:{
					System.out.println("Enter valid choice!!!");
					System.out.println();
				}
				
				}
			}
			
		}
		catch(InputMismatchException m) {
			System.out.println("Enter valid choice!!!");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}

	}

	public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
		System.out.print("Enter patient ID: ");
		int patientId = scanner.nextInt();
		
		System.out.print("Enter doctor ID: ");
		int doctorId = scanner.nextInt();
		
		System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
		String appointmentDate = scanner.next();
		
		// to check if doctor and patient both exists in a DB or not
		if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
			// to check if the doctor is available on particular date or not
			if(checkDoctorAvailability(doctorId,appointmentDate,connection)) {
				
				// We have make a DB call for making entry of that date and doctor in appointment table
				String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) values(?, ?, ?)";
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
					preparedStatement.setInt(1, patientId);
					preparedStatement.setInt(2, doctorId);
					preparedStatement.setString(3, appointmentDate);

					int rowsAffected = preparedStatement.executeUpdate();
					if(rowsAffected>0) {
						System.out.println("Apointment Booked Successfully!!!");
					}else {
						System.out.println("Failed to Book Appointment!!!");
					}
					
				}catch(SQLException e) {
					e.printStackTrace();
				}
				
				
			}else {
				System.out.println("Doctor not available on this date!!!");
			}
		}
		else {
			System.out.println("Either patient or doctor doesn't exists!!!");
		}
	}
	
	public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
		
		// * will return the total count of rows 
		// If count is 1 then will can say doctor is not available else he is
		String query = "SELECT COUNT(*) FROM appointments where doctor_id = ? AND appointment_date = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, doctorId);
			preparedStatement.setString(2, appointmentDate);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				
				// generally we pass column name below but
				// here we are passing 1 as it will return the column at index 1
				int count  = resultSet.getInt(1);
				if(count==0) {
					return true;
				}else {
					return false;
				}
			}
		}
		catch(SQLException e ) {
			e.printStackTrace();
		}
		return false;
	}
}
