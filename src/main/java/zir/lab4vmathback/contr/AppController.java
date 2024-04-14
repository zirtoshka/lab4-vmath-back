package zir.lab4vmathback.contr;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zir.lab4vmathback.utils.ApproximationManager;
import com.google.gson.Gson;


import java.math.BigDecimal;
import java.util.ArrayList;
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
        final HttpHeaders httpHeaders = new HttpHeaders();
        List<BigDecimal> x = new ArrayList<>();
        List<BigDecimal> y = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            x.add(BigDecimal.valueOf(i));
            y.add(BigDecimal.valueOf(i * 3));
        }
        BigDecimal[] linearIndexes = approximationManager.getLinearApproximationIndexes(x, y);
        Gson gson = new Gson();
        String json = gson.toJson(linearIndexes);
        return new ResponseEntity<>(json, httpHeaders, HttpStatus.OK);

    }


}
