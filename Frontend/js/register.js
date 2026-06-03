// Obtiene el formulario de registro
const form = document.querySelector("form");

// Obtiene la lista desplegable de ciudades
const citySelect = document.getElementById("citySelect");

/* ==========================
   CARGAR CIUDADES
========================== */

// Obtiene las ciudades disponibles desde los restaurantes registrados
async function loadCities() {

    try {

        // Solicita los restaurantes al servidor
        const response = await fetch(
            "http://localhost:4567/api/restaurantes"
        );

        // Convierte la respuesta a JSON
        const restaurants =
            await response.json();

        // Obtiene únicamente ciudades sin repetir
        const cities =
            [...new Set(
                restaurants.map(r => r.city)
            )];

        // Ordena las ciudades alfabéticamente
        cities.sort();

        // Agrega cada ciudad al select
        cities.forEach(city => {

            const option =
                document.createElement("option");

            option.value = city;
            option.textContent = city;

            citySelect.appendChild(option);
        });

    } catch(error) {

        // Muestra errores en consola
        console.error(error);
    }
}

// Ejecuta la carga de ciudades al abrir la página
loadCities();

/* ==========================
   REGISTRO DE USUARIO
========================== */

// Maneja el envío del formulario
form.addEventListener("submit", async function (event) {

    // Evita que la página se recargue
    event.preventDefault();

    // Obtiene todos los campos input del formulario
    const inputs = form.querySelectorAll("input");

    // Obtiene los datos ingresados por el usuario
    const name            = inputs[0].value;
    const email           = inputs[1].value;
    const password        = inputs[2].value;
    const confirmPassword = inputs[3].value;

    // Obtiene la ciudad seleccionada
    const city = citySelect.value;

    // Verifica que ambas contraseñas coincidan
    if (password !== confirmPassword) {

        alert("Las contraseñas no coinciden.");
        return;
    }

    try {

        // Envía los datos al backend para registrar al usuario
        const response = await fetch(
            "http://localhost:4567/api/register",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    name,
                    email,
                    password,
                    city
                })
            }
        );

        // Convierte la respuesta a JSON
        const data = await response.json();

        // Verifica si el servidor devolvió un error
        if (!response.ok) {

            alert(
                data.error ||
                "Error al registrarse."
            );

            return;
        }

        // Guarda automáticamente la sesión del usuario recién registrado
        sessionStorage.setItem(
            "userId",
            data.userId
        );

        sessionStorage.setItem(
            "userName",
            data.name
        );

        sessionStorage.setItem(
            "userCity",
            city
        );

        // Redirige al menú principal
        window.location.href =
            "menu.html";

    } catch (error) {

        // Muestra un mensaje si no se puede conectar con el servidor
        alert(
            "No se pudo conectar con el servidor."
        );

        // Muestra detalles del error en consola
        console.error(error);
    }
});