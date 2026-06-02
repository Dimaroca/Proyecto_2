const arrows =
document.querySelectorAll(".arrow");

arrows.forEach(arrow => {

    arrow.addEventListener(
        "click",
        () => {

            const sliderId =
                arrow.dataset.slider;

            const direction =
                Number(
                    arrow.dataset.direction
                );

            const slider =
                document.getElementById(
                    sliderId
                );

            if (direction > 0) {

                slider.appendChild(
                    slider.firstElementChild
                );

            } else {

                slider.insertBefore(
                    slider.lastElementChild,
                    slider.firstElementChild
                );
            }
        }
    );

});

function createCard(restaurant) {

    const card =
        document.createElement("div");

    card.className =
        "restaurant-card";

    card.innerHTML = `
        <h3>${restaurant.name}</h3>
        <p>${restaurant.category}</p>
        <span>⭐ ${restaurant.rating.toFixed(1)}</span>
    `;

    return card;
}

async function loadRecommendations() {

    const food =
        sessionStorage.getItem("food");

    const budget =
        sessionStorage.getItem("budget");

    const environment =
        sessionStorage.getItem(
            "environment"
        );

    const minRating =
        parseFloat(
            sessionStorage.getItem(
                "minRating"
            )
        );

    const distance =
        sessionStorage.getItem(
            "distance"
        );

    const userId =
        sessionStorage.getItem(
            "userId"
        ) || "";

    try {

        const response =
            await fetch(
                "http://localhost:4567/api/recomendaciones",
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

        const restaurants =
            await response.json();

        const slider =
            document.getElementById(
                "recommendedSlider"
            );

        slider.innerHTML = "";

        restaurants.forEach(r => {

            slider.appendChild(
                createCard(r)
            );
        });

    } catch(error) {

        console.error(error);
    }
}

loadRecommendations();