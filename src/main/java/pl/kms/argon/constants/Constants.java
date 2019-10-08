package pl.kms.argon.constants;

import pl.kms.argon.atom.Atom;

import java.util.Arrays;
import java.util.List;

public class Constants {
    /**
     * Number of atoms along one of the edges
     */
    public static Parameter n = new Parameter("n", 10);

    /**
     * Initial temperature [K]
     */
    public static Parameter T0 = new Parameter("T0", 100);

    /**
     * Length of elementary cell [nm]
     */
    public static Parameter a = new Parameter("a", 0.38);

    /**
     * Atom mass
     */
    public static Parameter m = new Parameter("m", 39.948); // masa atomu


    /**
     * Sum of all atoms
     */
    public static Parameter N = new Parameter("N", Math.pow(n.getValue(), 3));

    /**
     * Boltzmann constant
     */
    public static Parameter k_b = new Parameter("k_b", 0.00831);


    /**
     * Radius of spherical vessel [nm]
     */
    public static Parameter L = new Parameter("L", 1.2);


    /**
     * Interatomic distance
     */
    public static Parameter R = new Parameter("R", 0.38);

    /**
     * flexibility factor
     */
    public static Parameter f = new Parameter("f", 1e4);

    /**
     * Epsilon, potential
     */
    public static Parameter e = new Parameter("e", 1);

    public static List<Parameter> parameters = Arrays.asList(n, N, k_b, T0, a, m, L, R, f, e);

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
            else if (parameterName.equals(L.getName()))
                L.setValue(parameterValue);
            else if (parameterName.equals(R.getName()))
                R.setValue(parameterValue);
            else if (parameterName.equals(f.getName()))
                f.setValue(parameterValue);
            else if (parameterName.equals(e.getName()))
                e.setValue(parameterValue);
        }
    }

    // Wektory wyznaczające krawędzi komórki elementarnej kryształu (4)
    public static void initializeBsVectors() {
        b0.setPosition(a.getValue(), 0, 0);
        b1.setPosition(a.getValue() / 2.0, a.getValue() * Math.sqrt(3) / 2, 0);
        b2.setPosition(a.getValue() / 2.0, a.getValue() * Math.sqrt(3) / 6, a.getValue() * Math.sqrt(2.0 / 3.0));
    }

    public static Atom b0 = new Atom();
    public static Atom b1 = new Atom();
    public static Atom b2 = new Atom();

    static {
        initializeBsVectors();
    }
}
