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
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection connection = DriverManager.getConnection(URL, user, pass);
			actor = null;
			String sqlText = "SELECT * FROM actor WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sqlText);
			statement.setInt(1, actorId);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				actor = new Actor();
				actor.setId(results.getInt("id"));
				actor.setFirstName(results.getString("first_name"));
				actor.setLastName(results.getString("last_name"));
			}
			results.close();
			statement.close();
			connection.close();
			return actor;
		} catch (SQLException sqle) {
			System.out.println("Error Getting Actor " + actorId);
			sqle.printStackTrace();
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		Actor actor = null;
		List<Actor> actorsList = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(URL, user, pass);
			String sqlText = "SELECT actor.* FROM actor JOIN film_actor on actor.id = film_actor.actor_id WHERE film_actor.film_id = ?";
			PreparedStatement statement = connection.prepareStatement(sqlText);
			statement.setInt(1, filmId);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				actor = new Actor();
				actor.setId(results.getInt("id"));
				actor.setFirstName(results.getString("first_name"));
				actor.setLastName(results.getString("last_name"));
				actorsList.add(actor);
			}
			results.close();
			statement.close();
			connection.close();
		} catch (SQLException sqle) {

			sqle.printStackTrace();
		}
		return actorsList;
	}
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Unable to load JDBC driver.");
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;

		try {
			Connection connection = DriverManager.getConnection(URL, user, pass);
			String sqlText = "SELECT film.id, title, description, release_year, rental_duration, rental_rate, length, replacement_cost, rating, special_features, "
					+ "language.name AS language_name " + "FROM film "
					+ "JOIN language ON film.language_id = language.id " + "WHERE film.id = ?";
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

	public List<Film> findFilmsByKeyword(String keyword) {
		Film film = null;
		List<Film> films = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(URL, user, pass);
			String sqlText = "SELECT film.id, title, description, release_year, rental_duration, rental_rate, length, replacement_cost, rating, special_features,"
					+ "language.name AS language_name " + "FROM film "
					+ "JOIN language ON film.language_id = language.id " + "WHERE title LIKE ? OR description LIKE ?";
			PreparedStatement statement = connection.prepareStatement(sqlText);
			statement.setString(1, "%" + keyword + "%");
			statement.setString(2, "%" + keyword + "%");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				film = new Film();
				film.setId(results.getInt("id"));
				film.setTitle(results.getString("title"));
				film.setDescription(results.getString("description"));
				film.setReleaseYear(results.getInt("release_year"));
				film.setLanguage(results.getString("language_name"));
				film.setRentalDuration(results.getInt("rental_duration"));
				film.setRentalRate(results.getDouble("rental_rate"));
				film.setRentalDuration(results.getInt("length"));
				film.setReplacementCost(results.getDouble("replacement_cost"));
				film.setRating(results.getString("rating"));
				film.setSpecialFeatures(results.getString("special_features"));
				film.setFilmActors(findActorsByFilmId(film.getId()));
				films.add(film);
			}
			results.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}
}