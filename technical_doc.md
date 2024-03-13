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

## Planeación

Se realizó una planeación sencilla, teniendo en consideración el tiempo dado y el hecho de ser la única persona involucrada en el desarrollo.

[Planeación](https://docs.google.com/spreadsheets/d/1p9J36T2YZGpEVeJnoeIJfixLt54aDaZXmoyPLaze300/edit?usp=sharing)
