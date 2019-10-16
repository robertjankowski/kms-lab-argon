package pl.kms.argon;

import pl.kms.argon.constants.Loader;
import pl.kms.argon.simulation.Simulation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Boot {

    public static void main(String[] args) {
        Loader.loadParameters("input/5_1_Test_programu.txt");
        Simulation simulation = new Simulation();
        String posFile = "output/5_1_pos.csv";
        String outFile = "output/5_1_out_tau=1e-2.csv";
        //  removeFileIfExists(posFile);
        // removeFileIfExists(outFile);
        simulation.run(posFile, outFile);
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
