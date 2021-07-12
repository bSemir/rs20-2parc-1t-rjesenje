package ba.unsa.etf.rs.t8;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;

public class JSONFormat {
    private ArrayList<Drzava> drzave = new ArrayList<>();
    private ArrayList<Grad> gradovi = new ArrayList<>();

    public void ucitaj(File file) throws Exception {
        String ulaz = Files.readString(file.toPath());

        JSONArray jdrzave = new JSONArray(ulaz);
        for (int i=0; i<jdrzave.length(); i++) {
            JSONObject jdrzava = jdrzave.getJSONObject(i);
            Drzava drzava = new Drzava(0, jdrzava.getString("naziv"), null);

            JSONArray jgradovi = jdrzava.getJSONArray("gradovi");
            for (int j=0; j<jgradovi.length(); j++) {
                JSONObject jgrad = jgradovi.getJSONObject(j);
                Grad grad = new Grad(0, jgrad.getString("naziv"), jgrad.getInt("brojStanovnika"), drzava, LocalDate.parse(jgrad.getString("datumOsnivanja")));
                if (jgrad.has("glavni") && jgrad.getBoolean("glavni"))
                    drzava.setGlavniGrad(grad);
                gradovi.add(grad);
            }
            if (drzava.getGlavniGrad() == null)
                throw new Exception("Nijedan grad nije glavni");
            drzave.add(drzava);
        }
    }

    public void zapisi(File file)  {
        JSONArray jdrzave = new JSONArray();
        for(Drzava drzava : drzave) {
            JSONObject jdrzava = new JSONObject();
            jdrzava.put("naziv", drzava.getNaziv());
            JSONArray jgradovi = new JSONArray();
            for (Grad grad : gradovi) {
                if (grad.getDrzava().getId() == drzava.getId()) {
                    JSONObject jgrad = new JSONObject();
                    jgrad.put("naziv", grad.getNaziv());
                    jgrad.put("brojStanovnika", grad.getBrojStanovnika());
                    if (grad.getId() == drzava.getGlavniGrad().getId())
                        jgrad.put("glavni", true);
                    jgrad.put("datumOsnivanja", grad.getDatumOsnivanja().toString());
                    jgradovi.put(jgrad);
                }
            }
            jdrzava.put("gradovi", jgradovi);
            jdrzave.put(jdrzava);
        }
        try {
            Files.writeString(file.toPath(), jdrzave.toString());
        } catch (IOException e) {
            return;
        }
    }

    public ArrayList<Drzava> getDrzave() {
        return drzave;
    }

    public void setDrzave(ArrayList<Drzava> drzave) {
        this.drzave = drzave;
    }

    public ArrayList<Grad> getGradovi() {
        return gradovi;
    }

    public void setGradovi(ArrayList<Grad> gradovi) {
        this.gradovi = gradovi;
    }
}
