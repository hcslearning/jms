package cl.hcs.jms;

import cl.hcs.jms.util.TipoDestino;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import jakarta.jms.*;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.HashMap;
import java.util.Map;

import static cl.hcs.jms.util.Util.generarStringRandom;

public class EnviarSinJNDI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviarSinJNDI.class);

    public static void main(String[] args) {
        try {
            // JCommander - procesa args
            final OpcionesEnviarSinJNDI opciones = new OpcionesEnviarSinJNDI();
            final JCommander jCommander = JCommander.newBuilder()
                    .addObject(opciones)
                    .build();
            jCommander.usage();
            jCommander.parse(args);

            if(opciones.getBase().mostrarAyuda()) { return; }

            // envía mensaje
            if(opciones.getEnvio().getMensaje() == null || opciones.getEnvio().getMensaje().isBlank()) {
                opciones.getEnvio().setMensaje( generarStringRandom()  );
            }
            new EnviarSinJNDI().run(
                    opciones.getHost(),
                    opciones.getPort(),
                    opciones.getJms().getUsuario(),
                    opciones.getJms().getContrasena(),
                    opciones.getEnvio().getTipoDestino().equalsIgnoreCase("cola") ? TipoDestino.COLA : TipoDestino.TOPICO,
                    opciones.getNombreDestino(),
                    opciones.getEnvio().getMensaje(),
                    opciones.getEnvio().isAsincrono()
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

    /**
     * Crea una instancia de ConnectionFactory conectada al broker de ActiveMQ Artemis.
     * Utiliza el conector Netty para comunicación por TCP.
     *
     * @return un objeto ConnectionFactory listo para crear conexiones JMS
     */
    private ConnectionFactory createConnectionFactory(String host, int port) {
        // Define los parámetros de conexión al broker
        Map<String, Object> connectionParams = new HashMap<>();
        connectionParams.put("host", host);
        connectionParams.put("port", port);

        // Crea la configuración de transporte usando Netty
        TransportConfiguration transportConfiguration =
                new TransportConfiguration(NettyConnectorFactory.class.getName(), connectionParams);

        // Crea una ConnectionFactory para conexiones estándar
        return ActiveMQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);
    }

    private Queue getQueue(Session session, String name) throws JMSException {
        ActiveMQSession activeMQSession = (ActiveMQSession) session;
        return session.createQueue(name);
    }

    private Topic getTopic(Session session, String name) throws JMSException {
        ActiveMQSession activeMQSession = (ActiveMQSession) session;
        return session.createTopic(name);
    }

    public void run(
            String host,
            int port,
            String brokerUser,
            String brokerPassword,
            TipoDestino tipoDestino,
            String nombreDestino,
            String mensaje,
            boolean esAsincrono) {
        InitialContext jndi = null;
        try {
            ConnectionFactory connectionFactory = createConnectionFactory(host, port);

            try (Connection connection = connectionFactory.createConnection(brokerUser, brokerPassword)) {
                Session sesion = connection.createSession();

                Destination destino;
                if(tipoDestino.equals(TipoDestino.TOPICO)) {
                    LOGGER.info("Configurando destino de tipo TOPICO");
                    destino = getTopic(sesion, nombreDestino);
                } else {
                    LOGGER.info("Configurando destino de tipo COLA");
                    destino = getQueue(sesion, nombreDestino);
                }

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
        }
    }

}
