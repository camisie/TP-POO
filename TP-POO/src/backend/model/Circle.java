package backend.model;

import javafx.scene.paint.Color;

public class Circle extends Ellipse {

    public Circle(Point centerPoint, double radius, Color fillColor, Color borderColor, double borderWidth) {
        super(centerPoint, radius * 2, radius * 2, fillColor, borderColor, borderWidth);
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", centerPoint, getRadius());
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getRadius() {
        return getsMayorAxis();
    }

}
