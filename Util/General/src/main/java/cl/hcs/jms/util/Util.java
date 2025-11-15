package cl.hcs.jms.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    public static String generarStringRandom(String prefijo) {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String fechaHoraFormateada = ahora.format(formato);
        return prefijo + " " + fechaHoraFormateada;
    }

    public static String generarStringRandom() {
        return generarStringRandom("Hola Mundo!!!");
    }

}
