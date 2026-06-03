// Obtiene el formulario de inicio de sesión
const form = document.querySelector("form");

// Escucha el evento de envío del formulario
form.addEventListener("submit", async function (event) {

    // Evita que la página se recargue al enviar el formulario
    event.preventDefault();

    // Obtiene el correo ingresado por el usuario
    const email = form.querySelector("input[type='email']").value;

    // Obtiene la contraseña ingresada por el usuario
    const password = form.querySelector("input[type='password']").value;

    try {

        // Envía las credenciales al servidor para validar el inicio de sesión
        const response = await fetch("http://localhost:4567/api/login", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email,
                password
            })
        });

        // Convierte la respuesta del servidor a formato JSON
        const data = await response.json();

        // Verifica si la respuesta contiene un error
        if (!response.ok) {

            alert(data.error || "Error al iniciar sesión.");
            return;
        }

        // Guarda el identificador del usuario en la sesión
        sessionStorage.setItem("userId", data.userId);

        // Guarda el nombre del usuario en la sesión
        sessionStorage.setItem("userName", data.name);

        // Guarda la ciudad del usuario en la sesión
        sessionStorage.setItem("userCity", data.city);

        // Redirige al menú principal después de iniciar sesión
        window.location.href = "menu.html";

    } catch (error) {

        // Muestra un mensaje si no se puede conectar con el servidor
        alert("No se pudo conectar con el servidor.");

        // Muestra el error en la consola para depuración
        console.error(error);
    }
});