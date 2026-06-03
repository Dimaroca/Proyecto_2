const arrows =
document.querySelectorAll(".arrow");

/* ==========================
   ESTRELLAS
========================== */

function generateStars(rating){

    const rounded =
        Math.round(rating);

    return (
        "★".repeat(rounded) +
        "☆".repeat(5 - rounded)
    );
}

/* ==========================
   SLIDERS
========================== */

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

            if(!slider ||
               !slider.firstElementChild){

                return;
            }

            if(direction > 0){

                slider.appendChild(
                    slider.firstElementChild
                );

            }else{

                slider.insertBefore(
                    slider.lastElementChild,
                    slider.firstElementChild
                );
            }
        }
    );

});

/* ==========================
   CREAR TARJETA
========================== */

function createCard(restaurant){

    const card =
        document.createElement("div");

    card.className =
        "restaurant-card";

    card.innerHTML = `

        <div class="restaurant-name">
            ${restaurant.name}
        </div>

        <div class="restaurant-category">
            ${restaurant.category}
        </div>

        <div class="restaurant-rating">

            <span class="stars">
                ${generateStars(
                    restaurant.rating
                )}
            </span>

            <span class="rating-number">
                ${restaurant.rating.toFixed(1)}
            </span>

        </div>

    `;

    card.addEventListener(
        "click",
        () => {

            window.location.href =
                `restaurant.html?id=${restaurant.id}&from=recommendation`;
        }
    );

    return card;
}

/* ==========================
   CARGAR RECOMENDACIONES
========================== */

async function loadRecommendations(){

    const food =
        sessionStorage.getItem(
            "food"
        );

    const budget =
        sessionStorage.getItem(
            "budget"
        );

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

    try{

        const response =
            await fetch(
                "http://localhost:4567/api/recomendaciones",
                {
                    method:"POST",

                    headers:{
                        "Content-Type":
                        "application/json"
                    },

                    body:JSON.stringify({

                        userId,
                        food,
                        budget,
                        environment,
                        minRating,
                        distance
                    })
                }
            );

        if(!response.ok){

            throw new Error(
                "No se pudieron cargar las recomendaciones"
            );
        }

        const restaurants =
            await response.json();
        
            restaurants.sort(
            (a, b) => b.rating - a.rating
        );


        const slider =
            document.getElementById(
                "recommendedSlider"
            );

        slider.innerHTML = "";

        if(restaurants.length === 0){

            slider.innerHTML = `

                <div class="empty-state">

                    <h3>
                        No encontramos resultados
                    </h3>

                    <p>
                        Intenta modificar tus preferencias.
                    </p>

                </div>

            `;

            return;
        }

        restaurants.forEach(r => {

            slider.appendChild(
                createCard(r)
            );
        });

    }
    catch(error){

        console.error(
            "Error cargando recomendaciones:",
            error
        );
    }
}

/* ==========================
   INICIO
========================== */

loadRecommendations();