package ba.unsa.etf.rs.t8;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.time.LocalDate;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class IspitGradControllerTest {
    Stage theStage;
    GradController ctrl;
    KeyCode ctrlKey;

    // Pomoćna funkcija za provjeru validacije text polja
    // Može se pozivati iz drugih klasa
    public static boolean sadrziStil(Node polje, String stil) {
        for (String s : polje.getStyleClass())
            if (s.equals(stil)) return true;
        return false;
    }

    @Start
    public void start(Stage stage) throws Exception {
        GeografijaDAO dao = GeografijaDAO.getInstance();
        dao.vratiBazuNaDefault();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        ctrl = new GradController(null, dao.drzave());
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Grad");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
        theStage = stage;

        ctrlKey = KeyCode.CONTROL;
        if (System.getProperty("os.name").equals("Mac OS X"))
            ctrlKey = KeyCode.COMMAND;
    }

    @Test
    public void testPoljaPostoje(FxRobot robot) {
        DatePicker fieldDatum = robot.lookup("#fieldDatum").queryAs(DatePicker.class);
        assertNotNull(fieldDatum);
    }

    @Test
    public void testVracanjeGrada(FxRobot robot) {
        // Upisujemo grad
        robot.clickOn("#fieldNaziv");
        robot.write("London");
        robot.clickOn("#fieldBrojStanovnika");
        robot.write("8825000");
        robot.clickOn("#choiceDrzava");
        robot.clickOn("Velika Britanija");

        // Datum osnivanja
        robot.clickOn("#fieldDatum");
        robot.press(ctrlKey).press(KeyCode.A).release(KeyCode.A).release(ctrlKey);
        robot.press(KeyCode.DELETE).release(KeyCode.DELETE);
        // Default format DatePicker-a je MM/DD/YYYY
        robot.write("05/01/1500");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#fieldNaziv");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        Grad london = ctrl.getGrad();
        assertEquals("London", london.getNaziv());
        assertEquals(8825000, london.getBrojStanovnika());
        assertEquals("Velika Britanija", london.getDrzava().getNaziv());
        assertEquals(LocalDate.of(1500,5,1), london.getDatumOsnivanja());
    }

    @Test
    public void testNevalidanDatum(FxRobot robot) {
        // Upisujemo grad
        robot.clickOn("#fieldNaziv");
        robot.write("London");
        robot.clickOn("#fieldBrojStanovnika");
        robot.write("8825000");
        robot.clickOn("#choiceDrzava");
        robot.clickOn("Velika Britanija");

        // Datum osnivanja
        robot.clickOn("#fieldDatum");
        robot.press(ctrlKey).press(KeyCode.A).release(KeyCode.A).release(ctrlKey);
        robot.press(KeyCode.DELETE).release(KeyCode.DELETE);
        robot.write("05/01/2500");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#fieldNaziv");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        DatePicker fieldDatum = robot.lookup("#fieldDatum").queryAs(DatePicker.class);
        assertTrue(sadrziStil(fieldDatum, "poljeNijeIspravno"));
        assertFalse(sadrziStil(fieldDatum, "poljeIspravno"));

        // Datum osnivanja
        robot.clickOn("#fieldDatum");
        robot.press(ctrlKey).press(KeyCode.A).release(KeyCode.A).release(ctrlKey);
        robot.press(KeyCode.DELETE).release(KeyCode.DELETE);
        robot.write("05/01/2000");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#fieldNaziv");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        assertFalse(sadrziStil(fieldDatum, "poljeNijeIspravno"));
        assertTrue(sadrziStil(fieldDatum, "poljeIspravno"));
    }

}