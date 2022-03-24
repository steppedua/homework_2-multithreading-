package ru.digitalhabbits.homework2;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BrokenBarrierException;

class FileProcessorTest {

    private Path resFile;
    private Path textFile;
    private Path equalsFile;

    @BeforeEach
    public void init() {
        this.resFile = Path.of("/Users/edstepa/Desktop/ДЗ2/src/main/java/ru/digitalhabbits/homework2/lol2.txt");
        this.textFile = Path.of("/Users/edstepa/Desktop/ДЗ2/src/main/java/ru/digitalhabbits/homework2/lol.txt");
        this.equalsFile = Path.of("/Users/edstepa/Desktop/ДЗ2/src/main/java/ru/digitalhabbits/homework2/result.txt");
    }

    @Test
    void process() throws BrokenBarrierException, InterruptedException, IOException {
        new FileProcessor().process(textFile.toString(), resFile.toString());

        Assertions.assertEquals(
                FileUtils.readLines(new File(equalsFile.toString()), "UTF-8"),
                FileUtils.readLines(new File(resFile.toString()), "UTF-8")
        );
    }
}