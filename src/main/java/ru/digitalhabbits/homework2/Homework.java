package ru.digitalhabbits.homework2;

import javax.annotation.Nonnull;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

public class Homework {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
//        final String processingFileName = getProcessingFileName(args);
//        final String resultFileName = getResultFileName(args);
        new FileProcessor()
                .process("/Users/edstepa/Desktop/ДЗ2/src/main/java/ru/digitalhabbits/homework2/lol.txt",
                        "/Users/edstepa/Desktop/ДЗ2/src/main/java/ru/digitalhabbits/homework2/lol2.txt");
    }

    @Nonnull
    private static String getProcessingFileName(String[] args) {
        return getFileName(args, 0, "Enter processing file: ");
    }

    @Nonnull
    private static String getResultFileName(String[] args) {
        return getFileName(args, 1, "Enter result file: ");
    }

    @Nonnull
    private static String getFileName(String[] args, int index, String message) {
        if (args.length >= index + 1) {
            return args[index];
        }
        final Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }
}
