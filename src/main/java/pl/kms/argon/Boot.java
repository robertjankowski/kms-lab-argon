package pl.kms.argon;

import pl.kms.argon.constants.Loader;
import pl.kms.argon.simulation.Simulation;

import static pl.kms.argon.Utils.measureTime;
import static pl.kms.argon.Utils.removeFileIfExists;
import static pl.kms.argon.constants.Constants.So;

public class Boot {

    public static void main(String[] args) {
        // Argon melting point: 83.75K
        for (int s0 = 0; s0 <= 10000; s0 += 500) {
            Loader.loadParameters("input/5_4_Gaz.txt");
            So.setValue(s0);
            System.out.println("Simulating for S0=" + So.getValue());
            Simulation simulation = new Simulation();
            String posFile = "output/5_4_pos_S0=" + So.getValue() + ".csv";
            String outFile = "output/5_4_out_S0=" + So.getValue() + ".csv";
            removeFileIfExists(posFile);
            removeFileIfExists(outFile);
            long elapsed = measureTime(simulation::run, posFile, outFile);
            System.out.println("Elapsed: " + elapsed + "ms");
        }
    }
}
