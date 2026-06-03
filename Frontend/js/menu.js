// Obtiene el slider principal del top de restaurantes
const mainSlider =
    document.getElementById("mainSlider");

// Obtiene el slider de restaurantes por ciudad
const citySlider =
    document.getElementById("citySlider");

// Obtiene el título de la sección de ciudad
const cityTitle =
    document.getElementById("cityTitle");

// Obtiene el botón para buscar recomendaciones
const recommendBtn =
    document.querySelector(".recommend-btn");

// Obtiene el botón de cerrar sesión
const logoutBtn =
    document.getElementById("logoutBtn");

// Genera las estrellas según la calificación del restaurante
function generateStars(rating){

    const fullStars =
        Math.round(rating);

    return "★".repeat(fullStars) +
           "☆".repeat(5-fullStars);
}

/* ==========================
   CREAR TARJETA
========================== */

// Crea una tarjeta visual para cada restaurante
function createCard(r){

    const card =
        document.createElement("div");

    card.className =
        "restaurant-card";

    // Inserta la información del restaurante dentro de la tarjeta
    card.innerHTML = `

        <div class="restaurant-name">
            ${r.name}
        </div>

        <div class="restaurant-category">
            ${r.category}
        </div>

        <div class="restaurant-rating">

            ${generateStars(r.rating)}

            <span class="rating-number">
                ${r.rating.toFixed(1)}
            </span>

        </div>

    `;

    // Redirige a la página del restaurante al hacer clic
    card.addEventListener("click", () => {

        window.location.href =
            `restaurant.html?id=${r.id}&from=menu`;

    });

    return card;
}

/* ==========================
   CARGAR RESTAURANTES
========================== */

// Solicita los restaurantes al servidor y los muestra en los sliders
async function loadRestaurants(){

    // Obtiene la ciudad guardada del usuario
    const city =
        sessionStorage.getItem("userCity") || "";

    try{

        // Solicita la lista de restaurantes al backend
        const response =
            await fetch(
                "http://localhost:4567/api/restaurantes"
            );

        // Verifica si ocurrió un error en la respuesta
        if(!response.ok){

            throw new Error(
                "Error obteniendo restaurantes"
            );
        }

        // Convierte la respuesta a JSON
        const restaurants =
            await response.json();

        // Ordena los restaurantes por calificación y toma los primeros 10
        const top10 =
            [...restaurants]
            .sort((a,b)=>b.rating-a.rating)
            .slice(0,10);

        // Filtra restaurantes que coincidan con la ciudad del usuario
        const cityRestaurants =
            city
            ? restaurants.filter(r =>

                r.city &&
                r.city.toLowerCase() ===
                city.toLowerCase()

            )
            .slice(0,15)
            : [];

        // Cambia el título si el usuario tiene ciudad guardada
        if(city){

            cityTitle.textContent =
                `Restaurantes en ${city}`;
        }

        // Limpia los sliders antes de cargar nuevas tarjetas
        mainSlider.innerHTML = "";
        citySlider.innerHTML = "";

        // Agrega las tarjetas del top 10 al slider principal
        top10.forEach(r => {

            mainSlider.appendChild(
                createCard(r)
            );

        });

        // Agrega las tarjetas de ciudad al segundo slider
        cityRestaurants.forEach(r => {

            citySlider.appendChild(
                createCard(r)
            );

        });

    }
    catch(error){

        // Muestra el error en consola si falla la carga
        console.error(error);
    }
}

/* ==========================
   TOP 10
========================== */

// Mueve el slider principal hacia la derecha
document
.getElementById("nextBtn")
.addEventListener("click", () => {

    mainSlider.appendChild(
        mainSlider.firstElementChild
    );

});

// Mueve el slider principal hacia la izquierda
document
.getElementById("prevBtn")
.addEventListener("click", () => {

    mainSlider.insertBefore(
        mainSlider.lastElementChild,
        mainSlider.firstElementChild
    );

});

/* ==========================
   CERCA DE TI
========================== */

// Mueve el slider de ciudad hacia la derecha
document
.getElementById("nextCityBtn")
.addEventListener("click", () => {

    citySlider.appendChild(
        citySlider.firstElementChild
    );

});

// Mueve el slider de ciudad hacia la izquierda
document
.getElementById("prevCityBtn")
.addEventListener("click", () => {

    citySlider.insertBefore(
        citySlider.lastElementChild,
        citySlider.firstElementChild
    );

});

/* ==========================
   RECOMENDACIONES
========================== */

// Redirige a la página de preguntas para generar recomendaciones
recommendBtn.addEventListener("click", () => {

    window.location.href =
        "question.html";

});

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
   INICIO
========================== */

// Carga los restaurantes cuando se abre la página
loadRestaurants();