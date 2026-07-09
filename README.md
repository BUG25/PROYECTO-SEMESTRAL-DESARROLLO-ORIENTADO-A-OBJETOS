# DESARROLLO
Analisis del problema: 
El enunciado propone el desarrollo de un sistema capaz de realizar la gestión y el cuidado de mascotas en un entorno virtual. El jugador inicia con un presupuesto acotado que debe administrar estratégicamente para adquirir hábitats específicos (perreras, areneros, peceras, jaulas). Se entiende que, una vez que el usuario posee el hábitat compatible, queda habilitado para adoptar distintas especies de mascotas de forma gratuita (perros, gatos, peces, pájaros o hámsteres). A partir de la adopción, el sistema debe controlar atributos para cada animal (comida, salud y felicidad), el jugador tiene la obligación de mantener estables mediante acciones de cuidado. Durante el transcurso de la simulación, cada acción de mantenimiento (alimentar, limpiar el hábitat o jugar) debe retribuir económicamente al jugador de forma automática, permitiéndole reinvertir en la tienda, además de aumentar los parámetros de los atributos.

Estrategias de solución:
Para resolver diferentes problemas se uso:
- Patrón Factory Method: El ecosistema del simulador requiere la creación de especies distintas de mascotas con potencial de expansión a futuro. El proceso se modeló mediante la jerarquía de la clase abstracta MascotaFactory, implementada por creadores concretos como PerroFactory y GatoFactory. La clase central FabricaMascotas utiliza un EnumMap para despachar la instanciación según el TipoMascota solicitado. De este modo, la incorporación de un nuevo animal no exige modificar la lógica, protegiendo el código ya probado. 













# AUTOCRÍTICA
Al revisar el proceso de construcción del proyecto, hemos identificado varios puntos que si se rehiciera el proyecto, abordaremos de manera diferente, como:
- La validación conceptual del modelo de negocio se resolvió de forma tardía, lo cual provocó modificaciones reactivas y eliminación de atributos (como precioVenta) en componentes que ya se encontraban programados.
- Los test del núcleo del negocio fueron desarrollados de manera asíncrona por el equipo en lugar de construirse a la par con el avance de la lógica de dominio.
- Se evidenció una falta de control en el flujo de integración de Git al inicio del proyecto, confundiendo los paneles de historial y cambios pendientes, lo que retrasó la sincronización de las primeras líneas base.
- Mejorar la implementación de commits de manera correcta (feat,fix,refactor)


## PROPUESTA DE MEJORAS PARA FUTUROS PROYECTOS 
A partir de la autocrítica anterior, se plantean las siguientes recomendaciones para proyectos futuros de características similares.
- Estabilizar, documentar y validar las reglas de negocio y casos de uso de manera previa a la escritura de cualquier línea de código.
- Adoptar un glosario técnico común y una hoja de ruta compartida de nomenclaturas para clases, métodos y enums, evitando desalineaciones entre desarrolladores.
- Aumentar la frecuencia de las operaciones de sincronización (pull y merge) en el repositorio, limitando el aislamiento de las ramas de trabajo para evitar la posibilidad de coexistencia de modelos contradictorios.
