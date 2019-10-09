package pl.kms.argon;

import pl.kms.argon.constants.Loader;
import pl.kms.argon.simulation.Simulation;

public class Boot {

    public static void main(String[] args) {
//        Loader.loadParameters("input_parameters.txt");
        Simulation simulation = new Simulation();
        simulation.run();
    }

}
