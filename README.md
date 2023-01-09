
## _OOP - Ex 2 :_

In this assignment, we will create several text files and calculate the total number of lines in them.

We will use three methods:
* The normal method without using threads.
* Using Threads.
* Using ThreadPool.

<br>The results of the run time functions are: </br>

![Screenshot from 2023-01-09 10-40-33.png](Screenshot%20from%202023-01-09%2010-40-33.png)

<br> From the results of the run times, we can conclude the following:

When using a large number of files and a large number of lines in each file, the normal method is the least efficient and takes the longest time compared to the other methods. 

Additionally, with a smaller number of files and fewer lines per file, both of the methods, threads and ThreadPool compete for the best run time and no method consistently outperforms the other.

However, when the number of files and the number of lines in each file increase (over approximately 10,000), using threads generates better run times.
</br>


