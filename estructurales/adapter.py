class MQTTProtocol:
    def __init__(self):
        print("Inicializando protocolo MQTT")
    
    def publish_mqtt(self, topic, payload):
        # Simulación de publicación en formato MQTT
        formatted_message = {
            "topic": topic,
            "qos": 1,
            "retain": False,
            "payload": payload
        }
        print(f"MQTT >> Publicando en '{topic}': {payload}")
        return formatted_message
    
    def subscribe_mqtt(self, topic):
        # Simulación de suscripción en formato MQTT
        print(f"MQTT >> Suscrito al tópico: {topic}")
        return {"topic": topic, "qos": 1}


class CoAPProtocol:
    def __init__(self):
        print("Inicializando protocolo CoAP")
    
    def send_coap(self, resource, content, options=None):
        # Simulación de envío en formato CoAP (Constrained Application Protocol)
        if options is None:
            options = {"confirmable": True}
        
        formatted_message = {
            "uri-path": resource,
            "content-format": "application/json",
            "payload": content,
            "options": options
        }
        print(f"CoAP >> Enviando a recurso '{resource}': {content}")
        return formatted_message
    
    def observe_coap(self, resource):
        # Simulación de observación CoAP
        print(f"CoAP >> Observando recurso: {resource}")
        return {"uri-path": resource, "observe": 0}


class HTTPProtocol:
    def __init__(self):
        print("Inicializando protocolo HTTP")
    
    def post_http(self, endpoint, data, headers=None):
        # Simulación de petición HTTP POST
        if headers is None:
            headers = {"Content-Type": "application/json"}
        
        formatted_message = {
            "method": "POST",
            "url": endpoint,
            "headers": headers,
            "body": data
        }
        print(f"HTTP >> POST a '{endpoint}': {data}")
        return formatted_message
    
    def get_http(self, endpoint, params=None):
        # Simulación de petición HTTP GET
        if params is None:
            params = {}
        
        print(f"HTTP >> GET a '{endpoint}' con parámetros: {params}")
        return {"method": "GET", "url": endpoint, "params": params}


class IoTAdapter:
    def __init__(self, protocol, protocol_type):
        self.protocol = protocol
        self.protocol_type = protocol_type
    
    def _topic_to_resource(self, topic):
        # Convierte un tópico MQTT a un recurso CoAP o un endpoint HTTP
        return "/" + topic.replace("/", "_")
    
    def _resource_to_topic(self, resource):
        # Convierte un recurso CoAP o un endpoint HTTP a un tópico MQTT
        return resource.strip("/").replace("_", "/")
    
    def send_message(self, destination, message):
        """Envía un mensaje utilizando el protocolo configurado"""
        if self.protocol_type == "mqtt":
            return self.protocol.publish_mqtt(destination, message)
        elif self.protocol_type == "coap":
            resource = self._topic_to_resource(destination)
            return self.protocol.send_coap(resource, message)
        elif self.protocol_type == "http":
            endpoint = f"http://iot.example.com/api{self._topic_to_resource(destination)}"
            return self.protocol.post_http(endpoint, message)
        else:
            raise ValueError(f"Protocolo no soportado: {self.protocol_type}")
    
    def listen(self, source):
        if self.protocol_type == "mqtt":
            return self.protocol.subscribe_mqtt(source)
        elif self.protocol_type == "coap":
            resource = self._topic_to_resource(source)
            return self.protocol.observe_coap(resource)
        elif self.protocol_type == "http":
            endpoint = f"http://iot.example.com/api{self._topic_to_resource(source)}"
            return self.protocol.get_http(endpoint, {"subscribe": "true"})
        else:
            raise ValueError(f"Protocolo no soportado: {self.protocol_type}")


# Ejemplo de uso con diferentes dispositivos y protocolos
print("=== CONFIGURACIÓN DE DISPOSITIVOS IoT ===")

# Dispositivo 1: Termostato usando MQTT
print("\n[Dispositivo 1: Termostato]")
termo_adapter = IoTAdapter(MQTTProtocol(), "mqtt")
termo_message = {"device": "termostato_sala", "temperature": 22.5, "humidity": 45}
termo_adapter.send_message("home/living_room/temperature", termo_message)
termo_adapter.listen("home/control/thermostat")

# Dispositivo 2: Sensor de movimiento usando CoAP (protocolo para dispositivos restringidos)
print("\n[Dispositivo 2: Sensor de Movimiento]")
motion_adapter = IoTAdapter(CoAPProtocol(), "coap")
motion_message = {"device": "motion_sensor", "movement": True, "battery": 78}
motion_adapter.send_message("home/entrance/motion", motion_message)
motion_adapter.listen("home/control/security")

# Dispositivo 3: Asistente de voz usando HTTP
print("\n[Dispositivo 3: Asistente de Voz]")
voice_adapter = IoTAdapter(HTTPProtocol(), "http")
voice_message = {"device": "voice_assistant", "command": "lights_on", "room": "living_room"}
voice_adapter.send_message("home/voice/commands", voice_message)
voice_adapter.listen("home/notifications")

# Demostración de interoperabilidad: todos los dispositivos pueden comunicarse entre sí
print("\n=== COMUNICACIÓN ENTRE DISPOSITIVOS ===")
# El termostato (MQTT) puede enviar datos al asistente de voz (HTTP)
termo_adapter.send_message("home/voice/temperature_update", {"current": 23.5, "target": 22.0})

# El sensor de movimiento (CoAP) puede alertar al termostato (MQTT)
motion_adapter.send_message("home/living_room/presence", {"presence_detected": True})
