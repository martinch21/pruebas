# Sistema de Gestión de Tareas

Este proyecto es un sistema de gestión de tareas desarrollado en Java, utilizando MySQL para la gestión de datos. Permite a los usuarios crear, editar, y eliminar tareas, así como gestionar cuentas de usuario y roles dentro de una interfaz gráfica.

## Comenzando

Estas instrucciones te proporcionarán una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas. Sigue estos pasos para configurar el entorno de desarrollo.

### Prerrequisitos

Antes de comenzar, necesitarás instalar el siguiente software:

- Java JDK 11 o superior
- MySQL Server 8.0 o superior
- Eclipse IDE o cualquier otro IDE de Java
- Maven para la gestión de dependencias y compilación

### Instalación

Sigue estos pasos para configurar tu entorno de desarrollo:

1. **Clonar el repositorio**

    ```bash
    git clone https://github.com/martinch21/proyecto-gestion-tareas.git
    cd proyecto-gestion-tareas
    ```

2. **Configurar la base de datos**

    Primero, asegúrate de que MySQL está corriendo en tu máquina. Luego, crea la base de datos y las tablas usando el script SQL proporcionado en `database_setup.sql`:

    ```bash
    mysql -u root -p < database_setup.sql
    ```

3. **Configurar el proyecto en Eclipse**

    Abre Eclipse, selecciona 'Import' > 'Existing Projects into Workspace', navega hasta el directorio del proyecto clonado, y asegúrate de que está seleccionado para importar.

4. **Compilar y ejecutar la aplicación**

    Utiliza Maven para compilar y ejecutar la aplicación:

    ```bash
    mvn clean install
    mvn exec:java -Dexec.mainClass="com.martinch21.Main"
    ```

## Uso

Después de iniciar la aplicación, podrás:

- **Iniciar sesión** usando las credenciales de usuario predeterminadas (o las que hayas creado).
- **Agregar, modificar y eliminar tareas** a través de la interfaz de usuario.
- **Administrar usuarios** si tienes privilegios de administrador.

## Contribuir

Para contribuir al proyecto, por favor revisa el archivo `CONTRIBUTING.md` para detalles sobre el proceso para enviar pull requests y el código de conducta.

## Autores

- **Martin Leon** - *Desarrollador principal* - [martinch21](https://github.com/martinch21)

## Licencia

Este proyecto está bajo la Licencia MIT - ve el archivo `LICENSE.md` para más detalles.

## Agradecimientos

- Agradecimientos a todos los colaboradores y testers que ayudaron a mejorar la calidad del proyecto.
- Inspiración tomada de otros sistemas de gestión de tareas y proyectos de código abierto.
