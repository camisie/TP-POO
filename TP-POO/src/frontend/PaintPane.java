package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Slider;
import javafx.scene.control.ColorPicker;
// adolfo quiere poder contribuir
public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	// Canvas y relacionados
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	Color lineColor = Color.BLACK;
	Color fillColor = Color.YELLOW;

	//select color red?

	// Botones Barra Izquierda
	private final ToggleButton selectionButton = new ToggleButton("Seleccionar");
	private final ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	private final ToggleButton circleButton = new ToggleButton("Círculo");
	private final ToggleButton squareButton = new ToggleButton("Cuadrado");
	private final ToggleButton ellipseButton = new ToggleButton("Elipse");
	private final ToggleButton deleteButton = new ToggleButton("Borrar");

	private final ToggleButton zoomInButton = new ToggleButton("Agrandar");

	private final ToggleButton zoomOutButton = new ToggleButton("Achicar");

	private final Label borderLabel = new Label("Borde");

	private final Slider borderWidthSlider = new Slider(1, 50, 25);

	private final ColorPicker borderColorPicker = new ColorPicker(Color.RED);

	private final Label fillLabel = new Label("Relleno");

	private final ColorPicker fillColorPicker = new ColorPicker(Color.YELLOW);

	//Botones barra superior

	private final Label undoLabel = new Label("un texto que indique cuál será el efecto de presionar el botón");
	//tengo que hacer una coleccion de las cosas que se deshacen

	private final ToggleButton undoButton = new ToggleButton("Deshacer");

	private final Label redoLabel = new Label("un texto que indique cuál será el efecto de presionar el botón");
	//su numero se incrementa cuando se decrementa deshacer

	private final ToggleButton redoButton = new ToggleButton("Rehacer");

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura
	Figure selectedFigure;

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		borderWidthSlider.setShowTickLabels(true);		//COMENTARIO tengo los botones pero todavia no hacen nada, primero corregir muchas cosas de las clases
		borderWidthSlider.setShowTickMarks(true);
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton, zoomInButton, zoomOutButton};
		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}
		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(toolsArr);
		buttonsBox.getChildren().addAll(borderLabel, borderWidthSlider, borderColorPicker, fillLabel, fillColorPicker);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		HBox topButtonsBox = new HBox(10);
		topButtonsBox.getChildren().addAll(undoLabel, undoButton, redoButton, redoLabel);		//COMENTARIO estan los botones, falta funcionalidad
		topButtonsBox.setPadding(new Insets(5));
		topButtonsBox.setStyle("-fx-background-color: #999");
		topButtonsBox.setPrefHeight(40);

		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

		canvas.setOnMouseReleased(event -> {		//aca agrego las funciones que dibuje a la coleccion
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return ;
			}
			Figure newFigure = null;
			if(rectangleButton.isSelected()) {							//aca tambien se repite mucho codigo
				newFigure = new Rectangle(startPoint, endPoint);
			}
			else if(circleButton.isSelected()) {
				double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new Circle(startPoint, circleRadius);
			} else if(squareButton.isSelected()) {
				double size = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new Square(startPoint, size);
			} else if(ellipseButton.isSelected()) {
				Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
				double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
				double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
				newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis);
			}
			else {
				return ;
			}

			canvasState.addFigure(newFigure);
			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figures()) {
				if(figureBelongs(figure, eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			if(found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure figure : canvasState.figures()) {
					if(figureBelongs(figure, eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure); //le borre el to string pq no se si hacia falta
					}
				}
				if (found) {
					statusPane.updateStatus(label.toString());
				} else if (selectedFigure != null) {
					statusPane.updateStatus("Ninguna figura encontrada");
				}

				redrawCanvas();
			}
		});

		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX());
				double diffY = (eventPoint.getY() - startPoint.getY());
				selectedFigure.move(diffX, diffY);
				startPoint = eventPoint;
				redrawCanvas();
			}
		});

		deleteButton.setOnAction(event -> {
			if (selectedFigure != null) {
				canvasState.deleteFigure(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});

		zoomInButton.setOnAction(event -> {
			if(selectedFigure != null) {
				selectedFigure.zoomIn(10);
//				Figure toAdd = selectedFigure.zoomIn(10);
//				canvasState.deleteFigure(selectedFigure);
//				canvasState.addFigure(toAdd);
//				selectedFigure = toAdd;
				redrawCanvas();
			}
		});

		zoomOutButton.setOnAction(event -> {
			if(selectedFigure != null) {
				selectedFigure.zoomOut(10);
//				Figure toAdd = selectedFigure.zoomOut(10);
//				canvasState.deleteFigure(selectedFigure);
//				canvasState.addFigure(toAdd);
//				selectedFigure = toAdd;
				redrawCanvas();
			}
		});

		undoButton.setOnAction(event -> {
			canvasState.undo();
			redrawCanvas();
		});

		redoButton.setOnAction(event -> {
			canvasState.redo();
			redrawCanvas();
		});

		fillColorPicker.setOnAction(event ->{
			if(selectedFigure != null){
				selectedFigure.getFillColor();
			}
		});

		setLeft(buttonsBox);
		setTop(topButtonsBox);
		setRight(canvas);
	}

	void redrawCanvas() {													//COMENTARIO esta funcion asi gigante tambien corregir
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if(figure == selectedFigure) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(lineColor);
			}
			gc.setFill(fillColor);
			figure.draw(gc);

		}
	}

	boolean figureBelongs(Figure figure, Point eventPoint) {		//COMENTARIO lo mismo
		return figure.belongs(eventPoint);
	}

	private void setDefaultFillColor( Color color )
	{
		fillColor = color;
	}

	private void setDefaultBorderColor( Color color )
	{
		lineColor = color;
	}

}
