package backend.model;
import javafx.scene.paint.Color;

public class Square extends Rectangle {
    //hereda comportamiento del rectangulo
    public Square(Point topLeft, double size, Color fillColor, Color borderColor, double borderWidth) {
        super(topLeft, new Point(topLeft.getX() + size, topLeft.getY() + size), fillColor, borderColor, borderWidth);
    }

    @Override
    public String toString() {
        return "Cuadrado";
    }
}
