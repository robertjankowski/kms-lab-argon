package pl.kms.argon.constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static pl.kms.argon.constants.Constants.*;

public class Loader {

    public static void loadParameters(String filename) {
        try {
            List<Parameter> inputParameters = Files
                    .lines(Path.of(filename))
                    .map(parameter -> parameter.split("#"))
                    .map(parameter -> new Parameter(parameter[1].strip(), Double.parseDouble(parameter[0])))
                    .collect(Collectors.toList());
            List<String> names = inputParameters
                    .stream()
                    .map(Parameter::getName)
                    .collect(Collectors.toList());
            for (int i = 0; i < parameters.size(); i++) {
                Parameter param = parameters.get(i);
                if (names.contains(param.getName())) {
                    double inputParameterValue = inputParameters
                            .stream()
                            .filter(p -> p.getName().equals(param.getName()))
                            .map(Parameter::getValue)
                            .findFirst()
                            .orElse(0.0);
                    System.out.println("Initializing: [" + param.getName() + "] value: " + inputParameterValue);
                    param.setValue(inputParameterValue);
                    parameters.set(i, param);
                }
            }
            reinitializeConstants();
            initializeBsVectors();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
