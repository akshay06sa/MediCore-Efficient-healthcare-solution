package com.hospital.management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

	private Connection connection;

	private Scanner scanner;

	public Patient(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void addPatient() {

		System.out.print("Enter patient name: ");
		String name = scanner.next();
		System.out.print("Enter patient age: ");
		int age = scanner.nextInt();
		System.out.print("Enter patient gender: ");
		String gender = scanner.next();

		try {

			String insertQuery = "INSERT into patients(name, gender, age) values(?, ?, ?)";

			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, gender);
			preparedStatement.setInt(3, age);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Patient Added Successfully !");
			} else {
				System.out.println("Failed to Add Patient !");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void viewPatient() {
		String viewQuery = "SELECT * FROM patients";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(viewQuery);
			ResultSet resultSet = preparedStatement.executeQuery();

			// Printing the patients in tabular format
			System.out.println("Patients: ");
			System.out.println("+------------+-----------------------+--------+----------+");
			System.out.println("| Patient ID | Patient Name          |  Age   | Gender   |");
			System.out.println("+------------+-----------------------+--------+----------+");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String gender = resultSet.getString("gender");
				int age = resultSet.getInt("age");

				// to format the output we will use printF and not println
				// \n for going to the new line
				System.out.printf("| %-10s | %-21s | %-6s | %-8s |\n",id,name,age,gender);
				System.out.println("+------------+-----------------------+--------+----------+");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// to check if patient exists in DB or not
	public boolean getPatientById(int id) {
		String query = "SELECT * FROM patients where id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
