package cl.hcs.jms.util.jcommander;

import com.beust.jcommander.Parameter;

public class OpcionesJmsSuscripcion {

    @Parameter(names = {"-n", "--nombre"}, required = true, description = "Nombre de la suscripción")
    private String nombreSuscripcion;

    public OpcionesJmsSuscripcion() { }

    public String getNombreSuscripcion() { return nombreSuscripcion; }
    public void setNombreSuscripcion(String nombreSuscripcion) { this.nombreSuscripcion = nombreSuscripcion; }
}
