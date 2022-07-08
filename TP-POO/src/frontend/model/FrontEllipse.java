package frontend.model;
import backend.model.Ellipse;
import backend.model.Point;
import javafx.scene.paint.Color;

public class FrontEllipse extends FrontFigure {

    public FrontEllipse(Point centerPoint, double sMayorAxis, double sMinorAxis, Color fillColor, Color borderColor, double borderWidth){
        // version elipse y circulo
        figure = new Ellipse( centerPoint, sMayorAxis, sMinorAxis, fillColor, borderColor, borderWidth);
    }

    @Override
    public FrontFigure copy() {
        return new FrontEllipse(figure.getPoints()[0], figure.getsMayorAxis(), figure.getsMinorAxis(), figure.getFillColor(), figure.getBorderColor(), figure.getBorderWidth());
    }
}