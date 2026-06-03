// Obtiene todas las flechas utilizadas para mover los sliders
const arrows = document.querySelectorAll(".arrow");

/* ==========================
   ESTRELLAS
========================== */

// Genera una representación visual de estrellas según la calificación
function generateStars(rating){

    const rounded = Math.round(rating);

    return (
        "★".repeat(rounded) +
        "☆".repeat(5 - rounded)
    );
}

/* ==========================
   SLIDERS
========================== */

// Agrega funcionalidad a cada flecha de navegación
arrows.forEach(arrow => {

    arrow.addEventListener("click", () => {

        // Obtiene el id del slider asociado
        const sliderId = arrow.dataset.slider;

        // Obtiene la dirección del movimiento
        const direction = Number(
            arrow.dataset.direction
        );

        // Busca el slider correspondiente
        const slider = document.getElementById(
            sliderId
        );

        // Verifica que exista y tenga elementos
        if(
            !slider ||
            !slider.firstElementChild
        ){
            return;
        }

        // Mueve la primera tarjeta al final
        if(direction > 0){

            slider.appendChild(
                slider.firstElementChild
            );

        }else{

            // Mueve la última tarjeta al inicio
            slider.insertBefore(
                slider.lastElementChild,
                slider.firstElementChild
            );
        }
    });
});

/* ==========================
   CREAR TARJETA
========================== */

// Crea una tarjeta visual para cada restaurante
function createCard(restaurant){

    const card =
        document.createElement("div");

    card.className =
        "restaurant-card";

    // Inserta la información del restaurante
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

    // Redirige a la página de detalles al hacer clic
    card.addEventListener("click", () => {

        window.location.href =
            `restaurant.html?id=${restaurant.id}&from=recommendation`;

    });

    return card;
}

/* ==========================
   CARGAR RECOMENDACIONES
========================== */

// Obtiene las recomendaciones desde el backend
async function loadRecommendations(){

    // Recupera las preferencias guardadas
    const food =
        sessionStorage.getItem("food");

    const budget =
        sessionStorage.getItem("budget");

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

    // Obtiene el slider donde se mostrarán los resultados
    const slider =
        document.getElementById(
            "recommendedSlider"
        );

    try{

        // Solicita recomendaciones al servidor
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
                        environment: "",
                        minRating,
                        distance
                    })
                }
            );

        // Verifica si hubo un error en la respuesta
        if(!response.ok){

            throw new Error(
                "No se pudieron cargar las recomendaciones"
            );
        }

        // Convierte la respuesta a JSON
        const restaurants =
            await response.json();

        // Ordena las recomendaciones por calificación
        restaurants.sort(
            (a, b) => b.rating - a.rating
        );

        // Limpia el slider antes de mostrar resultados
        slider.innerHTML = "";

        // Muestra mensaje si no existen recomendaciones
        if(restaurants.length === 0){

            slider.innerHTML = `

                <div class="empty-state">

                    <h3>
                        No encontramos resultados
                    </h3>

                    <p>
                        Intenta modificar tus preferencias
                    </p>

                </div>

            `;

            return;
        }

        // Agrega cada restaurante al slider
        restaurants.forEach(r => {

            slider.appendChild(
                createCard(r)
            );

        });

    }catch(error){

        // Muestra el error en consola
        console.error(
            "Error cargando recomendaciones:",
            error
        );

        // Muestra mensaje de error en pantalla
        slider.innerHTML = `

            <div class="empty-state">

                <h3>
                    Error cargando recomendaciones
                </h3>

                <p>
                    Revisa que el servidor esté encendido
                </p>

            </div>

        `;
    }
}

/* ==========================
   INICIO
========================== */

// Ejecuta la carga de recomendaciones al abrir la página
loadRecommendations();