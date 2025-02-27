from abc import ABC, abstractmethod

class Observer(ABC):
    @abstractmethod
    def update(self, serie, episode):
        pass

class Serie:
    def __init__(self, name):
        self.name = name
        self._observers = []

    def add_observer(self, observer):
        self._observers.append(observer)

    def remove_observer(self, observer):
        self._observers.remove(observer)

    def notify_observers(self, episode):
        for observer in self._observers:
            observer.update(self, episode)

    def new_episode(self, episode):
        print(f"¡Nuevo episodio de {self.name}: {episode}!")
        self.notify_observers(episode)

class Usuario(Observer):
    def __init__(self, name):
        self.name = name

    def update(self, serie, episode):
        print(f"{self.name}: ¡Nuevo episodio de {serie.name} disponible! ({episode})")

class PlataformaStreaming:
    def __init__(self):
        self.series = {}

    def add_serie(self, serie):
        self.series[serie.name] = serie

    def release_episode(self, serie_name, episode):
        if serie_name in self.series:
            self.series[serie_name].new_episode(episode)
        else:
            print(f"Error: La serie '{serie_name}' no existe en la plataforma.")

if __name__ == "__main__":
    plataforma = PlataformaStreaming()

    serie1 = Serie("Breaking Bad")
    serie2 = Serie("Game of Thrones")

    plataforma.add_serie(serie1)
    plataforma.add_serie(serie2)

    usuario1 = Usuario("Alice")
    usuario2 = Usuario("Bob")
    usuario3 = Usuario("Charlie")

    serie1.add_observer(usuario1)
    serie1.add_observer(usuario2)
    serie2.add_observer(usuario3)

    print("=== Lanzamiento de nuevos episodios ===")
    plataforma.release_episode("Breaking Bad", "Episodio 5x01")
    plataforma.release_episode("Game of Thrones", "Episodio 8x03")
    plataforma.release_episode("Stranger Things", "Episodio 4x01")  
