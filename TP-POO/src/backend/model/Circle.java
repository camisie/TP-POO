package backend.model;
import javafx.scene.paint.Color;

public class Circle extends Ellipse {
    // hereda comportamiento de la elipse
    public Circle(Point centerPoint, double radius, Color fillColor, Color borderColor, double borderWidth) {
        super(centerPoint, radius * 2, radius * 2, fillColor, borderColor, borderWidth);
    }

    @Override
    public String toString() {
        return "Círculo";
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

}
