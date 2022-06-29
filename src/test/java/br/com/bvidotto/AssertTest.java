package br.com.bvidotto;

import br.com.bvidotto.entity.User;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test() {
        Assert.assertTrue(true);
        Assert.assertFalse(false);


        Assert.assertEquals(1,1);
        Assert.assertEquals(0.51, 0.51, 0.01);

        int i = 5;
        Integer i2 = 5;

        Assert.assertEquals(Integer.valueOf(i), i2);
        Assert.assertEquals(i, i2.intValue());

        Assert.assertEquals("ball", "ball");
        Assert.assertTrue("ball".equalsIgnoreCase("ball"));

        User u1 = new User("User 1");
        User u2 = new User("User 1");
        User u3 = null;

        Assert.assertEquals(u1, u2);

        Assert.assertSame(u2, u2);

        Assert.assertNull(u3);

        Assert.assertNotEquals(u1, u3);

        Assert.assertNotNull(u2);



    }
}
