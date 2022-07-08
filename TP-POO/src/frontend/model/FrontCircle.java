package frontend.model;

import backend.model.Circle;
import backend.model.Point;
import javafx.scene.paint.Color;

public class FrontCircle extends FrontFigure{

    public FrontCircle(Point centerPoint, double radius, Color fillColor, Color borderColor, double borderWidth)
    {
        figure = new Circle(centerPoint, radius, fillColor, borderColor, borderWidth);
    }

    @Override
    public FrontFigure copy() {
        return new FrontCircle(figure.getPoints()[0],figure.getsMayorAxis()/2,getFillColor(),getBorderColor(),getBorderWidth() );
    }
}
