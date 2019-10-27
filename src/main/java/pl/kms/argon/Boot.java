package pl.kms.argon;

import pl.kms.argon.constants.Loader;
import pl.kms.argon.simulation.Simulation;

import static pl.kms.argon.Utils.*;
import static pl.kms.argon.constants.Constants.T0;

public class Boot {

    public static void main(String[] args) {
        // Argon melting point: 83.75K
        Loader.loadParameters("input/5_4_Gaz.txt");
        Simulation simulation = new Simulation();
        String posFile = "output/5_4_pos_T0=" + T0.getValue() + ".csv";
        String outFile = "output/5_4_T0=" + T0.getValue() + ".csv";
        removeFileIfExists(posFile);
        removeFileIfExists(outFile);
        long elapsed = measureTime(simulation::run, posFile, outFile);
        System.out.println("Elapsed: " + elapsed + "ms");
    }
}
