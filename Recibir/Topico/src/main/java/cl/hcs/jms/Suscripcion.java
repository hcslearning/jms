package cl.hcs.jms;

import cl.hcs.jms.util.Recepcion;
import com.beust.jcommander.JCommander;
import jakarta.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Suscripcion {

    private static final Logger LOGGER = LoggerFactory.getLogger(Suscripcion.class);

    public static void main(String[] args) {
        // JCommander - procesa args
        OpcionesSuscripcion opciones = new OpcionesSuscripcion();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(opciones)
                .build();
        jCommander.usage();
        jCommander.parse(args);

        if(opciones.getBase().mostrarAyuda()) return;

        new Suscripcion().run(
                opciones.getJmsBase().getUsuario(),
                opciones.getJmsBase().getContrasena(),
                opciones.getJmsBase().getClientId(),
                opciones.getSuscripcion().getNombreSuscripcion(),
                opciones.getSuscripcion().isDurable(),
                opciones.getSuscripcion().isCompartida(),
                opciones.getRecepcion().isAsincrono(),
                opciones.getRecepcion().isEsperaHabilitada()
        );
    }

    public void run(
            String brokerUser,
            String brokerPassword,
            String clientId,
            String nombreSuscripcion,
            boolean esDurable,
            boolean esCompartida,
            boolean esAsincrono,
            boolean esperar
    ) {
        InitialContext jndi = null;
        try {
            jndi = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connFactoryBroker1");

            Topic topico = (Topic) jndi.lookup("topicos/topicoEjemplo");

            try (Connection connection = connectionFactory.createConnection(brokerUser, brokerPassword)) {
                if( clientId != null) connection.setClientID(clientId);
                LOGGER.info("ClientID: {}", connection.getClientID());

                Session sesion = connection.createSession(Session.CLIENT_ACKNOWLEDGE);

                MessageConsumer messageConsumer = null;
                if(esDurable) {
                    if( esCompartida ) {
                        LOGGER.info("Creando suscripción Durable y Compartida llamada: {}", nombreSuscripcion);
                        messageConsumer = sesion.createSharedDurableConsumer(topico, nombreSuscripcion);
                    } else {
                        LOGGER.info("Creando suscripción Durable y NO Compartida ...");
                        messageConsumer = sesion.createDurableConsumer(topico, nombreSuscripcion);
                    }
                } else {
                    if( esCompartida ) {
                        LOGGER.info("Creando suscripción NO Durable y Compartida llamada: {}", nombreSuscripcion);
                        messageConsumer = sesion.createSharedConsumer(topico, nombreSuscripcion);
                    } else {
                        LOGGER.info("Creando suscripción NO Durable y NO Compartida ...");
                        messageConsumer = sesion.createConsumer(topico);
                    }
                }

                // Se inicia conexión para recibir mensajes (necesario en API Clásica)
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