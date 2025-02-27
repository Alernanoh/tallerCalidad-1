import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

interface ContenidoStreaming {
    void reproducir();
    String obtenerDetalles();
}

class ContenidoReal implements ContenidoStreaming {
    private String titulo;
    private int duracion;  // en minutos
    private String calidad;  // HD, 4K, etc.
    
    public ContenidoReal(String titulo, int duracion, String calidad) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.calidad = calidad;
        System.out.println("Cargando contenido: " + titulo);
    }
    
    @Override
    public void reproducir() {
        System.out.println("Reproduciendo '" + titulo + "' en calidad " + calidad);
    }
    
    @Override
    public String obtenerDetalles() {
        return "Título: " + titulo + "\nDuración: " + duracion + " min\nCalidad: " + calidad;
    }
}

class ProxyContenido implements ContenidoStreaming {
    private String titulo;
    private int duracion;
    private String calidad;
    private String tipoSuscripcion;
    private ContenidoReal contenidoReal;
    private int contadorVisitas;
    
    public ProxyContenido(String titulo, int duracion, String calidad, String tipoSuscripcion) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.calidad = calidad;
        this.tipoSuscripcion = tipoSuscripcion;
        this.contadorVisitas = 0;
    }
    
    private boolean verificarAcceso() {
        if (tipoSuscripcion.equals("free") && calidad.equals("4K")) {
            return false;
        } else if (tipoSuscripcion.equals("free") && contadorVisitas >= 3) {
            return false;
        } else if (tipoSuscripcion.equals("básico") && calidad.equals("4K")) {
            return false;
        } else {
            return true;
        }
    }
    
    @Override
    public void reproducir() {
        if (verificarAcceso()) {
            if (contenidoReal == null) {
                contenidoReal = new ContenidoReal(titulo, duracion, calidad);
            }
            
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String horaActual = ahora.format(formatter);
            System.out.println("[" + horaActual + "] Acceso registrado para análisis de uso");
            
            contadorVisitas++;
            contenidoReal.reproducir();
        } else {
            String calidadPermitida = tipoSuscripcion.equals("básico") ? "HD" : "SD";
            System.out.println("Acceso denegado a '" + titulo + "' en calidad " + calidad);
            System.out.println("Actualiza tu suscripción o elige calidad " + calidadPermitida);
        }
    }
    
    @Override
    public String obtenerDetalles() {
        System.out.println("Consulta de detalles por usuario con plan " + tipoSuscripcion);
        
        if (contenidoReal == null) {
            contenidoReal = new ContenidoReal(titulo, duracion, calidad);
        }
        
        return contenidoReal.obtenerDetalles();
    }
}

public class ProxyDemo {
    public static void main(String[] args) {
        System.out.println("=== USUARIO CON PLAN FREE ===");
        ProxyContenido pelicula1 = new ProxyContenido("El Código Enigma", 114, "4K", "free");
        pelicula1.reproducir();
        System.out.println("\nDetalles:");
        System.out.println(pelicula1.obtenerDetalles());
        
        System.out.println("\n=== USUARIO CON PLAN PREMIUM ===");
        ProxyContenido pelicula2 = new ProxyContenido("El Código Enigma", 114, "4K", "premium");
        pelicula2.reproducir();
        
        System.out.println("\n=== LÍMITE DE REPRODUCCIONES (PLAN FREE) ===");
        ProxyContenido documental = new ProxyContenido("Vida Salvaje", 45, "HD", "free");
        for (int i = 0; i < 4; i++) {
            System.out.println("\nIntento " + (i+1) + ":");
            documental.reproducir();
        }
    }
}
