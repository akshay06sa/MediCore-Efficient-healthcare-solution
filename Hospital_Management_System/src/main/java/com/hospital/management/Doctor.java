package com.hospital.management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {

	private Connection connection;

	public Doctor(Connection connection) {
		this.connection = connection;
	}

	
	public void viewDoctors() {
		String viewQuery = "SELECT * FROM doctors";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(viewQuery);
			ResultSet resultSet = preparedStatement.executeQuery();

			// Printing the patients in tabular format
			System.out.println("Doctors: ");
			System.out.println("+-----------+----------------------+-----------------------+");
			System.out.println("| Doctor ID | Doctor Name          | Specialization        |");
			System.out.println("+-----------+----------------------+-----------------------+");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String specialization = resultSet.getString("speciallization");

				// to format the output we will use printF and not println
				// \n for going to new line
				System.out.printf("| %-9s | %-20s | %-21s |\n",id,name,specialization);
				System.out.println("+-----------+----------------------+-----------------------+");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// to check if doctor exists in DB or not
	public boolean getDoctorById(int id) {
		String query = "SELECT * FROM doctors where id = ?";
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
