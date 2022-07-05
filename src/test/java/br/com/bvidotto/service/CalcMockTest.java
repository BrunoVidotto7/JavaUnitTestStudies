package br.com.bvidotto.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalcMockTest {
    @Mock
    private Calcutator calcMock;

    @Spy
    private Calcutator calcSpy;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldShowTheDifferenceBetweenMockAndSpy() {
        Mockito.when(calcMock.sum(1,2)).thenReturn(8);
        Mockito.when(calcSpy.sum(1,5)).thenReturn(8);

        System.out.println("Mock " + calcMock.sum(1,2));
        System.out.println("Spy " + calcSpy.sum(1,2));
    }

    @Test
    public void test() {
        Calcutator calcutator = Mockito.mock(Calcutator.class);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calcutator.sum(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(5);

        Assert.assertEquals(5, calcutator.sum(1356, -59));
        System.out.println(argumentCaptor.getAllValues());
    }
}
