from abc import ABC, abstractmethod
from datetime import datetime

class ContenidoStreaming(ABC):
    @abstractmethod
    def reproducir(self):
        pass
    
    @abstractmethod
    def obtener_detalles(self):
        pass

class ContenidoReal(ContenidoStreaming):
    def __init__(self, titulo, duracion, calidad):
        self.titulo = titulo
        self.duracion = duracion  # en minutos
        self.calidad = calidad    # HD, 4K, etc.
        print(f"Cargando contenido: {titulo}")
        
    def reproducir(self):
        print(f"Reproduciendo '{self.titulo}' en calidad {self.calidad}")
        
    def obtener_detalles(self):
        return f"Título: {self.titulo}\nDuración: {self.duracion} min\nCalidad: {self.calidad}"

class ProxyContenido(ContenidoStreaming):
    def __init__(self, titulo, duracion, calidad, tipo_suscripcion):
        self.titulo = titulo
        self.duracion = duracion
        self.calidad = calidad
        self.tipo_suscripcion = tipo_suscripcion
        self.contenido_real = None
        self.contador_visitas = 0
        
    def _verificar_acceso(self):
        # Diferentes niveles de suscripción tienen diferentes permisos
        if self.tipo_suscripcion == "free" and self.calidad == "4K":
            return False
        elif self.tipo_suscripcion == "free" and self.contador_visitas >= 3:
            return False
        elif self.tipo_suscripcion == "básico" and self.calidad == "4K":
            return False
        else:
            return True
            
    def reproducir(self):
        if self._verificar_acceso():
            if not self.contenido_real:
                self.contenido_real = ContenidoReal(self.titulo, self.duracion, self.calidad)
            
            # Registrar hora de visualización para análisis
            hora_actual = datetime.now().strftime("%H:%M:%S")
            print(f"[{hora_actual}] Acceso registrado para análisis de uso")
            
            self.contador_visitas += 1
            self.contenido_real.reproducir()
        else:
            calidad_permitida = "HD" if self.tipo_suscripcion == "básico" else "SD"
            print(f"Acceso denegado a '{self.titulo}' en calidad {self.calidad}")
            print(f"Actualiza tu suscripción o elige calidad {calidad_permitida}")
            
    def obtener_detalles(self):
        # Todos pueden ver los detalles, pero se registra quién lo consulta
        print(f"Consulta de detalles por usuario con plan {self.tipo_suscripcion}")
        
        if not self.contenido_real:
            self.contenido_real = ContenidoReal(self.titulo, self.duracion, self.calidad)
            
        return self.contenido_real.obtener_detalles()

# Ejemplo de uso
print("=== USUARIO CON PLAN FREE ===")
pelicula1 = ProxyContenido("El Código Enigma", 114, "4K", "free")
pelicula1.reproducir()
print("\nDetalles:")
print(pelicula1.obtener_detalles())

print("\n=== USUARIO CON PLAN PREMIUM ===")
pelicula2 = ProxyContenido("El Código Enigma", 114, "4K", "premium")
pelicula2.reproducir()

print("\n=== LÍMITE DE REPRODUCCIONES (PLAN FREE) ===")
documental = ProxyContenido("Vida Salvaje", 45, "HD", "free")
for i in range(4):
    print(f"\nIntento {i+1}:")
    documental.reproducir()
