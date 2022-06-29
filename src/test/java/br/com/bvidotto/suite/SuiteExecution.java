package br.com.bvidotto.suite;

import br.com.bvidotto.service.CalcLocationValueTest;
import br.com.bvidotto.service.CalculatorTest;
import br.com.bvidotto.service.LocationServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    CalculatorTest.class,
    CalcLocationValueTest.class,
    LocationServiceTest.class
})
public class SuiteExecution {
    // Remove it if you can!
}
