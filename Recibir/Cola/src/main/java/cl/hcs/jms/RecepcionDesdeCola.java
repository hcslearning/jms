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
        OpcionesCola opciones = new OpcionesCola();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(opciones)
                .build();
        jCommander.usage();
        jCommander.parse(args);

        if(opciones.getBase().mostrarAyuda()) return;

        new RecepcionDesdeCola().run(
                opciones.getJms().getUsuario(),
                opciones.getJms().getContrasena(),
                opciones.getRecepcion().isAsincrono(),
                opciones.getRecepcion().isEsperaHabilitada()
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
                        // necesario para notificar al broker que se procesó el mensaje
                        mensaje.acknowledge();
                        LOGGER.info("Mensaje reconocido como procesado (acknowledge)");

                        // muestra mensaje en consola
                        LOGGER.info("Mensaje recibido síncronamente: {}", mensaje.getBody(String.class));
                    } else {
                        if(!esperar) {
                            LOGGER.info("No hay mensajes por el momento!!!");
                        }
                    }
                } else {
                    LOGGER.info("Esperando que lleguen mensajes vía MessageListener ...");
                    messageConsumer.setMessageListener(new MessageListener() {
                        @Override
                        public void onMessage(Message m) {
                            try {
                                LOGGER.info("Mensaje recibido asíncronamente vía MessageListener: {}", m.getBody(String.class));
                                m.acknowledge();
                            } catch (JMSException e) {
                                LOGGER.error("Error al recibir el mensaje de manera asíncrona con MessageListener.", e);
                            }
                        }
                    });

                    LOGGER.info("Uniéndose al hilo principal para evitar el cierre de la app...");
                    Thread.currentThread().join();
                }
            } catch (InterruptedException ie) {
                LOGGER.error("Error al unirse al hilo", ie);
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