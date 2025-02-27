import java.util.*;

interface Observer {
    void update(Serie serie, String episode);
}

class Serie {
    private String name;
    private List<Observer> observers;

    public Serie(String name) {
        this.name = name;
        this.observers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String episode) {
        for (Observer observer : observers) {
            observer.update(this, episode);
        }
    }

    public void newEpisode(String episode) {
        System.out.println("¡Nuevo episodio de " + name + ": " + episode + "!");
        notifyObservers(episode);
    }
}

class Usuario implements Observer {
    private String name;

    public Usuario(String name) {
        this.name = name;
    }

    @Override
    public void update(Serie serie, String episode) {
        System.out.println(name + ": ¡Nuevo episodio de " + serie.getName() + " disponible! (" + episode + ")");
    }
}

class PlataformaStreaming {
    private Map<String, Serie> series;

    public PlataformaStreaming() {
        this.series = new HashMap<>();
    }

    public void addSerie(Serie serie) {
        series.put(serie.getName(), serie);
    }

    public void releaseEpisode(String serieName, String episode) {
        if (series.containsKey(serieName)) {
            series.get(serieName).newEpisode(episode);
        } else {
            System.out.println("Error: La serie '" + serieName + "' no existe en la plataforma.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        PlataformaStreaming plataforma = new PlataformaStreaming();

        Serie serie1 = new Serie("Breaking Bad");
        Serie serie2 = new Serie("Game of Thrones");

        plataforma.addSerie(serie1);
        plataforma.addSerie(serie2);

        Usuario usuario1 = new Usuario("Alice");
        Usuario usuario2 = new Usuario("Bob");
        Usuario usuario3 = new Usuario("Charlie");

        serie1.addObserver(usuario1);
        serie1.addObserver(usuario2);
        serie2.addObserver(usuario3);

        System.out.println("=== Lanzamiento de nuevos episodios ===");
        plataforma.releaseEpisode("Breaking Bad", "Episodio 5x01");
        plataforma.releaseEpisode("Game of Thrones", "Episodio 8x03");
        plataforma.releaseEpisode("Stranger Things", "Episodio 4x01");  
    }
}
