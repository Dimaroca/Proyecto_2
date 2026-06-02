const mainSlider =
    document.getElementById("mainSlider");

const citySlider =
    document.getElementById("citySlider");

const cityTitle =
    document.getElementById("cityTitle");

const recommendBtn =
    document.querySelector(".recommend-btn");

const logoutBtn =
    document.getElementById("logoutBtn");

    function generateStars(rating){

    const fullStars =
        Math.round(rating);

    return "★".repeat(fullStars) +
           "☆".repeat(5-fullStars);
}
/* ==========================
   CREAR TARJETA
========================== */

function createCard(r){

    const card =
        document.createElement("div");

    card.className =
        "restaurant-card";

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

    card.addEventListener("click", () => {

        window.location.href =
            `restaurant.html?id=${r.id}&from=menu`;

    });

    return card;
}

/* ==========================
   CARGAR RESTAURANTES
========================== */

async function loadRestaurants(){

    const city =
        sessionStorage.getItem("userCity") || "";

    try{

        const response =
            await fetch(
                "http://localhost:4567/api/restaurantes"
            );

        if(!response.ok){

            throw new Error(
                "Error obteniendo restaurantes"
            );
        }

        const restaurants =
            await response.json();

        const top10 =
            [...restaurants]
            .sort((a,b)=>b.rating-a.rating)
            .slice(0,10);

        const cityRestaurants =
            city
            ? restaurants.filter(r =>

                r.city &&
                r.city.toLowerCase() ===
                city.toLowerCase()

            )
            .slice(0,15)
            : [];

        if(city){

            cityTitle.textContent =
                `Restaurantes en ${city}`;
        }

        mainSlider.innerHTML = "";
        citySlider.innerHTML = "";

        top10.forEach(r => {

            mainSlider.appendChild(
                createCard(r)
            );

        });

        cityRestaurants.forEach(r => {

            citySlider.appendChild(
                createCard(r)
            );

        });

    }
    catch(error){

        console.error(error);
    }
}

/* ==========================
   TOP 10
========================== */

document
.getElementById("nextBtn")
.addEventListener("click", () => {

    mainSlider.appendChild(
        mainSlider.firstElementChild
    );

});

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

document
.getElementById("nextCityBtn")
.addEventListener("click", () => {

    citySlider.appendChild(
        citySlider.firstElementChild
    );

});

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

recommendBtn.addEventListener("click", () => {

    window.location.href =
        "question.html";

});

/* ==========================
   CERRAR SESIÓN
========================== */

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

loadRestaurants();