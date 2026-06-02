const form = document.querySelector("form");

form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const food        = form.querySelector("input[name='food']:checked")?.value;
    const budget      = form.querySelector("input[name='budget']:checked")?.value;
    const environment = form.querySelector("input[name='environment']:checked")?.value;

    if (!food || !budget || !environment) {
        alert("Por favor responde todas las preguntas.");
        return;
    }

    const userId = sessionStorage.getItem("userId");

    // Guardar preferencias en el backend
    if (userId) {
        try {
            await fetch("http://localhost:4567/api/preferencias", {
                method:  "POST",
                headers: { "Content-Type": "application/json" },
                body:    JSON.stringify({ userId, food, budget, environment })
            });
        } catch (error) {
            console.warn("No se pudieron guardar preferencias:", error);
        }
    }

    // Guardar en sessionStorage para usarlas en recommendation.js
    sessionStorage.setItem("food",        food);
    sessionStorage.setItem("budget",      budget);
    sessionStorage.setItem("environment", environment);

    window.location.href = "recommendation.html";
});
