package csx55.threads;

import java.util.concurrent.CountDownLatch;

public class ThreadPoolThread extends Thread {
    CountDownLatch generateTasks;
    CountDownLatch generateTasks2;
    // CountDownLatch finishedResult;
    // CountDownLatch finishedResult2;
    Boolean isAvailable = true;
    Task task;
    public ThreadPoolThread(CountDownLatch generateTasks, CountDownLatch generateTasks2) {
        this.generateTasks = generateTasks;
        this.generateTasks2 = generateTasks2;
    }
    public void performTask(Task task) {
        this.task = task;
        setAvailable(false);
        synchronized(this) {
            this.notify();
        }
    }

    private void calculateCells(int rowIndex) {
        int rowSize =  task.getRowSize();
        // int resultantRow = rowIndex/rowSize;
        for (int i = 0; i < rowSize; ++i) {
            for (int j = 0; j < rowSize; ++j) {
                for (int k = 0; k < rowSize; ++k) {
                    task.getResultantMatrix()[rowIndex + i] += task.getMatrix1()[rowIndex + j] * task.getMatrix2()[0];
                }
            }
        }
    }
    public boolean isAvailable() {
        synchronized(isAvailable) {
            return isAvailable;
        }
    }
    private void setAvailable(Boolean bool) {
        synchronized(isAvailable) {
            isAvailable = bool;
        }
    }
    public void run() {
        while(true) {
        try{
            synchronized(this) {
                this.wait();
            }
            if (task.getAssociatedOperation() == 0) {
                int rowIndex = task.getRowIndex();
                calculateCells(rowIndex);
                generateTasks.countDown();
                setAvailable(true);
            }
            else {
                int rowIndex = task.getRowIndex();
                calculateCells(rowIndex);
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
