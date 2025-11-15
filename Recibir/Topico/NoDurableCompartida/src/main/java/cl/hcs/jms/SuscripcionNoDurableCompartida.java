package cl.hcs.jms;

import cl.hcs.jms.util.Recepcion;
import com.beust.jcommander.JCommander;
import jakarta.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SuscripcionNoDurableCompartida {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuscripcionNoDurableCompartida.class);

    public static void main(String[] args) {
        // JCommander - procesa args
        OpcionesSuscripcionNoDurableCompartida opciones = new OpcionesSuscripcionNoDurableCompartida();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(opciones)
                .build();
        jCommander.usage();
        jCommander.parse(args);

        if(opciones.mostrarAyuda()) return;

        new SuscripcionNoDurableCompartida().run(
                opciones.getUsuario(),
                opciones.getContrasena(),
                opciones.getClientId(),
                opciones.getNombreSuscripcion(),
                opciones.isAsincrono(),
                opciones.isEsperaHabilitada()
        );
    }

    public void run(
            String brokerUser,
            String brokerPassword,
            String clientId,
            String nombreSuscripcionCompartida,
            boolean esAsincrono,
            boolean esperar
    ) {
        InitialContext jndi = null;
        try {
            jndi = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connFactoryBroker1");

            Destination topico = (Destination) jndi.lookup("topicos/topicoEjemplo");

            try (Connection connection = connectionFactory.createConnection(brokerUser, brokerPassword)) {
                if( clientId != null) connection.setClientID(clientId);
                LOGGER.info("ClientID: {}", connection.getClientID());

                Session sesion = connection.createSession(Session.CLIENT_ACKNOWLEDGE);

                LOGGER.info("Creando suscripción NO Durable y Compartida llamada: {}", nombreSuscripcionCompartida);
                MessageConsumer messageConsumer = sesion.createSharedConsumer((Topic) topico, nombreSuscripcionCompartida);
                connection.start();

                // Se usa clase utilitaria para recibir mensaje, porque siempre es igual
                // usando alguno de los metodos receive(), receiveNoWait()
                // o usando un MessageListener
                Recepcion.recibirMensaje(
                        esAsincrono,
                        esperar,
                        messageConsumer
                );
            }
        } catch (NamingException | JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if(jndi != null) {
                try {
                    jndi.close();
                } catch (NamingException fne) {
                    LOGGER.error("Problema al cerrar JNDI", fne);
                }
            }
        }
    }
}