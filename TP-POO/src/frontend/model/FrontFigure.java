package frontend.model;

import backend.model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FrontFigure {

    private Figure figure;


    public FrontFigure(Point topLeft, Point bottomRight, Color fillColor, Color borderColor, double borderWidth){
        //version rectangulo
        figure = new Rectangle(topLeft, bottomRight, fillColor, borderColor, borderWidth);
    }
    //con este tambien usamos el cuadrado

    public FrontFigure(Point centerPoint, double sMayorAxis, double sMinorAxis, Color fillColor, Color borderColor, double borderWidth){
        //version ellipse
        figure = new Ellipse( centerPoint, sMayorAxis, sMinorAxis, fillColor, borderColor, borderWidth);
    }
    //con este tambien usamos el circulo

    //version cuadrado
//    public FrontFigure(Point topLeft, double size, Color fillColor, Color borderColor, double borderWidth){
//        figure = new Square(topLeft, size, fillColor, borderColor, borderWidth);
//    }
//
//    //version circulo
//    public FrontFigure(Point centerPoint, double radius, Color fillColor, Color borderColor, double borderWidth){
//        figure = new Circle( centerPoint, radius, fillColor, borderColor, borderWidth);
//    }

    public void move(double diffX, double diffY)
    {
        figure.move(diffX, diffY);
    }

    public void draw(GraphicsContext gc)
    {
        figure.draw(gc);
    }

    //para saber si selecciono una figura

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

    public Figure copy()
    {
        return figure.copy();
    }

    @Override
    public String toString() {
        return figure.toString();
    }
}
