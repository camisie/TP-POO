package frontend;

//import backend.model.FrontFigure;
import frontend.model.FrontFigure;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class CanvasState {

    private final List<FrontFigure> figures = new ArrayList<>();

    public void addFigure(FrontFigure figure) {
        figures.add(figure);
    }

    public void deleteFigure(FrontFigure figure) {
        figures.remove(figure);
    }

    public Iterable<FrontFigure> figures() {
        return new ArrayList<>(figures);
    }


    public List<FrontFigure> copyState()
    {
        List<FrontFigure> toReturn = new ArrayList<>();
        for (FrontFigure figure : figures){
            toReturn.add(figure.copy());
//            toReturn.add(figure);
        }
        return toReturn;
    }

    public void setState( List<FrontFigure> toSet )
    {
        figures.clear();
        figures.addAll(toSet);
    }
}
