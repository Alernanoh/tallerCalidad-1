import java.util.*;

class Tarea {
    private String nombre;
    private String descripcion;

    public Tarea(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return nombre + ": " + descripcion;
    }
}

class CategoriaTareas implements Iterable<Tarea> {
    private String nombre;
    private List<Tarea> tareas;

    public CategoriaTareas(String nombre) {
        this.nombre = nombre;
        this.tareas = new ArrayList<>();
    }

    public void agregarTarea(Tarea tarea) {
        tareas.add(tarea);
    }

    @Override
    public Iterator<Tarea> iterator() {
        return new IteradorCategoria(tareas);
    }
}

class IteradorCategoria implements Iterator<Tarea> {
    private List<Tarea> tareas;
    private int index;

    public IteradorCategoria(List<Tarea> tareas) {
        this.tareas = tareas;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < tareas.size();
    }

    @Override
    public Tarea next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return tareas.get(index++);
    }
}

class Proyecto implements Iterable<Tarea> {
    private String nombre;
    private List<CategoriaTareas> categorias;

    public Proyecto(String nombre) {
        this.nombre = nombre;
        this.categorias = new ArrayList<>();
    }

    public void agregarCategoria(CategoriaTareas categoria) {
        categorias.add(categoria);
    }

    @Override
    public Iterator<Tarea> iterator() {
        return new IteradorProyecto(categorias);
    }
}

class IteradorProyecto implements Iterator<Tarea> {
    private List<CategoriaTareas> categorias;
    private int categoriaIndex;
    private Iterator<Tarea> tareaIterador;

    public IteradorProyecto(List<CategoriaTareas> categorias) {
        this.categorias = categorias;
        this.categoriaIndex = 0;
        this.tareaIterador = categorias.isEmpty() ? Collections.emptyIterator() : categorias.get(0).iterator();
    }

    @Override
    public boolean hasNext() {
        while (!tareaIterador.hasNext() && categoriaIndex < categorias.size() - 1) {
            categoriaIndex++;
            tareaIterador = categorias.get(categoriaIndex).iterator();
        }
        return tareaIterador.hasNext();
    }

    @Override
    public Tarea next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return tareaIterador.next();
    }
}

public class Main {
    public static void main(String[] args) {
        Proyecto proyecto = new Proyecto("Desarrollo de Software");

        CategoriaTareas pendientes = new CategoriaTareas("Pendientes");
        CategoriaTareas enProgreso = new CategoriaTareas("En Progreso");
        CategoriaTareas completadas = new CategoriaTareas("Completadas");

        pendientes.agregarTarea(new Tarea("Diseñar interfaz", "Diseñar la interfaz de usuario"));
        pendientes.agregarTarea(new Tarea("Configurar servidor", "Configurar el servidor de desarrollo"));
        enProgreso.agregarTarea(new Tarea("Implementar API", "Implementar la API del backend"));
        enProgreso.agregarTarea(new Tarea("Escribir pruebas", "Escribir pruebas unitarias"));
        completadas.agregarTarea(new Tarea("Revisar requisitos", "Revisar y documentar los requisitos del proyecto"));

        proyecto.agregarCategoria(pendientes);
        proyecto.agregarCategoria(enProgreso);
        proyecto.agregarCategoria(completadas);

        System.out.println("=== Todas las tareas del proyecto ===");
        for (Tarea tarea : proyecto) {
            System.out.println(tarea);
        }

        System.out.println("\n=== Tareas en progreso ===");
        for (Tarea tarea : enProgreso) {
            System.out.println(tarea);
        }
    }
}
