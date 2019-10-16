package pl.kms.argon.generator;

import java.util.Random;

public class Generator {

    private Random random;

    public Generator(int seed) {
        random = new Random(seed);
    }

    public double uniform(int min, int max) {
        return min + (max - min) * random.nextDouble();
    }

    public boolean nextBoolean() {
        return random.nextBoolean();
    }
}
