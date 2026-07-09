# INTRODUCCIÓN
La gestión de un entorno interactivo virtual, como lo es el proyecto de simulación, requiere de una estructura lógica rigurosa que coordine el estado de las entidades (mascotas, cliente, las restricciones del inventario y las dinámicas económicas subyacentes). Administrar variables dinámicas en tiempo real (como salud, alimentación y felicidad de múltiples mascotas) a través de interacciones manuales o estructuras de código, incrementa exponencialmente el riesgo de errores de consistencia, acoplamiento rígido y fallos en tiempo de ejecución. El presente informe tiene como objetivo documentar el desarrollo de un Simulador de Tienda de Mascotas Virtual.

# DESARROLLO
Analisis del problema: 
El enunciado propone el desarrollo de un sistema capaz de realizar la gestión y el cuidado de mascotas en un entorno virtual. El jugador inicia con un presupuesto acotado que debe administrar estratégicamente para adquirir hábitats específicos (perreras, areneros, peceras, jaulas). Se entiende que, una vez que el usuario posee el hábitat compatible, queda habilitado para adoptar distintas especies de mascotas de forma gratuita (perros, gatos, peces, pájaros o hámsteres). A partir de la adopción, el sistema debe controlar atributos para cada animal (comida, salud y felicidad), el jugador tiene la obligación de mantener estables mediante acciones de cuidado. Durante el transcurso de la simulación, cada acción de mantenimiento (alimentar, limpiar el hábitat o jugar) debe retribuir económicamente al jugador de forma automática, permitiéndole reinvertir en la tienda, además de aumentar los parámetros de los atributos.

Estrategias de solución:
Para resolver diferentes problemas se uso:
- Patrón Factory Method: El ecosistema del simulador requiere la creación de especies distintas de mascotas con potencial de expansión a futuro. El proceso se modeló mediante la jerarquía de la clase abstracta MascotaFactory, implementada por creadores concretos como PerroFactory y GatoFactory. La clase central FabricaMascotas utiliza un EnumMap para despachar la instanciación según el TipoMascota solicitado. De este modo, la incorporación de un nuevo animal no exige modificar la lógica, protegiendo el código ya probado.

Decisiones de diseño:
Además de las estrategias mencionadas, se tomaron distintas decisiones durante el desarrollo. Los cambios en el flujo de los estados vitales se resolvieron prohibiendo el uso de métodos de mutación directos (setters) en la clase Mascota. Se determinó que los niveles solo cambien mediante métodos con significado de negocio (alimentar(), jugar()), acotados estrictamente entre 0 y 100 a través de funciones matemáticas de control. Nos percatamos de que sería necesaria una separación estricta de responsabilidades entre el Usuario y la Tienda. Se escogió que la clase Usuario almacene de forma exclusiva el balance financiero y el inventario de entidades del jugador, asegurando que la clase Tienda funcione únicamente como un validador de reglas de negocio sin retener datos de estado. 
Para identificar los hábitats de forma realista, se subdividió el enumerador TipoHabitat creando JAULA_PAJARO y JAULA_HAMSTER de forma independiente, reconociendo que cada especie exige requerimientos físicos particulares. TAmbien se determinó que la adopción sea permanente y gratuita. trasladando el peso de la mecánica económica a la compra de infraestructura

Problemas encontrados y soluciones: 
Se detectaron errores de escritura en los métodos de las fábricas (como escribir crearMoscota() en lugar de crearMascota()), lo que impedía la correcta sobrescritura de las operaciones. El problema se solucionó mediante refactorizaciones globales asistidas por el motor de búsqueda del IDE. Durante la integración por Git, se descubrió un archivo de casos de uso antiguo que describía un sistema transaccional con carrito de compras y facturación, incompatible con el diseño actual. Esto provocó que parte del equipo desarrollara código inconsistente (clases CarritoCompra y TiendaMascotas). La inconsistencia fue localizada y comunicada para su resolución en las sesiones de integración del equipo.

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
