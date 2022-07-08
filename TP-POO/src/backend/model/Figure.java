package backend.model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Figure{

    protected Point[] points;

    protected double sMayorAxis;
    protected double sMinorAxis;

    private Color fillColor;
    private Color borderColor;
    private double borderWidth;

    protected Figure(Point[] points, double sMayorAxis, double sMinorAxis, Color fillColor, Color borderColor, double borderWidth) {
        this.points = points;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    public Point[] getPoints(){
        return points;
    }

    public double getsMayorAxis(){
        return sMayorAxis;
    }

    public double getsMinorAxis(){
        return sMinorAxis;
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

    public abstract boolean belongs(Point eventPoint);

    public abstract void zoomIn(int amount );

    public abstract void zoomOut(int amount );

    public abstract Figure copy();

}
