package ba.unsa.etf.rs.t8;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class IspitGradTest {

    @Test
    void getSetTest() {
        Drzava d = new Drzava(1, "Bosna i Hercegovina", null);
        Grad g = new Grad(100, "Sarajevo", 350000, d);
        g.setDatumOsnivanja(LocalDate.of(1462,2,1));
        assertEquals(LocalDate.of(1462,2,1), g.getDatumOsnivanja());
    }

    @Test
    void ctorTest() {
        Drzava d = new Drzava(1, "Bosna i Hercegovina", null);
        Grad g = new Grad(100, "Sarajevo", 350000, d, LocalDate.of(1462,2,1));
        assertEquals(LocalDate.of(1462,2,1), g.getDatumOsnivanja());
    }
}