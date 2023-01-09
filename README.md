
## _OOP - Ex 2 :_

In this assignment, we will create several text files and calculate the total number of lines in them.

We will use three methods:
* The normal method without using threads.
* Using Threads.
* Using ThreadPool.

<br>The results of the running times of the functions are: </br>

![Screenshot from 2023-01-09 12-41-07.png](Screenshot%20from%202023-01-09%2012-41-07.png)

<br> From the results of the running times, we can conclude the following:

When using a large number of files and a large number of lines in each file, the normal method is the least efficient and takes the longest time compared to the other methods. 

Additionally, with a smaller number of files and fewer lines per file, both the method, threads and ThreadPool compete for the best running times and no method consistently outperforms the others.

However, when the number of files and the number of lines in each file increase (over approximately 10,000), using threads performs better running times.
</br>

We tried to find why threads work better than threads pool.
We run the program again but this time with n/2 and n/10 threads in the threads pool(instead of 2000).
Here are the results:

<br>1000 threads in thread pool</br>
![Screenshot from 2023-01-09 12-41-59.png](Screenshot%20from%202023-01-09%2012-41-59.png)

<br>200 threads in thread pool</br>
![Screenshot from 2023-01-09 12-42-35.png](Screenshot%20from%202023-01-09%2012-42-35.png)

In the above examples, we can see that when the number of threads in the thread pool is smaller, the results of the thread pool are better.<t>
Creating threads is a costly operation, so when the number of threads in the thread pool is equal to the number of files we create, all the benefits of the thread pool are lost.

*CountLinesThread Class Uml Diagram:* 

![CountLinesThread.png](src%2FCountLinesThread.png)

*CountLinesThreadPool Class Uml Diagram:*

![CountLinesThreadsPool.png](src%2FCountLinesThreadsPool.png)
