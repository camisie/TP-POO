package frontend.model;

import backend.model.Point;
import backend.model.Square;
import javafx.scene.paint.Color;

public class FrontSquare extends FrontFigure{

    public FrontSquare(Point topLeft, double size, Color fillColor, Color borderColor, double borderWidth)
    {
        figure = new Square(topLeft,size, fillColor, borderColor, borderWidth);
    }

    @Override
    public FrontFigure copy() {
        double size = figure.getPoints()[1].x - figure.getPoints()[0].x;
        return new FrontSquare(figure.getPoints()[0],size,getFillColor(),getBorderColor(),getBorderWidth());
    }
}
