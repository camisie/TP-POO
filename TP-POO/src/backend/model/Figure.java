package backend.model;

import javafx.scene.paint.Color;
//lo que las figuras tienen en comun -> se tienen que poder mover, seleccionar, colorear, dibujar
public abstract class Figure implements Movable {
    private Color fillColor;
    private Color borderColor;
    private double borderWidth;

    private Point[] points;

    protected Figure(Point[] points) {
        this.points = points;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void setBorderWidth(double borderWidth) {
        this.borderWidth = borderWidth;
    }

    @Override
    public void move(double diffX, double diffY) {
        for (Point p : points) {
            p.move(diffX, diffY);
        }

    }

}
