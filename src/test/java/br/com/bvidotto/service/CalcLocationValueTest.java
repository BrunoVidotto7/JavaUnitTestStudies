package br.com.bvidotto.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import br.com.bvidotto.dao.LocationDao;
import br.com.bvidotto.entity.Location;
import br.com.bvidotto.entity.Movie;
import br.com.bvidotto.entity.User;
import br.com.bvidotto.exceptions.MovieOutOfStockException;
import br.com.bvidotto.exceptions.RentalCompanyException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class CalcLocationValueTest {

    @Parameterized.Parameter
    public List<Movie> movies;

    @Parameterized.Parameter(value=1)
    public Double locationValue;

    @Parameterized.Parameter(value=2)
    public String description;

    private LocationService service;

    @Before
    public void setup() {
        LocationDao dao = Mockito.mock(LocationDao.class);
        service = new LocationService();
        service.setLocationDao(dao);
    }

    static Movie movie1 = new Movie("Movie 1", 2, 5.0);
    static Movie movie2 = new Movie("Movie 2", 3, 5.0);
    static Movie movie3 = new Movie("Movie 3", 3, 4.0);
    static Movie movie4 = new Movie("Movie 4", 3, 3.0);
    static Movie movie5 = new Movie("Movie 5", 3, 2.0);
    static Movie movie6 = new Movie("Movie 6", 3, 1.0);

    @Parameterized.Parameters(name="{2}")
    public static Collection<Object[]> getParameters() {

        return Arrays.asList(new Object[][] {
            {Arrays.asList(movie1, movie2, movie3), 13.0, "3 Movies: 25% off"},
            {Arrays.asList(movie1, movie2, movie3, movie4), 14.5, "4 Movies: 50% off"},
            {Arrays.asList(movie1, movie2, movie3, movie4, movie5), 15.0, "5 Movies: 75% off"},
            {Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6), 15.0, "6 Movies: 100% off"}
        });
    }

    @Test
    public void shouldCalculateLocationValueConsideringDiscounts() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        User user = new User("User 1");

        //action
        Location location = service.hireMovie(user, movies);

        //verification
        assertThat(location.getValue(), is(locationValue));
    }
}
