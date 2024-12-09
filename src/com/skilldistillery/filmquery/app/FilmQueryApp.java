package com.skilldistillery.filmquery.app;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
	DatabaseAccessor database = new DatabaseAccessorObject();
	Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		app.launch();
	}

	private void launch() {
		menuOptions();
	}

	private void menuOptions() {
		boolean running = true;
		do {

			while (running) {
				System.out.println("           Welcome to FilmFindr!");
				System.out.println("******************************************");
				System.out.println("|| 1) Search For a Film By Title ID    	||");
				System.out.println("|| 2) Search For a Film By a Keyword 	||");
				System.out.println("|| 3) Quit the Program 			||");
				System.out.println("******************************************");
				System.out.println("         What Would You Like To Do?");
				System.out.println();
				String selection = input.next();
				switch (selection) {
				case "1":
					System.out.println();
					System.out.println("Enter a Movie ID#(1-1,000):");
					int movieId = input.nextInt();
					Film film = database.findFilmById(movieId);
					System.out.println(film);
					break;
				case "2":
					filmsByKeyword();
					break;
				case "3":
					System.out.println("Thanks for Using FilmFindr!");
					running = false;
					break;
				default:
					System.err.println("Invalid Choice! Try Again");
				}
			}
		} while (true);

	}

	public void filmsByKeyword() {
		System.out.println("Enter a Movie Keyword: ");
		try {
			String selection = input.next();
			List<Film> films = database.findFilmsByKeyword(selection);
			for (Film film : films) {
				film.setFilmActors(database.findActorsByFilmId(film.getId()));
			}
			if (films.size() == 0) {
				System.err.println("Keyword Cannot Be Found");
				System.out.println();
			} else {
				System.out.println(films);
				System.out.println();
			}
		} catch (InputMismatchException ime) {
			input.nextLine();
		}
	}
}