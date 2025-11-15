package cl.hcs.jms;

import cl.hcs.jms.util.jcommander.OpcionesBase;
import cl.hcs.jms.util.jcommander.OpcionesJmsBase;
import cl.hcs.jms.util.jcommander.OpcionesJmsRecepcion;
import cl.hcs.jms.util.jcommander.OpcionesJmsSuscripcion;
import com.beust.jcommander.ParametersDelegate;

public class OpcionesSuscripcionNoDurableCompartida {

    @ParametersDelegate
    private OpcionesBase base = new OpcionesBase();

    @ParametersDelegate
    private OpcionesJmsBase jmsBase = new OpcionesJmsBase();

    @ParametersDelegate
    private OpcionesJmsRecepcion recepcion = new OpcionesJmsRecepcion();

    @ParametersDelegate
    private OpcionesJmsSuscripcion suscripcion = new OpcionesJmsSuscripcion();

    public OpcionesBase getBase() {
        return base;
    }

    public OpcionesJmsBase getJmsBase() {
        return jmsBase;
    }

    public OpcionesJmsRecepcion getRecepcion() {
        return recepcion;
    }

    public OpcionesJmsSuscripcion getSuscripcion() {
        return suscripcion;
    }
}
