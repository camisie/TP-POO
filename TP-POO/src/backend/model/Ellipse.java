package backend.model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ellipse extends Figure {

    protected Point centerPoint;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis, Color fillColor, Color borderColor, double borderWidth) {
        super(new Point[]{centerPoint}, sMayorAxis, sMinorAxis,fillColor, borderColor, borderWidth);
        this.centerPoint = centerPoint;
    }

    @Override
    public String toString() {
        return "Elipse";
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    @Override
    public void move(double diffX, double diffY) {
        getCenterPoint().x += diffX;
        getCenterPoint().y += diffY;
    }

    @Override
    public void draw(GraphicsContext gc) {
        //primero dibujamos el borde como la figura completa, con los colores del super
        gc.strokeOval(getCenterPoint().getX() - (getsMayorAxis() / 2), getCenterPoint().getY() - (getsMinorAxis() / 2), getsMayorAxis(), getsMinorAxis());
        gc.fillOval(getCenterPoint().getX() - (getsMayorAxis() / 2), getCenterPoint().getY() - (getsMinorAxis() / 2), getsMayorAxis(), getsMinorAxis());
        //dibujamos una segunda figura con dimension igual - borderWidth

    }

    @Override
    public boolean belongs(Point eventPoint) {
        return ((Math.pow(eventPoint.getX() - getCenterPoint().getX(), 2) / Math.pow(getsMayorAxis(), 2)) +
                (Math.pow(eventPoint.getY() - getCenterPoint().getY(), 2) / Math.pow(getsMinorAxis(), 2))) <= 0.30;
    }

    @Override
    public void zoomIn(int amount) {
        sMayorAxis += getsMayorAxis() * (double)amount/100;
        sMinorAxis += getsMinorAxis() * (double)amount/100;
    }

    @Override
    public void zoomOut(int amount) {
        sMayorAxis -= getsMayorAxis() * (double)amount/100;
        sMinorAxis -= getsMinorAxis() * (double)amount/100;
    }

    @Override
    public Figure copy() {
        return new Ellipse(centerPoint, sMayorAxis, sMinorAxis, getFillColor(), getBorderColor(), getBorderWidth());
    }
}
