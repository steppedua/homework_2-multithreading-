package ru.digitalhabbits.homework2;

import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.lang.Thread.currentThread;
import static org.slf4j.LoggerFactory.getLogger;

public class FileWriter implements Runnable {
    private static final Logger logger = getLogger(FileWriter.class);

    private final File resultFile;
    private final TreeMap<Integer, String> lineNumberAndResultConcatenationPairMap;

    public FileWriter(File resultFile, TreeMap<Integer, String> lineNumberAndResultConcatenationPairMap) {
        this.resultFile = resultFile;
        this.lineNumberAndResultConcatenationPairMap = lineNumberAndResultConcatenationPairMap;
    }

    @Override
    public void run() {

        logger.info("Started writer thread {}", currentThread().getName());

        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(resultFile, true), StandardCharsets.UTF_8)) {

            String collect = lineNumberAndResultConcatenationPairMap
                    .values()
                    .stream()
                    .parallel()
                    .collect(Collectors.joining("\n"));

            out.write(collect);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Finish writer thread {}", currentThread().getName());
    }
}
