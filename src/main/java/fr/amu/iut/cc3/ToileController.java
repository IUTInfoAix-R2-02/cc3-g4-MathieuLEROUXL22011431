package fr.amu.iut.cc3;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
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
    HBox emplacementErreur;
    ArrayList<Circle> points;
    ArrayList<Line> lignes;
    Label messageErreur;

    @FXML
    public void tracer() {
        System.out.println("tracer");
        ArrayList<Integer> notes = getNotes();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i) == -1) {
                points.get(i).setFill(Color.TRANSPARENT);
            } else {
                points.get(i).setCenterX(getXRadarChart(notes.get(i), i + 1));
                points.get(i).setCenterY(getYRadarChart(notes.get(i), i + 1));
                points.get(i).setFill(Color.BLACK);
            }
        }
        for (int i = 0; i < 5; i++) {
            System.out.println("passage 1");
            if (notes.get(i) == -1 || notes.get(i+1) == -1) {
                System.out.println("passage 2");
                lignes.get(i).setStroke(Color.TRANSPARENT);
            } else {
                lignes.get(i).setStartX(points.get(i).getCenterX());
                lignes.get(i).setEndX(points.get(i+1).getCenterX());
                lignes.get(i).setStartY(points.get(i).getCenterY());
                lignes.get(i).setEndY(points.get(i+1).getCenterY());
                lignes.get(i).setStroke(Color.BLACK);
            }
        }
        if (notes.get(5) == -1 || notes.get(0) == -1) {
            lignes.get(5).setStroke(Color.TRANSPARENT);
        } else {
            lignes.get(5).setStartX(points.get(5).getCenterX());
            lignes.get(5).setEndX(points.get(0).getCenterX());
            lignes.get(5).setStartY(points.get(5).getCenterY());
            lignes.get(5).setEndY(points.get(0).getCenterY());
            lignes.get(5).setStroke(Color.BLACK);
        }
    }

    @FXML
    public void vider() {
        afficherMessageErreur(true);
        for (int i = 0; i < 6; i++) {
            points.get(i).setFill(Color.TRANSPARENT);
        }
        for (int i = 0; i < 6; i++) {
            lignes.get(i).setStroke(Color.TRANSPARENT);
        }
        comp1.setText("");
        comp2.setText("");
        comp3.setText("");
        comp4.setText("");
        comp5.setText("");
        comp6.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        points = new ArrayList<Circle>(6);
        lignes = new ArrayList<Line>(6);
        for (int i = 0; i < 6; i++) {
            points.add(new Circle(getXRadarChart(0, i+1),getYRadarChart(0, i+1), 5));
        }
        for (int i = 0; i < 6; i++) {
            lignes.add(new Line(0,0,0,0));
        }
        toile.getChildren().addAll(points);
        toile.getChildren().addAll(lignes);
        messageErreur = new Label();
        messageErreur.setTextFill(Color.RED);
        emplacementErreur.getChildren().add(messageErreur);
    }

    int getXRadarChart(double value, int axe ){
        return (int) (rayonCercleExterieur + Math.cos(Math.toRadians(angleDepart - (axe-1)  * angleEnDegre)) * rayonCercleExterieur
                *  (value / noteMaximale));
    }

    int getYRadarChart(double value, int axe ){
        return (int) (rayonCercleExterieur - Math.sin(Math.toRadians(angleDepart - (axe-1)  * angleEnDegre)) * rayonCercleExterieur
                *  (value / noteMaximale));
    }

    public ArrayList<Integer> getNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.add(checkInput(comp1.getText()));
        notes.add(checkInput(comp2.getText()));
        notes.add(checkInput(comp3.getText()));
        notes.add(checkInput(comp4.getText()));
        notes.add(checkInput(comp5.getText()));
        notes.add(checkInput(comp6.getText()));
        if (notes.contains(-1)) {
            afficherMessageErreur(false);
            return notes;
        }
        afficherMessageErreur(true);
        return notes;
    }

    public int checkInput(String text) {
        if (text.length() == 0) {
            return -1;
        }
        ArrayList<Character> digits = new ArrayList<>(10);
        digits.add('1');
        digits.add('2');
        digits.add('3');
        digits.add('4');
        digits.add('5');
        digits.add('6');
        digits.add('7');
        digits.add('8');
        digits.add('9');
        digits.add('0');
        for (int i = 0; i < text.length(); i++) {
            if (digits.contains(text.charAt(i)) == false) {
                return -1;
            }
        }
        Integer note = Integer.valueOf(text);
        if (note < 0 || note > 20) {
            return -1;
        }
        return note;
    }

    public void afficherMessageErreur(boolean isInputCorrect) {
        if (isInputCorrect) {
            messageErreur.setText("");
        } else {
            messageErreur.setText("Erreur de saisie :\nLes valeurs doivent être entre 0 et 20");
        }
    }
}
