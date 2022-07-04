package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Rectangle extends Figure {

    private final Point topLeft, bottomRight;

    //faltan validaciones por si el usuario dibuja mal?
    public Rectangle(Point topLeft, Point bottomRight) {
        super(new Point[]{topLeft, bottomRight});
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
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

    @Override
    public void move(double diffX, double diffY){
        getTopLeft().x += diffX;
        getBottomRight().x += diffX;
        getTopLeft().y += diffY;
        getBottomRight().y += diffY;
    }

    @Override
    public  void draw(GraphicsContext gc){
        gc.fillRect(getTopLeft().getX(), getTopLeft().getY(),
                Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
        gc.strokeRect(getTopLeft().getX(), getTopLeft().getY(),
                Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));

    }

    @Override
    public boolean belongs(Point eventPoint) {
        return eventPoint.getX() > getTopLeft().getX() && eventPoint.getX() < getBottomRight().getX() && eventPoint.getY() > getTopLeft().getY() && eventPoint.getY() < getBottomRight().getY();
    }

    private double deltaX( int amount )  // 100% returns deltaX
    {
        double deltaX = Math.abs( getBottomRight().x - getTopLeft().x );
        double amountX = (deltaX/2) * (((double)amount)/100); //amount es un porcentaje entero
        return amountX;
    }

    private double deltaY( int amount ) //100% returns deltaY
    {
        double deltaY = Math.abs( getBottomRight().y - getTopLeft().y );
        double amountY = (deltaY/2) * (((double)amount)/100); //amount es un porcentaje entero
        return amountY;
    }

    @Override
    public void zoomIn(int amount) {
        double amountX = deltaX(amount);
        double amountY = deltaY(amount);

        getTopLeft().x -= amountX;
        getBottomRight().x += amountX;
        getTopLeft().y -= amountY;
        getBottomRight().y += amountY;
    }
    @Override
    public void zoomOut(int amount) {
        double amountX = deltaX(amount);
        double amountY = deltaY(amount);

        getTopLeft().x += amountX;
        getBottomRight().x -= amountX;
        getTopLeft().y += amountY;
        getBottomRight().y -= amountY;
    }
}
