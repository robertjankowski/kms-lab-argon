package pl.kms.argon.generator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private Random random;
    private ThreadLocalRandom threadLocalRandom;

    public Generator() {
        random = new Random();
        threadLocalRandom = ThreadLocalRandom.current();
    }

    public double uniform(int min, int max) {
        return threadLocalRandom.nextDouble(min, max);
    }

    public boolean nextBoolean() {
        return random.nextBoolean();
    }
}
