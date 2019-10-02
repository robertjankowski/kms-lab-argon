package pl.kms.argon;

import pl.kms.argon.simulation.Simulation;

public class Boot {

    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.run();
        simulation.saveMomentumToFile("test.dat");
    }

}
