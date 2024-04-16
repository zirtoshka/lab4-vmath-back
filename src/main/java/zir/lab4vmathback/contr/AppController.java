package zir.lab4vmathback.contr;

import exp.DeterminantException;
import exp.IncorrectValueForMethodException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zir.lab4vmathback.utils.ApproximationManager;
import com.google.gson.Gson;
import zir.lab4vmathback.utils.RequestInfo;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/app-controller")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AppController {

    ApproximationManager approximationManager = new ApproximationManager();

    @GetMapping
    public ResponseEntity<String> sayHello() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        System.out.println("it's method sayHello");
        return new ResponseEntity<>("{\"message\": \"Hello from secured endpoint\"}", httpHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> getApproximation(@Valid @RequestBody RequestInfo requestInfo) {
        String response = "";
        final HttpHeaders httpHeaders = new HttpHeaders();
//        BigDecimal[] xx = new BigDecimal[]{
//                BigDecimal.valueOf(1.1), BigDecimal.valueOf(2.3),
//                BigDecimal.valueOf(3.7),
//                BigDecimal.valueOf(4.5),BigDecimal.valueOf(5.4),
//                BigDecimal.valueOf(6.8), BigDecimal.valueOf(7.5)};
//        BigDecimal[] yy = new BigDecimal[]{
//                BigDecimal.valueOf(3.5), BigDecimal.valueOf(4.1),
//                BigDecimal.valueOf(5.2),
//                BigDecimal.valueOf(6.9),BigDecimal.valueOf(8.3),
//                BigDecimal.valueOf(14.8), BigDecimal.valueOf(21.2)};

        List<BigDecimal> x = new ArrayList<>(Arrays.asList(requestInfo.getX()));
        List<BigDecimal> y = new ArrayList<>(Arrays.asList(requestInfo.getY()));

        List<BigDecimal> linearIndexes = new ArrayList<>();
        List<BigDecimal> squareIndexes = new ArrayList<>();
        List<BigDecimal> thirdIndexes = new ArrayList<>();
        List<BigDecimal> powerIndexes = new ArrayList<>(), exponentIndexes = new ArrayList<>(), logarithmicIndexes = new ArrayList<>();
        ;


        try {
            linearIndexes = approximationManager.getLinearApproximationIndexes(x, y);
            squareIndexes = approximationManager.getSquareApproximationIndexes(x, y);
            thirdIndexes = approximationManager.getThirdApproximationIndexes(x, y);
            powerIndexes = approximationManager.getPowerApproximationIndexes(x, y);
            exponentIndexes = approximationManager.getExponentialApproximationIndexes(x, y);
            logarithmicIndexes = approximationManager.getLogarithmicApproximationIndexes(x, y);

        } catch (DeterminantException e) {
            System.out.println("det is not good((");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);

        } catch (ArithmeticException e) {
            System.out.println("det is not good((");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (IncorrectValueForMethodException e) {
            System.out.println("points is not good for method");
        }

        try {
            exponentIndexes = approximationManager.getExponentialApproximationIndexes(x, y);

        } catch (DeterminantException e) {
            System.out.println("det is not good((");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);

        } catch (ArithmeticException e) {
            System.out.println("det is not good((");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (IncorrectValueForMethodException e) {
            System.out.println("points is not good for method");
        }
        try {
            logarithmicIndexes = approximationManager.getLogarithmicApproximationIndexes(x, y);

        } catch (DeterminantException e) {
            System.out.println("det is not good((");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);

        } catch (ArithmeticException e) {
            System.out.println("det is not good((");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (IncorrectValueForMethodException e) {
            System.out.println("points is not good for method");
        }


        Gson gson = new Gson();
        String json = gson.toJson(linearIndexes);
        response += "{\"linear\": " + json + ",\n";
        json = gson.toJson(squareIndexes);
        response += "\"square\": " + json + ",\n";

        json = gson.toJson(thirdIndexes);
        response += "\"third\": " + json + ",\n";

        json = gson.toJson(powerIndexes);
        response += "\"power\": " + json + ",\n";

        json = gson.toJson(exponentIndexes);
        response += "\"exponent\": " + json + ",\n";

        json = gson.toJson(logarithmicIndexes);
        response += "\"logarithmic\": " + json + "}";

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);

    }


}
