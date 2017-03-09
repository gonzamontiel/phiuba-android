package mont.gonzalo.phiuba.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import mont.gonzalo.phiuba.R;

/**
 * Created by Gonzalo Montiel on 1/14/17.
 */
public class Department {
    String code;
    String altCode;
    String contacto;
    String name;
    String mailto;
    @SerializedName("CA-docentes")
    String docentesConsejo;
    @SerializedName("CA-auxiliares")
    String auxiliaresConsejo;
    @SerializedName("CA-graduados")
    String graduadosConsejo;
    @SerializedName("CA-alumnos")
    String alumnosConsejo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAltCode() {
        return altCode;
    }

    public void setAltCode(String altCode) {
        this.altCode = altCode;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailto() {
        return mailto;
    }

    public void setMailto(String mailto) {
        this.mailto = mailto;
    }

    public String getDocentesConsejo() {
        return docentesConsejo;
    }

    public void setDocentesConsejo(String docentesConsejo) {
        this.docentesConsejo = docentesConsejo;
    }

    public String getAuxiliaresConsejo() {
        return auxiliaresConsejo;
    }

    public void setAuxiliaresConsejo(String auxiliaresConsejo) {
        this.auxiliaresConsejo = auxiliaresConsejo;
    }

    public String getGraduadosConsejo() {
        return graduadosConsejo;
    }

    public void setGraduadosConsejo(String graduadosConsejo) {
        this.graduadosConsejo = graduadosConsejo;
    }

    public String getAlumnosConsejo() {
        return alumnosConsejo;
    }

    public void setAlumnosConsejo(String alumnosConsejo) {
        this.alumnosConsejo = alumnosConsejo;
    }

    public int getImageResource() {
        return getIconByDepartmentCode(this.code);
    }


    static private HashMap<String,Integer> depMap = createIconsMap();
    static private HashMap<String,Integer> createIconsMap() {
        depMap = new HashMap<String, Integer>();
        depMap.put("61", R.drawable.dep61);
        depMap.put("62", R.drawable.dep62);
        depMap.put("63", R.drawable.dep63);
        depMap.put("64", R.drawable.dep64);
        depMap.put("65", R.drawable.dep65);
        depMap.put("66", R.drawable.dep66);
        depMap.put("67", R.drawable.dep67);
        depMap.put("68", R.drawable.dep68);
        depMap.put("69", R.drawable.dep69);
        depMap.put("70", R.drawable.dep70);
        depMap.put("71", R.drawable.dep71);
        depMap.put("72", R.drawable.dep72);
        depMap.put("73", R.drawable.dep73);
        depMap.put("74", R.drawable.dep74);
        depMap.put("75", R.drawable.dep75);
        depMap.put("76", R.drawable.dep76);
        depMap.put("77", R.drawable.dep77);
        depMap.put("78", R.drawable.dep78);
        depMap.put("ALIM", R.drawable.dep_alim);
        return depMap;
    }

    public static int getIconByDepartmentCode(String depCode) {
        return depMap.containsKey(depCode) ? depMap.get(depCode) : R.drawable.dep_default;
    }

    public String getDescription() {
        String sede = "";
        if (contacto.contains("Paseo")) {
            sede = "Paseo Colón";
        } else if (contacto.contains("Heras")) {
            sede = "Las Heras";
        }
        return "10 materias." + (!sede.isEmpty() ? "Sede " + sede : "");
    }
}
