import java.util.HashMap;
import java.util.Map;

class MQTTProtocol {
    public MQTTProtocol() {
        System.out.println("Inicializando protocolo MQTT");
    }

    public Map<String, Object> publishMqtt(String topic, Object payload) {
        Map<String, Object> formattedMessage = new HashMap<>();
        formattedMessage.put("topic", topic);
        formattedMessage.put("qos", 1);
        formattedMessage.put("retain", false);
        formattedMessage.put("payload", payload);
        System.out.println("MQTT >> Publicando en '" + topic + "': " + payload);
        return formattedMessage;
    }

    public Map<String, Object> subscribeMqtt(String topic) {
        System.out.println("MQTT >> Suscrito al tópico: " + topic);
        Map<String, Object> response = new HashMap<>();
        response.put("topic", topic);
        response.put("qos", 1);
        return response;
    }
}

class CoAPProtocol {
    public CoAPProtocol() {
        System.out.println("Inicializando protocolo CoAP");
    }

    public Map<String, Object> sendCoap(String resource, Object content, Map<String, Object> options) {
        if (options == null) {
            options = new HashMap<>();
            options.put("confirmable", true);
        }

        Map<String, Object> formattedMessage = new HashMap<>();
        formattedMessage.put("uri-path", resource);
        formattedMessage.put("content-format", "application/json");
        formattedMessage.put("payload", content);
        formattedMessage.put("options", options);
        System.out.println("CoAP >> Enviando a recurso '" + resource + "': " + content);
        return formattedMessage;
    }

    public Map<String, Object> observeCoap(String resource) {
        System.out.println("CoAP >> Observando recurso: " + resource);
        Map<String, Object> response = new HashMap<>();
        response.put("uri-path", resource);
        response.put("observe", 0);
        return response;
    }
}

class HTTPProtocol {
    public HTTPProtocol() {
        System.out.println("Inicializando protocolo HTTP");
    }

    public Map<String, Object> postHttp(String endpoint, Object data, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
        }

        Map<String, Object> formattedMessage = new HashMap<>();
        formattedMessage.put("method", "POST");
        formattedMessage.put("url", endpoint);
        formattedMessage.put("headers", headers);
        formattedMessage.put("body", data);
        System.out.println("HTTP >> POST a '" + endpoint + "': " + data);
        return formattedMessage;
    }

    public Map<String, Object> getHttp(String endpoint, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }

        System.out.println("HTTP >> GET a '" + endpoint + "' con parámetros: " + params);
        Map<String, Object> response = new HashMap<>();
        response.put("method", "GET");
        response.put("url", endpoint);
        response.put("params", params);
        return response;
    }
}

class IoTAdapter {
    private Object protocol;
    private String protocolType;

    public IoTAdapter(Object protocol, String protocolType) {
        this.protocol = protocol;
        this.protocolType = protocolType;
    }

    private String topicToResource(String topic) {
        return "/" + topic.replace("/", "_");
    }

    private String resourceToTopic(String resource) {
        return resource.strip("/").replace("_", "/");
    }

    public Map<String, Object> sendMessage(String destination, Object message) {
        if (protocolType.equals("mqtt")) {
            return ((MQTTProtocol) protocol).publishMqtt(destination, message);
        } else if (protocolType.equals("coap")) {
            String resource = topicToResource(destination);
            return ((CoAPProtocol) protocol).sendCoap(resource, message, null);
        } else if (protocolType.equals("http")) {
            String endpoint = "http://iot.example.com/api" + topicToResource(destination);
            return ((HTTPProtocol) protocol).postHttp(endpoint, message, null);
        } else {
            throw new IllegalArgumentException("Protocolo no soportado: " + protocolType);
        }
    }

    public Map<String, Object> listen(String source) {
        if (protocolType.equals("mqtt")) {
            return ((MQTTProtocol) protocol).subscribeMqtt(source);
        } else if (protocolType.equals("coap")) {
            String resource = topicToResource(source);
            return ((CoAPProtocol) protocol).observeCoap(resource);
        } else if (protocolType.equals("http")) {
            String endpoint = "http://iot.example.com/api" + topicToResource(source);
            return ((HTTPProtocol) protocol).getHttp(endpoint, Map.of("subscribe", "true"));
        } else {
            throw new IllegalArgumentException("Protocolo no soportado: " + protocolType);
        }
    }
}

public class AdapterPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== CONFIGURACIÓN DE DISPOSITIVOS IoT ===");

        System.out.println("\n[Dispositivo 1: Termostato]");
        IoTAdapter termoAdapter = new IoTAdapter(new MQTTProtocol(), "mqtt");
        Map<String, Object> termoMessage = Map.of("device", "termostato_sala", "temperature", 22.5, "humidity", 45);
        termoAdapter.sendMessage("home/living_room/temperature", termoMessage);
        termoAdapter.listen("home/control/thermostat");

        System.out.println("\n[Dispositivo 2: Sensor de Movimiento]");
        IoTAdapter motionAdapter = new IoTAdapter(new CoAPProtocol(), "coap");
        Map<String, Object> motionMessage = Map.of("device", "motion_sensor", "movement", true, "battery", 78);
        motionAdapter.sendMessage("home/entrance/motion", motionMessage);
        motionAdapter.listen("home/control/security");

        System.out.println("\n[Dispositivo 3: Asistente de Voz]");
        IoTAdapter voiceAdapter = new IoTAdapter(new HTTPProtocol(), "http");
        Map<String, Object> voiceMessage = Map.of("device", "voice_assistant", "command", "lights_on", "room", "living_room");
        voiceAdapter.sendMessage("home/voice/commands", voiceMessage);
        voiceAdapter.listen("home/notifications");

        System.out.println("\n=== COMUNICACIÓN ENTRE DISPOSITIVOS ===");
        termoAdapter.sendMessage("home/voice/temperature_update", Map.of("current", 23.5, "target", 22.0));

        motionAdapter.sendMessage("home/living_room/presence", Map.of("presence_detected", true));
    }
}
