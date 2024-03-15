# File-Sharing-API

El desarrollo de la File-Sharing API se enmarca dentro de la necesidad de ofrecer un sistema seguro y eficiente para compartir archivos. Este documento presenta una descripción técnica de la aplicación, incluyendo requisitos funcionales y no funcionales, decisiones de diseño y desarrollo, y medidas de seguridad adoptadas.

## Requisitos Funcionales

- Autenticación y registro de usuarios.
- Carga y descarga de archivos con nombres específicos.
- Compartición de archivos entre usuarios.
- Recuperación de metadatos de los archivos del usuario.
- Modificación y eliminación de archivos propios y acceso a archivos compartidos.

## Requisitos No Funcionales

- **Seguridad**: Incorporada en cada etapa del desarrollo.
- **Escalabilidad**: Capacidad para modificar las tecnologías de uso actuales y extensión de casos de uso.
- **Modularidad**: Diseño que permite la evolución y mantenimiento eficiente del sistema.

## Supuestos

- La arquitectura actual de la aplicación está pensada como un MVP.
- Soporte para carga de archivos JPEG y PNG, con un tamaño máximo de 10MB.
- Arquitectura monolítica y limpia considerada adecuada para el alcance del proyecto.
- La aplicación no se conectará con otros servicios nuevos o existentes.
- La aplicación no formará parte de un proceso crítico.
- Los archivos no contienen información PII, PHA o sensible.
- No hay requisitos de cumplimiento normativo o de seguridad que deban cumplirse.

## Tecnologías y Estrategias

- **Encriptación de contraseñas**: Para proteger la información de usuario.
- **Autenticación**: Mediante tokens JWT, asegurando las sesiones de usuario y el acceso a endpoints protegidos.
- **Almacenamiento de Archivos**: Inicialmente en sistema de archivos local, pero se propondría migrar a servicios como Amazon S3 para escalabilidad.
- **Base de Datos**: Inicialmente se usa el servicio H2, el cual hace un guardado de información en memoria, pero se propondría el uso de PostgreSQL para gestionar datos de usuario y metadatos de archivos.
- **Seguridad**: Implementación de HTTPS, validación de entradas para evitar inyecciones SQL, y almacenamiento seguro de contraseñas.

## Componentes y Arquitectura

La API se compone de varios componentes claves, cada uno abordando un aspecto específico del sistema:

- **Componente de autenticación**: Gestiona el proceso de autenticación y autorización.
- **Componente de Usuario**: Administra perfiles y permisos de usuario.
- **Componente de Gestión de Archivos**: Encargado de las operaciones de archivo.
- **Componente de Compartición de Archivos**: Facilita compartir archivos entre usuarios.
- **Componente de Auditoría**: Registra operaciones para auditorías de seguridad.
- **Componente de Repositorio**: Almacena información estructurada del sistema.
- **Componente de Almacenamiento**: Realiza operaciones sobre archivos del sistema.

## Principios de Diseño

- **Separación de Preocupaciones (SoC)**: Divide el software en secciones distintas, cada una abordando una preocupación separada.
- **Manejo Centralizado de Excepciones**: Mejora la mantenibilidad y legibilidad del código.
- **Programación Orientada a Interfaces**: Flexibiliza y desacopla el código de las implementaciones específicas.

## Base de datos

[Diagram ERM](https://drive.google.com/file/d/1jBoMsBqXWQvR113wu9Bw965-Kg_wvJjo/view?usp=sharing)
- **Acceso consola H2**: http://localhost:8080/h2-console

## Swagger

**Swagger**: http://localhost:8080/swagger-ui/index.html

## Control de Accesos

El control de accesos es un componente crítico en la File-Sharing API, asegurando que los usuarios solo puedan realizar operaciones permitidas según sus roles y privilegios. Este sistema de control se implementa mediante dos enfoques complementarios: Control de Acceso Basado en Roles (RBAC) y Control de Acceso Discrecional (DAC).

### Control de Acceso Basado en Roles (RBAC)

- **Usuarios**: Tienen permisos para realizar operaciones CRU (Crear, Leer, Actualizar) sobre sus propios archivos y aquellos compartidos con ellos. No pueden acceder a la gestión de usuarios ni modificar permisos de otros usuarios.
- **Administradores**: Poseen todos los permisos de los usuarios, además de capacidades para gestionar cuentas de usuarios, incluyendo creación, modificación y eliminación

# Control de Acceso Discrecional (DAC)

El DAC complementa al RBAC proporcionando un control más granular sobre el acceso a los archivos individuales. Permite a los usuarios (propietarios de archivos) gestionar quién puede acceder a sus archivos y en qué medida. Los aspectos clave del DAC incluyen:

- **Propietario del Archivo**: El usuario que sube el archivo se convierte automáticamente en su propietario, con plenos derechos sobre él.
- **Compartición de Archivos**: El propietario puede compartir archivos con otros usuarios, especificando el nivel de acceso (por ejemplo, sólo lectura, edición o eliminación).
- **Revocación de Acceso**: El propietario puede revocar el acceso a un archivo en cualquier momento, eliminando los permisos otorgados previamente a otros usuarios.

## Seguridad y Modelado de Amenazas

El modelado de amenazas se realizó siguiendo el framework STRIDE, identificando y mitigando riesgos como suplantación de identidad, alteración de datos, negación de servicio, entre otros, a través de autenticación fuerte, encriptación, controles de acceso, y auditoría de acciones.

### Suplantación de Identidad (Spoofing)

- **Amenaza**: Los atacantes podrían hacerse pasar por usuarios legítimos para obtener acceso no autorizado a la aplicación.
- **Mitigación**: Implementar mecanismos de autenticación fuertes y asegurar que los JWTs (JSON Web Tokens) sean manejados y almacenados de manera segura. Usar HTTPS para cifrar los datos en tránsito.

### Alteración de los datos (Tampering)

- **Amenaza**: Modificaciones no autorizadas a archivos o metadatos podrían comprometer la integridad de los datos almacenados por la aplicación.
- **Mitigación**: Usar sumas de verificación y firmas digitales para asegurar la integridad de los archivos durante la carga, descarga y almacenamiento. Implementar controles de acceso estrictos para limitar quién puede modificar los datos.
- **Amenaza**: Usuarios o atacantes podrían subir malware al sistema.
- **Mitigación**: Asegurar que las cargas de archivos sean escaneadas en busca de malware y que los límites de tamaño sean impuestos para prevenir la sobrecarga del sistema.

### Repudio (Repudiation)

- **Amenaza**: Usuarios o atacantes podrían negar haber realizado una acción dentro de la aplicación, como subir un archivo sensible o malware.
- **Mitigación**: Implementar capacidades de registro y auditoría exhaustivas para rastrear y almacenar registros de todas las acciones de los usuarios y las respuestas del sistema. Asegurar que los registros sean a prueba de manipulaciones y revisados regularmente.

### Divulgación de Información (Data Confidentiality)

- **Amenaza**: Podría ocurrir un acceso no autorizado a archivos o metadatos, llevando a una filtración de datos.
- **Mitigación**: Cifrar los datos tanto en tránsito (usando HTTPS u otros protocolos seguros) como en reposo. Aplicar el principio de privilegio mínimo para limitar el acceso a los datos.

### Denegación de Servicio (DoS)

- **Amenaza**: Los atacantes podrían abrumar el sistema con una inundación de solicitudes, haciéndolo no disponible para los usuarios legítimos.
- **Mitigación**: Implementar ratelimit. Usar servicios de protección contra DoS basados en la nube y tener una infraestructura escalable para manejar aumentos en el tráfico.

### Elevación de Privilegios

- **Amenaza**:: Los atacantes podrían explotar vulnerabilidades para obtener niveles de acceso más altos de lo originalmente previsto, permitiéndoles acceder o modificar datos restringidos.
- **Mitigación**: Actualizar y parchear regularmente todos los componentes del sistema para corregir vulnerabilidades conocidas. Control de Acceso Discrecional (DAC) y asegurar que se aplique correctamente. Realizar auditorías de seguridad y pruebas de penetración regularmente para identificar y remediar vulnerabilidades.

# Limitaciones y áreas de mejora

## Correcta configuración de componentes iniciales
La implementación de la lógica adecuada para crear un objeto único de almacenamiento asegura que en este punto el servidor permite la creación de una carpeta de almacenamiento y a futuro las conexiones a los componentes de almacenamiento. De esta forma se evita inicializar la aplicación si no se tienen las configuraciones críticas correctamente.

## DTO’s
Se tienen los mappers y los DTO 's implementados, pero no se ha implementado la lógica de mapeo entre la capa de servicio y repositorio para separar las clases usadas para contener la información que confluye en la aplicación.

## Estrategia de logs
Agregar logs en los diferentes procesos para tener una trazabilidad y registro de lo que está ocurriendo al interior del sistema, facilitando el debugging y mejorando la seguridad aportando información para mitigar el no repudio.

## Test
La aplicación no cuenta con test unitarios, de componentes e integración. Por lo que es necesaria su implementación para asegurar que la aplicación tenga el comportamiento adecuado.

## Planeación

Se realizó una planeación sencilla, teniendo en consideración el tiempo dado y el hecho de ser la única persona involucrada en el desarrollo.

[Planeación](https://docs.google.com/spreadsheets/d/1p9J36T2YZGpEVeJnoeIJfixLt54aDaZXmoyPLaze300/edit?usp=sharing)
