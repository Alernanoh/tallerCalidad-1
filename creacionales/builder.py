class Computer:
    def __init__(self):
        self.components = []
        
    def add(self, component):
        self.components.append(component)
        
    def get_total_price(self):
        return sum(component.get_price() for component in self.components)
        
    def __str__(self):
        text = "Computadora: \n"
        text += "\n".join(str(component) for component in self.components)
        text += f"\nPrecio total: ${self.get_total_price():.2f}"
        return text
        
class Component:
    def __init__(self, name, price):
        self.name = name
        self.price = price
        
    def get_price(self):
        return self.price
        
    def __str__(self):
        return f"{self.name}: ${self.price:.2f}"
        
class ComputerBuilder:
    def __init__(self):
        self.computer = Computer()
        
    def add_processor(self):
        pass
        
    def add_motherboard(self):
        pass
        
    def add_memory(self):
        pass
        
    def add_storage(self):
        pass
        
    def add_graphics_card(self):
        pass
        
    def add_power_supply(self):
        pass
        
    def reset(self):
        self.computer = Computer()
        
    def get_computer(self):
        return self.computer
        
class GamingComputerBuilder(ComputerBuilder):
    def add_processor(self):
        self.computer.add(Component("AMD Ryzen 9 5900X", 499.99))
        
    def add_motherboard(self):
        self.computer.add(Component("ASUS ROG Strix X570-E Gaming", 329.99))
        
    def add_memory(self):
        self.computer.add(Component("32GB DDR4 3600MHz RGB", 189.99))
        
    def add_storage(self):
        self.computer.add(Component("2TB NVMe SSD", 249.99))
        
    def add_graphics_card(self):
        self.computer.add(Component("NVIDIA RTX 4080", 899.99))
        
    def add_power_supply(self):
        self.computer.add(Component("850W 80+ Gold", 149.99))
        
class OfficeComputerBuilder(ComputerBuilder):
    def add_processor(self):
        self.computer.add(Component("Intel Core i5-12400", 199.99))
        
    def add_motherboard(self):
        self.computer.add(Component("ASUS Prime B660M-A", 129.99))
        
    def add_memory(self):
        self.computer.add(Component("16GB DDR4 3200MHz", 79.99))
        
    def add_storage(self):
        self.computer.add(Component("512GB NVMe SSD", 89.99))
        
    def add_graphics_card(self):
        self.computer.add(Component("Intel UHD Graphics 730 (Integrada)", 0.00))
        
    def add_power_supply(self):
        self.computer.add(Component("550W 80+ Bronze", 69.99))
        
class ComputerDirector:
    def __init__(self, builder):
        self.builder = builder
        
    def construct_full_computer(self):
        self.builder.reset()
        self.builder.add_processor()
        self.builder.add_motherboard()
        self.builder.add_memory()
        self.builder.add_storage()
        self.builder.add_graphics_card()
        self.builder.add_power_supply()
        
    def construct_basic_computer(self):
        self.builder.reset()
        self.builder.add_processor()
        self.builder.add_motherboard()
        self.builder.add_memory()
        self.builder.add_storage()
        
# Creación de una computadora para gaming
gaming_builder = GamingComputerBuilder()
gaming_director = ComputerDirector(gaming_builder)
gaming_director.construct_full_computer()
gaming_computer = gaming_builder.get_computer()
print("=== Computadora Gaming ===")
print(gaming_computer)
print()

# Creación de una computadora para oficina
office_builder = OfficeComputerBuilder()
office_director = ComputerDirector(office_builder)
office_director.construct_basic_computer()
office_computer = office_builder.get_computer()
print("=== Computadora de Oficina ===")
print(office_computer)
