package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        Random r = new Random();
        ThreadLocalRandom threadRandom = ThreadLocalRandom.current();

        int N = 1000; // liczba atomów
        int n = 10; // liczba atomów wzdłuż każdej z krawędzi
        double k_b = 0.00831;
        double T0 = 100; // K
        double a = 0.38; // n
        double m = 39.948; // masa atomu

        List<Point> atoms = new ArrayList<>(N);

        Point b0 = new Point(a, 0, 0);
        Point b1 = new Point(a / 2.0, a * Math.sqrt(3) / 2, 0);
        Point b2 = new Point(a / 2.0, a * Math.sqrt(3) / 6, a * Math.sqrt(2.0 / 3.0));
        Point P = new Point();

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    int pos = i + j * n + k * n * n;
                    double r0 = (i - (n - 1) / 2.0) * b0.x +
                            (j - (n - 1) / 2.0) * b1.x +
                            (k - (n - 1) / 2.0) * b2.x;
                    double r1 = (i - (n - 1) / 2.0) * b0.y +
                            (j - (n - 1) / 2.0) * b1.y +
                            (k - (n - 1) / 2.0) * b2.y;
                    double r2 = (i - (n - 1) / 2.0) * b0.z +
                            (j - (n - 1) / 2.0) * b1.z +
                            (k - (n - 1) / 2.0) * b2.z;
                    Point p = new Point(r0, r1, r2);
                    p.pos = pos;

                    double E_kin_x = -0.5 * k_b * T0 * Math.log(threadRandom.nextDouble(0, 1));
                    double E_kin_y = -0.5 * k_b * T0 * Math.log(threadRandom.nextDouble(0, 1));
                    double E_kin_z = -0.5 * k_b * T0 * Math.log(threadRandom.nextDouble(0, 1));

                    double p0, p1, p2;
                    p0 = Math.sqrt(2 * m * E_kin_x);
                    p1 = Math.sqrt(2 * m * E_kin_y);
                    p2 = Math.sqrt(2 * m * E_kin_z);
                    if (r.nextBoolean())
                        p0 *= -1;
                    if (r.nextBoolean())
                        p1 *= -1;
                    if (r.nextBoolean())
                        p2 *= -1;

                    p.px = p0;
                    p.py = p1;
                    p.pz = p2;

                    // (8)
                    P.px += p0;
                    P.py += p1;
                    P.pz += p2;

                    //System.out.println(p0 + "\t" + p1 + "\t" + p2);
                    atoms.add(i + j + k, p);
                }
            }
        }

        // Środek masy, pęd (8)
        P.px /= N;
        P.py /= N;
        P.pz /= N;
        for (int i = 0; i < N; ++i) {
            Point p_prim = atoms.get(i);
            p_prim.px -= P.px;
            p_prim.py -= P.py;
            p_prim.pz -= P.pz;
            atoms.set(i, p_prim);
        }

        try (PrintWriter out = new PrintWriter("xyz_1.dat")) {
            for (int i = 0; i < atoms.size(); ++i) {
                String text = atoms.get(i).momentumsToFile();
                System.out.println(text);
                out.print(text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
