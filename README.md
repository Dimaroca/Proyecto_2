ThropFinder - Sistema de Recomendación de Restaurantes

Autores: Diego Rodríguez - 25215, Jair Morales - 25351, Christopher Serrano - 251375

Descripción General

ThropFinder es una aplicación web diseñada para ayudar a los usuarios a descubrir restaurantes que se adapten a sus gustos y preferencias personales. El sistema utiliza información almacenada en una base de datos Neo4j y aplica algoritmos de recomendación para generar sugerencias personalizadas según los criterios seleccionados por cada usuario.

La plataforma permite registrar usuarios, gestionar preferencias, buscar restaurantes y recibir recomendaciones basadas en factores como tipo de comida, presupuesto, calificación mínima y ubicación.

Funcionalidades Principales
Registro e Inicio de Sesión

El sistema permite:

Crear nuevas cuentas de usuario.
Iniciar sesión con credenciales registradas.
Almacenar información básica del usuario.
Mantener una sesión activa durante la navegación.
Gestión de Preferencias

Los usuarios pueden personalizar sus recomendaciones mediante:

Tipo de comida preferida.
Nivel de presupuesto.
Ambiente deseado.
Calificación mínima aceptable.
Distancia o ubicación preferida.

Estas preferencias se almacenan para mejorar la calidad de las recomendaciones generadas.

Búsqueda de Restaurantes

El sistema permite consultar restaurantes utilizando distintos criterios:

Ciudad.
Tipo de comida.
Presupuesto.
Calificación mínima.
Zona o localidad.

Los resultados son obtenidos directamente desde la base de datos Neo4j.

Recomendaciones Personalizadas

El sistema analiza las preferencias seleccionadas por el usuario y calcula una puntuación de afinidad para cada restaurante disponible.

Entre los factores considerados se encuentran:

Coincidencia de tipo de comida.
Compatibilidad con el presupuesto.
Calificación del restaurante.
Preferencias de ubicación.
Preferencias de valoración.

Los restaurantes son ordenados según su nivel de afinidad para presentar primero las opciones más relevantes.

Visualización de Restaurantes

Cada restaurante muestra información relevante como:

Nombre.
Categoría gastronómica.
Calificación.
Ciudad.
Zona.
Información adicional almacenada en la base de datos.
Arquitectura del Sistema

El proyecto está dividido en tres componentes principales:

Frontend

Desarrollado utilizando:

HTML5
CSS3
JavaScript

Responsable de:

Interfaz gráfica.
Formularios.
Visualización de resultados.
Comunicación con la API.
Backend

Desarrollado en Java utilizando Spark Java.

Responsable de:

Procesamiento de solicitudes.
Gestión de usuarios.
Generación de recomendaciones.
Acceso a la base de datos.
Base de Datos

Implementada con Neo4j.

Responsable de:

Almacenamiento de usuarios.
Almacenamiento de restaurantes.
Persistencia de preferencias.
Consultas utilizadas por el sistema.
Algoritmos Utilizados
Cálculo de Afinidad

El sistema utiliza una función de ponderación que asigna puntajes a los restaurantes según la compatibilidad con las preferencias del usuario.

Los factores evaluados incluyen:

Tipo de comida.
Presupuesto.
Calificación.
Distancia.
Preferencias de valoración.
Algoritmo de Dijkstra

El sistema incorpora una implementación del algoritmo de Dijkstra para el procesamiento y evaluación de los resultados dentro del sistema de recomendación.

Requerimientos de Software

Para ejecutar correctamente el sistema se requiere:

Sistema Operativo
Windows 10 o superior
Linux
macOS
Java
JDK 17 o superior

Verificar instalación:

java -version
Maven
Apache Maven 3.8 o superior

Verificar instalación:

mvn -version
Docker
Docker Desktop o Docker Engine

Verificar instalación:

docker --version
Docker Compose

Verificar instalación:

docker compose version
Navegador Web

Cualquiera de los siguientes:

Google Chrome
Microsoft Edge
Mozilla Firefox
Instalación del Sistema
1. Clonar o descargar el proyecto
git clone <repositorio>

o descargar el archivo comprimido del proyecto.

2. Ingresar a la carpeta del proyecto
cd Proyecto_2
3. Iniciar los contenedores
docker compose up --build

Este comando iniciará:

Neo4j
Backend Java
Servicios necesarios para la aplicación
4. Verificar Neo4j

Abrir:

http://localhost:7474

Credenciales por defecto:

Usuario: neo4j
Contraseña: 12345678
5. Verificar API

Abrir:

http://localhost:4567

La API quedará disponible para recibir solicitudes desde el frontend.

6. Ejecutar el Frontend

Abrir los archivos HTML mediante un servidor local o desde el entorno configurado para el proyecto.

La navegación principal inicia desde:

login.html
Flujo de Uso
Registrar un nuevo usuario.
Iniciar sesión.
Completar el formulario de preferencias.
Seleccionar criterios de búsqueda.
Generar recomendaciones.
Visualizar restaurantes recomendados.
Consultar información detallada de cada restaurante.
Estructura General del Proyecto
Proyecto_2
│
├── Backend
│   ├── algorithms
│   ├── app
│   ├── database
│   ├── models
│   ├── services
│   └── pom.xml
│
├── Frontend
│   ├── html
│   ├── css
│   ├── js
│   └── assets
│
├── Data
│   └── restaurants.csv
│
├── docker-compose.yml
│
└── README.txt
Tecnologías Utilizadas
Java
Spark Java
Neo4j
Maven
Docker
HTML5
CSS3
JavaScript