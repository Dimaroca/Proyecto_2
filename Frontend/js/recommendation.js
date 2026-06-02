// ── Navegación de sliders (igual que antes) ───────────────────────
const arrows = document.querySelectorAll(".arrow");

arrows.forEach(arrow => {
    arrow.addEventListener("click", () => {
        const sliderId  = arrow.dataset.slider;
        const direction = Number(arrow.dataset.direction);
        const slider    = document.getElementById(sliderId);

        if (direction > 0) {
            slider.appendChild(slider.firstElementChild);
        } else {
            slider.insertBefore(slider.lastElementChild, slider.firstElementChild);
        }
    });
});

// ── Render de una tarjeta de restaurante ─────────────────────────
function createCard(restaurant) {
    const card = document.createElement("div");
    card.className = "restaurant-card";
    card.innerHTML = `
        <img src="${restaurant.image || '../assets/comida.jpg'}" alt="${restaurant.name}">
        <h3>${restaurant.name}</h3>
        <p>${restaurant.category || ""}</p>
        <span>⭐ ${restaurant.rating.toFixed(1)}</span>
    `;
    return card;
}

// ── Cargar recomendaciones personalizadas ─────────────────────────
async function loadRecommendations() {
    const food        = sessionStorage.getItem("food");
    const budget      = sessionStorage.getItem("budget");
    const environment = sessionStorage.getItem("environment");
    const userId      = sessionStorage.getItem("userId") || "";

    if (!food || !budget || !environment) {
        console.warn("Sin preferencias guardadas, mostrando cards estáticas.");
        return;
    }

    try {
        const response = await fetch("http://localhost:4567/api/recomendaciones", {
            method:  "POST",
            headers: { "Content-Type": "application/json" },
            body:    JSON.stringify({ userId, food, budget, environment })
        });

        if (!response.ok) throw new Error("Error en recomendaciones.");

        const recommended = await response.json();

        // Reemplazar las tarjetas estáticas del slider "Recomendados para ti"
        const recommendedSlider = document.getElementById("recommendedSlider");
        if (recommendedSlider && recommended.length > 0) {
            recommendedSlider.innerHTML = "";
            recommended.forEach(r => recommendedSlider.appendChild(createCard(r)));
        }

        // "Mejor calificados" = los mismos ordenados por rating
        const topSlider = document.getElementById("topSlider");
        if (topSlider && recommended.length > 0) {
            const byRating = [...recommended].sort((a, b) => b.rating - a.rating);
            topSlider.innerHTML = "";
            byRating.forEach(r => topSlider.appendChild(createCard(r)));
        }

    } catch (error) {
        console.error("No se pudo conectar con el servidor:", error);
    }
}

loadRecommendations();
