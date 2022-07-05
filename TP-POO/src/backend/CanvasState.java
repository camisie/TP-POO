package backend;

import backend.model.Figure;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


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


    public List<Figure> copyState()
    {
        List<Figure> toReturn = new ArrayList<>();
        for ( Figure figure : figures )
        {
            toReturn.add(figure.copy());
        }

        return toReturn;
    }

    public void setState( List<Figure> toSet )
    {
        figures.clear();
        figures.addAll(toSet);
    }
}
