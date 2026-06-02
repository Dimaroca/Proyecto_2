const form = document.querySelector("form");

const citySelect = document.getElementById("citySelect");

async function loadCities() {

    try {

        const response = await fetch(
            "http://localhost:4567/api/restaurantes"
        );

        const restaurants =
            await response.json();

        const cities =
            [...new Set(
                restaurants.map(r => r.city)
            )];

        cities.sort();

        cities.forEach(city => {

            const option =
                document.createElement("option");

            option.value = city;
            option.textContent = city;

            citySelect.appendChild(option);
        });

    } catch(error) {

        console.error(error);
    }
}

loadCities();

form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const inputs = form.querySelectorAll("input");
    const name            = inputs[0].value;
    const email           = inputs[1].value;
    const password        = inputs[2].value;
    const confirmPassword = inputs[3].value;
    const city            = citySelect.value;

    if (password !== confirmPassword) {
        alert("Las contraseñas no coinciden.");
        return;
    }

    try {
        const response = await fetch("http://localhost:4567/api/register", {
            method:  "POST",
            headers: { "Content-Type": "application/json" },
            body:    JSON.stringify({ name, email, password, city })
        });

        const data = await response.json();

        if (!response.ok) {
            alert(data.error || "Error al registrarse.");
            return;
        }

        // Guardar sesión automáticamente tras registro
        sessionStorage.setItem("userId",   data.userId);
        sessionStorage.setItem("userName", data.name);
        sessionStorage.setItem("userCity", city);

        window.location.href = "menu.html";

    } catch (error) {
        alert("No se pudo conectar con el servidor.");
        console.error(error);
    }
});
