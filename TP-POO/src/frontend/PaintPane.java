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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	private final Stack<List<Figure>> undoes = new Stack<>();
	private final Stack<List<Figure>> redoes = new Stack<>();

	// Canvas y relacionados
	private final Canvas canvas = new Canvas(800, 600);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();
	private final Color SELECTED_COLOR = Color.RED;						//color default de la figura seleccionada
	private final int ZOOM_AMOUNT = 10;

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

	private final ColorPicker borderColorPicker = new ColorPicker(Color.BLACK);		//color default del borde

	private final Label fillLabel = new Label("Relleno");

	private final ColorPicker fillColorPicker = new ColorPicker(Color.YELLOW);		//color default del relleno


	//Botones barra superior
	private final Label undoLabel = new Label("un texto que indique cuál será el efecto de presionar el botón");
	//tengo que hacer una coleccion de las cosas que se deshacen

	private final ToggleButton undoButton = new ToggleButton("Deshacer");

	private final Label redoLabel = new Label("un texto que indique cuál será el efecto de presionar el botón");
	//su numero se incrementa cuando se decrementa deshacer

	private final ToggleButton redoButton = new ToggleButton("Rehacer");

	// Dibujar una figura
	private Point startPoint;

	// Seleccionar una figura
	private Figure selectedFigure;

	// StatusBar
	private StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		borderWidthSlider.setShowTickLabels(true);
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
		topButtonsBox.getChildren().addAll(undoLabel, undoButton, redoButton, redoLabel);
		topButtonsBox.setPadding(new Insets(5));
		topButtonsBox.setStyle("-fx-background-color: #999");
		topButtonsBox.setPrefHeight(40);

		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return ;
			}
			Figure newFigure = null;


			if(rectangleButton.isSelected()) {
				//aca tambien se repite mucho codigo
				newFigure = new Rectangle(startPoint, endPoint, fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getMin());
			}
			else if(circleButton.isSelected()) {
				double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new Circle(startPoint, circleRadius,fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getMin());
			} else if(squareButton.isSelected()) {
				double size = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new Square(startPoint, size, fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getMin());
			} else if(ellipseButton.isSelected()) {
				Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
				double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
				double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
				newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis, fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getMin());
			}
			else {
				return ;
			}
//			newFigure.setFillColor(fillColorPicker.getValue());
//			newFigure.setBorderColor(borderColorPicker.getValue());
			setHistory();
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
					label.append(figure);
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
					selectedFigure = null;
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
				setHistory();
				canvasState.deleteFigure(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});

		zoomInButton.setOnAction(event -> {
			if(selectedFigure != null) {
				setHistory();
				selectedFigure.zoomIn(ZOOM_AMOUNT);
				redrawCanvas();
			}
		});

		zoomOutButton.setOnAction(event -> {
			if(selectedFigure != null) {
				setHistory();
				selectedFigure.zoomOut(ZOOM_AMOUNT);
				redrawCanvas();
			}
		});

		undoButton.setOnAction(event -> {
			redoes.add(canvasState.copyState());
			canvasState.setState(undoes.pop());
			redrawCanvas();
		});

		redoButton.setOnAction(event -> {
			undoes.add(canvasState.copyState());
			canvasState.setState(redoes.pop());
			redrawCanvas();
		});

		fillColorPicker.setOnAction(event -> {
			if(selectedFigure != null) {
				setHistory();
				gc.setFill(fillColorPicker.getValue());
				selectedFigure.setFillColor(fillColorPicker.getValue());
				redrawCanvas();
			}
		});

		borderColorPicker.setOnAction(event -> {
			if(selectedFigure != null) {
				setHistory();
				gc.setStroke(borderColorPicker.getValue());
				selectedFigure.setBorderColor(borderColorPicker.getValue());
				redrawCanvas();
		}});

		borderWidthSlider.setOnMouseDragged(event -> {
			if(selectedFigure != null) {
				gc.setLineWidth(borderWidthSlider.getValue());
				selectedFigure.setBorderWidth(borderWidthSlider.getValue());
				redrawCanvas();
			}
		});

		setLeft(buttonsBox);
		setTop(topButtonsBox);
		setRight(canvas);
	}

	private void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if(figure == selectedFigure) {
				gc.setStroke(SELECTED_COLOR);
			} else {
				gc.setStroke(figure.getBorderColor());

			}
			gc.setLineWidth(figure.getBorderWidth());
			gc.setFill(figure.getFillColor());
			figure.draw(gc);
		}
	}

	private boolean figureBelongs(Figure figure, Point eventPoint) {
		return figure.belongs(eventPoint);
	}

	private void setHistory() {
		undoes.add(canvasState.copyState());
		redoes.clear();
	}


}
