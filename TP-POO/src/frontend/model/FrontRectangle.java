package frontend.model;
import backend.model.Point;
import backend.model.Rectangle;
import javafx.scene.paint.Color;

public class FrontRectangle extends FrontFigure {

    public FrontRectangle(Point topLeft, Point bottomRight, Color fillColor, Color borderColor, double borderWidth) {
        figure = new Rectangle(topLeft, bottomRight, fillColor, borderColor, borderWidth);
    }

    @Override
    public FrontFigure copy() {
        return new FrontRectangle(figure.getPoints()[0].copy(), figure.getPoints()[1].copy(), figure.getFillColor(), figure.getBorderColor(), figure.getBorderWidth());
    }
}
