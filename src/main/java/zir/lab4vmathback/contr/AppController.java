package zir.lab4vmathback.contr;

import exp.DeterminantException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zir.lab4vmathback.utils.ApproximationManager;
import com.google.gson.Gson;


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
    public ResponseEntity<String> getApproximation() {
        String response = "";
        final HttpHeaders httpHeaders = new HttpHeaders();
        BigDecimal[] xx = new BigDecimal[]{
                BigDecimal.valueOf(1.1), BigDecimal.valueOf(2.3),
                BigDecimal.valueOf(3.7),
                BigDecimal.valueOf(4.5),BigDecimal.valueOf(5.4),
                BigDecimal.valueOf(6.8), BigDecimal.valueOf(7.5)};
        BigDecimal[] yy = new BigDecimal[]{
                BigDecimal.valueOf(3.5), BigDecimal.valueOf(4.1),
                BigDecimal.valueOf(5.2),
                BigDecimal.valueOf(6.9),BigDecimal.valueOf(8.3),
                BigDecimal.valueOf(14.8), BigDecimal.valueOf(21.2)};

        List<BigDecimal> x = new ArrayList<>(Arrays.asList(xx));
        List<BigDecimal> y = new ArrayList<>(Arrays.asList(yy));

        BigDecimal[] linearIndexes = approximationManager.getLinearApproximationIndexes(x, y);
        BigDecimal[] squareIndexes=null;

        try {
           squareIndexes = approximationManager.getSquareApproximationIndexes(x, y);
        }catch (DeterminantException e){
            System.out.println("det is not good((");
        }

        System.out.println("-------");
        for (BigDecimal i : linearIndexes) {
            System.out.println(i);
        }
        System.out.println("-------");
        for (BigDecimal i : squareIndexes) {
            System.out.println(i);
        }
        Gson gson = new Gson();
        String json = gson.toJson(linearIndexes);
        response+="{\"linear\": " + json + ",\n";
        json = gson.toJson(squareIndexes);
        response+="\"square\": " + json + "}";
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);

    }


}
