package zir.lab4vmathback.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

public  class Summator {

    public BigDecimal getSumOfPointsNDegree(List<BigDecimal> points,int nDegree){
        return points
                .stream()
                .map(x -> x.pow(nDegree))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getSumOfMultipliedPoints(List<BigDecimal> pointsX, int nDegreeX,List<BigDecimal> pointsY){
        return IntStream.range(0, pointsX.size())
                .mapToObj(i -> pointsX.get(i).pow(nDegreeX).multiply(pointsY.get(i)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
