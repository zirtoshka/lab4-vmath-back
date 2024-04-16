package zir.lab4vmathback.utils;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class RequestInfo {
    @NotNull
    private BigDecimal[] x;

    @NotNull
    private BigDecimal[] y;

}
