package pl.kms.argon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

public class Utils {

    static long measureTime(BiConsumer<String, String> func, String posFile, String outFile) {
        long start = System.currentTimeMillis();
        func.accept(posFile, outFile);
        return System.currentTimeMillis() - start;
    }

    static void removeFileIfExists(String file) {
        Path fileToDeletePath = Paths.get(file);
        try {
            Files.delete(fileToDeletePath);
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    public static double power(double x, int pow) {
        double result = 1;
        for (int i = 0; i < pow; ++i) {
            result *= x;
        }
        return result;
    }

}
