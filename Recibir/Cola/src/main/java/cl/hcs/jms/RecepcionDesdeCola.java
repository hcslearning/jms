package cl.hcs.jms;

import com.beust.jcommander.JCommander;
import jakarta.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RecepcionDesdeCola {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecepcionDesdeCola.class);

    public static void main(String[] args) {
        // JCommander - procesa args
        Opciones opciones = new Opciones();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(opciones)
                .build();
        jCommander.usage();
        jCommander.parse(args);

        new RecepcionDesdeCola().run(
                opciones.getUsuario(),
                opciones.getContrasena(),
                opciones.isAsincrono(),
                opciones.isEsperaHabilitada()
        );
    }

    public void run(
            String brokerUser,
            String brokerPassword,
            boolean esAsincrono,
            boolean esperar
    ) {
        InitialContext jndi = null;
        Message mensaje = null;
        try {
            jndi = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connFactoryBroker1");

            Destination cola = (Destination) jndi.lookup("colas/colaEjemplo");

            try (Connection connection = connectionFactory.createConnection(brokerUser, brokerPassword)) {
                Session sesion = connection.createSession(Session.CLIENT_ACKNOWLEDGE);
                MessageConsumer messageConsumer = sesion.createConsumer(cola);
                connection.start();

                if( !esAsincrono) {

                    if( esperar ) {
                        LOGGER.info("Esperando que llegue un mensaje...");
                        mensaje = messageConsumer.receive();// bloquea hasta que llegue un mensaje
                    } else {
                        LOGGER.info("Revisando si hay un mensaje para procesar, o si no null...");
                        mensaje = messageConsumer.receiveNoWait(); // recibe solo si hay mensaje de inmediato o si no devuelve nulo
                    }

                    if( mensaje != null) {
                        // necesario para notificar como mensaje procesado
                        mensaje.acknowledge();
                        // muestra mensaje en consola
                        LOGGER.info("Mensaje recibido síncronamente: {}", mensaje.getBody(String.class));
                    } else {
                        if(!esperar) {
                            LOGGER.info("No hay mensajes por el momento!!!");
                        }
                    }
                } else {

                }
            }
        } catch (NamingException | JMSException e) {
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