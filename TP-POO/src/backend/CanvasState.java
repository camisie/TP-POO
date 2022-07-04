package backend;

import backend.model.Figure;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class CanvasState {

//    private final List<Figure> figures = new ArrayList<>();
    private final Stack<Figure> figures = new Stack<>();

    private final Stack<Figure> redoes = new Stack<>();

    public void addFigure(Figure figure) {
        figures.add(figure);
        redoes.clear();
    }

    public void deleteFigure(Figure figure) {
        figures.remove(figure);
        redoes.add(figure);
    }

    public Iterable<Figure> figures() {
        return new ArrayList<>(figures);
    }


    public void undo()
    {
        redoes.add(figures.pop());
    }

    public void redo()
    {
        figures.add(redoes.pop());
    }
}
