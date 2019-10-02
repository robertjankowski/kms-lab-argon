package pl.kms.argon.constants;

import pl.kms.argon.atom.Atom;

public class Constants {
    // liczba atomów
    public static int N = 1000;
    // liczba atomów wzdłuż każdej z krawędzi
    public static int n = 10;
    public static double k_b = 0.00831;
    public static double T0 = 100; // Kelvin
    public static double a = 0.38; // nm
    public static double m = 39.948; // masa atomu

    // Wektory wyznaczające krawędzi komórki elementarnej kryształu (4)
    public static Atom b0 = new Atom(a, 0, 0);
    public static Atom b1 = new Atom(a / 2.0, a * Math.sqrt(3) / 2, 0);
    public static Atom b2 = new Atom(a / 2.0, a * Math.sqrt(3) / 6, a * Math.sqrt(2.0 / 3.0));
}
