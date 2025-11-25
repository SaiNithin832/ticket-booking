package service;

import model.Movie;
import model.Show;
import storage.FileStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookingService {
    private final FileStorage storage;
    private final Map<String, Movie> movies;
    private final Map<String, Show> showsById;
    private final List<Show> schedule;

    public BookingService(FileStorage storage) {
        this.storage = storage;
        this.movies = new LinkedHashMap<>(storage.loadMovies());
        this.schedule = new ArrayList<>(storage.loadShows());
        this.showsById = new LinkedHashMap<>();
        for (Show show : schedule) {
            showsById.put(show.getId(), show);
        }
    }

    public List<Movie> getMovies() {
        return new ArrayList<>(movies.values());
    }

    public List<Show> getShows() {
        return new ArrayList<>(schedule);
    }

    public Movie getMovie(String movieId) {
        return movies.get(movieId);
    }

    public Show getShow(String showId) {
        return showsById.get(showId);
    }

    public boolean bookSeats(String showId, int quantity) {
        Show show = showsById.get(showId);
        if (show == null) {
            return false;
        }
        boolean result = show.bookSeats(quantity);
        if (result) {
            persistShows();
        }
        return result;
    }

    public boolean cancelSeats(String showId, int quantity) {
        Show show = showsById.get(showId);
        if (show == null) {
            return false;
        }
        boolean result = show.cancelSeats(quantity);
        if (result) {
            persistShows();
        }
        return result;
    }

    private void persistShows() {
        storage.saveShows(schedule);
    }
}
