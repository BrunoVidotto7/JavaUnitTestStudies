package br.com.bvidotto.service;

import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {

    @Test
    public void shouldSumTwoValues() {
        //scenery
        int a = 5;
        int b = 3;
        Calcutator calc = new Calcutator();

        //action
        int result = calc.sum(a,b);

        //verification
        Assert.assertEquals(8, result);
    }

    @Test
    public void shouldSubtractTwoValues() {
        //scenery
        int a = 5;
        int b = 3;
        Calcutator calc = new Calcutator();

        //action
        int result = calc.sub(a,b);

        //verification
        Assert.assertEquals(2, result);
    }
}
