package com.skilldistillery.filmquery.database;

import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.Actor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static String URL = "jdbc:mysql://localhost:3306/sdvid";
	String user = "student";
	String pass = "student";

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;

		try {
			Connection connection = DriverManager.getConnection(URL, user, pass);
//			String sqlText = "SELECT id, title, description, release_year";
			String sqlText = "SELECT * FROM film WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sqlText);
			statement.setInt(1, filmId);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				film = new Film();
				film.setId(results.getInt("id"));
				film.setTitle(results.getString("title"));
				film.setDescription(results.getString("description"));
				film.setReleaseYear(results.getInt("release_year"));
				film.setLanguageId(results.getInt("language_id"));
				film.setRentalDuration(results.getInt("rental_duration"));
				film.setRentalRate(results.getDouble("rental_rate"));
				film.setLength(results.getInt("length"));
				film.setReplacementCost(results.getDouble("replacement_cost"));
				film.setRating(results.getString("rating"));
				film.setSpecialFeatures(results.getString("special_features"));

				System.out.println(results.getString(1) + " " + results.getString(2) + " " + results.getString(3));
			}
			results.close();
			statement.close();
			connection.close();

		} catch (SQLException sqle) {
			System.out.println("Error Getting Film " + filmId);
			sqle.printStackTrace();
		}

		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		return null;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		while() {
			
		}
		
		
		return actors;
	}

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Unable to load JDBC driver.");
		}
	}
}