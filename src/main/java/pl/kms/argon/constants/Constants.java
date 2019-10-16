package pl.kms.argon.constants;

import pl.kms.argon.atom.Atom;

import java.util.Arrays;
import java.util.List;

public class Constants {
    /**
     * Number of atoms along one of the edges
     */
    public static Parameter n = new Parameter("n", 5);

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
    public static Parameter m = new Parameter("m", 39.948);


    /**
     * Sum of all atoms
     */
    public static Parameter N = new Parameter("N", (int) Math.pow(n.getValue(), 3));

    /**
     * Boltzmann constant
     */
    public static Parameter k_b = new Parameter("k_b", 0.00831);


    /**
     * Radius of spherical vessel [nm]
     */
    public static Parameter L = new Parameter("L", 2.3);

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

    /**
     * Steps before simulation
     */
    public static Parameter So = new Parameter("So", 100);

    /**
     * Simulation steps
     */
    public static Parameter Sd = new Parameter("Sd", 1000);

    /**
     * Step
     */
    public static Parameter tau = new Parameter("tau", 1e-3);

    /**
     * Step to save t,H,V,T,P
     */
    public static Parameter Sout = new Parameter("S_out", 10);

    /**
     * Step to save x_i, y_i, z_u, E_i kinetic
     */
    public static Parameter Sxyz = new Parameter("S_xyz", 10);

    public static List<Parameter> parameters = Arrays.asList(n, N, k_b, T0, a, m, L, R, f, e, So, Sd, Sout, Sxyz, tau);

    public static void reinitializeConstants() {
        for (Parameter parameter : parameters) {
            String parameterName = parameter.getName();
            double parameterValue = parameter.getValue();
            if (parameterName.equals(n.getName())) {
                n.setValue(parameterValue);
                N.setValue(Math.pow(n.getValue(), 3));
            } else if (parameterName.equals(T0.getName()))
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
            else if (parameterName.equals(So.getName()))
                So.setValue(parameterValue);
            else if (parameterName.equals(Sd.getName()))
                Sd.setValue(parameterValue);
            else if (parameterName.equals(Sout.getName()))
                Sout.setValue(parameterValue);
            else if (parameterName.equals(Sxyz.getName()))
                Sxyz.setValue(parameterValue);
            else if (parameterName.equals(tau.getName()))
                tau.setValue(parameterValue);
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
