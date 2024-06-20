package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLambdaImp extends JavaGrepImp{
    public static void main(String[] args) {
        if(args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }
        // Use default logger config
        BasicConfigurator.configure();
        //creating JavaGrepLambdaImp instead of JavaGrepImp
        //JavaGrepLambdaImp inherits all methods except two override methods
        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try{
            //calling parent method,
            //but it will override method (in this class)
            javaGrepLambdaImp.process();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Implement using lambda and streams API
     */
    @Override
    public List<String> readLines(File inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error reading file: " + inputFile.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }
    /**
     * Implement using lambda and streams API
     */
    @Override
    public List<File> listFiles(String rootDir) {
        try {
            Path rootPath = Paths.get(rootDir);
            logger.debug("Root path: {}", rootPath);
            if (!Files.exists(rootPath)) {
                throw new IllegalArgumentException("Root directory does not exist: " + rootDir);
            }

            try (Stream<Path> fileStream = Files.walk(rootPath)) {
                return fileStream
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            logger.error("Error walking file tree: " + rootDir, e);
            throw new RuntimeException(e);
        }
    }
}
