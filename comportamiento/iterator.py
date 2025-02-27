from collections.abc import Iterator, Iterable

class Tarea:
    def __init__(self, nombre, descripcion):
        self.nombre = nombre
        self.descripcion = descripcion

    def __str__(self):
        return f"{self.nombre}: {self.descripcion}"

class CategoriaTareas(Iterable):
    def __init__(self, nombre):
        self.nombre = nombre
        self.tareas = []

    def agregar_tarea(self, tarea):
        self.tareas.append(tarea)

    def __iter__(self):
        return IteradorCategoria(self.tareas)

class IteradorCategoria(Iterator):
    def __init__(self, tareas):
        self.tareas = tareas
        self.index = 0

    def __next__(self):
        if self.index >= len(self.tareas):
            raise StopIteration
        tarea = self.tareas[self.index]
        self.index += 1
        return tarea

class Proyecto(Iterable):
    def __init__(self, nombre):
        self.nombre = nombre
        self.categorias = []

    def agregar_categoria(self, categoria):
        self.categorias.append(categoria)

    def __iter__(self):
        return IteradorProyecto(self.categorias)

class IteradorProyecto(Iterator):
    def __init__(self, categorias):
        self.categorias = categorias
        self.categoria_index = 0
        self.tarea_index = 0

    def __next__(self):
        if self.categoria_index >= len(self.categorias):
            raise StopIteration
        categoria_actual = self.categorias[self.categoria_index]
        if self.tarea_index >= len(categoria_actual.tareas):
            self.categoria_index += 1
            self.tarea_index = 0
            return self.__next__()
        tarea = categoria_actual.tareas[self.tarea_index]
        self.tarea_index += 1
        return tarea

if __name__ == "__main__":
    proyecto = Proyecto("Desarrollo de Software")

    pendientes = CategoriaTareas("Pendientes")
    en_progreso = CategoriaTareas("En Progreso")
    completadas = CategoriaTareas("Completadas")

    pendientes.agregar_tarea(Tarea("Diseñar interfaz", "Diseñar la interfaz de usuario"))
    pendientes.agregar_tarea(Tarea("Configurar servidor", "Configurar el servidor de desarrollo"))

    en_progreso.agregar_tarea(Tarea("Implementar API", "Implementar la API del backend"))
    en_progreso.agregar_tarea(Tarea("Escribir pruebas", "Escribir pruebas unitarias"))

    completadas.agregar_tarea(Tarea("Revisar requisitos", "Revisar y documentar los requisitos del proyecto"))

    proyecto.agregar_categoria(pendientes)
    proyecto.agregar_categoria(en_progreso)
    proyecto.agregar_categoria(completadas)

    print("=== Todas las tareas del proyecto ===")
    for tarea in proyecto:
        print(tarea)

    print("\n=== Tareas en progreso ===")
    for tarea in en_progreso:
        print(tarea)
