const params =
    new URLSearchParams(
        window.location.search
    );

const restaurantId =
    params.get("id");

async function loadRestaurant() {

    if (!restaurantId) return;

    try {

        const response =
            await fetch(
                `http://localhost:4567/api/restaurante/${restaurantId}`
            );

        const restaurant =
            await response.json();

        document.getElementById(
            "restaurantName"
        ).textContent =
            restaurant.name;

    } catch(error) {

        console.error(error);
    }
}

document
    .getElementById("reviewForm")
    .addEventListener(
        "submit",
        async function(e){

            e.preventDefault();

            const rating =
                parseInt(
                    document.getElementById(
                        "rating"
                    ).value
                );

            const userId =
                sessionStorage.getItem(
                    "userId"
                );

            try {

                await fetch(
                    "http://localhost:4567/api/review",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                            "application/json"
                        },

                        body: JSON.stringify({
                            userId,
                            rating
                        })
                    }
                );

            } catch(error) {

                console.error(error);
            }

            window.location.href =
                "menu.html";
        }
    );

loadRestaurant();