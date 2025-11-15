package cl.hcs.jms;

import com.beust.jcommander.Parameter;

public class OpcionesSuscripcionNoDurableCompartida {

    @Parameter(names = {"-h", "--help"}, description = "Mostrar ayuda")
    private boolean mostrarAyuda;
    @Parameter(names = {"-n", "--nombre"}, required = true, description = "Nombre de la suscripción")
    private String nombreSuscripcion;
    @Parameter(names = {"-c", "--clientId"}, description = "Client ID de la conexión")
    private String clientId;
    @Parameter(names = {"-u", "--usuario"}, required = true, description = "Usuario del broker")
    private String usuario;
    @Parameter(names = {"-p", "--password"}, required = true, description = "Contraseña del broker")
    private String contrasena;
    @Parameter(names = {"-a", "--asincrono"}, description = "Si los mensajes se reciben a través de un listener asíncrono")
    private boolean asincrono;
    @Parameter(names = {"-e", "--esperar"}, description = "Si en el modo síncrono se debe esperar hasta que llegue al menos 1 mensaje")
    private boolean esperaHabilitada;

    public OpcionesSuscripcionNoDurableCompartida() {}

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isAsincrono() {
        return asincrono;
    }
    public void setAsincrono(boolean asincrono) {
        this.asincrono = asincrono;
    }

    public boolean isEsperaHabilitada() {
        return esperaHabilitada;
    }
    public void setEsperaHabilitada(boolean esperaHabilitada) {
        this.esperaHabilitada = esperaHabilitada;
    }

    public boolean isMostrarAyuda() {return mostrarAyuda;}
    public boolean mostrarAyuda() {return mostrarAyuda;}
    public void setMostrarAyuda(boolean mostrarAyuda) {
        this.mostrarAyuda = mostrarAyuda;
    }

    public String getNombreSuscripcion() { return nombreSuscripcion; }
    public void setNombreSuscripcion(String nombreSuscripcion) { this.nombreSuscripcion = nombreSuscripcion; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
}
