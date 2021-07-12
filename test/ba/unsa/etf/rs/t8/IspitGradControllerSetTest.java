package ba.unsa.etf.rs.t8;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
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
public class IspitGradControllerSetTest {
    Stage theStage;
    GradController ctrl;
    Grad globalniGrad;
    KeyCode ctrlKey;

    @Start
    public void start(Stage stage) throws Exception {
        // Kreiramo formu sa popunjenim gradom
        GeografijaDAO dao = GeografijaDAO.getInstance();
        dao.vratiBazuNaDefault();

        // Postavljamo Veliku Britaniju za olimpijsku državu u bazi kako bismo mogli testirati
        Drzava vbr = dao.nadjiDrzavu("Velika Britanija");
        // London je osnovan 43. godine (mjesec i dan sam izmislio)
        Grad london = new Grad(0, "London", 8825000, vbr, LocalDate.of(43, 5, 15));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        ctrl = new GradController(london, dao.drzave());
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
    public void testIspravneVrijednosti(FxRobot robot) {
        DatePicker fieldDatum = robot.lookup("#fieldDatum").queryAs(DatePicker.class);
        // Default format DatePicker-a je MM/DD/YYYY
        assertEquals("5/15/0043", fieldDatum.getEditor().getText());
    }

    @Test
    public void testIzmjene(FxRobot robot) {
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
}
