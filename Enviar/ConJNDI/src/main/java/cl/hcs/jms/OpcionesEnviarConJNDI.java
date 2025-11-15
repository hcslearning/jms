package cl.hcs.jms;

import com.beust.jcommander.Parameter;

public class OpcionesEnviarConJNDI {

    @Parameter(names = {"-u", "--usuario"}, required = true, description = "Usuario del broker")
    private String usuario;
    @Parameter(names = {"-p", "--password"}, required = true, description = "Contraseña del broker")
    private String contrasena;
    @Parameter(names = {"-a", "--asincrono"}, description = "Si el mensaje se debe enviar de manera asíncrona")
    private boolean asincrono;
    @Parameter(names = {"-d", "--tipo-destino"}, description = "Valores posibles COLA o TOPICO", required = true)
    private String tipoDestino;
    @Parameter(names = {"-m", "--mensaje"}, required = false, description = "Mensaje a enviar")
    private String mensaje;

    public OpcionesEnviarConJNDI() {
    }

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

    public String getTipoDestino() {
        return tipoDestino;
    }

    public void setTipoDestino(String tipoDestino) {
        this.tipoDestino = tipoDestino;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
