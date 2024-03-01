package csx55.threads;

import java.util.concurrent.CountDownLatch;

public class ThreadPool {

    ThreadPoolThread[] fixedPool;
    TaskQueue queue = new TaskQueue();

    int[] matrixA;
    int[] matrixB;
    int[] matrixC;
    int[] matrixD;

    int[] intermediateMatrixX;
    int[] intermediateMatrixY;
    CountDownLatch generateTasks;
    CountDownLatch generateTasks2;
    // CountDownLatch finishedResult;
    // CountDownLatch finishedResult2;
    int matrixDimension;

    public ThreadPool(int numberOfThreads, MatrixGenerator gen, int matrixDimension) {
        fixedPool = new ThreadPoolThread[numberOfThreads];
        this.matrixDimension = matrixDimension;
        generateTasks = new CountDownLatch(matrixDimension);
        generateTasks2 = new CountDownLatch(matrixDimension);
        // CountDownLatch finishedResult = new CountDownLatch(matrixDimension);
        // CountDownLatch finishedResult2 = new CountDownLatch(matrixDimension);

        for (int i = 0; i < numberOfThreads; ++i) {
            ThreadPoolThread newThread = new ThreadPoolThread(generateTasks, generateTasks2);
            Thread starter = newThread;
            starter.start();
            fixedPool[i] = newThread;
            // fixedPool[i] = new ThreadPoolThread();
            // fixedPool[i].start();
        }

        gen.generateMatrices();
        matrixA = gen.getMatrixA();
        matrixB = gen.getMatrixB();
        matrixC = gen.getMatrixC();
        matrixD = gen.getMatrixD();
        System.out.print("Matrix A");
        printMatrix(matrixA);
        System.out.println("Matrix B");
        printMatrix(matrixB);

        intermediateMatrixX = new int[matrixDimension * matrixDimension];
        intermediateMatrixY = new int[matrixDimension * matrixDimension];
    }

    public void execute() {
        try{
            int count = 0;
            while (!queue.isEmpty()) {
                ThreadPoolThread tempRef = fixedPool[count % fixedPool.length];
                if (tempRef.isAvailable()) {
                    tempRef.performTask(queue.poll());
                }
                ++count;
         }
            generateTasks.await();
            System.out.println("I am no longer awaiting");
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        } 
    }

    public void generateTasks(int mode) {
        if (mode == 0) {
            for (int i = 0; i < matrixDimension * matrixDimension; i += matrixDimension) {
                queue.add(new Task(i, matrixDimension, matrixA, matrixB, intermediateMatrixX, 0));
            }
        }
        else {
            for (int i = 0; i < matrixDimension * matrixDimension; i +=matrixDimension) {
                queue.add(new Task(i, matrixDimension, matrixC, matrixD, intermediateMatrixY, 1));
            }
        }
    }
    //for testing
    public void printMatrix(int[] matrix) {
        for (int i = 0; i < matrixDimension; ++i) {
            for (int j = 0; j < matrixDimension; ++j) {
                System.out.print(matrix[i*matrixDimension + j] + " ");
            }
            System.out.println();
        }
    }

    public void multiplyMatrices() {
        //generate tasks for first matrix
        generateTasks(0);
        execute();
        System.out.println("Matrix Intermediate 1: ");
        printMatrix(intermediateMatrixX);
        // generateTasks(1);
        // execute();
        
    }

}
