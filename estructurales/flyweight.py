class CaracteristicaFuente:
    def __init__(self, familia, tamaño, estilo, color):
        self.familia = familia
        self.tamaño = tamaño
        self.estilo = estilo  # normal, negrita, cursiva, etc.
        self.color = color
        print(f"Creando nueva configuración de fuente: {familia}, {tamaño}pt, {estilo}, {color}")

    def renderizar(self, caracter, x, y):
        print(f"Renderizando '{caracter}' en ({x}, {y}) con fuente {self.familia} {self.tamaño}pt {self.estilo}, color {self.color}")


class FabricaFuentes:
    _fuentes = {}

    @staticmethod
    def obtener_fuente(familia, tamaño, estilo, color):
        # Crea una clave única para identificar esta configuración de fuente
        clave = f"{familia}-{tamaño}-{estilo}-{color}"
        
        # Reutiliza la configuración si ya existe
        if clave not in FabricaFuentes._fuentes:
            FabricaFuentes._fuentes[clave] = CaracteristicaFuente(familia, tamaño, estilo, color)
        
        return FabricaFuentes._fuentes[clave]


class Caracter:
    def __init__(self, caracter, fuente, posicion_x, posicion_y):
        self.caracter = caracter
        self.fuente = fuente  # Referencia al objeto flyweight
        self.posicion_x = posicion_x
        self.posicion_y = posicion_y

    def dibujar(self):
        """Dibuja el caracter en pantalla"""
        self.fuente.renderizar(self.caracter, self.posicion_x, self.posicion_y)


class DocumentoTexto:
    def __init__(self):
        self.caracteres = []
    
    def añadir_caracter(self, caracter, fuente, x, y):
        self.caracteres.append(Caracter(caracter, fuente, x, y))
    
    def renderizar_documento(self):
        print("\n=== RENDERIZANDO DOCUMENTO ===")
        for caracter in self.caracteres:
            caracter.dibujar()


# Creamos un documento
documento = DocumentoTexto()

# Creamos algunas configuraciones de fuente compartidas (flyweights)
fuente_normal = FabricaFuentes.obtener_fuente("Arial", 12, "normal", "negro")
fuente_titulo = FabricaFuentes.obtener_fuente("Times New Roman", 18, "negrita", "azul")
fuente_enfasis = FabricaFuentes.obtener_fuente("Arial", 12, "cursiva", "negro")
fuente_codigo = FabricaFuentes.obtener_fuente("Courier New", 10, "normal", "gris")

# Simulamos añadir texto al documento
print("\n=== AÑADIENDO CARACTERES AL DOCUMENTO ===")

# Título
for i, c in enumerate("DOCUMENTO DE EJEMPLO"):
    documento.añadir_caracter(c, fuente_titulo, 10 + i*10, 10)

# Texto normal
texto = "Este es un ejemplo del patrón Flyweight."
for i, c in enumerate(texto):
    documento.añadir_caracter(c, fuente_normal, 10 + i*8, 40)

# Palabra con énfasis
for i, c in enumerate("optimiza"):
    documento.añadir_caracter(c, fuente_enfasis, 10 + i*8, 60)

# Texto de código
codigo = "class Ejemplo: pass"
for i, c in enumerate(codigo):
    documento.añadir_caracter(c, fuente_codigo, 10 + i*8, 80)

# Reutilizando la fuente normal (no creará un nuevo objeto)
print("\n=== REUTILIZANDO FUENTES EXISTENTES ===")
fuente_normal_reuso = FabricaFuentes.obtener_fuente("Arial", 12, "normal", "negro")
for i, c in enumerate("La memoria se ahorra."):
    documento.añadir_caracter(c, fuente_normal_reuso, 10 + i*8, 100)

# Renderizamos el documento completo
documento.renderizar_documento()

# Estadísticas de uso de memoria
print("\n=== ESTADÍSTICAS DE MEMORIA ===")
print(f"Número total de caracteres: {len(documento.caracteres)}")
print(f"Número de objetos fuente creados: {len(FabricaFuentes._fuentes)}")
print(f"Ratio de compartición: {len(documento.caracteres) / len(FabricaFuentes._fuentes):.2f} caracteres por fuente")
