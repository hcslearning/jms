package cl.hcs.jms.util.jcommander;

import com.beust.jcommander.Parameter;

public class OpcionesJmsEnvio {
    @Parameter(names = {"-a", "--asincrono"}, description = "Si el mensaje se debe enviar de manera asíncrona")
    private boolean asincrono;
    @Parameter(names = {"-d", "--tipo-destino"}, required = true, description = "Valores posibles COLA o TOPICO")
    private String tipoDestino;
    @Parameter(names = {"-m", "--mensaje"}, description = "Mensaje a enviar")
    private String mensaje;

    public boolean isAsincrono() {
        return asincrono;
    }

    public String getTipoDestino() {
        return tipoDestino;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
