package zir.lab4vmathback.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

public  class Summator {
    public BigDecimal getSumPoints(List<BigDecimal> points){
        return points
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getSumOfSquares(List<BigDecimal> points){
        return points
                .stream()
                .map(x -> x.pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getSumOfMultipliedPoints(List<BigDecimal> pointsX, List<BigDecimal> pointsY){
        return IntStream.range(0, pointsX.size())
                .mapToObj(i -> pointsX.get(i).multiply(pointsY.get(i)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
