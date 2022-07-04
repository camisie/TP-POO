package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Stack;

//lo que las figuras tienen en comun -> se tienen que poder mover, seleccionar, colorear, dibujar
public abstract class Figure{
    private Color fillColor;
    private Color borderColor;
    private double borderWidth;
//    protected Stack<Figure> history = new Stack<>();

    protected Point[] points;

    protected Figure(Point[] points) {
        this.points = points;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

//    public void setFillColor(Color fillColor) {
//        this.fillColor = fillColor;
//    }
//
//    public void setBorderColor(Color borderColor) {
//        this.borderColor = borderColor;
//    }

    public void setBorderWidth(double borderWidth) {
        this.borderWidth = borderWidth;
    }

    public abstract void move(double diffX, double diffY);

    public abstract void draw(GraphicsContext gc);

    //para saber si selecciono una figura

    public abstract boolean belongs(Point eventPoint);

    public abstract Figure zoomIn(int amount );

    public abstract Figure zoomOut(int amount );

//    public abstract boolean undo();


}
