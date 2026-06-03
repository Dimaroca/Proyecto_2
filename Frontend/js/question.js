const form = document.querySelector("form");

const rating = document.getElementById("minRating");
const ratingValue = document.getElementById("ratingValue");
const ratingStars = document.getElementById("ratingStars");

function updateRating() {

    const value = parseFloat(rating.value);

    ratingValue.textContent = value.toFixed(1);

    const fullStars = Math.floor(value);
    const emptyStars = 5 - fullStars;

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

    logoutBtn.addEventListener("click", () => {

        sessionStorage.clear();

        window.location.href =
            "login.html";
    });
}

/* ==========================
   FORMULARIO
========================== */

form.addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();

        const food =
            form.querySelector(
                "input[name='food']:checked"
            )?.value;

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

            } catch (error) {

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