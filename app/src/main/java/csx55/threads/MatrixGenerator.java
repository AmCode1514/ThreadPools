package csx55.threads;

import java.util.Random;

public class MatrixGenerator {
    int[] matrixA;
    int[] matrixB;
    int[] matrixC;
    int[] matrixD;
    int matrixDimension;
    int seed;
    public long aSize,bSize,cSize,dSize;

    public MatrixGenerator(int matrixDimension, int seed) {
        int matrixCells = matrixDimension * matrixDimension;
        matrixA = new int[matrixCells];
        matrixB = new int[matrixCells];
        matrixC = new int[matrixCells];
        matrixD = new int[matrixCells];
        this.seed = seed;
        this.matrixDimension = matrixDimension;
    }
    public int[] getMatrixA() {
        return matrixA;
    }
    public int[] getMatrixB() {
        return matrixB;
    }
    public int[] getMatrixC() {
        return matrixC;
    }
    public int[] getMatrixD() {
        return matrixD;
    }

    public void generateMatrices() {
        Random rand = new Random(seed);
        for(int i = 0; i < matrixA.length; ++i) {
            int number = 1000 - rand.nextInt(1000 - -1000);
            matrixA[i] = number;
            aSize += number;
        }
        for (int i = 0; i < matrixA.length; ++i) {
            int number = 1000 - rand.nextInt(1000 - -1000);
            matrixB[i] = number;
            bSize += number;
        }
        for (int i = 0; i < matrixA.length; ++i) {
            int number = 1000 - rand.nextInt(1000 - -1000);
            matrixC[i] = number;
            cSize += number;
        }
        for (int i = 0; i < matrixA.length; ++i) {
            int number = 1000 - rand.nextInt(1000 - -1000);
            matrixD[i] = number;
            dSize += number;
        }
    }
}
