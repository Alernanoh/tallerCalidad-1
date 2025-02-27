import java.util.HashMap;
import java.util.Map;

class FontProperties {
    private String family;
    private int size;
    private String style;
    private String color;

    public FontProperties(String family, int size, String style, String color) {
        this.family = family;
        this.size = size;
        this.style = style;
        this.color = color;
        System.out.println("Creando nueva configuración de fuente: " + family + ", " + size + "pt, " + style + ", " + color);
    }

    public void render(char character, int x, int y) {
        System.out.println("Renderizando '" + character + "' en (" + x + ", " + y + ") con fuente " + family + " " + size + "pt " + style + ", color " + color);
    }
}

class FontFactory {
    private static Map<String, FontProperties> fonts = new HashMap<>();

    public static FontProperties getFont(String family, int size, String style, String color) {
        String key = family + "-" + size + "-" + style + "-" + color;

        if (!fonts.containsKey(key)) {
            fonts.put(key, new FontProperties(family, size, style, color));
        }

        return fonts.get(key);
    }

    public static int getTotalFontsCreated() {
        return fonts.size();
    }
}

class Character {
    private char character;
    private FontProperties font;
    private int x;
    private int y;

    public Character(char character, FontProperties font, int x, int y) {
        this.character = character;
        this.font = font;
        this.x = x;
        this.y = y;
    }

    public void draw() {
        font.render(character, x, y);
    }
}

class TextDocument {
    private java.util.List<Character> characters = new java.util.ArrayList<>();

    public void addCharacter(char character, FontProperties font, int x, int y) {
        characters.add(new Character(character, font, x, y));
    }

    public void renderDocument() {
        System.out.println("\n=== RENDERIZANDO DOCUMENTO ===");
        for (Character character : characters) {
            character.draw();
        }
    }

    public int getTotalCharacters() {
        return characters.size();
    }
}

public class FlyweightPatternDemo {
    public static void main(String[] args) {
        TextDocument document = new TextDocument();

        FontProperties normalFont = FontFactory.getFont("Arial", 12, "normal", "negro");
        FontProperties titleFont = FontFactory.getFont("Times New Roman", 18, "negrita", "azul");
        FontProperties emphasisFont = FontFactory.getFont("Arial", 12, "cursiva", "negro");
        FontProperties codeFont = FontFactory.getFont("Courier New", 10, "normal", "gris");

        System.out.println("\n=== AÑADIENDO CARACTERES AL DOCUMENTO ===");

        String title = "DOCUMENTO DE EJEMPLO";
        for (int i = 0; i < title.length(); i++) {
            document.addCharacter(title.charAt(i), titleFont, 10 + i * 10, 10);
        }

        String text = "Este es un ejemplo del patrón Flyweight.";
        for (int i = 0; i < text.length(); i++) {
            document.addCharacter(text.charAt(i), normalFont, 10 + i * 8, 40);
        }

        String emphasisWord = "optimiza";
        for (int i = 0; i < emphasisWord.length(); i++) {
            document.addCharacter(emphasisWord.charAt(i), emphasisFont, 10 + i * 8, 60);
        }

        String code = "class Ejemplo: pass";
        for (int i = 0; i < code.length(); i++) {
            document.addCharacter(code.charAt(i), codeFont, 10 + i * 8, 80);
        }

        System.out.println("\n=== REUTILIZANDO FUENTES EXISTENTES ===");
        FontProperties normalFontReuse = FontFactory.getFont("Arial", 12, "normal", "negro");
        String reuseText = "La memoria se ahorra.";
        for (int i = 0; i < reuseText.length(); i++) {
            document.addCharacter(reuseText.charAt(i), normalFontReuse, 10 + i * 8, 100);
        }

        document.renderDocument();

        System.out.println("\n=== ESTADÍSTICAS DE MEMORIA ===");
        System.out.println("Número total de caracteres: " + document.getTotalCharacters());
        System.out.println("Número de objetos fuente creados: " + FontFactory.getTotalFontsCreated());
        System.out.printf("Ratio de compartición: %.2f caracteres por fuente\n", (double) document.getTotalCharacters() / FontFactory.getTotalFontsCreated());
    }
}
