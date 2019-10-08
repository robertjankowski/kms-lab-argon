package pl.kms.argon.simulation;

import pl.kms.argon.atom.Atom;
import pl.kms.argon.constants.Parameter;
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
    private Parameter VP = new Parameter("V_P", 0.0); // (9)
    private Parameter VS = new Parameter("V_S", 0.0); // (10)
    private Parameter V = new Parameter("V", 0.0);    // (11)
    private Parameter pressure = new Parameter("P", 0.0); // (15)

    public void run() {
        initialize();
        saveForcesToFile("forces.txt");
    }

    private void initialize() {
        double nVal = n.getValue();
        double nDividedBy2 = (nVal - 1) / 2.0;
        for (int i0 = 0; i0 < nVal; i0++) {
            for (int i1 = 0; i1 < nVal; i1++) {
                for (int i2 = 0; i2 < nVal; i2++) {

                    int i = (int) (i0 + i1 * nVal + i2 * nVal * nVal);
                    b0.multPos(i0 - nDividedBy2);
                    b1.multPos(i1 - nDividedBy2);
                    b2.multPos(i2 - nDividedBy2);
                    Atom r0 = sumAtomsPosition(b0, b1, b2);

                    double p0 = getInitialMomentum(getKineticEnergy());
                    double p1 = getInitialMomentum(getKineticEnergy());
                    double p2 = getInitialMomentum(getKineticEnergy());
                    r0.setMomentum(p0, p1, p2);
                    r0.pos = i;

                    P.setMomentum(P.px + p0, P.py + p1, P.pz + p2);
                    atoms.add(r0);

                    initializeBsVectors();
                }
            }
        }
        adjustInitialMomentum();

        // Potential and forces
        for (int i = 0; i < N.getValue(); i++) {
            Atom atom = atoms.get(i);
            Atom newAtom = new Atom(atom);
            Atom tmpAtom = new Atom(atom);
            double ri = atom.absPosition();
            if (ri >= L.getValue()) {
                // (10)
                double fV = 0.5 * f.getValue() * Math.pow(ri - L.getValue(), 2);
                VS.setValue(VS.getValue() + fV);
                // (14)
                atom.multPos(f.getValue() * (L.getValue() - ri) / ri);
                newAtom.setForces(atom.x, atom.y, atom.z);
            }
            // (15)
            pressure.setValue(pressure.getValue() + newAtom.absForce());
            for (int j = 0; j < i - 1; j++) {
                Atom atomJ = atoms.get(j);
                double rij = newAtom.absPositionSubstraction(atomJ);
                // (9)
                if (rij < 1e-5)
                    rij = 1e-5; // Produce NaN, e.g. maybe introduce epsilon=1e-6
                double R12 = Math.pow(R.getValue() / rij, 12);
                double R6 = Math.pow(R.getValue() / rij, 6);
                double vp = e.getValue() * (R12 - 2 * R6);
                VP.setValue(VP.getValue() + vp);
                // (13)
                double fp = 12 * e.getValue() * (R12 - R6) / (rij * rij);
                tmpAtom.substractPosition(atomJ);
                double Fx = fp * tmpAtom.x;
                double Fy = fp * tmpAtom.y;
                double Fz = fp * tmpAtom.z;
                newAtom.setForces(newAtom.Fx + Fx, newAtom.Fy + Fy, newAtom.Fz + Fz);
                atomJ.setForces(atomJ.Fx - Fx, atomJ.Fy - Fy, atomJ.Fz - Fz);
                atoms.set(j, atomJ);
            }
            atoms.set(i, newAtom);
        }
        V.setValue(VS.getValue() + VP.getValue());
        pressure.setValue(pressure.getValue() / (4 * Math.PI * L.getValue() * L.getValue()));
        System.out.println("V = " + V + ", p = " + pressure + ", T = " + calculateTemperature());
    }

    private double calculateTemperature() {
        double energy = atoms.stream()
                .mapToDouble(atom -> atom.absMomentum() / (2 * m.getValue()))
                .sum();
        return 2 / (3 * N.getValue() * k_b.getValue()) * energy;
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

    public void saveForcesToFile(String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            for (Atom atom : atoms)
                out.print(atom.forcesToString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
