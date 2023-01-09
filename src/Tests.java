import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.concurrent.*;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);

    @Test
    public void partialTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        Task<Integer> task = Task.createTask(() -> {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);
        Future<Integer> sumTask = customExecutor.submit(task);
        final int sum;
        try {
            sum = sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Sum of 1 through 10 = " + sum);
        Task<Integer> task2 = Task.createTask(() -> {
            int sum2 = 1;
            for (int i = 1; i <= 100; i++) {
                sum2 *= i;
            }
            return sum2;
        }, TaskType.COMPUTATIONAL);
        Future<Integer> sumTask2 = customExecutor.submit(task2);
        final int sum2;
        try {
            sum2 = sumTask2.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        Callable<Double> callable1 = () -> {
            return 1000 * Math.pow(1.02, 5);
        };
        Task<Double> task3=Task.createTask(callable1,TaskType.COMPUTATIONAL);
        Future<Double> sumTask3 = customExecutor.submit(task3);
        final double sum3;
        try {
            sum3 = sumTask3.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        });
        customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        });
        customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        });
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
        // var is used to infer the declared type automatically
        Future<Double> priceTask = customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        Future<String> reverseTask = customExecutor.submit(callable2, TaskType.IO);
        for (int i = 0; i < 100; i++) {
            customExecutor.submit(callable2, TaskType.IO);
        }
        customExecutor.submit(callable2, TaskType.IO);

        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = priceTask.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        reverseTask = customExecutor.submit(callable2, TaskType.IO);
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());

        customExecutor.gracefullyTerminate();
        Assertions.assertThrows(RejectedExecutionException.class, ()-> customExecutor.submit(() ->{return 5;}));
    }
}