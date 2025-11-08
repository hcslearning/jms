package cl.hcs.jms;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import jakarta.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Objects;
import cl.hcs.util.TipoDestino;

import static cl.hcs.util.Util.*;

public class EnviarConJNDI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviarConJNDI.class);

    public static void main(String[] args) {
        try {
            // JCommander - procesa args
            Opciones opciones = new Opciones();
            JCommander jCommander = JCommander.newBuilder()
                    .addObject(opciones)
                    .build();
            jCommander.usage();
            jCommander.parse(args);

            // envía mensaje
            if(opciones.getMensaje() == null || opciones.getMensaje().isBlank()) {
                opciones.setMensaje( generarStringRandom()  );
            }
            new EnviarConJNDI().run(
                    opciones.getUsuario(),
                    opciones.getContrasena(),
                    Objects.equals(opciones.getTipoDestino(), "topico") ? TipoDestino.TOPICO : TipoDestino.COLA,
                    opciones.getMensaje(),
                    opciones.isAsincrono()
            );
        } catch (ParameterException pe) {
            LOGGER.error(pe.getMessage());
        }
    }

    private void enviarMensajeSincrono(
            MessageProducer messageProducer,
            Session sesion,
            String mensaje) throws JMSException {
        TextMessage textMessage = sesion.createTextMessage(mensaje);
        messageProducer.send(textMessage);
        LOGGER.info("Enviando mensaje síncrono: {}", mensaje);
    }

    private void enviarMensajeAsincrono(
            MessageProducer messageProducer,
            Session sesion,
            String mensaje,
            CompletionListener listener) throws JMSException {
        TextMessage textMessage = sesion.createTextMessage(mensaje);
        messageProducer.send(textMessage, listener);
    }

    public void run(
            String brokerUser,
            String brokerPassword,
            TipoDestino tipoDestino,
            String mensaje,
            boolean esAsincrono) {
        InitialContext jndi = null;
        try {
            jndi = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connFactoryBroker1");

            Destination destino;
            if(tipoDestino.equals(TipoDestino.TOPICO)) {
                destino = (Destination) jndi.lookup("topicos/topicoEjemplo");
            } else {
                destino = (Destination) jndi.lookup("colas/colaEjemplo");
            }

            try (Connection connection = connectionFactory.createConnection(brokerUser, brokerPassword)) {
                Session sesion = connection.createSession();
                MessageProducer messageProducer = sesion.createProducer(destino);

                if( !esAsincrono) {
                    enviarMensajeSincrono(
                            messageProducer,
                            sesion,
                            mensaje
                    );
                } else {
                    enviarMensajeAsincrono(
                            messageProducer,
                            sesion,
                            mensaje,
                            new CompletionListener() {
                                @Override
                                public void onCompletion(Message message) {
                                    LOGGER.info("Mensaje asíncrono enviado satisfactoriamente: {}", mensaje);
                                }

                                @Override
                                public void onException(Message message, Exception e) {
                                    LOGGER.error("Se produjo un error al enviar el mensaje async: {}", e.getMessage());
                                }
                            }
                    );
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