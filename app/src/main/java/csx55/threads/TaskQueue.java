package csx55.threads;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskQueue {

    private final ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<Task>();

    public TaskQueue() {

    }

    public void add(Task task) {
        taskQueue.add(task);
    }

    public Task poll() {
        return taskQueue.poll();
    }

    public int size() {
        synchronized(taskQueue) {
            return taskQueue.size();
        }
    }
    public boolean isEmpty() {
        synchronized(taskQueue) {
            return taskQueue.isEmpty();
        }
    }
}
