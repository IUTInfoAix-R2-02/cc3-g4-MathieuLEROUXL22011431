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
    Label messageErreur;

    @FXML
    public void tracer() {
        System.out.println("tracer");
        ArrayList<Integer> notes = getNotes();
        for (int i = 0; i < notes.size(); i++) {
            points.get(i).setCenterX(getXRadarChart(notes.get(i), i+1));
            points.get(i).setCenterY(getYRadarChart(notes.get(i), i+1));
        }
    }

    @FXML
    public void vider() {
        System.out.println("vider");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        points = new ArrayList<Circle>(6);
        for (int i = 0; i < 6; i++) {
            points.add(new Circle(getXRadarChart(0, i+1),getYRadarChart(0, i+1), 5));
        }
        toile.getChildren().addAll(points);
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
            return new ArrayList<Integer>();
        }
        afficherMessageErreur(true);
        return notes;
    }

    public int checkInput(String text) {
        if (text.length() == 0) {
            return 0;
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
