package br.com.bvidotto.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import br.com.bvidotto.entity.Location;
import br.com.bvidotto.entity.Movie;
import br.com.bvidotto.entity.User;
import br.com.bvidotto.exceptions.MovieOutOfStockException;
import br.com.bvidotto.exceptions.RentalCompanyException;
import br.com.bvidotto.utils.DataUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

public class LocationServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void LocationTest() throws Exception {
        //scenery
        LocationService service = new LocationService();
        User user = new User("User 1");
        Movie movie = new Movie("Movie 1", 2, 5.0);

        //action
        Location location = service.hireMovie(user, movie);

        //validation
        assertEquals(5.0, location.getValue(), 0.01);
        assertTrue(DataUtils.isSameDate(location.getLocationDate(), new Date()));
        assertTrue(DataUtils.isSameDate(location.getReturnDate(), DataUtils.getDataWithDifferentDate(1)));

        assertThat(location.getValue(), is(equalTo(5.0)));
        assertThat(location.getValue(), is(not(4.0)));
        assertThat(DataUtils.isSameDate(location.getLocationDate(), new Date()), is(true));
        assertThat(DataUtils.isSameDate(location.getReturnDate(), DataUtils.getDataWithDifferentDate(1)), is(true));

        error.checkThat(location.getValue(), is(equalTo(5.0)));
        error.checkThat(location.getValue(), is(not(4.0)));
        error.checkThat(DataUtils.isSameDate(location.getLocationDate(), new Date()), is(true));
        error.checkThat(DataUtils.isSameDate(location.getReturnDate(), DataUtils.getDataWithDifferentDate(1)), is(true));
    }

    @Test(expected = MovieOutOfStockException.class)
    public void locationTest_outOfStockMovie() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        LocationService service = new LocationService();
        User user = new User("User 1");
        Movie movie = new Movie("Movie 1", 0, 5.0);

        //action
        service.hireMovie(user, movie);
    }

    @Test
    public void locationTest_EmptyUser() throws MovieOutOfStockException {
        //scenery
        LocationService service = new LocationService();
        Movie movie = new Movie("Movie 1", 2, 5.0);

        //action
        try {
            service.hireMovie(null, movie);
            fail();
        } catch (RentalCompanyException e) {
            assertThat(e.getMessage(), is("User is empty."));
        }
    }

    @Test
    public void locationTest_EmptyMovie() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        LocationService service = new LocationService();
        User user = new User("User 1");

        exception.expect(RentalCompanyException.class);
        exception.expectMessage("Movie is empty.");

        //action
        service.hireMovie(user, null);
        fail();
    }
}
