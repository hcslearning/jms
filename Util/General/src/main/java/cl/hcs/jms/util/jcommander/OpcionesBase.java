package cl.hcs.jms.util.jcommander;

import com.beust.jcommander.Parameter;

public class OpcionesBase {

    @Parameter(names = {"-h", "--help"}, description = "Mostrar ayuda")
    private boolean mostrarAyuda;


}
