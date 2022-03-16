package ru.digitalhabbits.homework2;

import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;

import static java.nio.charset.Charset.defaultCharset;
import static org.slf4j.LoggerFactory.getLogger;

public class FileProcessor {
    private static final Logger logger = getLogger(FileProcessor.class);
    //TODO: Тут работает с двумя потокам, если > 2 потоков, то будет простой, метод shutdownNow()
    // не сработал, кидает кучу Exception
    //public static final int CHUNK_SIZE = 2 * getRuntime().availableProcessors();
    public static final int CHUNK_SIZE = 2;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(CHUNK_SIZE);
    private static final Exchanger<String> exchanger = new Exchanger<>();
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(CHUNK_SIZE);

    public void process(@Nonnull String processingFileName, @Nonnull String resultFileName) throws InterruptedException, BrokenBarrierException {
        checkFileExists(processingFileName);

        final File file = new File(processingFileName);
        final File resFile = new File(resultFileName);

        // TODO: NotImplemented: запускаем FileWriter в отдельном потоке
        try (final Scanner scanner = new Scanner(file, defaultCharset())) {
            while (scanner.hasNext()) {

                // TODO: NotImplemented: вычитываем CHUNK_SIZE строк для параллельной обработки
                //  Для чего это???

                // TODO: NotImplemented: обрабатывать строку с помощью LineProcessor. Каждый поток обрабатывает свою строку.
                executorService.submit(new LineCounterProcessor(scanner.nextLine(), exchanger, cyclicBarrier));

                // TODO: NotImplemented: добавить обработанные данные в результирующий файл
                executorService.submit(new FileWriter(resFile, exchanger, cyclicBarrier));

            }
        } catch (IOException exception) {
            logger.error("", exception);
        }

        // TODO: NotImplemented: остановить поток writerThread

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        logger.info("Finish main thread {}", Thread.currentThread().getName());
    }

    private void checkFileExists(@Nonnull String fileName) {
        final File file = new File(fileName);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("File '" + fileName + "' not exists");
        }
    }
}
