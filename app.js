const movies = [
    { id: "M1", title: "The Grand Heist", genre: "Action", duration: 130, synopsis: "Elite thieves attempt an impossible vault break-in." },
    { id: "M2", title: "Romance in Rome", genre: "Romance", duration: 115, synopsis: "Two travelers find love amid the Eternal City." },
    { id: "M3", title: "Galactic Quest", genre: "Sci-Fi", duration: 140, synopsis: "A crew races to stop a cosmic catastrophe." }
];

const shows = [
    { id: "S1", movieId: "M1", startTime: "2025-11-25 18:00", totalSeats: 100, availableSeats: 95 },
    { id: "S2", movieId: "M1", startTime: "2025-11-25 21:30", totalSeats: 80, availableSeats: 80 },
    { id: "S3", movieId: "M2", startTime: "2025-11-25 17:15", totalSeats: 60, availableSeats: 55 },
    { id: "S4", movieId: "M3", startTime: "2025-11-26 19:45", totalSeats: 120, availableSeats: 120 }
];

const moviesListEl = document.getElementById("movies-list");
const showsTableBody = document.getElementById("shows-table-body");
const showSelect = document.getElementById("show-select");
const seatCount = document.getElementById("seat-count");
const showDetails = document.getElementById("show-details");
const statusMessage = document.getElementById("status-message");
const bookBtn = document.getElementById("book-btn");
const cancelBtn = document.getElementById("cancel-btn");

function getMovieById(id) {
    return movies.find((movie) => movie.id === id);
}

function getShowById(id) {
    return shows.find((show) => show.id === id);
}

function renderMovies() {
    moviesListEl.innerHTML = movies
        .map(
            (movie) => `
            <article class="movie-card">
                <h3>${movie.title}</h3>
                <div class="movie-meta">${movie.genre}</div>
                <div class="movie-duration">${movie.duration} mins</div>
                <p>${movie.synopsis}</p>
            </article>
        `
        )
        .join("");
}

function renderShows() {
    showsTableBody.innerHTML = shows
        .map((show) => {
            const movie = getMovieById(show.movieId);
            const title = movie ? movie.title : "Unknown";
            return `
                <tr data-show-id="${show.id}">
                    <td>${show.id}</td>
                    <td>${title}</td>
                    <td>${show.startTime}</td>
                    <td>${show.availableSeats}/${show.totalSeats}</td>
                </tr>
            `;
        })
        .join("");
}

function renderShowOptions() {
    showSelect.innerHTML = shows
        .map((show) => {
            const movie = getMovieById(show.movieId);
            const title = movie ? movie.title : show.movieId;
            return `<option value="${show.id}">${show.id} — ${title} (${show.startTime})</option>`;
        })
        .join("");
}

function renderShowDetails(showId) {
    const show = getShowById(showId);
    if (!show) {
        showDetails.innerHTML = "Select a show to view details.";
        return;
    }
    const movie = getMovieById(show.movieId);
    showDetails.innerHTML = `
        <h3>${movie?.title ?? "Unknown Show"}</h3>
        <p>${movie?.genre ?? ""} • ${movie?.duration ? movie.duration + " mins" : ""}</p>
        <p><strong>Show Time:</strong> ${show.startTime}</p>
        <p><strong>Seats Available:</strong> ${show.availableSeats} / ${show.totalSeats}</p>
    `;
}

function updateStatus(message, type = "") {
    statusMessage.textContent = message;
    statusMessage.className = `status ${type}`.trim();
}

function handleSeats(showId, quantity, mode) {
    const show = getShowById(showId);
    if (!show) {
        updateStatus("Show not found.", "error");
        return;
    }
    if (!Number.isInteger(quantity) || quantity <= 0) {
        updateStatus("Enter a valid seat count.", "error");
        return;
    }

    if (mode === "book") {
        if (quantity > show.availableSeats) {
            updateStatus("Insufficient seats available.", "error");
            return;
        }
        show.availableSeats -= quantity;
        updateStatus(`Booked ${quantity} seat(s) for ${show.id}.`, "success");
    } else if (mode === "cancel") {
        if (show.availableSeats + quantity > show.totalSeats) {
            updateStatus("Cannot cancel more than total seats.", "error");
            return;
        }
        show.availableSeats += quantity;
        updateStatus(`Cancelled ${quantity} seat(s) for ${show.id}.`, "success");
    }
    renderShows();
    renderShowDetails(showId);
}

bookBtn.addEventListener("click", () => {
    const showId = showSelect.value;
    const quantity = Number.parseInt(seatCount.value, 10);
    handleSeats(showId, quantity, "book");
});

cancelBtn.addEventListener("click", () => {
    const showId = showSelect.value;
    const quantity = Number.parseInt(seatCount.value, 10);
    handleSeats(showId, quantity, "cancel");
});

showSelect.addEventListener("change", (event) => renderShowDetails(event.target.value));

// Initialize UI
renderMovies();
renderShows();
renderShowOptions();
renderShowDetails(showSelect.value || shows[0].id);
updateStatus("Select an action to begin.");
