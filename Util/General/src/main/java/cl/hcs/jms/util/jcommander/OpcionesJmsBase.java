package cl.hcs.jms.util.jcommander;

import com.beust.jcommander.Parameter;

public class OpcionesJmsBase {

    @Parameter(names = {"-u", "--usuario"}, required = true, description = "Usuario del broker")
    private String usuario;
    @Parameter(names = {"-p", "--password"}, required = true, description = "Contraseña del broker")
    private String contrasena;
    @Parameter(names = {"-i", "--clientId"}, description = "Client ID de la conexión")
    private String clientId;

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getClientId() {
        return clientId;
    }
}
