package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

//import java.util.Stack;

//lo que las figuras tienen en comun -> se tienen que poder mover, seleccionar, colorear, dibujar
public abstract class Figure{

    private static final Double DEFAULT_WIDTH = 1.0;
    private Color fillColor = Color.YELLOW;
    private Color borderColor = Color.BLACK;

    private double borderWidth = DEFAULT_WIDTH;

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

    public double getBorderWidth() {
        return borderWidth;
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

    public abstract void move(double diffX, double diffY);

    public abstract void draw(GraphicsContext gc);

    //para saber si selecciono una figura

    public abstract boolean belongs(Point eventPoint);

    public abstract void zoomIn(int amount );

    public abstract void zoomOut(int amount );

//    public abstract Figure zoomIn(int amount );
//
//    public abstract Figure zoomOut(int amount );

//    public abstract boolean undo();


}
