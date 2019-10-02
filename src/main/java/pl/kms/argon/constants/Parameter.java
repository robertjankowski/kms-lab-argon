package pl.kms.argon.constants;

public class Parameter {

    private String name;
    private double value;

    public Parameter(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
