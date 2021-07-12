package ba.unsa.etf.rs.t8;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.sql.SQLException;
import java.time.LocalDate;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class IspitGlavnaTest {
    Stage theStage;
    GlavnaController ctrl;
    GeografijaDAO dao = GeografijaDAO.getInstance();
    KeyCode ctrlKey;

    @Start
    public void start (Stage stage) throws Exception {
        dao.vratiBazuNaDefault();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavna.fxml"));
        ctrl = new GlavnaController();
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Gradovi svijeta");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();

        stage.toFront();

        theStage = stage;

        ctrlKey = KeyCode.CONTROL;
        if (System.getProperty("os.name").equals("Mac OS X"))
            ctrlKey = KeyCode.COMMAND;
    }

    @BeforeEach
    public void resetujBazu() throws SQLException {
        dao.vratiBazuNaDefault();
    }

    @AfterEach
    public void zatvoriGrad(FxRobot robot) {
        if (robot.lookup("#btnCancel").tryQuery().isPresent())
            robot.clickOn("#btnCancel");
    }

    @Test
    public void testDodajGrad(FxRobot robot) {
        // Dodajemo grad
        robot.clickOn("#btnDodajGrad");

        // Čekamo da dijalog postane vidljiv
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        // Sakrivam glavni prozor da nam ne smeta
        Platform.runLater(() -> theStage.hide());

        robot.clickOn("#fieldNaziv");
        robot.write("Sarajevo");
        robot.clickOn("#fieldBrojStanovnika");
        robot.write("500000");
        robot.clickOn("#choiceDrzava");
        robot.clickOn("Velika Britanija");

        // Datum osnivanja
        robot.clickOn("#fieldDatum");
        robot.press(ctrlKey).press(KeyCode.A).release(KeyCode.A).release(ctrlKey);
        robot.press(KeyCode.DELETE).release(KeyCode.DELETE);
        // Default format DatePicker-a je MM/DD/YYYY
        robot.write("02/01/1462");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#fieldNaziv");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        // Da li se grad dodao u bazu?
        Grad sarajevo = dao.nadjiGrad("Sarajevo");
        assertEquals(LocalDate.of(1462,2,1), sarajevo.getDatumOsnivanja());

        // Vraćamo glavni prozor
        Platform.runLater(() -> theStage.show());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Klikamo na Sarajevo
        robot.clickOn("Sarajevo");
        robot.clickOn("#btnIzmijeniGrad");
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        // Ispravna vrijednost DatePickera
        DatePicker fieldDatum = robot.lookup("#fieldDatum").queryAs(DatePicker.class);
        // Default format DatePicker-a je M/D/YYYY
        assertEquals("2/1/1462", fieldDatum.getEditor().getText());
    }

    @Test
    public void testIzmijeniGrad(FxRobot robot) {
        // Uzimamo datum osnivanja Londona
        Grad london = dao.nadjiGrad("Pariz");
        LocalDate londonDatum = london.getDatumOsnivanja();

        // Klikamo na Sarajevo
        robot.clickOn("Pariz");
        robot.clickOn("#btnIzmijeniGrad");
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        // Datum osnivanja
        robot.clickOn("#fieldDatum");
        robot.press(ctrlKey).press(KeyCode.A).release(KeyCode.A).release(ctrlKey);
        robot.press(KeyCode.DELETE).release(KeyCode.DELETE);
        // Default format DatePicker-a je MM/DD/YYYY
        robot.write("09/21/259");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#fieldNaziv");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        // Vraćamo glavni prozor
        Platform.runLater(() -> theStage.show());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Da li je grad promijenjen u bazi?
        Grad pariz = dao.nadjiGrad("Pariz");
        assertEquals(LocalDate.of(259,9,21), pariz.getDatumOsnivanja());

        // Klikamo na London
        robot.clickOn("London");
        robot.clickOn("#btnIzmijeniGrad");
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        // Ispravna vrijednost DatePickera
        DatePicker fieldDatum = robot.lookup("#fieldDatum").queryAs(DatePicker.class);
        // Default format DatePicker-a je MM/DD/YYYY
        assertEquals(londonDatum, fieldDatum.getValue());
        robot.clickOn("#btnCancel");

        // Vraćamo glavni prozor
        Platform.runLater(() -> theStage.show());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Klikamo na Pariz
        robot.clickOn("Pariz");
        robot.clickOn("#btnIzmijeniGrad");
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        // Ispravna vrijednost DatePickera
        fieldDatum = robot.lookup("#fieldDatum").queryAs(DatePicker.class);
        // Default format DatePicker-a je M/D/YYYY
        assertEquals("9/21/0259", fieldDatum.getEditor().getText());
    }
}
