package pl.kms.argon.simulation;

import pl.kms.argon.atom.Atom;
import pl.kms.argon.generator.Generator;
import pl.kms.argon.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static pl.kms.argon.constants.Constants.*;

public class Simulation {

    private List<Atom> atoms = new ArrayList<>((int) N.getValue());
    private Generator generator = new Generator(123);

    public void run(String positionsFile, String meanValuesFile) {
        initialize();
        simulation(positionsFile, meanValuesFile);
    }

    private void initialize() {
        Atom P = new Atom(); // (8)
        int nVal = (int) n.getValue();
        double nDividedBy2 = (nVal - 1) / 2.0;
        for (int i0 = 0; i0 < nVal; i0++) {
            for (int i1 = 0; i1 < nVal; i1++) {
                for (int i2 = 0; i2 < nVal; i2++) {
                    initializeBsVectors();
                    b0.multPos(i0 - nDividedBy2);
                    b1.multPos(i1 - nDividedBy2);
                    b2.multPos(i2 - nDividedBy2);
                    Atom r0 = sumAtomsPosition(b0, b1, b2);

                    double p0 = getInitialMomentum(getKineticEnergy());
                    double p1 = getInitialMomentum(getKineticEnergy());
                    double p2 = getInitialMomentum(getKineticEnergy());
                    r0.setMomentum(p0, p1, p2);

                    P.setMomentum(P.px + p0, P.py + p1, P.pz + p2);
                    atoms.add(r0);
                }
            }
        }
        adjustInitialMomentum(P);
        calculateForcesAndPotentials(atoms);
    }


    private void simulation(String positionsFile, String meanValuesFile) {
        int S = (int) (Sd.getValue() + So.getValue());
        double Tmean = 0;
        double Pmean = 0;
        double Hmean = 0;
        double t = 0.0;
        for (int s = 0; s < S; s++) {
            atoms.forEach(atom -> {
                // (18a)
                double pTmp = 0.5 * tau.getValue();
                atom.px += pTmp * atom.Fx;
                atom.py += pTmp * atom.Fy;
                atom.pz += pTmp * atom.Fz;

                // (18b)
                double rTmp = 1 / m.getValue() * tau.getValue();
                atom.x += rTmp * atom.px;
                atom.y += rTmp * atom.py;
                atom.z += rTmp * atom.pz;
            });
            Pair<Double, Double> p = calculateForcesAndPotentials(atoms);
            atoms.forEach(atom -> {
                double pTmp = 0.5 * tau.getValue();
                atom.px += pTmp * atom.Fx;
                atom.py += pTmp * atom.Fy;
                atom.pz += pTmp * atom.Fz;
            });

            // Temporary values
            double V = p.t;
            double P = p.v;
            double kineticEnergy = calculateKineticEnergy();
            double T = calculateTemperature(kineticEnergy);
            double H = calculateHamiltonian(kineticEnergy, V);
            //System.out.println("T: " + T + ", H: " + H + ", V: " + V);

            // Accumulate mean values
            if (s >= So.getValue()) {
                Tmean += T;
                Pmean += P;
                Hmean += H;
            }

            if (s % Sout.getValue() == 0)
                saveTemporaryValues(meanValuesFile, t, H, V, T, P);
            if (s % Sxyz.getValue() == 0)
                savePositionsWithEnergy(positionsFile);
            t += tau.getValue();
        }
        Tmean /= Sd.getValue();
        Pmean /= Sd.getValue();
        Hmean /= Sd.getValue();
        System.out.println("T_mean: = " + Tmean);
        System.out.println("P_mean: = " + Pmean);
        System.out.println("H_mean: = " + Hmean);
    }

    private Pair<Double, Double> calculateForcesAndPotentials(List<Atom> atoms) {
        double V = 0.0;
        double P = 0.0;
        atoms.forEach(atom -> {
            atom.Fx = 0;
            atom.Fy = 0;
            atom.Fz = 0;
        });
        for (int i = 0; i < N.getValue(); i++) {
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
                P += Math.sqrt(FxS * FxS + FyS * FyS + FzS * FzS);
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
        P /= 4 * Math.PI * Math.pow(L.getValue(), 2);
        return new Pair<>(V, P);
    }

    private double calculateKineticEnergy() {
        return atoms.stream()
                .mapToDouble(atom -> Math.pow(atom.absMomentum(), 2))
                .sum() / (2 * m.getValue());
    }

    private double calculateTemperature(double kineticEnergy) {
        return 2 / (3 * N.getValue() * k_b.getValue()) * kineticEnergy;
    }

    private double calculateHamiltonian(double kineticEnergy, double V) {
        return kineticEnergy + V;
    }

    private void adjustInitialMomentum(Atom P) {
        // (8)
        atoms.forEach(atom -> {
            atom.px -= P.px / N.getValue();
            atom.py -= P.py / N.getValue();
            atom.pz -= P.pz / N.getValue();
        });
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

    private void saveTemporaryValues(String filename, double t, double H, double V, double T, double P) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(filename, true))) {
            out.println(t + "," + H + "," + V + "," + T + "," + P);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void savePositionsWithEnergy(String filename) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(filename, true))) {
            atoms.forEach(atom -> {
                double energy = Math.pow(atom.absMomentum(), 2) / (2 * m.getValue());
                out.println(atom.x + "," + atom.y + "," + atom.z + "," + energy);
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveMetricsForStability(double Hmean, double Pmean, double Tmean) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream("stability_v1.csv", true))) {
            out.println(tau.getValue() + "," + Hmean + "," + Pmean + "," + Tmean);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
