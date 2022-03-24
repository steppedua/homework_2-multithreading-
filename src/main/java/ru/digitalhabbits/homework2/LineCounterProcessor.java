package ru.digitalhabbits.homework2;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

public class LineCounterProcessor implements LineProcessor<Integer>, Runnable {
    private static final Logger logger = getLogger(LineCounterProcessor.class);

    private static final int BASE_DELAY = 200;
    private static final int RANDOM_DELAY = 200;

    private final Random random = new Random(currentTimeMillis());

    private final String valueLineInFile;
    private final TreeMap<Integer, String> lineNumberAndResultConcatenationPairMap;
    private final int lineNumber;
    private final CyclicBarrier cyclicBarrier;

    public LineCounterProcessor(String valueLineInFile, int lineNumber, TreeMap<Integer, String> lineNumberAndResultConcatenationPairMap, CyclicBarrier cyclicBarrier) {
        this.valueLineInFile = valueLineInFile;
        this.lineNumber = lineNumber;
        this.lineNumberAndResultConcatenationPairMap = lineNumberAndResultConcatenationPairMap;
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

        logger.info("Phase {} in LineCounterProcessor started", 1);
        Pair<String, Integer> resultProcess = process(valueLineInFile);
        String resultConcatenationPair = resultProcess.getLeft() + " " + resultProcess.getRight();

        lineNumberAndResultConcatenationPairMap.put(lineNumber, resultConcatenationPair);

        logger.info("Phase {} in LineCounterProcessor finished", 1);

        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }


    }
}
