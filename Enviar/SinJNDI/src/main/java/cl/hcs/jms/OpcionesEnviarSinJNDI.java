package cl.hcs.jms;

import cl.hcs.jms.util.jcommander.OpcionesBase;
import cl.hcs.jms.util.jcommander.OpcionesJmsBase;
import cl.hcs.jms.util.jcommander.OpcionesJmsEnvio;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

public class OpcionesEnviarSinJNDI {

    @Parameter(names = {"-h", "--host"}, required = true, description = "Dirección IP o dominio del broker, ejemplo, localhost")
    private String host;
    @Parameter(names = {"-p", "--port"}, required = true, description = "Puerto para conectar con el broker, ejemplo, 61616")
    private int port;
    @Parameter(names = {"-n", "--nombre-destino"}, required = true, description = "Nombre del destino del mensaje (cola o tópico) hacia donde enviar el mensaje")
    private String nombreDestino;

    @ParametersDelegate
    private final OpcionesBase base = new OpcionesBase();
    @ParametersDelegate
    private final OpcionesJmsBase jms = new OpcionesJmsBase();
    @ParametersDelegate
    private final OpcionesJmsEnvio envio = new OpcionesJmsEnvio();

    public String getNombreDestino() {
        return nombreDestino;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public OpcionesBase getBase() {
        return base;
    }

    public OpcionesJmsBase getJms() {
        return jms;
    }

    public OpcionesJmsEnvio getEnvio() {
        return envio;
    }
}

