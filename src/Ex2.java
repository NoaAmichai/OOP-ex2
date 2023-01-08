import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
public class Ex2 {
    public static class Ex2_1 { //TODO not sure about the class name

        public static String[] createTextFiles(int n, int seed, int bound) {
            String[] filesNames = new String[n];
            Random random = new Random(seed);
            for (int i = 1; i <= n; i++) { //Creating n files
                try {
                    FileWriter writer = new FileWriter("file" + i + ".txt");
                    filesNames[i - 1] = "file" + i + ".txt";
                    int numOfRows = random.nextInt(bound);
                    for (int j = 0; j < numOfRows; j++) {
                        if (j == numOfRows - 1) { //if we reached the last row in a file
                            writer.write("Hello World");
                        } else writer.write("Hello World\n");
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return filesNames;
        }

        public static int getNumOfLines(String[] fileNames) {
            int numOfLines = 0;
            for (String file : fileNames) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    while (reader.readLine() != null) {
                        numOfLines++;
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return numOfLines;
        }

        public static int getNumOfLinesThreads(String[] fileNames) {
            CountLinesThread[] threads = new CountLinesThread[fileNames.length];
            int totalRows = 0;
            for (int i = 0; i < fileNames.length; i++) {
                threads[i] = new CountLinesThread(fileNames[i]);
                threads[i].start();
            }
            for (CountLinesThread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            for (CountLinesThread thread : threads) {
                totalRows += thread.getCount();
            }
            return totalRows;
        }

        public static int getNumOfLinesThreadPool(String[] fileNames) {

            ArrayList<Future<Integer>> futures = new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(fileNames.length);
            for (String fileName : fileNames) {
                Future<Integer> future = executor.submit(new CountLinesThreadsPool(fileName));
                futures.add(future);
            }
            executor.shutdown();
            int totalCount = 0;
            try {
                executor.awaitTermination(1, TimeUnit.DAYS);
                for (Future<Integer> f : futures) {
                    int count = f.get();
                    totalCount += count;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return totalCount;
        }


        public static void main(String[] args) {
            int n = 1000;
            String[] textFiles = createTextFiles(n, 5, 10000);
            System.out.println(Arrays.deepToString(textFiles));

            //Without Threads
            long startTimeR = System.nanoTime();
            int regularTask = getNumOfLines(textFiles);
            long estimatedTimeR = System.nanoTime() - startTimeR;
            System.out.print("Num of Lines : " + regularTask + " ,");
            System.out.println("Total time without threads: " + estimatedTimeR + " nanoseconds");

            //With Threads
            long startTimeThreads = System.nanoTime();
            int threadsTask = getNumOfLinesThreads(textFiles);
            long estimatedTimeT = System.nanoTime() - startTimeThreads;
            System.out.print("Num of Lines : " + threadsTask + " ,");
            System.out.println("Total time using threads: " + estimatedTimeT + " nanoseconds");

            //With Thread Pool
            long startTimeThreadsPool = System.nanoTime();
            int threadPoolTask = getNumOfLinesThreadPool(textFiles);
            long estimatedTimeThreadPoll = System.nanoTime() - startTimeThreadsPool;
            System.out.print("Num of Lines : " + threadPoolTask + " ,");
            System.out.println("Total time using thread pool: " + estimatedTimeThreadPoll + " nanoseconds");


            //Delete all files
            for (int i = 1; i <= n; i++) {
                File file = new File("file" + i + ".txt");
                file.delete();
            }

        }
    }
}