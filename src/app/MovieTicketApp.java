package app;

import model.Movie;
import model.Show;
import service.BookingService;
import storage.FileStorage;

import java.util.List;
import java.util.Scanner;

public class MovieTicketApp {
    private final BookingService bookingService;
    private final Scanner scanner;

    public MovieTicketApp() {
        FileStorage storage = new FileStorage("data");
        this.bookingService = new BookingService(storage);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1 -> listMovies();
                case 2 -> listShows();
                case 3 -> showDetails();
                case 4 -> bookTickets();
                case 5 -> cancelTickets();
                case 6 -> running = false;
                default -> System.out.println("Unknown option. Try again.");
            }
        }
        System.out.println("Thank you for using the Movie Ticket Booking System!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Movie Ticket Booking System ===");
        System.out.println("1. List Movies");
        System.out.println("2. List Shows");
        System.out.println("3. Show Details");
        System.out.println("4. Book Tickets");
        System.out.println("5. Cancel Tickets");
        System.out.println("6. Exit");
    }

    private void listMovies() {
        List<Movie> movies = bookingService.getMovies();
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }
        System.out.println("\n-- Movies --");
        for (Movie movie : movies) {
            System.out.printf("%s: %s%n", movie.getId(), movie);
        }
    }

    private void listShows() {
        List<Show> shows = bookingService.getShows();
        if (shows.isEmpty()) {
            System.out.println("No shows scheduled.");
            return;
        }
        System.out.println("\n-- Shows --");
        for (Show show : shows) {
            Movie movie = bookingService.getMovie(show.getMovieId());
            String movieInfo = movie != null ? movie.getTitle() : "Unknown Movie";
            System.out.printf("%s | %s | %s | Seats %d/%d%n",
                    show.getId(),
                    movieInfo,
                    show.getStartTime(),
                    show.getAvailableSeats(),
                    show.getTotalSeats());
        }
    }

    private void showDetails() {
        System.out.print("Enter show ID: ");
        String showId = scanner.nextLine().trim();
        Show show = bookingService.getShow(showId);
        if (show == null) {
            System.out.println("Show not found.");
            return;
        }
        Movie movie = bookingService.getMovie(show.getMovieId());
        System.out.println("\n-- Show Details --");
        if (movie != null) {
            System.out.println("Movie: " + movie.getTitle());
            System.out.println("Genre: " + movie.getGenre());
            System.out.println("Duration: " + movie.getDurationMinutes() + " mins");
        }
        System.out.println("Show Time: " + show.getStartTime());
        System.out.println("Seats Available: " + show.getAvailableSeats() + "/" + show.getTotalSeats());
    }

    private void bookTickets() {
        System.out.print("Enter show ID: ");
        String showId = scanner.nextLine().trim();
        int quantity = readInt("How many seats? ");
        if (quantity <= 0) {
            System.out.println("Invalid seat count.");
            return;
        }
        boolean booked = bookingService.bookSeats(showId, quantity);
        if (booked) {
            System.out.println("Successfully booked " + quantity + " seat(s).");
        } else {
            System.out.println("Booking failed. Check show ID or seat availability.");
        }
    }

    private void cancelTickets() {
        System.out.print("Enter show ID: ");
        String showId = scanner.nextLine().trim();
        int quantity = readInt("How many seats to cancel? ");
        if (quantity <= 0) {
            System.out.println("Invalid seat count.");
            return;
        }
        boolean cancelled = bookingService.cancelSeats(showId, quantity);
        if (cancelled) {
            System.out.println("Successfully cancelled " + quantity + " seat(s).");
        } else {
            System.out.println("Cancellation failed. Check show ID or seat count.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        new MovieTicketApp().start();
    }
}
