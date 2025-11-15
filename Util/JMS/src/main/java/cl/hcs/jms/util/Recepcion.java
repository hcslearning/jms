package cl.hcs.jms.util;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Recepcion {

    public static Logger LOGGER = LoggerFactory.getLogger(Recepcion.class);

    public static void recibirMensaje(
        boolean esAsincrono,
        boolean esperar,
        MessageConsumer messageConsumer
    ) throws JMSException, InterruptedException {
        Message mensaje = null;
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
    }
}
