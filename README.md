Autores: Jair Morales, Diego Rodríguez, Christopher Serrano

# Sistema de Recomendación de Restaurantes con Neo4j

Este proyecto demuestra la conexión entre Java y Neo4j utilizando una estructura de grafo ponderado basada en datos de restaurantes. El programa carga información desde un dataset CSV y crea relaciones dentro de un grafo que simulan la base de un sistema de recomendación.

## Características

- Conexión entre Java y Neo4j
- Carga de dataset CSV
- Generación automática de grafos
- Relaciones ponderadas usando ratings de restaurantes
- Carga limitada a 20 datos para pruebas y visualización

## Tecnologías Utilizadas

- Java 17
- Maven
- Neo4j
- Neo4j Java Driver


## Requisitos

Antes de ejecutar el programa es necesario tener instalado:

- Java 17 o superior
- Maven
- Neo4j Desktop

## Importante

Neo4j DEBE estar encendido antes de ejecutar el programa.

Pasos:

1. Abrir Neo4j Desktop
2. Iniciar la instancia local de Neo4j
3. Crear una base de datos llamada `restaurants`
4. Verificar que la base de datos esté activa antes de ejecutar Maven

Si Neo4j no está ejecutándose, el programa no podrá conectarse correctamente.

## Configuración de la Base de Datos

Usar las mismas credenciales configuradas en `Main.java`.

Ejemplo:

```java
String uri = "bolt://localhost:7687";
String user = "neo4j";
String password = "tu_password";
```

## Archivo CSV

El dataset debe llamarse:

```text
restaurant.csv
```

y debe colocarse dentro de la carpeta `import` de Neo4j.

## Cómo Ejecutar el Programa

Compilar y ejecutar usando Maven:

```bash
mvn clean compile
mvn exec:java
```

## Visualización del Grafo

Después de ejecutar el programa, abrir Neo4j Query y ejecutar:

```cypher
MATCH (n)-[r]->(m)
RETURN n,r,m;
```

Esto mostrará el grafo generado con nodos y relaciones ponderadas.

## Notas

- El programa únicamente carga 20 entradas del dataset para facilitar pruebas y visualización.
- Las relaciones utilizan los ratings de los restaurantes como peso.
- La estructura del grafo sirve como base para un sistema de recomendación.