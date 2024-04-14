package zir.lab4vmathback.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.IntStream;

public class ApproximationManager {
    private final Summator summator=new Summator();

    public BigDecimal[] getLinearApproximationIndexes(List<BigDecimal> pointsX, List<BigDecimal> pointsY) {
        int numberPoints = pointsX.size();
        BigDecimal sumX = summator.getSumPoints(pointsX);
        BigDecimal sumXX = summator.getSumOfSquares(pointsX);
        BigDecimal sumY = summator.getSumPoints(pointsY);
        BigDecimal sumXY = summator.getSumOfMultipliedPoints(pointsX,pointsY);
        BigDecimal determinant = sumXX.multiply(BigDecimal.valueOf(numberPoints))
                .subtract(sumX.pow(2));
        BigDecimal determinantA = sumXY.multiply(BigDecimal.valueOf(numberPoints))
                .subtract(sumX.multiply(sumY));
        BigDecimal determinantB = sumXX.multiply(sumY).subtract(sumX.multiply(sumXY));

        return new BigDecimal[]{determinantA.divide(determinant, MathContext.DECIMAL32),
                determinantB.divide(determinant,MathContext.DECIMAL32)};

    }

}
