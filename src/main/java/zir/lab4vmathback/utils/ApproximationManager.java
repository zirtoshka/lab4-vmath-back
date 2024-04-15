package zir.lab4vmathback.utils;

import exp.DeterminantException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.IntStream;

public class ApproximationManager {
    private final Summator summator = new Summator();
    private final MatrixManager matrixManager = new MatrixManager();

    public BigDecimal[] getLinearApproximationIndexes(List<BigDecimal> pointsX, List<BigDecimal> pointsY) {
        int numberPoints = pointsX.size();
        BigDecimal sumX = summator.getSumOfPointsNDegree(pointsX,1);
        BigDecimal sumXX = summator.getSumOfPointsNDegree(pointsX,2);
        BigDecimal sumY = summator.getSumOfPointsNDegree(pointsY,1);
        BigDecimal sumXY = summator.getSumOfMultipliedPoints(pointsX,1, pointsY);

        BigDecimal determinant = sumXX.multiply(BigDecimal.valueOf(numberPoints))
                .subtract(sumX.pow(2));
        BigDecimal determinantA = sumXY.multiply(BigDecimal.valueOf(numberPoints))
                .subtract(sumX.multiply(sumY));
        BigDecimal determinantB = sumXX.multiply(sumY).subtract(sumX.multiply(sumXY));


        return new BigDecimal[]{determinantA.divide(determinant, MathContext.DECIMAL32),
                determinantB.divide(determinant, MathContext.DECIMAL32)};

    }

    public BigDecimal[] getSquareApproximationIndexes(List<BigDecimal> pointsX, List<BigDecimal> pointsY) throws DeterminantException {
        int numberPoints = pointsX.size();
        BigDecimal sumX = summator.getSumOfPointsNDegree(pointsX,1);
        BigDecimal sumXX = summator.getSumOfPointsNDegree(pointsX,2);
        BigDecimal sumY = summator.getSumOfPointsNDegree(pointsY,1);
        BigDecimal sumXY = summator.getSumOfMultipliedPoints(pointsX,1, pointsY);
        BigDecimal sumX3 = summator.getSumOfPointsNDegree(pointsX,3);
        BigDecimal sumX4=summator.getSumOfPointsNDegree(pointsX,4);
        BigDecimal sumXXY=summator.getSumOfMultipliedPoints(pointsX,2,pointsY);
        BigDecimal[][] matrix = new BigDecimal[][]{
                {BigDecimal.valueOf(numberPoints),sumX, sumXX, sumY},
                {sumX, sumXX,sumX3, sumXY},
                {sumXX,sumX3, sumX4, sumXXY}
        } ;


        BigDecimal[] indexes = matrixManager.reverseRunning(matrixManager.straightRunning(matrix));


        return indexes;

    }


}
