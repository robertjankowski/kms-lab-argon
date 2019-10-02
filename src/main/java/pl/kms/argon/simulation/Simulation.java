package pl.kms.argon.simulation;

import pl.kms.argon.atom.Atom;
import pl.kms.argon.generator.Generator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static pl.kms.argon.constants.Constants.*;

public class Simulation {

    private List<Atom> atoms = new ArrayList<>((int) N.getValue());
    private Generator generator = new Generator();
    private Atom P = new Atom(); // (8)

    public void run() {
        initialize();
        saveMomentumToFile("test.dat");
    }

    private void initialize() {
        for (int i0 = 0; i0 < n.getValue(); i0++) {
            for (int i1 = 0; i1 < n.getValue(); i1++) {
                for (int i2 = 0; i2 < n.getValue(); i2++) {

                    int i = (int) (i0 + i1 * n.getValue() + i2 * n.getValue() * n.getValue());
                    Atom b00 = b0.mult(i0 - (n.getValue() - 1) / 2.0);
                    Atom b11 = b1.mult(i1 - (n.getValue() - 1) / 2.0);
                    Atom b22 = b2.mult(i2 - (n.getValue() - 1) / 2.0);

                    Atom r0 = sumAtomsPosition(b00, b11, b22);

                    double p0 = getInitialMomentum(getKineticEnergy());
                    double p1 = getInitialMomentum(getKineticEnergy());
                    double p2 = getInitialMomentum(getKineticEnergy());
                    r0.setMomentum(p0, p1, p2);

                    P.setMomentum(P.px + p0, P.py + p1, P.pz + p2);
                    atoms.add(r0);
                }
            }
        }
        adjustInitialMomentum();
    }

    private void adjustInitialMomentum() {
        // (8)
        for (int i = 0; i < N.getValue(); i++) {
            Atom pPrim = atoms.get(i);
            pPrim.setMomentum(
                    pPrim.px - P.px / N.getValue(),
                    pPrim.py - P.py / N.getValue(),
                    pPrim.pz - P.pz / N.getValue()
            );
            atoms.set(i, pPrim);
        }
    }

    private double getInitialMomentum(double E_k) {
        double p = Math.sqrt(2 * m.getValue() * E_k);
        if (generator.nextBoolean())
            p *= -1;
        return p;
    }

    private double getKineticEnergy() {
        return -0.5 * k_b.getValue() * T0.getValue() * Math.log(generator.uniform(0, 1));
    }

    private Atom sumAtomsPosition(Atom... atoms) {
        double x = 0, y = 0, z = 0;
        for (Atom atom : atoms) {
            x += atom.x;
            y += atom.y;
            z += atom.z;
        }
        Atom a = new Atom();
        a.setPosition(x, y, z);
        return a;
    }

    private String showPositions() {
        StringBuilder positions = new StringBuilder();
        for (Atom atom : atoms) {
            positions.append(atom.positionsToString());
        }
        return positions.toString();
    }

    public void savePositionsToFile(String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            for (Atom atom : atoms)
                out.print(atom.positionsToString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveMomentumToFile(String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            for (Atom atom : atoms)
                out.print(atom.momentumToString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
