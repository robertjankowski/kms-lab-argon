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
    private Parameter V = new Parameter("V", 0.0);    // (11)
    private Parameter pressure = new Parameter("P", 0.0); // (15)

    public void run() {
        initialize();
        simulation();
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
        V.setValue(calculateForcesAndPotentials());
    }


    private void simulation() {
        int S = (int) (Sd.getValue() + So.getValue());
        double Tmean = 0;
        double Pmean = 0;
        double Hmean = 0;
        for (int s = 0; s < S; s++) {
            // Iterate over every atoms
            for (int i = 0; i < (int) N.getValue(); i++) {
                Atom atomI = atoms.get(i);

                // (18a)
                atomI.px += 0.5 * atomI.Fx * tau.getValue();
                atomI.py += 0.5 * atomI.Fy * tau.getValue();
                atomI.pz += 0.5 * atomI.Fz * tau.getValue();

                // (18b)
                double rTmp = 1 / m.getValue() * tau.getValue();
                atomI.x += rTmp * atomI.px;
                atomI.y += rTmp * atomI.py;
                atomI.z += rTmp * atomI.pz;
                atoms.set(i, atomI);
            }
            double v = calculateForcesAndPotentials();
            for (int i = 0; i < (int) N.getValue(); i++) {
                Atom atomI = atoms.get(i);
                // (18c)
                atomI.px += 0.5 * atomI.Fx * tau.getValue();
                atomI.py += 0.5 * atomI.Fy * tau.getValue();
                atomI.pz += 0.5 * atomI.Fz * tau.getValue();
                atoms.set(i, atomI);
            }

            // Temporary values
            V.setValue(v);
            double T = calculateTemperature();
            double H = calculateHamiltonian();
            System.out.println("T: " + T + ", H: " + H + ", V: " + V.getValue());

            // Accumulate mean values
            if (s >= So.getValue()) {
                Tmean += T;
                Pmean += pressure.getValue();
                Hmean += H;
            }
        }
        Tmean /= Sd.getValue();
        Pmean /= Sd.getValue();
        Hmean /= Sd.getValue();
        System.out.println("T_mean: = " + Tmean);
        System.out.println("P_mean: = " + Pmean);
        System.out.println("H_mean: = " + Hmean);
    }

    private double calculateForcesAndPotentials() {
        double V = 0;
        pressure.setValue(0);
        atoms.forEach(atom -> {
            atom.Fx = 0;
            atom.Fy = 0;
            atom.Fz = 0;
        });
        for (int i = 0; i < (int) N.getValue(); i++) {
            Atom atomI = atoms.get(i);
            double ri = atomI.absPosition();
            if (ri >= L.getValue()) {
                // (10)
                double Vtmp = 0.5 * f.getValue() * Math.pow(ri - L.getValue(), 2);
                V += Vtmp;

                // (14)
                double fTmp = f.getValue() * (L.getValue() - ri) / ri;
                double FxS = fTmp * atomI.x;
                double FyS = fTmp * atomI.y;
                double FzS = fTmp * atomI.z;
                atomI.Fx += FxS;
                atomI.Fy += FyS;
                atomI.Fz += FzS;

                // (15)
                double p = Math.sqrt(FxS * FxS + FyS * FyS + FzS * FzS);
                pressure.setValue(pressure.getValue() + p);
            }

            for (int j = 0; j < i; j++) {
                Atom atomJ = atoms.get(j);
                double xij = atomI.x - atomJ.x;
                double yij = atomI.y - atomJ.y;
                double zij = atomI.z - atomJ.z;
                double rij = Math.sqrt(xij * xij + yij * yij + zij * zij);
                double R12 = Math.pow(R.getValue() / rij, 12);
                double R6 = Math.pow(R.getValue() / rij, 6);
                // (9)
                double Vtmp = e.getValue() * (R12 - 2 * R6);
                V += Vtmp;

                // (13)
                double ftmp = 12 * e.getValue() * (R12 - R6) / (rij * rij);
                double FxP = ftmp * xij;
                double FyP = ftmp * yij;
                double FzP = ftmp * zij;

                atomI.Fx += FxP;
                atomI.Fy += FyP;
                atomI.Fz += FzP;

                atomJ.Fx -= FxP;
                atomJ.Fy -= FyP;
                atomJ.Fz -= FzP;
                atoms.set(j, atomJ);
            }
            atoms.set(i, atomI);
        }
        pressure.setValue(pressure.getValue() / (4 * Math.PI * Math.pow(L.getValue(), 2)));
        return V;
    }

    private double calculateTemperature() {
        double energy = atoms.stream()
                .mapToDouble(atom -> Math.pow(atom.absMomentum(), 2) / (2 * m.getValue()))
                .sum();
        return 2 / (3 * N.getValue() * k_b.getValue()) * energy;
    }

    private double calculateHamiltonian() {
        double energy = atoms.stream()
                .mapToDouble(atom -> Math.pow(atom.absMomentum(), 2) / (2 * m.getValue()))
                .sum();
        return energy + V.getValue();
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
