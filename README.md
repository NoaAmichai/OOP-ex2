
## _OOP - Ex 2 :_


### _Part 1 :_

In this part of the assignment, we will create several text files and calculate the total number of lines in them.

We will use three methods:<br>
* The normal method without using threads.
* Using Threads.
* Using ThreadPool.

The results of the running times of the functions are: <br>

![Screenshot from 2023-01-09 12-41-07.png](Screenshot%20from%202023-01-09%2012-41-07.png)

From the results of the running times, we can conclude the following:<br>
When using a large number of files and a large number of lines in each file, the normal method is the least efficient and takes the longest time compared to the other methods.<br>
Additionally, with a smaller number of files and fewer lines per file, both the method, threads and ThreadPool compete for the best running times and no method consistently outperforms the others.<br>
However, when the number of files and the number of lines in each file increase (over approximately 10,000), using threads performs better running times.<br>
We tried to find why threads work better than threads pool.<br>
We run the program again but this time with n/2 and n/10 threads in the threads pool (instead of 2000).<br>
Here are the results:<br>

1000 threads in thread pool<br>

![Screenshot from 2023-01-09 12-41-59.png](Screenshot%20from%202023-01-09%2012-41-59.png)

200 threads in thread pool<br>

![Screenshot from 2023-01-09 12-42-35.png](Screenshot%20from%202023-01-09%2012-42-35.png)

In the above examples, we can see that when the number of threads in the thread pool is smaller, the results of the thread pool are better.<br>
Creating threads is a costly operation, so when the number of threads in the thread pool is equal to the number of files we create, all the benefits of the thread pool are lost.<br>

#### *Uml Diagram:*

![Part 1.png](Part%201%2FPart%201.png)

### _Part 2 :_

In this part of the assignment, we create a new type of ThreadPool that represents an asynchronous task with a priority and type. 
The Task class represents an operation that can be run asynchronously and can return a value of any type.
The CustomExecutor class represents a new type of ThreadPool that supports a queue of tasks with priorities. 
The CustomExecutor creates a Task before its entry into the queue through the transfer of a Callable and an enum of type TaskType. 
The CustomExecutor executes the tasks according to their priorities.

#### *Uml Diagram:*

<img alt="Part 2.png" height="500" src="Part%202%2FPart%202.png" width="300"/>