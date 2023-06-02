package fr.amu.iut.cc3;

import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ToileController implements Initializable {

    private static int rayonCercleExterieur = 200;
    private static int angleEnDegre = 60;
    private static int angleDepart = 90;
    private static int noteMaximale = 20;

    @FXML
    TextField comp1;
    @FXML
    TextField comp2;
    @FXML
    TextField comp3;
    @FXML
    TextField comp4;
    @FXML
    TextField comp5;
    @FXML
    TextField comp6;
    @FXML
    Pane toile;
    @FXML
    HBox scene;
    @FXML
    Label messageErreur;
    private ArrayList<Circle> points;
    private ArrayList<Line> lignes;
    private ArrayList<TextField> textFields;
    private SimpleStringProperty couleurErreur;
    private static ObservableList<Note> lesNotes;
    private static ListChangeListener<Note> unChangementListener;

    @FXML
    public void tracer() {
        update();
    }

    @FXML
    public void vider() {
        couleurErreur.set("#9db2e3");
        for (Note note: lesNotes) {
            note.setValue(0);
        }
        for (Circle point : points) {
            point.setFill(Color.TRANSPARENT);
        }
        for (Line ligne : lignes) {
            ligne.setStroke(Color.TRANSPARENT);
        }
        for (TextField textField : textFields) {
            textField.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des listes
        points = new ArrayList<Circle>(6);
        lignes = new ArrayList<Line>(6);
        textFields = new ArrayList<TextField>(6);
        // Remplissage des listes
        for (int i = 0; i < 6; i++) {
            points.add(new Circle(getXRadarChart(0, i+1),getYRadarChart(0, i+1), 5, Color.TRANSPARENT));
            lignes.add(new Line(0,0,0,0));
        }
        textFields.add(comp1);
        textFields.add(comp2);
        textFields.add(comp3);
        textFields.add(comp4);
        textFields.add(comp5);
        textFields.add(comp6);
        messageErreur.setText("Erreur de saisie :\nLes valeurs doivent être entre 0 et 20");
        couleurErreur = new SimpleStringProperty("#9db2e3");
        toile.getChildren().addAll(points);
        toile.getChildren().addAll(lignes);
        createBinding();
    }

    @FXML
    public void update() {
        boolean invalidInput = false;
        for (int i = 0; i < textFields.size(); i++) {
            if (checkInput(textFields.get(i).getText()) == -1) {
                invalidInput = true;
            }
            lesNotes.get(i).setValue(checkInput(textFields.get(i).getText()));
        }
        if (invalidInput) {
            couleurErreur.set("#ff0000");
        } else {
            couleurErreur.set("#9db2e3");
        }
    }

    public void createBinding() {
        lesNotes = FXCollections.observableArrayList(note -> new Observable[]{note.valueProperty()});

        unChangementListener = new ListChangeListener<Note>() {
            public void onChanged(Change<? extends Note> c) {
                c.next();
                if (c.wasUpdated()) {
                    // update des coordonnées du point représentant la note
                    if (lesNotes.get(c.getFrom()).getValue() == -1) {
                        points.get(c.getFrom()).setFill(Color.TRANSPARENT);
                    } else {
                        points.get(c.getFrom()).setCenterX(getXRadarChart(lesNotes.get(c.getFrom()).getValue(), c.getFrom() + 1));
                        points.get(c.getFrom()).setCenterY(getYRadarChart(lesNotes.get(c.getFrom()).getValue(), c.getFrom()+ 1));
                        points.get(c.getFrom()).setFill(Color.BLACK);
                    }
                    // Placement des lignes
                    for (int i = 0; i < 6; i++) {
                        lignes.get(i).setStartX(points.get(i).getCenterX());
                        lignes.get(i).setStartY(points.get(i).getCenterY());
                        if (i == 5) {
                            lignes.get(i).setEndX(points.get(0).getCenterX());
                            lignes.get(i).setEndY(points.get(0).getCenterY());
                            if (lesNotes.get(i).getValue() == -1 || lesNotes.get(0).getValue() == -1) {
                                lignes.get(i).setStroke(Color.TRANSPARENT);
                            } else {
                                lignes.get(i).setStroke(Color.BLACK);
                            }
                        } else {
                            lignes.get(i).setEndX(points.get(i+ 1).getCenterX());
                            lignes.get(i).setEndY(points.get(i + 1).getCenterY());
                            if (lesNotes.get(i).getValue() == -1 || lesNotes.get(i + 1).getValue() == -1) {
                                lignes.get(i).setStroke(Color.TRANSPARENT);
                            } else {
                                lignes.get(i).setStroke(Color.BLACK);
                            }
                        }
                    }
                }
            };
        };
        lesNotes.addListener(unChangementListener);
        for (int i = 0; i < 6; i++) {
            lesNotes.add(new Note(0));
        }
        messageErreur.styleProperty().bind(Bindings.concat("-fx-text-fill:", couleurErreur));
    }



    int getXRadarChart(double value, int axe ){
        return (int) (rayonCercleExterieur + Math.cos(Math.toRadians(angleDepart - (axe-1)  * angleEnDegre)) * rayonCercleExterieur
                *  (value / noteMaximale));
    }

    int getYRadarChart(double value, int axe ){
        return (int) (rayonCercleExterieur - Math.sin(Math.toRadians(angleDepart - (axe-1)  * angleEnDegre)) * rayonCercleExterieur
                *  (value / noteMaximale));
    }

    public int checkInput(String text) {
        if (text.length() == 0) {
            return -1;
        }
        for (int i = 0; i < text.length(); i++) {
            if (Character.isDigit(text.charAt(i)) == false) {
                return -1;
            }
        }
        Integer note = Integer.valueOf(text);
        if (note < 0 || note > 20) {
            return -1;
        }
        return note;
    }
}
