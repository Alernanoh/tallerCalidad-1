class Computer {
    private final List<Component> components = new ArrayList<>();
    
    public void add(Component component) {
        components.add(component);
    }
    
    public double getTotalPrice() {
        double total = 0;
        for (Component component : components) {
            total += component.getPrice();
        }
        return total;
    }
    
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("Computadora: \n");
        for (Component component : components) {
            text.append(component.toString()).append("\n");
        }
        text.append(String.format("Precio total: $%.2f", getTotalPrice()));
        return text.toString();
    }
}

class Component {
    private final String name;
    private final double price;
    
    public Component(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public double getPrice() {
        return price;
    }
    
    @Override
    public String toString() {
        return String.format("%s: $%.2f", name, price);
    }
}

abstract class ComputerBuilder {
    protected Computer computer = new Computer();
    
    public abstract void addProcessor();
    public abstract void addMotherboard();
    public abstract void addMemory();
    public abstract void addStorage();
    public abstract void addGraphicsCard();
    public abstract void addPowerSupply();
    
    public void reset() {
        computer = new Computer();
    }
    
    public Computer getComputer() {
        return computer;
    }
}

class GamingComputerBuilder extends ComputerBuilder {
    @Override
    public void addProcessor() {
        computer.add(new Component("AMD Ryzen 9 5900X", 499.99));
    }
    
    @Override
    public void addMotherboard() {
        computer.add(new Component("ASUS ROG Strix X570-E Gaming", 329.99));
    }
    
    @Override
    public void addMemory() {
        computer.add(new Component("32GB DDR4 3600MHz RGB", 189.99));
    }
    
    @Override
    public void addStorage() {
        computer.add(new Component("2TB NVMe SSD", 249.99));
    }
    
    @Override
    public void addGraphicsCard() {
        computer.add(new Component("NVIDIA RTX 4080", 899.99));
    }
    
    @Override
    public void addPowerSupply() {
        computer.add(new Component("850W 80+ Gold", 149.99));
    }
}

class OfficeComputerBuilder extends ComputerBuilder {
    @Override
    public void addProcessor() {
        computer.add(new Component("Intel Core i5-12400", 199.99));
    }
    
    @Override
    public void addMotherboard() {
        computer.add(new Component("ASUS Prime B660M-A", 129.99));
    }
    
    @Override
    public void addMemory() {
        computer.add(new Component("16GB DDR4 3200MHz", 79.99));
    }
    
    @Override
    public void addStorage() {
        computer.add(new Component("512GB NVMe SSD", 89.99));
    }
    
    @Override
    public void addGraphicsCard() {
        computer.add(new Component("Intel UHD Graphics 730 (Integrada)", 0.00));
    }
    
    @Override
    public void addPowerSupply() {
        computer.add(new Component("550W 80+ Bronze", 69.99));
    }
}

class ComputerDirector {
    private final ComputerBuilder builder;
    
    public ComputerDirector(ComputerBuilder builder) {
        this.builder = builder;
    }
    
    public void constructFullComputer() {
        builder.reset();
        builder.addProcessor();
        builder.addMotherboard();
        builder.addMemory();
        builder.addStorage();
        builder.addGraphicsCard();
        builder.addPowerSupply();
    }
    
    public void constructBasicComputer() {
        builder.reset();
        builder.addProcessor();
        builder.addMotherboard();
        builder.addMemory();
        builder.addStorage();
    }
}

public class ComputerBuilderDemo {
    public static void main(String[] args) {
        GamingComputerBuilder gamingBuilder = new GamingComputerBuilder();
        ComputerDirector gamingDirector = new ComputerDirector(gamingBuilder);
        gamingDirector.constructFullComputer();
        Computer gamingComputer = gamingBuilder.getComputer();
        System.out.println("=== Computadora Gaming ===");
        System.out.println(gamingComputer);
        System.out.println();
        
        OfficeComputerBuilder officeBuilder = new OfficeComputerBuilder();
        ComputerDirector officeDirector = new ComputerDirector(officeBuilder);
        officeDirector.constructBasicComputer();
        Computer officeComputer = officeBuilder.getComputer();
        System.out.println("=== Computadora de Oficina ===");
        System.out.println(officeComputer);
    }
}
