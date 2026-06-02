const form = document.querySelector("form");

const ratingSlider =
    document.getElementById("minRating");

const ratingValue =
    document.getElementById("ratingValue");

ratingSlider.addEventListener("input", () => {

    ratingValue.textContent =
        ratingSlider.value;
});

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
            parseFloat(
                ratingSlider.value
            );

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

            } catch(error) {

                console.warn(
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