package pl.kms.argon;

import pl.kms.argon.constants.Loader;
import pl.kms.argon.simulation.Simulation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static pl.kms.argon.constants.Constants.*;

public class Boot {

    public static void main(String[] args) {
        // melting point: 83.75K
        double initialT = 50;
        for (int i = 0; i < 10; ++i) {
            Loader.loadParameters("input/5_3_Topnienie_krysztalu.txt");
            T0.setValue(initialT);
            Simulation simulation = new Simulation();
            String posFile = "output/5_3_pos_T=" + T0.getValue() + ".csv";
            String outFile = "output/tmp.csv";
            removeFileIfExists(posFile);
            removeFileIfExists(outFile);
            long start = System.currentTimeMillis();
            simulation.run(posFile, outFile);
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("Elapsed: " + elapsed + "ms");
            initialT += 10;
        }
    }

    private static void removeFileIfExists(String file) {
        Path fileToDeletePath = Paths.get(file);
        try {
            Files.delete(fileToDeletePath);
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
