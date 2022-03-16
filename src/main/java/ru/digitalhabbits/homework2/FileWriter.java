package ru.digitalhabbits.homework2;

import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

import static java.lang.Thread.currentThread;
import static org.slf4j.LoggerFactory.getLogger;

public class FileWriter implements Runnable {
    private static final Logger logger = getLogger(FileWriter.class);

    private final File file;
    private final Exchanger<String> exchanger;
    private String message;
    //        private final Phaser phaser;
    private final CyclicBarrier cyclicBarrier;

    //        public FileWriter(File resFile, Exchanger<String> exchanger, Phaser phaser) {
//        this.file = resFile;
//        this.exchanger = exchanger;
//        this.phaser = phaser;
//        phaser.register();
//    }
    public FileWriter(File resFile, Exchanger<String> exchanger, CyclicBarrier cyclicBarrier) {
        this.file = resFile;
        this.exchanger = exchanger;
        this.cyclicBarrier = cyclicBarrier;
    }


    @Override
    public void run() {

        logger.info("Started writer thread {}", currentThread().getName());

        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8)) {

            logger.info("Phase {} in FileWriter started", 2);

            message = exchanger.exchange("edfdswf", 1000, TimeUnit.MINUTES);

            logger.info("FileWriter {}", message);

            out.write(message);

            logger.info("Phase {} in FileWriter finished", 2);
//            Thread.sleep(100);
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        logger.info("Finish writer thread {}", currentThread().getName());
    }
}
