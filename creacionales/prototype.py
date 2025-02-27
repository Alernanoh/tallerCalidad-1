import copy
from abc import ABC, abstractmethod
import random

class IAConfig(ABC):
    
    def __init__(self, nombre, dificultad, comportamiento):
        self.nombre = nombre
        self.dificultad = dificultad  # 1-10
        self.comportamiento = comportamiento  # agresivo, defensivo, etc.
        self.parametros_avanzados = {}
        
    @abstractmethod
    def clone(self):
        pass
    
    def añadir_parametro(self, clave, valor):
        self.parametros_avanzados[clave] = valor
    
    def __str__(self):
        params = ", ".join([f"{k}={v}" for k, v in self.parametros_avanzados.items()])
        return f"{self.nombre} (Dif: {self.dificultad}, {self.comportamiento}) - Params: {{{params}}}"


class NPCEnemigo(IAConfig):
    
    def __init__(self, nombre, dificultad, comportamiento, tipo_arma, velocidad_ataque):
        super().__init__(nombre, dificultad, comportamiento)
        self.tipo_arma = tipo_arma
        self.velocidad_ataque = velocidad_ataque
        self.patrones_movimiento = []
        
    def añadir_patron_movimiento(self, patron):
        self.patrones_movimiento.append(patron)
    
    def clone(self):
        return copy.deepcopy(self)
    
    def __str__(self):
        base = super().__str__()
        patrones = ", ".join(self.patrones_movimiento)
        return f"{base}\nArma: {self.tipo_arma}, Vel. Ataque: {self.velocidad_ataque}\nPatrones: [{patrones}]"


class Compañero(IAConfig):
    
    def __init__(self, nombre, dificultad, comportamiento, especialidad, lealtad):
        super().__init__(nombre, dificultad, comportamiento)
        self.especialidad = especialidad
        self.lealtad = lealtad  # 0-100
        self.habilidades_especiales = []
        self.historial_decisiones = []
        
    def añadir_habilidad(self, habilidad):
        self.habilidades_especiales.append(habilidad)
    
    def registrar_decision(self, situacion, decision):
        self.historial_decisiones.append((situacion, decision))
    
    def clone(self):
        return copy.deepcopy(self)
    
    def __str__(self):
        base = super().__str__()
        habilidades = ", ".join(self.habilidades_especiales)
        return f"{base}\nEspecialidad: {self.especialidad}, Lealtad: {self.lealtad}%\nHabilidades: [{habilidades}]\nHistorial: {len(self.historial_decisiones)} decisiones"


class SistemaAmbiente(IAConfig):
    
    def __init__(self, nombre, dificultad, comportamiento, frecuencia_eventos, impacto_jugador):
        super().__init__(nombre, dificultad, comportamiento)
        self.frecuencia_eventos = frecuencia_eventos  # eventos/hora
        self.impacto_jugador = impacto_jugador  # 1-10
        self.tipos_evento = {}  # tipo: probabilidad
        
    def añadir_tipo_evento(self, tipo, probabilidad):
        self.tipos_evento[tipo] = probabilidad
    
    def generar_evento(self):
        tipos = list(self.tipos_evento.keys())
        probs = list(self.tipos_evento.values())
        return random.choices(tipos, weights=probs, k=1)[0]
    
    def clone(self):
        return copy.deepcopy(self)
    
    def __str__(self):
        base = super().__str__()
        eventos = ", ".join([f"{k}:{v*100:.1f}%" for k, v in self.tipos_evento.items()])
        return f"{base}\nFrecuencia: {self.frecuencia_eventos} evt/h, Impacto: {self.impacto_jugador}/10\nEventos: {{{eventos}}}"


# Creamos prototipos base para cada tipo de IA
print("=== CREANDO PROTOTIPOS BASE ===")

# Prototipo para enemigos básicos
enemigo_base = NPCEnemigo("Enemigo Base", 5, "equilibrado", "desarmado", 1.0)
enemigo_base.añadir_parametro("vision_distancia", 100)
enemigo_base.añadir_parametro("tiempo_reaccion", 0.5)
enemigo_base.añadir_patron_movimiento("patrulla")

# Prototipo para compañeros básicos
compañero_base = Compañero("Compañero Base", 4, "defensivo", "apoyo", 75)
compañero_base.añadir_parametro("radio_asistencia", 50)
compañero_base.añadir_parametro("prioridad_curación", 0.8)
compañero_base.añadir_habilidad("primeros auxilios")

# Prototipo para sistema ambiental básico
ambiente_base = SistemaAmbiente("Ambiente Base", 3, "aleatorio", 2.0, 4)
ambiente_base.añadir_parametro("duracion_media", 120)
ambiente_base.añadir_tipo_evento("lluvia", 0.4)
ambiente_base.añadir_tipo_evento("niebla", 0.3)
ambiente_base.añadir_tipo_evento("soleado", 0.3)

print(enemigo_base)
print("\n" + str(compañero_base))
print("\n" + str(ambiente_base))

# Creamos variantes específicas a partir de los prototipos
print("\n=== CLONANDO Y CONFIGURANDO VARIANTES ESPECÍFICAS ===")

# Enemigo avanzado: Jefe de nivel
jefe_nivel = enemigo_base.clone()
jefe_nivel.nombre = "Guardia Real"
jefe_nivel.dificultad = 8
jefe_nivel.comportamiento = "agresivo"
jefe_nivel.tipo_arma = "espada de dos manos"
jefe_nivel.velocidad_ataque = 1.5
jefe_nivel.añadir_patron_movimiento("emboscada")
jefe_nivel.añadir_patron_movimiento("persecución")
jefe_nivel.añadir_parametro("daño_crítico", 3.0)

# Enemigo avanzado: Francotirador
francotirador = enemigo_base.clone()
francotirador.nombre = "Centinela"
francotirador.comportamiento = "cauteloso"
francotirador.tipo_arma = "arco largo"
francotirador.velocidad_ataque = 0.7
francotirador.patrones_movimiento = ["esconderse", "reposicionar"]
francotirador.añadir_parametro("vision_distancia", 300)
francotirador.añadir_parametro("precision", 0.9)

# Compañero especializado: Sanador
sanador = compañero_base.clone()
sanador.nombre = "Clérigo"
sanador.especialidad = "curación"
sanador.lealtad = 90
sanador.añadir_habilidad("resurrección")
sanador.añadir_habilidad("purificación")
sanador.añadir_parametro("radio_asistencia", 75)
sanador.añadir_parametro("eficacia_curativa", 2.0)

# Sistema ambiental: Tormenta
tormenta = ambiente_base.clone()
tormenta.nombre = "Sistema de Tormentas"
tormenta.dificultad = 7
tormenta.impacto_jugador = 8
tormenta.tipos_evento = {}  # Reemplazamos los eventos base
tormenta.añadir_tipo_evento("tormenta eléctrica", 0.3)
tormenta.añadir_tipo_evento("granizo", 0.2)
tormenta.añadir_tipo_evento("vendaval", 0.5)
tormenta.añadir_parametro("daño_estructuras", True)

print(jefe_nivel)
print("\n" + str(francotirador))
print("\n" + str(sanador))
print("\n" + str(tormenta))

# Demostración de evento generado por el sistema ambiental
print("\n=== SIMULACIÓN DE EVENTOS ===")
for _ in range(3):
    evento = tormenta.generar_evento()
    print(f"El sistema '{tormenta.nombre}' ha generado un evento: {evento}")
