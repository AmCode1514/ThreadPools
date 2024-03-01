package csx55.threads;

public class MatrixThreads {
    public static void main(String[] args) {
        int numberOfThreads = Integer.valueOf(args[0]);
        int matrixDimension = Integer.valueOf(args[1]);
        int seed = Integer.valueOf(args[2]);
        MatrixGenerator gen = new MatrixGenerator(matrixDimension, seed);
        ThreadPool fixedPool = new ThreadPool(numberOfThreads, gen, matrixDimension);
        //entry point
        fixedPool.multiplyMatrices();
    }
}
