package pl.kms.argon.constants;

import pl.kms.argon.atom.Atom;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    // liczba atomów wzdłuż każdej z krawędzi
    public static Parameter n = new Parameter("n", 10);
    public static Parameter T0 = new Parameter("T0", 100); // Kelvin
    public static Parameter a = new Parameter("a", 0.38); // nm
    public static Parameter m = new Parameter("m", 39.948); // masa atomu

    // liczba atomów
    public static Parameter N = new Parameter("N", Math.pow(n.getValue(), 3));
    public static Parameter k_b = new Parameter("k_b", 0.00831);

    public static List<Parameter> parameters = new ArrayList<>(List.of(n, N, k_b, T0, a, m));

    public static void reinitializeConstants() {
        for (Parameter parameter : parameters) {
            String parameterName = parameter.getName();
            double parameterValue = parameter.getValue();
            if (parameterName.equals(n.getName()))
                n.setValue(parameterValue);
            else if (parameterName.equals(T0.getName()))
                T0.setValue(parameterValue);
            else if (parameterName.equals(a.getName()))
                a.setValue(parameterValue);
            else if (parameterName.equals(m.getName()))
                m.setValue(parameterValue);
        }
    }

    // Wektory wyznaczające krawędzi komórki elementarnej kryształu (4)
    public static void initializeBsVectors() {
        b0.setPosition(a.getValue(), 0, 0);
        b1.setPosition(a.getValue() / 2.0, a.getValue() * Math.sqrt(3) / 2, 0);
        b2.setPosition(a.getValue() / 2.0, a.getValue() * Math.sqrt(3) / 6, a.getValue() * Math.sqrt(2.0 / 3.0));
    }

    public static Atom b0 = new Atom(a.getValue(), 0, 0);
    public static Atom b1 = new Atom(a.getValue() / 2.0, a.getValue() * Math.sqrt(3) / 2, 0);
    public static Atom b2 = new Atom(a.getValue() / 2.0, a.getValue() * Math.sqrt(3) / 6, a.getValue() * Math.sqrt(2.0 / 3.0));
}
