// Obtiene los parámetros enviados en la URL
const params = new URLSearchParams(window.location.search);

// Obtiene el id del restaurante desde la URL
const restaurantId = params.get("id");

// Obtiene desde qué página llegó el usuario
const from = params.get("from");

// Obtiene los botones de la página
const backBtn = document.getElementById("backBtn");
const reviewBtn = document.getElementById("reviewBtn");
const logoutBtn = document.getElementById("logoutBtn");

/* ==========================
   BOTONES DE NAVEGACIÓN
========================== */

// Define a dónde debe regresar el usuario según la página anterior
if (from === "recommendation") {

    backBtn.href = "recommendation.html";

    reviewBtn.href =
        `review.html?id=${restaurantId}&from=recommendation`;

} else {

    backBtn.href = "menu.html";

    reviewBtn.href =
        `review.html?id=${restaurantId}&from=menu`;
}

/* ==========================
   CERRAR SESIÓN
========================== */

// Limpia la sesión y devuelve al login
if(logoutBtn){

    logoutBtn.addEventListener("click", () => {

        sessionStorage.clear();

        window.location.href =
            "login.html";
    });
}

/* ==========================
   ESTRELLAS
========================== */

// Genera estrellas visuales según la calificación del restaurante
function generateStars(rating){

    const rounded =
        Math.round(rating);

    return (
        "★".repeat(rounded) +
        "☆".repeat(5 - rounded)
    );
}

/* ==========================
   CARGAR RESTAURANTE
========================== */

// Carga la información completa del restaurante seleccionado
async function loadRestaurant() {

    // Verifica si existe un id en la URL
    if (!restaurantId) {

        document.getElementById(
            "restaurantName"
        ).textContent =
            "Restaurante no encontrado";

        return;
    }

    try {

        // Solicita la información del restaurante al backend
        const response = await fetch(
            `http://localhost:4567/api/restaurante/${restaurantId}`
        );

        // Verifica si el restaurante fue encontrado
        if (!response.ok) {

            throw new Error(
                "Restaurante no encontrado"
            );
        }

        // Convierte la respuesta a JSON
        const restaurant =
            await response.json();

        // Muestra el nombre del restaurante
        document.getElementById(
            "restaurantName"
        ).textContent =
            restaurant.name;

        // Genera las estrellas según la calificación
        const stars =
            generateStars(
                restaurant.rating
            );

        // Muestra estrellas y calificación numérica
        document.getElementById(
            "restaurantRating"
        ).textContent =
            `${stars} ${restaurant.rating.toFixed(1)}`;

        // Muestra la categoría del restaurante
        document.getElementById(
            "restaurantCategory"
        ).textContent =
            restaurant.category || "No disponible";

        // Muestra la ciudad del restaurante
        document.getElementById(
            "restaurantCity"
        ).textContent =
            restaurant.city || "No disponible";

        // Muestra la zona del restaurante
        document.getElementById(
            "restaurantZone"
        ).textContent =
            restaurant.zone || "No disponible";


    } catch (error) {

        // Muestra el error en consola
        console.error(error);

        // Muestra mensaje de error en pantalla
        document.getElementById(
            "restaurantName"
        ).textContent =
            "Error cargando restaurante";
    }
}

// Ejecuta la carga del restaurante al abrir la página
loadRestaurant();