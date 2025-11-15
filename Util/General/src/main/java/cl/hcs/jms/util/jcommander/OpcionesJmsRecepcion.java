package cl.hcs.jms.util.jcommander;

import com.beust.jcommander.Parameter;

public class OpcionesJmsRecepcion {

    private String contrasena;
    @Parameter(names = {"-a", "--asincrono"}, description = "Si los mensajes se reciben a través de un listener asíncrono")
    private boolean asincrono;
    @Parameter(names = {"-e", "--esperar"}, description = "Si en el modo síncrono se debe esperar hasta que llegue al menos 1 mensaje")
    private boolean esperaHabilitada;

    public OpcionesJmsRecepcion() {}

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public boolean isAsincrono() { return asincrono; }
    public void setAsincrono(boolean asincrono) { this.asincrono = asincrono; }

    public boolean isEsperaHabilitada() { return esperaHabilitada; }
    public void setEsperaHabilitada(boolean esperaHabilitada) { this.esperaHabilitada = esperaHabilitada; }
}
