package com.sheunis.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class.
 */
@RestController
class CalculatorController {
    @Autowired
    private Calculator calculator;
    @RequestMapping("/sum")
    String sum(@RequestParam("a") Integer a1,
               @RequestParam("b") Integer b1) {
        return String.valueOf(calculator.sum(a1, b1));
    }
}