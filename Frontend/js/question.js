const form = document.querySelector("form");

const rating = document.getElementById("minRating");
const ratingValue = document.getElementById("ratingValue");
const ratingStars = document.getElementById("ratingStars");

const foodSelect = document.getElementById("foodSelect");

/* ==========================
   CARGAR TIPOS DE COMIDA
========================== */

async function loadFoods() {

    try {

        const response = await fetch(
            "http://localhost:4567/api/restaurantes"
        );

        if (!response.ok) {
            throw new Error(
                "No se pudieron cargar los restaurantes"
            );
        }

        const restaurants =
            await response.json();

        const foods = new Set();

        restaurants.forEach(r => {

            if (!r.category) return;

            r.category
                .split(",")
                .forEach(food => {

                    const cleanFood =
                        food.trim();

                    if (cleanFood.length > 0) {
                        foods.add(cleanFood);
                    }
                });
        });

        [...foods]
            .sort()
            .forEach(food => {

                const option =
                    document.createElement("option");

                option.value = food;
                option.textContent = food;

                foodSelect.appendChild(option);
            });

    } catch(error) {

        console.error(
            "Error cargando tipos de comida:",
            error
        );
    }
}

loadFoods();

/* ==========================
   RATING
========================== */

function updateRating() {

    const value =
        parseFloat(rating.value);

    ratingValue.textContent =
        value.toFixed(1);

    const fullStars =
        Math.floor(value);

    const emptyStars =
        5 - fullStars;

    ratingStars.textContent =
        "★".repeat(fullStars) +
        "☆".repeat(emptyStars);
}

rating.addEventListener(
    "input",
    updateRating
);

updateRating();

/* ==========================
   CERRAR SESIÓN
========================== */

const logoutBtn =
    document.getElementById("logoutBtn");

if (logoutBtn) {

    logoutBtn.addEventListener(
        "click",
        () => {

            sessionStorage.clear();

            window.location.href =
                "login.html";
        }
    );
}

/* ==========================
   FORMULARIO
========================== */

form.addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();

        const food =
            foodSelect.value;

        const budget =
            form.querySelector(
                "input[name='budget']:checked"
            )?.value;

        const environment =
            form.querySelector(
                "input[name='environment']:checked"
            )?.value;

        const minRating =
            parseFloat(rating.value);

        const distance =
            document.getElementById(
                "distance"
            ).value;

        if (
            !food ||
            !budget ||
            !environment
        ) {

            alert(
                "Por favor responde todas las preguntas."
            );

            return;
        }

        const userId =
            sessionStorage.getItem(
                "userId"
            );

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
                                environment,
                                minRating,
                                distance
                            })
                        }
                    );

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

        sessionStorage.setItem(
            "food",
            food
        );

        sessionStorage.setItem(
            "budget",
            budget
        );

        sessionStorage.setItem(
            "environment",
            environment
        );

        sessionStorage.setItem(
            "minRating",
            minRating
        );

        sessionStorage.setItem(
            "distance",
            distance
        );

        window.location.href =
            "recommendation.html";
    }
);