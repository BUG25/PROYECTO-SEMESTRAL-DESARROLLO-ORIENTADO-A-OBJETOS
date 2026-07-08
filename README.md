##AUTOCRÍTICA
Al revisar el proceso de construcción del proyecto, hemos identificado varios puntos que si se rehiciera el proyecto, abordaremos de manera diferente, como: 
■ La validación conceptual del modelo de negocio se resolvió de forma tardía, lo cual provocó modificaciones reactivas y eliminación de atributos (como precioVenta) en componentes que ya se encontraban programados.
■ Los test del núcleo del negocio fueron desarrollados de manera asíncrona por el equipo en lugar de construirse a la par con el avance de la lógica de dominio. 
■ Se evidenció una falta de control en el flujo de integración de Git al inicio del proyecto, confundiendo los paneles de historial y cambios pendientes, lo que retrasó la sincronización de las primeras líneas base. 
■ Mejorar la implementación de commits de manera correcta (feat,fix,refactor)


##PROPUESTA DE MEJORAS PARA FUTUROS PROYECTOS 
A partir de la autocrítica anterior, se plantean las siguientes recomendaciones para proyectos futuros de características similares. 
■ Estabilizar, documentar y validar las reglas de negocio y casos de uso de manera previa a la escritura de cualquier línea de código. 
■ Adoptar un glosario técnico común y una hoja de ruta compartida de nomenclaturas para clases, métodos y enums, evitando desalineaciones entre desarrolladores. 
■ Aumentar la frecuencia de las operaciones de sincronización (pull y merge) en el repositorio, limitando el aislamiento de las ramas de trabajo para evitar la posibilidad de coexistencia de modelos contradictorios.
