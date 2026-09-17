package escanerred;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EscanerRed {

    public Equipo escanear(String ip, int espera, int reintentos) {

        try {

            long inicio = System.currentTimeMillis();

            boolean activo = false;

            for (int i = 0; i <= reintentos; i++) {

                Process proceso = Runtime.getRuntime().exec(
                    "ping -n 1 -w " + espera + " " + ip
                );

                BufferedReader lector = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream())
                );

                String linea;

                while ((linea = lector.readLine()) != null) {

                    if (linea.contains("TTL=")) {
                        activo = true;
                    }
                }

                proceso.waitFor();

                if (activo) {
                    break;
                }
            }

            long tiempo = System.currentTimeMillis() - inicio;

            String nombre = obtenerNombre(ip);

            return new Equipo(
                ip,
                nombre,
                activo,
                tiempo
            );

        } catch (Exception e) {

            return new Equipo(
                ip,
                "Desconocido",
                false,
                0
            );
        }
    }


    private String obtenerNombre(String ip) {

        try {

            Process proceso = Runtime.getRuntime().exec(
                "nslookup " + ip
            );

            BufferedReader lector = new BufferedReader(
                new InputStreamReader(proceso.getInputStream())
            );

            String linea;

            while ((linea = lector.readLine()) != null) {

                if (linea.startsWith("Name:")) {
                    return linea.substring(5).trim();
                }

                if (linea.startsWith("Nombre:")) {
                    return linea.substring(7).trim();
                }
            }

        } catch (Exception e) {
            // No se pudo obtener el nombre
        }

        return "Desconocido";
    }
}