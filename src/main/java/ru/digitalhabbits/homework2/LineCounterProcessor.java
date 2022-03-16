package ru.digitalhabbits.homework2;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

public class LineCounterProcessor implements LineProcessor<Integer>, Runnable {
    private static final Logger logger = getLogger(LineCounterProcessor.class);

    private static final int BASE_DELAY = 200;
    private static final int RANDOM_DELAY = 200;

    private final Random random = new Random(currentTimeMillis());

    private final String lineInFile;
    private final Exchanger<String> exchanger;
    //    private final Phaser phaser;
    private final CyclicBarrier cyclicBarrier;

    //    public LineCounterProcessor(String line, Exchanger<String> exchanger, Phaser phaser) {
//        this.lineInFile = line;
//        this.exchanger = exchanger;
//        this.phaser = phaser;
//        phaser.register();
//    }
    public LineCounterProcessor(String line, Exchanger<String> exchanger, CyclicBarrier cyclicBarrier) {
        this.lineInFile = line;
        this.exchanger = exchanger;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Nonnull
    @Override
    public Pair<String, Integer> process(@Nonnull String line) {
        randomSleep();
        // TODO: NotImplemented: подсчет кол-ва символов в строке + произвольная задержка randomSleep()
        return Pair.of(line, line.length());
    }

    private void randomSleep() {
        try {
            Thread.sleep(BASE_DELAY + random.nextInt(RANDOM_DELAY));
        } catch (InterruptedException exception) {
            logger.warn("", exception);
        }
    }

    @Override
    public void run() {

        try {
            logger.info("Phase {} in LineCounterProcessor started", 1);
            Pair<String, Integer> process = process(lineInFile);
            String s = process.getLeft() + " " + process.getRight() + "\n";

//            exchanger.exchange(s, 1000, TimeUnit.MINUTES);
            logger.info("LineCounterProcessor {}", exchanger.exchange(s, 1000, TimeUnit.HOURS));


            logger.info("Phase {} in LineCounterProcessor finished", 1);
//            Thread.sleep(100);
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }


    }
}
