package backend;

import backend.model.Figure;
import java.util.ArrayList;
import java.util.List;

public class CanvasState {

    private final List<Figure> figures = new ArrayList<>();

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public void deleteFigure(Figure figure) {
        figures.remove(figure);
    }

    public Iterable<Figure> figures() {
        return new ArrayList<>(figures);
    }

}
