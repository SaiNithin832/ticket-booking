package storage;

import model.Movie;
import model.Show;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileStorage {
    private final Path dataDirectory;
    private final Path movieFile;
    private final Path showFile;

    public FileStorage(String dataDirectoryName) {
        this.dataDirectory = Paths.get(dataDirectoryName);
        this.movieFile = dataDirectory.resolve("movies.csv");
        this.showFile = dataDirectory.resolve("shows.csv");
        ensureDataFiles();
    }

    private void ensureDataFiles() {
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
            if (!Files.exists(movieFile)) {
                Files.createFile(movieFile);
            }
            if (!Files.exists(showFile)) {
                Files.createFile(showFile);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to prepare data directory", e);
        }
    }

    public Map<String, Movie> loadMovies() {
        Map<String, Movie> movies = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(movieFile);
            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    continue;
                }
                Movie movie = new Movie(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                movies.put(movie.getId(), movie);
            }
        } catch (IOException e) {
            System.out.println("Failed to load movies: " + e.getMessage());
        }
        return movies;
    }

    public List<Show> loadShows() {
        List<Show> shows = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(showFile);
            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 5) {
                    continue;
                }
                Show show = new Show(
                        parts[0],
                        parts[1],
                        parts[2],
                        Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4])
                );
                shows.add(show);
            }
        } catch (IOException e) {
            System.out.println("Failed to load shows: " + e.getMessage());
        }
        return shows;
    }

    public void saveShows(List<Show> shows) {
        List<String> lines = new ArrayList<>();
        for (Show show : shows) {
            lines.add(String.join("|",
                    show.getId(),
                    show.getMovieId(),
                    show.getStartTime(),
                    String.valueOf(show.getTotalSeats()),
                    String.valueOf(show.getAvailableSeats())));
        }
        try {
            Files.write(showFile, lines);
        } catch (IOException e) {
            System.out.println("Failed to save shows: " + e.getMessage());
        }
    }
}
