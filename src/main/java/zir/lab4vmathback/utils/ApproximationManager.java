package zir.lab4vmathback.utils;

import exp.DeterminantException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class ApproximationManager {
    private final Summator summator = new Summator();
    private final MatrixManager matrixManager = new MatrixManager();

    public List<BigDecimal> getLinearApproximationIndexes(List<BigDecimal> pointsX, List<BigDecimal> pointsY) {
        int numberPoints = pointsX.size();
        BigDecimal sumX = summator.getSumOfPointsNDegree(pointsX, 1);
        BigDecimal sumXX = summator.getSumOfPointsNDegree(pointsX, 2);
        BigDecimal sumY = summator.getSumOfPointsNDegree(pointsY, 1);
        BigDecimal sumXY = summator.getSumOfMultipliedPointsXoFDegree(pointsX, 1, pointsY);

        BigDecimal determinant = sumXX.multiply(BigDecimal.valueOf(numberPoints))
                .subtract(sumX.pow(2));
        BigDecimal determinantA = sumXY.multiply(BigDecimal.valueOf(numberPoints))
                .subtract(sumX.multiply(sumY));
        BigDecimal determinantB = sumXX.multiply(sumY).subtract(sumX.multiply(sumXY));


        List<BigDecimal> res = new ArrayList<>();
        res.add(determinantA.divide(determinant, MathContext.DECIMAL32)); //a
        res.add(determinantB.divide(determinant, MathContext.DECIMAL32)); //b

        List<BigDecimal> approxFun = pointsX.stream().map(
                x -> x.multiply(res.get(0)).add(res.get(1))   //ax+b
        ).toList();

        BigDecimal deviationMeasure = summator.getSumOfSquaresOfDiff(approxFun, pointsY, 2); //S
        res.add(deviationMeasure);

        BigDecimal standardDeviation = getStandardDeviation(deviationMeasure, numberPoints); //ско
        res.add(standardDeviation);

        BigDecimal accuracyOfApproximation = getAccuracyOfApproximation(deviationMeasure, approxFun, pointsY, numberPoints);
        res.add(accuracyOfApproximation); //R

        BigDecimal correlationCoefficient = getCorrelationCoefficient(pointsX, pointsY, numberPoints);
        res.add(correlationCoefficient); //r

        return res;

    }

    public List<BigDecimal> getSquareApproximationIndexes(List<BigDecimal> pointsX, List<BigDecimal> pointsY) throws DeterminantException {
        int numberPoints = pointsX.size();
        BigDecimal sumX = summator.getSumOfPointsNDegree(pointsX, 1);
        BigDecimal sumXX = summator.getSumOfPointsNDegree(pointsX, 2);
        BigDecimal sumY = summator.getSumOfPointsNDegree(pointsY, 1);
        BigDecimal sumXY = summator.getSumOfMultipliedPointsXoFDegree(pointsX, 1, pointsY);
        BigDecimal sumX3 = summator.getSumOfPointsNDegree(pointsX, 3);
        BigDecimal sumX4 = summator.getSumOfPointsNDegree(pointsX, 4);
        BigDecimal sumXXY = summator.getSumOfMultipliedPointsXoFDegree(pointsX, 2, pointsY);
        BigDecimal[][] matrix = new BigDecimal[][]{
                {BigDecimal.valueOf(numberPoints), sumX, sumXX, sumY},
                {sumX, sumXX, sumX3, sumXY},
                {sumXX, sumX3, sumX4, sumXXY}
        };
        List<BigDecimal> res = matrixManager.reverseRunning(matrixManager.straightRunning(matrix));

        List<BigDecimal> approxFun = pointsX.stream().map(
                x -> res.get(0).add(
                        res.get(1).multiply(x).add(
                                res.get(2).multiply(x.pow(2))
                        ))   //a+bx+cx^2
        ).toList();


        BigDecimal deviationMeasure = summator.getSumOfSquaresOfDiff(approxFun, pointsY, 2); //S
        res.add(deviationMeasure);

        BigDecimal standardDeviation = getStandardDeviation(deviationMeasure, numberPoints); //ско
        res.add(standardDeviation);

        BigDecimal accuracyOfApproximation = getAccuracyOfApproximation(deviationMeasure, approxFun, pointsY, numberPoints);
        res.add(accuracyOfApproximation); //R
        return res;

    }

    public List<BigDecimal> getThirdApproximationIndexes(List<BigDecimal> pointsX, List<BigDecimal> pointsY) throws DeterminantException {
        int numberPoints = pointsX.size();
        BigDecimal sumX = summator.getSumOfPointsNDegree(pointsX, 1);
        BigDecimal sumXX = summator.getSumOfPointsNDegree(pointsX, 2);
        BigDecimal sumY = summator.getSumOfPointsNDegree(pointsY, 1);
        BigDecimal sumXY = summator.getSumOfMultipliedPointsXoFDegree(pointsX, 1, pointsY);
        BigDecimal sumX3 = summator.getSumOfPointsNDegree(pointsX, 3);
        BigDecimal sumX4 = summator.getSumOfPointsNDegree(pointsX, 4);
        BigDecimal sumX5 = summator.getSumOfPointsNDegree(pointsX, 5);
        BigDecimal sumX6 = summator.getSumOfPointsNDegree(pointsX, 6);

        BigDecimal sumXXY = summator.getSumOfMultipliedPointsXoFDegree(pointsX, 2, pointsY);
        BigDecimal sumX3Y = summator.getSumOfMultipliedPointsXoFDegree(pointsX, 3, pointsY);

        BigDecimal[][] matrix = new BigDecimal[][]{
                {BigDecimal.valueOf(numberPoints), sumX, sumXX, sumX3, sumY},
                {sumX, sumXX, sumX3, sumX4, sumXY},
                {sumXX, sumX3, sumX4, sumX5, sumXXY},
                {sumX3, sumX4, sumX5, sumX6, sumX3Y},

        };
        List<BigDecimal> res = matrixManager.reverseRunning(matrixManager.straightRunning(matrix));

        List<BigDecimal> approxFun = pointsX.stream().map(
                x -> res.get(0).add(
                        res.get(1).multiply(x).add(
                                res.get(2).multiply(x.pow(2)).add(
                                        res.get(3).multiply(x.pow(3))
                                )
                        ))   //a+bx+cx^2+dx^3
        ).toList();

        BigDecimal deviationMeasure = summator.getSumOfSquaresOfDiff(approxFun, pointsY, 2); //S
        res.add(deviationMeasure);

        BigDecimal standardDeviation = getStandardDeviation(deviationMeasure, numberPoints); //ско
        res.add(standardDeviation);

        BigDecimal accuracyOfApproximation = getAccuracyOfApproximation(deviationMeasure, approxFun, pointsY, numberPoints);
        res.add(accuracyOfApproximation); //R

        return res;
//        List<BigDecimal> indexes = matrixManager.reverseRunning(matrixManager.straightRunning(matrix));
//        return indexes;
    }

    public List<BigDecimal> getPowerApproximationIndexes(List<BigDecimal> pointsX, List<BigDecimal> pointsY) throws DeterminantException {
//        ax^b
        int numberPoints = pointsX.size();
        List<BigDecimal> xAfterLn = pointsX.stream().map(x -> BigDecimal.valueOf(Math.log(x.doubleValue()))).collect(Collectors.toList());
        List<BigDecimal> yAfterLn = pointsY.stream().map(x -> BigDecimal.valueOf(Math.log(x.doubleValue()))).collect(Collectors.toList());

        BigDecimal sumX = summator.getSumOfPointsNDegree(xAfterLn, 1);
        BigDecimal sumXX = summator.getSumOfPointsNDegree(xAfterLn, 2);
        BigDecimal sumY = summator.getSumOfPointsNDegree(yAfterLn, 1);
        BigDecimal sumXY = summator.getSumOfMultipliedPointsXoFDegree(xAfterLn, 1, yAfterLn);

        BigDecimal[][] matrix = new BigDecimal[][]{
                {BigDecimal.valueOf(numberPoints), sumX, sumY},
                {sumX, sumXX, sumXY},
        };
        List<BigDecimal> res = matrixManager.reverseRunning(matrixManager.straightRunning(matrix));
        res.set(0, BigDecimal.valueOf(Math.exp(res.get(0).doubleValue())));


        List<BigDecimal> approxFun = pointsX.stream().map(
                x -> res.get(0).multiply(
                        (BigDecimal.valueOf(
                                Math.pow(x.doubleValue(), res.get(1).doubleValue())
                        ))
                )   //ax^b
        ).toList();

        BigDecimal deviationMeasure = summator.getSumOfSquaresOfDiff(approxFun, pointsY, 2); //S
        res.add(deviationMeasure);

        BigDecimal standardDeviation = getStandardDeviation(deviationMeasure, numberPoints); //ско
        res.add(standardDeviation);

        BigDecimal accuracyOfApproximation = getAccuracyOfApproximation(deviationMeasure, approxFun, pointsY, numberPoints);
        res.add(accuracyOfApproximation); //R
        return res;
//        return new BigDecimal[]{BigDecimal.valueOf(Math.exp(indexes[0].doubleValue())), indexes[1]};
    }


    public List<BigDecimal> getExponentialApproximationIndexes(List<BigDecimal> pointsX, List<BigDecimal> pointsY) throws DeterminantException {
//        ae^(bx)
        int numberPoints = pointsX.size();
        List<BigDecimal> yAfterLn = pointsY.stream().map(x -> BigDecimal.valueOf(Math.log(x.doubleValue()))).collect(Collectors.toList());

        BigDecimal sumX = summator.getSumOfPointsNDegree(pointsX, 1);
        BigDecimal sumXX = summator.getSumOfPointsNDegree(pointsX, 2);
        BigDecimal sumY = summator.getSumOfPointsNDegree(yAfterLn, 1);
        BigDecimal sumXY = summator.getSumOfMultipliedPointsXoFDegree(pointsX, 1, yAfterLn);

        BigDecimal[][] matrix = new BigDecimal[][]{
                {BigDecimal.valueOf(numberPoints), sumX, sumY},
                {sumX, sumXX, sumXY},
        };
        List<BigDecimal> res = matrixManager.reverseRunning(matrixManager.straightRunning(matrix));
        res.set(0, BigDecimal.valueOf(Math.exp(res.get(0).doubleValue())));

        List<BigDecimal> approxFun = pointsX.stream().map(
                x -> res.get(0).multiply(
                        (BigDecimal.valueOf(
                                Math.exp(x.multiply(res.get(1)).doubleValue())
                        ))
                )   //ae^(xb)
        ).toList();

        BigDecimal deviationMeasure = summator.getSumOfSquaresOfDiff(approxFun, pointsY, 2); //S
        res.add(deviationMeasure);

        BigDecimal standardDeviation = getStandardDeviation(deviationMeasure, numberPoints); //ско
        res.add(standardDeviation);

        BigDecimal accuracyOfApproximation = getAccuracyOfApproximation(deviationMeasure, approxFun, pointsY, numberPoints);
        res.add(accuracyOfApproximation); //R
        return res;
    }

    public List<BigDecimal> getLogarithmicApproximationIndexes(List<BigDecimal> pointsX, List<BigDecimal> pointsY) throws DeterminantException {
//        alnx+b
        int numberPoints = pointsX.size();
        List<BigDecimal> xAfterLn = pointsX.stream().map(x -> BigDecimal.valueOf(Math.log(x.doubleValue()))).collect(Collectors.toList());

        BigDecimal sumX = summator.getSumOfPointsNDegree(xAfterLn, 1);
        BigDecimal sumXX = summator.getSumOfPointsNDegree(xAfterLn, 2);
        BigDecimal sumY = summator.getSumOfPointsNDegree(pointsY, 1);
        BigDecimal sumXY = summator.getSumOfMultipliedPointsXoFDegree(xAfterLn, 1, pointsY);

        BigDecimal[][] matrix = new BigDecimal[][]{
                {BigDecimal.valueOf(numberPoints), sumX, sumY},
                {sumX, sumXX, sumXY},
        };
        List<BigDecimal> res = matrixManager.reverseRunning(matrixManager.straightRunning(matrix));


        List<BigDecimal> approxFun = pointsX.stream().map(
                x -> (res.get(1).multiply(
                        (BigDecimal.valueOf(
                                Math.log(x.doubleValue())
                        )))).add(res.get(0))
                  //alnx+b
        ).toList();

        BigDecimal deviationMeasure = summator.getSumOfSquaresOfDiff(approxFun, pointsY, 2); //S
        res.add(deviationMeasure);

        BigDecimal standardDeviation = getStandardDeviation(deviationMeasure, numberPoints); //ско
        res.add(standardDeviation);

        BigDecimal accuracyOfApproximation = getAccuracyOfApproximation(deviationMeasure, approxFun, pointsY, numberPoints);
        res.add(accuracyOfApproximation); //R
        return res;
    }


    private BigDecimal getStandardDeviation(BigDecimal deviationMeasure, int numberPoints) {
        return BigDecimal.valueOf(Math.sqrt(deviationMeasure.divide(BigDecimal.valueOf(numberPoints), MathContext.DECIMAL32).doubleValue())); //ско
    }

    private BigDecimal getAccuracyOfApproximation(BigDecimal deviationMeasure, List<BigDecimal> approxFun, List<BigDecimal> pointsY, int numberPoints) {
        BigDecimal medApproxFun = summator.getSumOfPointsNDegree(approxFun, 1).divide(BigDecimal.valueOf(numberPoints), MathContext.DECIMAL32);

        List<BigDecimal> denominatorFun = pointsY.stream().map(x -> x.subtract(medApproxFun)).collect(Collectors.toList());
        BigDecimal denominator = summator.getSumOfPointsNDegree(denominatorFun, 2);
        return BigDecimal.ONE.subtract(deviationMeasure.divide(denominator, MathContext.DECIMAL32));
    }

    private BigDecimal getCorrelationCoefficient(List<BigDecimal> x, List<BigDecimal> y, int numberPoints) {
        BigDecimal xMed = summator.getSumOfPointsNDegree(x, 1).divide(BigDecimal.valueOf(numberPoints), MathContext.DECIMAL32);
        BigDecimal yMed = summator.getSumOfPointsNDegree(y, 1).divide(BigDecimal.valueOf(numberPoints), MathContext.DECIMAL32);

        List<BigDecimal> xDiffFun = x.stream().map(a -> a.subtract(xMed)).toList();
        List<BigDecimal> yDiffFun = y.stream().map(a -> a.subtract(yMed)).toList();

        BigDecimal numerator = summator.getSumOfMultipliedPointsXoFDegree(xDiffFun, 1, yDiffFun);

        BigDecimal denominator = BigDecimal.valueOf(Math.sqrt(
                summator.getSumOfPointsNDegree(xDiffFun, 2).multiply(summator.getSumOfPointsNDegree(yDiffFun, 2)).doubleValue()
        ));

        return numerator.divide(denominator, MathContext.DECIMAL32);
    }
}
