// Obtiene el formulario principal
const form = document.querySelector("form");

// Elementos relacionados con la calificación mínima
const rating = document.getElementById("minRating");
const ratingValue = document.getElementById("ratingValue");
const ratingStars = document.getElementById("ratingStars");

// Lista desplegable de tipos de comida
const foodSelect = document.getElementById("foodSelect");

/* ==========================
   CARGAR TIPOS DE COMIDA
========================== */

// Obtiene los tipos de comida desde los restaurantes registrados
async function loadFoods() {

    try {

        // Solicita los restaurantes al backend
        const response = await fetch(
            "http://localhost:4567/api/restaurantes"
        );

        // Verifica si la respuesta fue correcta
        if (!response.ok) {
            throw new Error(
                "No se pudieron cargar los restaurantes"
            );
        }

        // Convierte la respuesta a JSON
        const restaurants =
            await response.json();

        // Conjunto para evitar categorías repetidas
        const foods = new Set();

        // Recorre todos los restaurantes
        restaurants.forEach(r => {

            // Ignora restaurantes sin categoría
            if (!r.category) return;

            // Divide las categorías separadas por comas
            r.category
                .split(",")
                .forEach(food => {

                    const cleanFood =
                        food.trim();

                    // Agrega la categoría si no está vacía
                    if (cleanFood.length > 0) {
                        foods.add(cleanFood);
                    }
                });
        });

        // Ordena las categorías alfabéticamente
        [...foods]
            .sort()
            .forEach(food => {

                // Crea una nueva opción para el select
                const option =
                    document.createElement("option");

                option.value = food;
                option.textContent = food;

                // Agrega la opción al select
                foodSelect.appendChild(option);
            });

    } catch(error) {

        // Muestra errores en consola
        console.error(
            "Error cargando tipos de comida:",
            error
        );
    }
}

// Ejecuta la carga de categorías al iniciar la página
loadFoods();

/* ==========================
   CALIFICACIÓN
========================== */

// Actualiza el valor y las estrellas de la calificación
function updateRating() {

    const value =
        parseFloat(rating.value);

    // Muestra el valor numérico
    ratingValue.textContent =
        value.toFixed(1);

    // Calcula estrellas llenas
    const fullStars =
        Math.floor(value);

    // Calcula estrellas vacías
    const emptyStars =
        5 - fullStars;

    // Actualiza la visualización de estrellas
    ratingStars.textContent =
        "★".repeat(fullStars) +
        "☆".repeat(emptyStars);
}

// Actualiza la calificación al mover el slider
rating.addEventListener(
    "input",
    updateRating
);

// Muestra la calificación inicial
updateRating();

/* ==========================
   CERRAR SESIÓN
========================== */

// Obtiene el botón de cerrar sesión
const logoutBtn =
    document.getElementById("logoutBtn");

// Verifica que el botón exista
if (logoutBtn) {

    logoutBtn.addEventListener(
        "click",
        () => {

            // Limpia todos los datos de sesión
            sessionStorage.clear();

            // Regresa a la página de login
            window.location.href =
                "login.html";
        }
    );
}

/* ==========================
   FORMULARIO
========================== */

// Maneja el envío del formulario de preferencias
form.addEventListener(
    "submit",
    async function(event) {

        // Evita que la página se recargue
        event.preventDefault();

        // Obtiene el tipo de comida seleccionado
        const food =
            foodSelect.value;

        // Obtiene el presupuesto seleccionado
        const budget =
            form.querySelector(
                "input[name='budget']:checked"
            )?.value;

        // Obtiene la calificación mínima seleccionada
        const minRating =
            parseFloat(rating.value);

        // Obtiene la distancia seleccionada
        const distance =
            document.getElementById(
                "distance"
            ).value;

        // Verifica que todas las preguntas obligatorias estén respondidas
        if (
            !food ||
            !budget
        ) {

            alert(
                "Por favor responde todas las preguntas."
            );

            return;
        }

        // Obtiene el identificador del usuario actual
        const userId =
            sessionStorage.getItem(
                "userId"
            );

        // Guarda las preferencias en la base de datos si existe usuario
        if (userId) {

            try {

                const response =
                    await fetch(
                        "http://localhost:4567/api/preferencias",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({
                                userId,
                                food,
                                budget,
                                minRating,
                                distance
                            })
                        }
                    );

                // Verifica si hubo error al guardar
                if (!response.ok) {

                    throw new Error(
                        "Error al guardar preferencias"
                    );
                }

            } catch(error) {

                console.error(
                    "No se pudieron guardar preferencias:",
                    error
                );
            }
        }

        // Guarda las preferencias en sessionStorage
        sessionStorage.setItem(
            "food",
            food
        );

        sessionStorage.setItem(
            "budget",
            budget
        );

        sessionStorage.setItem(
            "minRating",
            minRating
        );

        sessionStorage.setItem(
            "distance",
            distance
        );

        // Redirige a la página de recomendaciones
        window.location.href =
            "recommendation.html";
    }
);