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

        if(!response.ok){

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

    } catch(error) {

        console.error(error);

        document.getElementById(
            "restaurantName"
        ).textContent =
            "Error cargando restaurante";
    }
}

const stars =
    document.querySelectorAll(".star");

const ratingInput =
    document.getElementById("rating");

stars.forEach(star => {

    star.addEventListener(
        "click",
        () => {

            const value =
                Number(
                    star.dataset.value
                );

            ratingInput.value =
                value;

            stars.forEach(s => {

                if(
                    Number(
                        s.dataset.value
                    ) <= value
                ){

                    s.classList.add(
                        "active"
                    );

                } else {

                    s.classList.remove(
                        "active"
                    );
                }
            });
        }
    );
});

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

        const comment =
            document.getElementById(
                "comment"
            ).value;

        const userId =
            sessionStorage.getItem(
                "userId"
            );

        try {

            await fetch(
                "http://localhost:4567/api/review",
                {
                    method:"POST",

                    headers:{
                        "Content-Type":
                        "application/json"
                    },

                    body:JSON.stringify({

                        userId,
                        rating,
                        comment
                    })
                }
            );

            alert(
                "Reseña enviada correctamente"
            );

            window.location.href =
                "menu.html";

        }
        catch(error){

            console.error(error);

            alert(
                "No se pudo enviar la reseña"
            );
        }
    }
);

loadRestaurant();