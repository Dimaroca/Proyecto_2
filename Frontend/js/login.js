const form = document.querySelector("form");

form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const email    = form.querySelector("input[type='email']").value;
    const password = form.querySelector("input[type='password']").value;

    try {
        const response = await fetch("http://localhost:4567/api/login", {
            method:  "POST",
            headers: { "Content-Type": "application/json" },
            body:    JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (!response.ok) {
            alert(data.error || "Error al iniciar sesión.");
            return;
        }

        // Guardar userId y nombre para usarlos en otras páginas
        sessionStorage.setItem("userId", data.userId);
        sessionStorage.setItem("userName", data.name);
        sessionStorage.setItem("userCity", data.city);

        window.location.href = "menu.html";

    } catch (error) {
        alert("No se pudo conectar con el servidor.");
        console.error(error);
    }
});
