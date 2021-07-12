package ba.unsa.etf.rs.t8;

import java.time.LocalDate;

public class Grad {
    private int id;
    private String naziv;
    private int brojStanovnika;
    private Drzava drzava;
    private LocalDate datumOsnivanja;

    public Grad(int id, String naziv, int brojStanovnika, Drzava drzava) {
        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzava = drzava;
        this.datumOsnivanja = LocalDate.now();
    }

    public Grad(int id, String naziv, int brojStanovnika, Drzava drzava, LocalDate datumOsnivanja) {
        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzava = drzava;
        this.datumOsnivanja = datumOsnivanja;
    }

    public Grad() {
        this.datumOsnivanja = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava = drzava;
    }

    public LocalDate getDatumOsnivanja() {
        return datumOsnivanja;
    }

    public void setDatumOsnivanja(LocalDate datumOsnivanja) {
        this.datumOsnivanja = datumOsnivanja;
    }

    @Override
    public String toString() { return naziv; }
}
