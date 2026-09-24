package escanerred;

public class Equipo {

    String ip;
    String nombre;
    boolean activo;
    long tiempo;

    public Equipo(String ip, String nombre, boolean activo, long tiempo) {
        this.ip = ip;
        this.nombre = nombre;
        this.activo = activo;
        this.tiempo = tiempo;
    }
}