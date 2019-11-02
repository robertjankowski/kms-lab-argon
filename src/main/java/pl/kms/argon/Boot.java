package pl.kms.argon;

import pl.kms.argon.constants.Loader;
import pl.kms.argon.simulation.Simulation;

import static pl.kms.argon.Utils.measureTime;
import static pl.kms.argon.Utils.removeFileIfExists;
import static pl.kms.argon.constants.Constants.T0;

public class Boot {

    public static void main(String[] args) {
        // Argon melting point: 83.75K
        for (int t0 = 500; t0 <= 2000; t0 += 500) {
            Loader.loadParameters("input/5_4_Gaz.txt");
            T0.setValue(t0);
            System.out.println("Simulating for T0=" + T0.getValue());
            Simulation simulation = new Simulation();
            String posFile = "output/5_4_pos_T0=" + T0.getValue() + ".csv";
            String outFile = "output/5_4_out_T0=" + T0.getValue() + ".csv";
            removeFileIfExists(posFile);
            removeFileIfExists(outFile);
            long elapsed = measureTime(simulation::run, posFile, outFile);
            System.out.println("Elapsed: " + elapsed + "ms");
        }
    }
}
