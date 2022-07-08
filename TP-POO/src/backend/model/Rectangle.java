package backend.model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Figure {

    private final Point topLeft, bottomRight;

    public Rectangle(Point topLeft, Point bottomRight, Color fillColor, Color borderColor, double borderWidth) {
        super(new Point[]{topLeft, bottomRight}, 0, 0, fillColor, borderColor, borderWidth);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return "Rectángulo";
    }

    @Override
    public void move(double diffX, double diffY){
        getTopLeft().x += diffX;
        getBottomRight().x += diffX;
        getTopLeft().y += diffY;
        getBottomRight().y += diffY;
    }

    @Override
    public  void draw(GraphicsContext gc) {
        gc.fillRect(getTopLeft().getX(), getTopLeft().getY(),
                Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
        gc.strokeRect(getTopLeft().getX(), getTopLeft().getY(),
                Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
    }

    @Override
    public boolean belongs(Point eventPoint) {
        return eventPoint.getX() > getTopLeft().getX() && eventPoint.getX() < getBottomRight().getX() && eventPoint.getY() > getTopLeft().getY() && eventPoint.getY() < getBottomRight().getY();
    }

    private double deltaX( int amount ) {                                    // 100% returns deltaX
        double deltaX = Math.abs( getBottomRight().x - getTopLeft().x );
        return (deltaX/2) * (((double)amount)/100);
    }

    private double deltaY( int amount ) {                                    //100% returns deltaY
        double deltaY = Math.abs( getBottomRight().y - getTopLeft().y );
        return (deltaY/2) * (((double)amount)/100);                         //amount es un porcentaje entero
    }

    @Override
    public void zoomIn(int amount) {
        getTopLeft().x -= deltaX(amount);
        getBottomRight().x += deltaX(amount);
        getTopLeft().y -= deltaY(amount);
        getBottomRight().y += deltaY(amount);
    }

    @Override
    public void zoomOut(int amount) {
        getTopLeft().x += deltaX(amount);
        getBottomRight().x -= deltaX(amount);
        getTopLeft().y += deltaY(amount);
        getBottomRight().y -= deltaY(amount);
    }
    @Override
    public Figure copy() {
        return new Rectangle(topLeft.copy(), bottomRight.copy(), getFillColor(), getBorderColor(), getBorderWidth());
    }

}
