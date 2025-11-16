package cl.hcs.jms;

import cl.hcs.jms.util.jcommander.OpcionesBase;
import cl.hcs.jms.util.jcommander.OpcionesJmsBase;
import cl.hcs.jms.util.jcommander.OpcionesJmsRecepcion;

public class OpcionesCola {

    private final OpcionesBase base = new OpcionesBase();
    private final OpcionesJmsBase jms = new OpcionesJmsBase();
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
