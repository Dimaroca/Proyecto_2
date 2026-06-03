const params = new URLSearchParams(window.location.search);

const restaurantId = params.get("id");
const from = params.get("from");

const backBtn = document.getElementById("backBtn");
const reviewBtn = document.getElementById("reviewBtn");
const logoutBtn = document.getElementById("logoutBtn");

if (from === "recommendation") {

    backBtn.href = "recommendation.html";

    reviewBtn.href =
        `review.html?id=${restaurantId}&from=recommendation`;

} else {

    backBtn.href = "menu.html";

    reviewBtn.href =
        `review.html?id=${restaurantId}&from=menu`;
}

if(logoutBtn){

    logoutBtn.addEventListener("click", () => {

        sessionStorage.clear();

        window.location.href =
            "login.html";
    });
}

function generateStars(rating){

    const rounded =
        Math.round(rating);

    return (
        "★".repeat(rounded) +
        "☆".repeat(5 - rounded)
    );
}

async function loadRestaurant() {

    if (!restaurantId) {

        document.getElementById(
            "restaurantName"
        ).textContent =
            "Restaurante no encontrado";

        return;
    }

    try {

        const response = await fetch(
            `http://localhost:4567/api/restaurante/${restaurantId}`
        );

        if (!response.ok) {

            throw new Error(
                "Restaurante no encontrado"
            );
        }

        const restaurant =
            await response.json();

        document.getElementById(
            "restaurantName"
        ).textContent =
            restaurant.name;

        const stars =
            generateStars(
                restaurant.rating
            );

        document.getElementById(
            "restaurantRating"
        ).textContent =
            `${stars} ${restaurant.rating.toFixed(1)}`;

        document.getElementById(
            "restaurantCategory"
        ).textContent =
            restaurant.category || "No disponible";

        document.getElementById(
            "restaurantCity"
        ).textContent =
            restaurant.city || "No disponible";

        document.getElementById(
            "restaurantZone"
        ).textContent =
            restaurant.zone || "No disponible";

        document.getElementById(
            "restaurantEnvironment"
        ).textContent =
            restaurant.environment || "No disponible";

        document.getElementById(
            "restaurantDescription"
        ).textContent =
            restaurant.description || "No disponible";

    } catch (error) {

        console.error(error);

        document.getElementById(
            "restaurantName"
        ).textContent =
            "Error cargando restaurante";
    }
}

loadRestaurant();