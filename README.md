ESCÁNER DE RED

Proyecto realizado para la materia Redes de E.T. 36 – 5TO 1RA.

1. DESCRIPCIÓN

Escáner de Red es una aplicación de escritorio desarrollada en Java que permite analizar un rango de direcciones IP y determinar qué equipos se encuentran activos en una red.

Para cada dirección IP, el programa realiza un ping para comprobar la conectividad y utiliza nslookup para intentar obtener el nombre del equipo.

Los resultados se muestran en una tabla y pueden ser ordenados, filtrados y guardados en un archivo CSV.

2. OBJETIVOS

El proyecto tiene como objetivos:

1. Aplicar conceptos básicos de comunicación en redes.

2. Utilizar el protocolo ICMP mediante el comando ping.

3. Utilizar DNS mediante el comando nslookup.

4. Desarrollar una aplicación con interfaz gráfica.

5. Aplicar programación orientada a objetos.

6. Implementar manejo de errores y excepciones.

7. Organizar y documentar correctamente el proyecto.

8. TECNOLOGÍAS UTILIZADAS

9. Java.

10. Eclipse.

11. Java Swing para la interfaz gráfica.

12. ICMP mediante el comando ping.

13. DNS mediante el comando nslookup.

14. Archivos CSV para guardar los resultados.

15. REQUISITOS

Para ejecutar el programa se necesita:

1. Un equipo con Windows.

2. Java instalado.

3. Eclipse u otro entorno compatible con Java.

4. Conexión a una red para realizar el escaneo.

5. CÓMO EJECUTAR EL PROGRAMA

6. Descargar o clonar el repositorio.

7. Abrir el proyecto desde Eclipse.

8. Verificar que las clases se encuentren dentro del paquete escanerred.

9. Ejecutar la clase Main.java.

10. Se abrirá la ventana principal del Escáner de Red.

11. CÓMO UTILIZAR EL PROGRAMA

6.1. Ingresar el rango de IP

Se deben ingresar una dirección IP inicial y una dirección IP final.

Ejemplo:

IP de inicio: 192.168.1.1
IP de fin: 192.168.1.10

El programa verifica las direcciones IP mientras se escriben y también realiza una validación antes de comenzar el escaneo.

6.2. Configurar el escaneo

Se puede configurar el tiempo de espera y la cantidad de reintentos.

El tiempo de espera determina cuánto tiempo se espera una respuesta del ping.

La cantidad de reintentos determina cuántas veces se vuelve a probar una dirección IP que no respondió.

6.3. Iniciar el escaneo

Al presionar el botón Iniciar escaneo, el programa analiza cada dirección IP del rango.

Para cada dirección se realizan las siguientes acciones:

1. Se ejecuta el comando ping.
2. Se determina si el equipo respondió.
3. Se mide el tiempo empleado.
4. Se utiliza nslookup para intentar obtener el nombre del equipo.
5. El resultado se agrega a la tabla.

La barra de progreso muestra el avance del escaneo.

6.4. Consultar los resultados

La tabla muestra:

1. Dirección IP.
2. Nombre del equipo.
3. Estado del equipo.
4. Tiempo de respuesta.

También se muestra la cantidad total de equipos activos encontrados.

6.5. Ordenar y filtrar

La información de la tabla puede ordenarse haciendo clic sobre las columnas.

También existe un filtro que permite mostrar:

1. Todos los equipos.
2. Equipos activos.
3. Equipos inactivos.

6.6. Detener el escaneo

El botón Detener permite cancelar un escaneo que se encuentre en curso.

6.7. Limpiar los resultados

El botón Limpiar elimina los resultados de la tabla y reinicia el contador de equipos activos y la barra de progreso.

6.8. Guardar los resultados

El botón Guardar permite exportar los resultados a un archivo CSV.

El archivo contiene la dirección IP, el nombre del equipo, el estado y el tiempo empleado.

7. ESTRUCTURA DEL PROYECTO

El proyecto está dividido en cuatro clases principales.

7.1. Main.java

Se encarga de iniciar la aplicación y crear la ventana principal.

7.2. VentanaPrincipal.java

Contiene la interfaz gráfica, los botones, la tabla, la barra de progreso, los filtros y las acciones realizadas por el usuario.

7.3. EscanerRed.java

Contiene la lógica principal del escaneo. Ejecuta ping, controla los reintentos y utiliza nslookup para intentar obtener el nombre del equipo.

7.4. Equipo.java

Representa la información de cada equipo encontrado durante el escaneo.

8. PROTOCOLOS UTILIZADOS

8.1. ICMP

Se utiliza mediante el comando ping del sistema para comprobar si una dirección IP responde.

8.2. DNS

Se utiliza mediante nslookup para intentar obtener el nombre asociado a una dirección IP.

9. MANEJO DE ERRORES

El programa controla diferentes situaciones, entre ellas:

1. Direcciones IP incorrectas.
2. IP inicial mayor que IP final.
3. Direcciones IP de inicio y fin pertenecientes a rangos diferentes.
4. Tiempo de espera inválido.
5. Cantidad de reintentos inválida.
6. Errores durante el ping.
7. Errores al obtener el nombre mediante DNS.
8. Intentos de guardar resultados cuando no existen datos.

Cuando ocurre un problema, el programa muestra un mensaje claro al usuario.

10. PROGRAMACIÓN ORIENTADA A OBJETOS

El proyecto separa la interfaz gráfica de la lógica del escaneo.

La clase VentanaPrincipal se encarga de la interacción con el usuario.

La clase EscanerRed realiza las tareas relacionadas con el escaneo.

La clase Equipo representa los resultados obtenidos.

La clase Main se encarga de iniciar la aplicación.

Esta organización permite mantener el código ordenado y facilita futuras modificaciones.
