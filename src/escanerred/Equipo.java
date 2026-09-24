package escanerred;

public class Equipo {

    // Dirección IP del equipo
    String ip;

    // Nombre obtenido mediante DNS
    String nombre;

    // Indica si el equipo respondió al ping
    boolean activo;

    // Tiempo total de respuesta en milisegundos
    long tiempo;

    public Equipo(
        String ip,
        String nombre,
        boolean activo,
        long tiempo
    ) {

        this.ip = ip;
        this.nombre = nombre;
        this.activo = activo;
        this.tiempo = tiempo;
    }
}