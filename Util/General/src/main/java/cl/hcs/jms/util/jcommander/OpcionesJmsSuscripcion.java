package cl.hcs.jms.util.jcommander;

import com.beust.jcommander.Parameter;

public class OpcionesJmsSuscripcion {

    @Parameter(names = {"-n", "--nombre"}, required = true, description = "Nombre de la suscripción")
    private String nombreSuscripcion;
    @Parameter(names = {"-d", "--durable"}, description = "Si la suscripcion debe ser durable")
    private boolean durable;
    @Parameter(names = {"-c", "--compartida"}, description = "Si la suscripcion debe ser compartida")
    private boolean compartida;

    public String getNombreSuscripcion() {
        return nombreSuscripcion;
    }

    public boolean isDurable() {
        return durable;
    }

    public boolean isCompartida() {
        return compartida;
    }
}
