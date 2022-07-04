package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CanvasState {

    private final List<Figure> figures = new ArrayList<>();

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public void deleteFigure(Figure figure) {
        figures.remove(figure);
    }

    private final Set<Figure> selectedFigure = new HashSet<>();

    public void addSelectedFigure(Figure figure) {
        selectedFigure.add(figure);
    }

    public void removeSelectedFigure(Figure figure) {
        selectedFigure.remove(figure);
    }

    private boolean hasSelectedFigure() {
        return !selectedFigure.isEmpty();
    }

    public Iterable<Figure> figures() {
        return new ArrayList<>(figures);
    }

    public void moveSelectedFigure(double diffX, double diffY){
        for (Figure figure : selectedFigure) {
            figure.move(diffX, diffY);
        }
//        figures.forEach(figure -> figure.move(diffX,diffY)); aca muevo todas las figuras a la vez y no es lo que quiero
    }

}
