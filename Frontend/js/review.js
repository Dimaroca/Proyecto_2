// Obtiene los parámetros enviados en la URL
const params =
    new URLSearchParams(
        window.location.search
    );

// Obtiene el id del restaurante desde la URL
const restaurantId =
    params.get("id");

/* ==========================
   CARGAR RESTAURANTE
========================== */

// Carga el nombre del restaurante que será calificado
async function loadRestaurant() {

    // Si no hay id de restaurante no realiza la búsqueda
    if (!restaurantId) return;

    try {

        // Solicita la información del restaurante al backend
        const response =
            await fetch(
                `http://localhost:4567/api/restaurante/${restaurantId}`
            );

        // Verifica si el restaurante existe
        if(!response.ok){

            throw new Error(
                "Restaurante no encontrado"
            );
        }

        // Convierte la respuesta a JSON
        const restaurant =
            await response.json();

        // Muestra el nombre del restaurante en la página
        document.getElementById(
            "restaurantName"
        ).textContent =
            restaurant.name;

    } catch(error) {

        // Muestra el error en consola
        console.error(error);

        // Muestra un mensaje de error en pantalla
        document.getElementById(
            "restaurantName"
        ).textContent =
            "Error cargando restaurante";
    }
}

/* ==========================
   ESTRELLAS
========================== */

// Obtiene todas las estrellas de calificación
const stars =
    document.querySelectorAll(".star");

// Obtiene el input oculto donde se guarda la calificación
const ratingInput =
    document.getElementById("rating");

// Permite seleccionar una calificación con estrellas
stars.forEach(star => {

    star.addEventListener(
        "click",
        () => {

            // Obtiene el valor de la estrella seleccionada
            const value =
                Number(
                    star.dataset.value
                );

            // Guarda la calificación seleccionada
            ratingInput.value =
                value;

            // Activa o desactiva estrellas según la calificación
            stars.forEach(s => {

                if(
                    Number(
                        s.dataset.value
                    ) <= value
                ){

                    s.classList.add(
                        "active"
                    );

                } else {

                    s.classList.remove(
                        "active"
                    );
                }
            });
        }
    );
});

/* ==========================
   FORMULARIO DE RESEÑA
========================== */

// Maneja el envío de la reseña
document
.getElementById("reviewForm")
.addEventListener(
    "submit",
    async function(e){

        // Evita que la página se recargue
        e.preventDefault();

        // Obtiene la calificación seleccionada
        const rating =
            parseInt(
                document.getElementById(
                    "rating"
                ).value
            );

        // Obtiene el comentario escrito
        const comment =
            document.getElementById(
                "comment"
            ).value;

        // Obtiene el usuario actual desde la sesión
        const userId =
            sessionStorage.getItem(
                "userId"
            );

        try {

            // Envía la reseña al backend
            await fetch(
                "http://localhost:4567/api/review",
                {
                    method:"POST",

                    headers:{
                        "Content-Type":
                        "application/json"
                    },

                    body:JSON.stringify({

                        userId,
                        rating,
                        comment
                    })
                }
            );

            // Muestra confirmación al usuario
            alert(
                "Reseña enviada correctamente"
            );

            // Regresa al menú principal
            window.location.href =
                "menu.html";

        }
        catch(error){

            // Muestra el error en consola
            console.error(error);

            // Muestra mensaje de error al usuario
            alert(
                "No se pudo enviar la reseña"
            );
        }
    }
);

/* ==========================
   INICIO
========================== */

// Carga el restaurante al abrir la página
loadRestaurant();