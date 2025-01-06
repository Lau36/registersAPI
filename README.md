# Proyecto Registers API

Registers API es una aplicación Spring Boot diseñada para la gestión de pacientes con epilepsia. Este README describe los pasos necesarios para configurar y ejecutar la aplicación, así como para probar sus endpoints utilizando Swagger.

---

## Pasos para Configurar y Ejecutar la Aplicación

### 1. Generar el Build de la Aplicación

Ejecuta el siguiente comando en la raíz del proyecto para generar el build:

```bash
./gradlew build
```

### 2. Iniciar los Contenedores

Levanta los contenedores de Docker con el siguiente comando:

```bash
docker-compose up
```

### 3. Configurar el Usuario de MongoDB

En otra terminal, accede al contenedor de MongoDB y crea un usuario con los permisos necesarios:

```bash
docker exec -it mongo-db-registers mongosh --username root --password secret --authenticationDatabase admin
```

Una vez dentro del shell de MongoDB, ejecuta los siguientes comandos:

```javascript
use epilepsyRegister

db.createUser({
  user: "root",
  pwd: "secret",
  roles: [{ role: "readWrite", db: "epilepsyRegister" }]
})
```

Esto configurará el usuario `root` con contraseña `secret` y permisos de lectura y escritura para la base de datos `epilepsyRegister`.

---

## Probar la Aplicación

### Acceder a Swagger

Abre un navegador web y accede a la siguiente URL para ver la documentación y probar los endpoints disponibles:

```
http://localhost:8080/swagger-ui/index.html
```

En Swagger puedes:

1. Explorar la documentación de los endpoints.
2. Probar los servicios haciendo clic en **Try it out** para cada endpoint.

### URL Base del Backend

La URL base de la aplicación es la siguiente:

```
http://localhost:8080
```

---

## Notas Adicionales

- Asegúrate de que los contenedores estén ejecutándose antes de probar los endpoints.
- Si necesitas reiniciar los contenedores, puedes hacerlo con:

```bash
docker-compose down && docker-compose up --build
```

