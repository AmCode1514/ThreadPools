package csx55.threads;

import java.util.concurrent.CountDownLatch;

public class ThreadPoolThread extends Thread {
    CountDownLatch generateTasks;
    CountDownLatch generateTasks2;
    CountDownLatch generateTasks3;
    // CountDownLatch finishedResult;
    // CountDownLatch finishedResult2;
    Boolean isAvailable = true;
    Task task;
    public ThreadPoolThread(CountDownLatch generateTasks, CountDownLatch generateTasks2, CountDownLatch generateTasks3) {
        this.generateTasks = generateTasks;
        this.generateTasks2 = generateTasks2;
        this.generateTasks3 = generateTasks3;
    }
    public void performTask(Task task) {
        this.task = task;
        setAvailable(false);
        synchronized(this) {
            this.notify();
        }
    }

    private void calculateCells(int rowIndex, int associatedOperation) {
        int rowSize =  task.getRowSize();
        int[] matrix1 = task.getMatrix1();
        int[] matrix2 = task.getMatrix2();
        int[] resultantMatrix = task.getResultantMatrix();
        for (int i = 0; i < rowSize; ++i) {
            for (int j = 0; j < rowSize; ++j) {
                    resultantMatrix[rowIndex + i] += matrix1[rowIndex + j] * matrix2[i + (j*rowSize)];
            }
            synchronized(ThreadPool.class) {
            switch(associatedOperation) {
                case 0:
                ThreadPool.addSumToX(resultantMatrix[rowIndex + i]);
                break;
                case 1:
                ThreadPool.addSumToY(resultantMatrix[rowIndex + i]);
                break;
                case 2:
                ThreadPool.addSumToZ(resultantMatrix[rowIndex + i]);
                break;
            }
        }
        }
    }
    public boolean isAvailable() {
        synchronized(this) {
            return isAvailable;
        }
    }
    private void setAvailable(Boolean bool) {
        synchronized(this) {
            isAvailable = bool;
        }
    }
    public void run() {
        synchronized(this) {
        while(true) {
        try{
                this.wait();
                int code = task.getAssociatedOperation();
            if (code == 0) {
                int rowIndex = task.getRowIndex();
                calculateCells(rowIndex, code);
                generateTasks.countDown();
                setAvailable(true);
            }
            else if (code == 2) {
                int rowIndex = task.getRowIndex();
                calculateCells(rowIndex, code);
                generateTasks3.countDown();
                setAvailable(true);
            }
            else {
                int rowIndex = task.getRowIndex();
                calculateCells(rowIndex, code);
                generateTasks2.countDown();
                setAvailable(true);
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
        }
    }
}
