import java.util.*;

abstract class IAConfig implements Cloneable {
    protected String nombre;
    protected int dificultad; // 1-10
    protected String comportamiento; // agresivo, defensivo, etc.
    protected Map<String, Object> parametrosAvanzados;
    
    public IAConfig(String nombre, int dificultad, String comportamiento) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.comportamiento = comportamiento;
        this.parametrosAvanzados = new HashMap<>();
    }
    
    public abstract IAConfig clone();
    
    public void añadirParametro(String clave, Object valor) {
        this.parametrosAvanzados.put(clave, valor);
    }
    
    @Override
    public String toString() {
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, Object> entry : parametrosAvanzados.entrySet()) {
            if (params.length() > 0) params.append(", ");
            params.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return String.format("%s (Dif: %d, %s) - Params: {%s}", 
                             nombre, dificultad, comportamiento, params.toString());
    }
}

class NPCEnemigo extends IAConfig {
    private String tipoArma;
    private double velocidadAtaque;
    private List<String> patronesMovimiento;
    
    public NPCEnemigo(String nombre, int dificultad, String comportamiento, 
                     String tipoArma, double velocidadAtaque) {
        super(nombre, dificultad, comportamiento);
        this.tipoArma = tipoArma;
        this.velocidadAtaque = velocidadAtaque;
        this.patronesMovimiento = new ArrayList<>();
    }
    
    public void añadirPatronMovimiento(String patron) {
        this.patronesMovimiento.add(patron);
    }
    
    @Override
    public NPCEnemigo clone() {
        try {
            NPCEnemigo clonado = (NPCEnemigo) super.clone();
            // Copia profunda para colecciones
            clonado.parametrosAvanzados = new HashMap<>(this.parametrosAvanzados);
            clonado.patronesMovimiento = new ArrayList<>(this.patronesMovimiento);
            return clonado;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar NPCEnemigo", e);
        }
    }
    
    @Override
    public String toString() {
        String base = super.toString();
        String patrones = String.join(", ", patronesMovimiento);
        return String.format("%s\nArma: %s, Vel. Ataque: %.1f\nPatrones: [%s]", 
                            base, tipoArma, velocidadAtaque, patrones);
    }
}

class Compañero extends IAConfig {
    private String especialidad;
    private int lealtad; // 0-100
    private List<String> habilidadesEspeciales;
    private List<Map.Entry<String, String>> historialDecisiones;
    
    public Compañero(String nombre, int dificultad, String comportamiento, 
                    String especialidad, int lealtad) {
        super(nombre, dificultad, comportamiento);
        this.especialidad = especialidad;
        this.lealtad = lealtad;
        this.habilidadesEspeciales = new ArrayList<>();
        this.historialDecisiones = new ArrayList<>();
    }
    
    public void añadirHabilidad(String habilidad) {
        this.habilidadesEspeciales.add(habilidad);
    }
    
    public void registrarDecision(String situacion, String decision) {
        this.historialDecisiones.add(new AbstractMap.SimpleEntry<>(situacion, decision));
    }
    
    @Override
    public Compañero clone() {
        try {
            Compañero clonado = (Compañero) super.clone();
            // Copia profunda para colecciones
            clonado.parametrosAvanzados = new HashMap<>(this.parametrosAvanzados);
            clonado.habilidadesEspeciales = new ArrayList<>(this.habilidadesEspeciales);
            clonado.historialDecisiones = new ArrayList<>(this.historialDecisiones);
            return clonado;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar Compañero", e);
        }
    }
    
    @Override
    public String toString() {
        String base = super.toString();
        String habilidades = String.join(", ", habilidadesEspeciales);
        return String.format("%s\nEspecialidad: %s, Lealtad: %d%%\nHabilidades: [%s]\nHistorial: %d decisiones", 
                            base, especialidad, lealtad, habilidades, historialDecisiones.size());
    }
}

class SistemaAmbiente extends IAConfig {
    private double frecuenciaEventos; // eventos/hora
    private int impactoJugador; // 1-10
    private Map<String, Double> tiposEvento; // tipo: probabilidad
    
    public SistemaAmbiente(String nombre, int dificultad, String comportamiento, 
                          double frecuenciaEventos, int impactoJugador) {
        super(nombre, dificultad, comportamiento);
        this.frecuenciaEventos = frecuenciaEventos;
        this.impactoJugador = impactoJugador;
        this.tiposEvento = new HashMap<>();
    }
    
    public void añadirTipoEvento(String tipo, double probabilidad) {
        this.tiposEvento.put(tipo, probabilidad);
    }
    
    public String generarEvento() {
        List<String> tipos = new ArrayList<>(tiposEvento.keySet());
        List<Double> probs = new ArrayList<>();
        
        for (String tipo : tipos) {
            probs.add(tiposEvento.get(tipo));
        }
        
        Random random = new Random();
        double r = random.nextDouble();
        double acumulado = 0.0;
        
        for (int i = 0; i < tipos.size(); i++) {
            acumulado += probs.get(i);
            if (r <= acumulado) {
                return tipos.get(i);
            }
        }
        
        return tipos.get(tipos.size() - 1);
    }
    
    @Override
    public SistemaAmbiente clone() {
        try {
            SistemaAmbiente clonado = (SistemaAmbiente) super.clone();
            // Copia profunda para colecciones
            clonado.parametrosAvanzados = new HashMap<>(this.parametrosAvanzados);
            clonado.tiposEvento = new HashMap<>(this.tiposEvento);
            return clonado;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar SistemaAmbiente", e);
        }
    }
    
    @Override
    public String toString() {
        String base = super.toString();
        StringBuilder eventos = new StringBuilder();
        
        for (Map.Entry<String, Double> entry : tiposEvento.entrySet()) {
            if (eventos.length() > 0) eventos.append(", ");
            eventos.append(entry.getKey()).append(":").append(String.format("%.1f%%", entry.getValue() * 100));
        }
        
        return String.format("%s\nFrecuencia: %.1f evt/h, Impacto: %d/10\nEventos: {%s}", 
                            base, frecuenciaEventos, impactoJugador, eventos.toString());
    }
}

public class PrototypeDemo {
    public static void main(String[] args) {
        System.out.println("=== CREANDO PROTOTIPOS BASE ===");
        
        NPCEnemigo enemigoBase = new NPCEnemigo("Enemigo Base", 5, "equilibrado", "desarmado", 1.0);
        enemigoBase.añadirParametro("vision_distancia", 100);
        enemigoBase.añadirParametro("tiempo_reaccion", 0.5);
        enemigoBase.añadirPatronMovimiento("patrulla");
        
        Compañero compañeroBase = new Compañero("Compañero Base", 4, "defensivo", "apoyo", 75);
        compañeroBase.añadirParametro("radio_asistencia", 50);
        compañeroBase.añadirParametro("prioridad_curación", 0.8);
        compañeroBase.añadirHabilidad("primeros auxilios");
        
        SistemaAmbiente ambienteBase = new SistemaAmbiente("Ambiente Base", 3, "aleatorio", 2.0, 4);
        ambienteBase.añadirParametro("duracion_media", 120);
        ambienteBase.añadirTipoEvento("lluvia", 0.4);
        ambienteBase.añadirTipoEvento("niebla", 0.3);
        ambienteBase.añadirTipoEvento("soleado", 0.3);
        
        System.out.println(enemigoBase);
        System.out.println("\n" + compañeroBase);
        System.out.println("\n" + ambienteBase);
        
        System.out.println("\n=== CLONANDO Y CONFIGURANDO VARIANTES ESPECÍFICAS ===");
        
        NPCEnemigo jefeNivel = enemigoBase.clone();
        jefeNivel.nombre = "Guardia Real";
        jefeNivel.dificultad = 8;
        jefeNivel.comportamiento = "agresivo";
        jefeNivel.tipoArma = "espada de dos manos";
        jefeNivel.velocidadAtaque = 1.5;
        jefeNivel.añadirPatronMovimiento("emboscada");
        jefeNivel.añadirPatronMovimiento("persecución");
        jefeNivel.añadirParametro("daño_crítico", 3.0);
        
        NPCEnemigo francotirador = enemigoBase.clone();
        francotirador.nombre = "Centinela";
        francotirador.comportamiento = "cauteloso";
        francotirador.tipoArma = "arco largo";
        francotirador.velocidadAtaque = 0.7;
        francotirador.patronesMovimiento.clear();
        francotirador.añadirPatronMovimiento("esconderse");
        francotirador.añadirPatronMovimiento("reposicionar");
        francotirador.añadirParametro("vision_distancia", 300);
        francotirador.añadirParametro("precision", 0.9);
        
        Compañero sanador = compañeroBase.clone();
        sanador.nombre = "Clérigo";
        sanador.especialidad = "curación";
        sanador.lealtad = 90;
        sanador.añadirHabilidad("resurrección");
        sanador.añadirHabilidad("purificación");
        sanador.añadirParametro("radio_asistencia", 75);
        sanador.añadirParametro("eficacia_curativa", 2.0);
        
        SistemaAmbiente tormenta = ambienteBase.clone();
        tormenta.nombre = "Sistema de Tormentas";
        tormenta.dificultad = 7;
        tormenta.impactoJugador = 8;
        tormenta.tiposEvento.clear();
        tormenta.añadirTipoEvento("tormenta eléctrica", 0.3);
        tormenta.añadirTipoEvento("granizo", 0.2);
        tormenta.añadirTipoEvento("vendaval", 0.5);
        tormenta.añadirParametro("daño_estructuras", true);
        
        System.out.println(jefeNivel);
        System.out.println("\n" + francotirador);
        System.out.println("\n" + sanador);
        System.out.println("\n" + tormenta);
        
        System.out.println("\n=== SIMULACIÓN DE EVENTOS ===");
        for (int i = 0; i < 3; i++) {
            String evento = tormenta.generarEvento();
            System.out.println(String.format("El sistema '%s' ha generado un evento: %s", 
                                            tormenta.nombre, evento));
        }
    }
}
