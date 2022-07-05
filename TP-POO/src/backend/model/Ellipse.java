package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Ellipse extends Figure {

    protected Point centerPoint;
    private double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        super(new Point[]{centerPoint});
        this.centerPoint = centerPoint;
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", centerPoint, sMayorAxis, sMinorAxis);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getsMayorAxis() {
        return sMayorAxis;
    }

    public double getsMinorAxis() {
        return sMinorAxis;
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

    //    @Override
//    public Figure zoomIn(int amount) {
//
//        Ellipse newEllipse = new Ellipse(getCenterPoint(), getsMayorAxis(), getsMinorAxis());
//
//        newEllipse.sMayorAxis += getsMayorAxis() * (double)amount/100;
//        newEllipse.sMinorAxis += getsMinorAxis() * (double)amount/100;
//
//        return newEllipse;
//    }
//
//    @Override
//    public Figure zoomOut(int amount) {
//
//        Ellipse newEllipse = new Ellipse(getCenterPoint(),getsMayorAxis(),getsMinorAxis());
//
//        newEllipse.sMayorAxis -= getsMayorAxis() * (double)amount/100;
//        newEllipse.sMinorAxis -= getsMinorAxis() * (double)amount/100;
//
//        return newEllipse;
//    }

//    @Override
//    public boolean undo() {
//        if ( history.isEmpty() )
//            return false;
//
//        Figure toCopy = history.pop();
//        this.sMinorAxis = toCopy.points[1];
//        this.sMayorAxis
//        return true;
//    }
}
