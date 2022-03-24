package ru.digitalhabbits.homework2;

import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.TreeMap;
import java.util.concurrent.*;

import static org.slf4j.LoggerFactory.getLogger;

public class FileProcessor {
    private static final Logger logger = getLogger(FileProcessor.class);
//    public static final int CHUNK_SIZE = 2 * getRuntime().availableProcessors();

    private static final TreeMap<Integer, String> lineNumberAndResultConcatenationPairMap = new TreeMap<>();

    public void process(@Nonnull String processingFileName, @Nonnull String resultFileName) throws InterruptedException, BrokenBarrierException {

        //Условно CHUNK_SIZE заменили на количество линий в текстовом файле
        int chunkSize = getNumberLinesInFile(processingFileName);

        final ExecutorService executorService = Executors.newFixedThreadPool(chunkSize);

        final File file = new File(processingFileName);
        final File resultFile = new File(resultFileName);

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(
                chunkSize,
                new FileWriter(resultFile, lineNumberAndResultConcatenationPairMap)
        );

        try (final LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file))) {
            String line;
            while ((line = lineNumberReader.readLine()) != null) {

                executorService.submit(new LineCounterProcessor(
                        line,
                        lineNumberReader.getLineNumber(),
                        lineNumberAndResultConcatenationPairMap,
                        cyclicBarrier)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        cyclicBarrier.await();

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        logger.info("Finish main thread {}", Thread.currentThread().getName());
    }

    private static int getNumberLinesInFile(@Nonnull String processingFileName) {
        checkFileExists(processingFileName);
        try (final LineNumberReader lineNumberReader = new LineNumberReader(new BufferedReader(new FileReader(processingFileName)))) {
            int countLines = 0;
            while (lineNumberReader.readLine() != null) {
                countLines++;
            }
            //На 1 больше, т.к. в строке 43 есть еще один cyclicBarrier.await();
            return countLines + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static void checkFileExists(@Nonnull String fileName) {
        final File file = new File(fileName);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("File '" + fileName + "' not exists");
        }
    }
}
