package frontend.model;
import backend.model.Point;
import backend.model.Rectangle;
import javafx.scene.paint.Color;

public class FrontRectangle extends FrontFigure {

    public FrontRectangle(Point topLeft, Point bottomRight, Color fillColor, Color borderColor, double borderWidth){
        // version rectangulo y cuadrado
        figure = new Rectangle(topLeft, bottomRight, fillColor, borderColor, borderWidth);
    }

    @Override
    public FrontFigure copy() {
        return new FrontRectangle(figure.getPoints()[0], figure.getPoints()[1], figure.getFillColor(), figure.getBorderColor(), figure.getBorderWidth());
    }
}
