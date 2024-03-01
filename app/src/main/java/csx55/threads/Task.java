package csx55.threads;

public class Task {
    int rowIndex;
    int rowSize;
    int[] matrix1;
    int[] matrix2;
    int[] resultantMatrix;
    int associatedOperation;
    public Task(int rowIndex, int rowSize, int[] matrix1, int[] matrix2, int[] resultantMatrix, int associatedOperation) {
        this.rowIndex = rowIndex;
        this.rowSize = rowSize;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.resultantMatrix = resultantMatrix;
        this.associatedOperation = associatedOperation;
    }
    public int getRowIndex() {
        return rowIndex;
    }
    public int getRowSize() {
        return rowSize;
    }
    public int[] getMatrix1() {
        return matrix1;
    }
    public int[] getMatrix2() {
        return matrix2;
    }
    public int[] getResultantMatrix() {
        return resultantMatrix;
    }
    public int getAssociatedOperation() {
        return associatedOperation;
    }
}
