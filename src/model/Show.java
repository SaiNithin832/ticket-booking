package model;

public class Show {
    private final String id;
    private final String movieId;
    private final String startTime;
    private final int totalSeats;
    private int availableSeats;

    public Show(String id, String movieId, String startTime, int totalSeats, int availableSeats) {
        this.id = id;
        this.movieId = movieId;
        this.startTime = startTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    public String getId() {
        return id;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public boolean bookSeats(int quantity) {
        if (quantity <= 0 || quantity > availableSeats) {
            return false;
        }
        availableSeats -= quantity;
        return true;
    }

    public boolean cancelSeats(int quantity) {
        if (quantity <= 0 || availableSeats + quantity > totalSeats) {
            return false;
        }
        availableSeats += quantity;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Show %s at %s | Seats: %d/%d", id, startTime, availableSeats, totalSeats);
    }
}
