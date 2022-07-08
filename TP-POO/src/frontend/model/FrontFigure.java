package frontend.model;
import backend.model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class FrontFigure {

    protected Figure figure;

    public Color getFillColor() {
        return figure.getFillColor();
    }

    public Color getBorderColor() {
        return figure.getBorderColor();
    }

    public double getBorderWidth() {
        return figure.getBorderWidth();
    }

    public void setFillColor(Color fillColor) {
        figure.setFillColor(fillColor);
    }

    public void setBorderColor(Color borderColor) {
        figure.setBorderColor(borderColor);
    }

    public void setBorderWidth(double borderWidth) {
        figure.setBorderWidth(borderWidth);
    }

    public void move(double diffX, double diffY)
    {
        figure.move(diffX, diffY);
    }

    public void draw(GraphicsContext gc)
    {
        figure.draw(gc);
    }

    public boolean belongs(Point eventPoint)
    {
        return figure.belongs(eventPoint);
    }

    public void zoomIn(int amount )
    {
        figure.zoomIn(amount);
    }

    public void zoomOut(int amount )
    {
        figure.zoomOut(amount);
    }

    public abstract FrontFigure copy();

    @Override
    public String toString() {
        return figure.toString();
    }
}
