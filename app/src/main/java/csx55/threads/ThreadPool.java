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
    int[] intermediateMatrixZ;
    CountDownLatch generateTasks;
    CountDownLatch generateTasks2;
    CountDownLatch generateTasks3;
    int matrixDimension;

    static long sumOfIntermediateX, sumOfIntermediateY, sumOfIntermediateZ;

    public ThreadPool(int numberOfThreads, MatrixGenerator gen, int matrixDimension) {
        fixedPool = new ThreadPoolThread[numberOfThreads];
        this.matrixDimension = matrixDimension;
        generateTasks = new CountDownLatch(matrixDimension);
        generateTasks2 = new CountDownLatch(matrixDimension);
        generateTasks3 = new CountDownLatch(matrixDimension);
        // CountDownLatch finishedResult = new CountDownLatch(matrixDimension);
        // CountDownLatch finishedResult2 = new CountDownLatch(matrixDimension);

        for (int i = 0; i < numberOfThreads; ++i) {
            ThreadPoolThread newThread = new ThreadPoolThread(generateTasks, generateTasks2, generateTasks3);
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
        // System.out.print("Matrix A");
        // printMatrix(matrixA);
        // System.out.println("Matrix B");
        // printMatrix(matrixB);

        intermediateMatrixX = new int[matrixDimension * matrixDimension];
        intermediateMatrixY = new int[matrixDimension * matrixDimension];
        intermediateMatrixZ = new int[matrixDimension * matrixDimension];
    }

    public void execute(CountDownLatch latch) {
        try{
            int count = 0;
            while (!queue.isEmpty()) {
                ThreadPoolThread tempRef = fixedPool[count];
                if (tempRef.isAvailable()) {
                    tempRef.performTask(queue.poll());
                }
                count = (count + 1) % fixedPool.length;
                //System.out.println(queue.size());
         }
            latch.await();
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
        else if (mode == 1) {
            for (int i = 0; i < matrixDimension * matrixDimension; i +=matrixDimension) {
                queue.add(new Task(i, matrixDimension, matrixC, matrixD, intermediateMatrixY, 1));
            }
        }
        else {
            for (int i = 0; i < matrixDimension * matrixDimension; i +=matrixDimension) {
                queue.add(new Task(i, matrixDimension, intermediateMatrixX, intermediateMatrixY, intermediateMatrixZ, 2));
            }
        }
    }
    public synchronized static void addSumToX(int sum) {
        sumOfIntermediateX += sum;
    }
    public synchronized static void addSumToY(int sum) {
        sumOfIntermediateY += sum;
    }
    public synchronized static void addSumToZ(int sum) {
        sumOfIntermediateZ += sum;
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
        long currentTime = System.currentTimeMillis();
        generateTasks(0);
        execute(generateTasks);
        System.out.println("Finished calculating matrix X with time " + ((System.currentTimeMillis() - currentTime) / 1000f));
        System.out.println("Finished calculating Matrix X with sum:" + sumOfIntermediateX);
        currentTime = System.currentTimeMillis();
        generateTasks(1);
        execute(generateTasks2);
        System.out.println("Finished calculating matrix Y with time " + ((System.currentTimeMillis() - currentTime) / 1000f));
        System.out.println("Finished calculating Matrix Y with sum:" + sumOfIntermediateY);
        currentTime = System.currentTimeMillis();
        generateTasks(2);
        execute(generateTasks3);
        System.out.println("Finished calculating matrix Z with time " + ((System.currentTimeMillis() - currentTime) / 1000f));
        System.out.println("Finished calculating Matrix Z with sum:" + sumOfIntermediateZ);
    }

}
