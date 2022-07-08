package frontend;

import backend.model.*;
import frontend.model.*;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
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
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

import java.util.*;

public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	private Pair<String,String> currentPair = null;

	private boolean redid;
	private final Stack<Pair<List<FrontFigure>, Pair<String, String>>> undoes = new Stack<>();
	private final Stack<Pair<List<FrontFigure>, Pair<String, String>>> redoes = new Stack<>();

	// Canvas y relacionados
	private static final double CANVAS_WIDTH = 800;
	private static final double CANVAS_HEIGHT = 600;
	private final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();
	private final Color SELECTED_COLOR = Color.RED;		//color default de la figura seleccionada
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

	private final Slider borderWidthSlider = new Slider(1, 50, 1);

	private final ColorPicker borderColorPicker = new ColorPicker(Color.BLACK);		//color default del borde

	private final Label fillLabel = new Label("Relleno");

	private final ColorPicker fillColorPicker = new ColorPicker(Color.YELLOW);		//color default del relleno


	//Botones barra superior
	private final Label undoLabel = new Label("No hay acciones que deshacer");
	private final Label undoCounter = new Label("0");
	//tengo que hacer una coleccion de las cosas que se deshacen

	private final ToggleButton undoButton = new ToggleButton("Deshacer");

	private final Label redoLabel = new Label("No hay acciones que rehacer");
	private final Label redoCounter = new Label("0");
	//su numero se incrementa cuando se decrementa deshacer

	private final ToggleButton redoButton = new ToggleButton("Rehacer");

	// Dibujar una figura
	private Point startPoint;

	private FrontFigure selectedFigure;

	// StatusBar
	private final StatusPane statusPane;

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

		HBox topButtonsBox = new HBox(10); // repite codigo
		topButtonsBox.getChildren().addAll(undoLabel,undoCounter,undoButton,redoButton,redoCounter,redoLabel);
		topButtonsBox.setAlignment(Pos.CENTER);
		topButtonsBox.setPadding(new Insets(5));
		topButtonsBox.setStyle("-fx-background-color: #999");
		topButtonsBox.setPrefHeight(40);

		//800
		undoLabel.setPrefWidth(200);
		undoLabel.setTextAlignment(TextAlignment.RIGHT);
		undoLabel.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		undoCounter.setPrefWidth(20);
		undoCounter.setAlignment(Pos.CENTER_LEFT);
		undoButton.setPrefWidth(100);
		undoButton.setAlignment(Pos.CENTER);
		redoButton.setPrefWidth(100);
		redoButton.setAlignment(Pos.CENTER);
		redoCounter.setPrefWidth(20);
		redoCounter.setAlignment(Pos.CENTER_RIGHT);
		redoLabel.setPrefWidth(200);
		redoLabel.setTextAlignment(TextAlignment.RIGHT);

		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

		final String MESSAGE_DRAW = "Dibujar ";
		final String MESSAGE_DELETE = "Borrar ";

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				statusPane.updateStatus("Las figuras deben realizarse desde la esquina superior izquierda hasta la esquina inferior derecha");
				statusPane.errorColor();
				return ;
			}
			FrontFigure newFigure;


			if(rectangleButton.isSelected()) {
				//aca tambien se repite mucho codigo
				newFigure = new FrontRectangle(startPoint, endPoint, fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getValue());
			}
			else if(circleButton.isSelected()) {
				double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new FrontCircle(startPoint, circleRadius, fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getValue());
			} else if(squareButton.isSelected()) {
				double size = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new FrontSquare(startPoint, size, fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getValue());
			} else if(ellipseButton.isSelected()) {
				Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
				double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
				double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
				newFigure = new FrontEllipse(centerPoint, sMayorAxis, sMinorAxis, fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getValue());
			}
			else {
				return ;
			}

			Pair<String, String> labels = new Pair<>(MESSAGE_DELETE + newFigure, MESSAGE_DRAW + newFigure);
			setHistory(labels);
			setCurrent(labels);
			canvasState.addFigure(newFigure);
			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(FrontFigure figure : canvasState.figures()) {
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
			statusPane.normalColor();
		});

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (FrontFigure figure : canvasState.figures()) {
					if(figureBelongs(figure, eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure);
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
				if(selectedFigure != null) {
					Point eventPoint = new Point(event.getX(), event.getY());
					double diffX = (eventPoint.getX() - startPoint.getX());
					double diffY = (eventPoint.getY() - startPoint.getY());
					selectedFigure.move(diffX, diffY);
					startPoint = eventPoint;
				}
				redrawCanvas();
			}
		});

		deleteButton.setOnAction(event -> {
			if (selectedFigure != null) {
				Pair<String,String> labels = new Pair<>(MESSAGE_DRAW + selectedFigure, MESSAGE_DELETE + selectedFigure);
				setHistory(labels);
				setCurrent(labels);
				canvasState.deleteFigure(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});

		final String ZOOM_IN = "Agrandar ";
		final String ZOOM_OUT = "Achicar ";

		zoomInButton.setOnAction(event -> {
			if(selectedFigure != null) {
				Pair<String,String> labels = new Pair<>(ZOOM_OUT + selectedFigure, ZOOM_IN + selectedFigure);
				setHistory(labels);
				setCurrent(labels);
				selectedFigure.zoomIn(ZOOM_AMOUNT);
				redrawCanvas();
			}
		});

		zoomOutButton.setOnAction(event -> {
			if(selectedFigure != null) {
				Pair<String,String> labels = new Pair<>(ZOOM_IN + selectedFigure, ZOOM_OUT + selectedFigure);
				setHistory(labels);
				setCurrent(labels);
				selectedFigure.zoomOut(ZOOM_AMOUNT);
				redrawCanvas();
			}
		});

		undoButton.setOnAction(event -> {

				if (undoes.isEmpty()) {
					statusPane.updateStatus("No hay nada para deshacer");
					statusPane.errorColor();
					return;
				}
				String undoL = null;
//				String redoL = null;
//
//				if (!undoes.isEmpty()) {
//					undoL = undoes.peek().getValue().getKey();
//				}
//				if (!redoes.isEmpty()) {
//					redoL = redoes.peek().getValue().getValue();
//				}

				Pair<List<FrontFigure>, Pair<String, String>> current = new Pair<>(canvasState.copyState(), currentPair);
				Pair<List<FrontFigure>, Pair<String, String>> aux = undoes.pop();

				currentPair = aux.getValue();

				redid = true;

				redoes.add(current);

				if (!undoes.isEmpty()) {
					undoL = undoes.peek().getValue().getKey();
				}
				canvasState.setState(aux.getKey());
				setLabels(undoL, aux.getValue().getValue());
				redrawCanvas();

		});

		redoButton.setOnAction(event -> {
			if(redoes.isEmpty()){
				statusPane.updateStatus("No hay nada para rehacer");
				statusPane.errorColor();
				return;
			}

			String undoL = null;


			Pair<List<FrontFigure>,Pair<String,String>> current = new Pair<>(canvasState.copyState(),currentPair);
			Pair<List<FrontFigure>,Pair<String,String>> aux = redoes.pop();


			undoL = currentPair.getKey();

			currentPair = aux.getValue();



//			if (!redoes.isEmpty()) {
//				redoL = aux.getValue().getValue();
//			}
			undoes.add(current);

			canvasState.setState(aux.getKey());
			setLabels(undoL,aux.getValue().getValue());
			redrawCanvas();
		});

		fillColorPicker.setOnAction(event -> {
			if(selectedFigure != null) {
				String label = String.format("Cambiar color de relleno %s",selectedFigure);
				Pair<String,String> labels = new Pair<>(label,label);
				setHistory(labels);
				setCurrent(labels);
				gc.setFill(fillColorPicker.getValue());
				selectedFigure.setFillColor(fillColorPicker.getValue());
				redrawCanvas();
			}
		});

		borderColorPicker.setOnAction(event -> {
			if(selectedFigure != null) {
				String label = String.format("Cambiar color del borde %s",selectedFigure);
				Pair<String,String> labels = new Pair<>(label,label);
				setHistory(labels);
				setCurrent(labels);
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
		for(FrontFigure figure : canvasState.figures()) {
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

	private boolean figureBelongs(FrontFigure figure, Point eventPoint) {
		return figure.belongs(eventPoint);
	}

	private void setHistory( Pair<String,String> labels ) {
		Pair<List<FrontFigure>,Pair<String,String>> pair = new Pair<>(canvasState.copyState(),labels);
		undoes.add(pair);
		redoes.clear();
		setLabels(pair.getValue().getKey(),pair.getValue().getValue());
	}

	private void setLabels( String undoL, String redoL ) {
		undoLabel.setText(undoes.size()==0? "No hay acciones para deshacer":undoL);
		redoLabel.setText(redoes.size()==0? "No hay acciones para rehacer":redoL);
//		System.out.println(undoes.pop());
		undoCounter.setText(String.format("%d",undoes.size()));
		redoCounter.setText(String.format("%d",redoes.size()));
	}

	private void setCurrent( Pair<String,String> pair )
	{
		currentPair = pair;
	}
}
