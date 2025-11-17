package cl.hcs.jms;

import cl.hcs.jms.util.jcommander.OpcionesBase;
import cl.hcs.jms.util.jcommander.OpcionesJmsBase;
import cl.hcs.jms.util.jcommander.OpcionesJmsRecepcion;
import com.beust.jcommander.ParametersDelegate;

public class OpcionesCola {

    @ParametersDelegate
    private final OpcionesBase base = new OpcionesBase();
    @ParametersDelegate
    private final OpcionesJmsBase jms = new OpcionesJmsBase();
    @ParametersDelegate
    private final OpcionesJmsRecepcion recepcion = new OpcionesJmsRecepcion();

    public OpcionesBase getBase() {
        return base;
    }

    public OpcionesJmsBase getJms() {
        return jms;
    }

    public OpcionesJmsRecepcion getRecepcion() {
        return recepcion;
    }
}
