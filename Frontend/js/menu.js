const slider      = document.getElementById("mainSlider");
const prevBtn     = document.getElementById("prevBtn");
const nextBtn     = document.getElementById("nextBtn");
const recommendBtn = document.querySelector(".recommend-btn");

// ── Cargar restaurantes desde el backend ──────────────────────────
async function loadRestaurants() {

    const city =
        sessionStorage.getItem("userCity") || "";

    try {

        const url = city
            ? `http://localhost:4567/api/restaurantes?city=${encodeURIComponent(city)}`
            : "http://localhost:4567/api/restaurantes";

        const response = await fetch(url);

        if (!response.ok)
            throw new Error(
                "Error al obtener restaurantes."
            );

        const restaurants =
            await response.json();

        const top10 = restaurants
            .sort((a, b) => b.rating - a.rating)
            .slice(0, 10);

        slider.innerHTML = "";

        top10.forEach(r => {

            const card =
                document.createElement("div");

            card.className =
                "restaurant-card";

            card.innerHTML = `
                <h3>${r.name}</h3>
                <p>${r.category}</p>
                <span>⭐ ${r.rating.toFixed(1)}</span>
            `;

            slider.appendChild(card);
        });

    } catch (error) {

        console.error(
            "No se pudo conectar con el servidor:",
            error
        );
    }
}

// Navegación del slider 
nextBtn.addEventListener("click", function () {
    slider.appendChild(slider.firstElementChild);
});

prevBtn.addEventListener("click", function () {
    slider.insertBefore(slider.lastElementChild, slider.firstElementChild);
});

recommendBtn.addEventListener("click", function () {
    window.location.href = "question.html";
});

// Cargar al iniciar
loadRestaurants();
