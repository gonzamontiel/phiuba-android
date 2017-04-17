package mont.gonzalo.phiuba.model;

/**
 * Created by Gonzalo Montiel on 4/6/17.
 */
public class Branch {
    private String code;
    private String name;
    private String[] required;

    public String[] getRequired() {
        return required;
    }

    public void setRequired(String[] required) {
        this.required = required;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
