import java.io.*;
import java.util.ArrayList;
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

        public int getNumOfLinesThreads(String[] fileNames) {
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

        public int getNumOfLinesThreadPool(String[] fileNames) {
            ArrayList<Future<Integer>> futures = new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(fileNames.length/10);
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
            int n = 2000;
            String[] textFiles = createTextFiles(n, 10, 20000);
            System.out.println("n = 2000, seed = 10 , bound = 20,000");

            //Without Threads
            long startTimeR = System.currentTimeMillis();
            int regularTask = getNumOfLines(textFiles);
            long estimatedTimeR = System.currentTimeMillis() - startTimeR;
            System.out.print("Count: " + regularTask + " ,");
            System.out.println("WITHOUT THREADS: " + estimatedTimeR + " ms");

            Ex2_1 ex2_1 = new Ex2_1();
            //With Threads
            long startTimeThreads = System.currentTimeMillis();
            int threadsTask = ex2_1.getNumOfLinesThreads(textFiles);
            long estimatedTimeT = System.currentTimeMillis() - startTimeThreads;
            System.out.print("Count: " + threadsTask + " ,");
            System.out.println("THREADS: " + estimatedTimeT + " ms");

            //With Thread Pool
            long startTimeThreadsPool = System.currentTimeMillis();
            int threadPoolTask = ex2_1.getNumOfLinesThreadPool(textFiles);
            long estimatedTimeThreadPool = System.currentTimeMillis() - startTimeThreadsPool;
            System.out.print("Count: " + threadPoolTask + " ,");
            System.out.println("THREAD POOL: " + estimatedTimeThreadPool + " ms");

            //Delete all files
            for (int i = 1; i <= n; i++) {
                File file = new File("file" + i + ".txt");
                file.delete();
            }

        }
    }
}