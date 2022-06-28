package br.com.bvidotto.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.bvidotto.entity.Location;
import br.com.bvidotto.entity.Movie;
import br.com.bvidotto.entity.User;
import br.com.bvidotto.exceptions.MovieOutOfStockException;
import br.com.bvidotto.exceptions.RentalCompanyException;
import br.com.bvidotto.utils.DataUtils;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

public class LocationServiceTest {

    private LocationService service;

    private static int count = 0;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        service = new LocationService();
    }

    @After
    public void tearDown() {
        System.out.printf("Test %d finished.%n", count);
        count = count + 1;
    }

    @Test
    public void shouldHireMovie() throws Exception {
        Assume.assumeFalse(DataUtils.verifyDayOfWeek(new Date(), Calendar.SATURDAY)); //Only execute if is not saturday
        //scenery
        User user = new User("User 1");
        Movie movie1 = new Movie("Movie 1", 2, 5.0);
        Movie movie2 = new Movie("Movie 2", 1, 2.0);
        List<Movie> movies = Arrays.asList(movie1, movie2);

        //action
        Location location = service.hireMovie(user, movies);

        //validation
        assertEquals(7.0, location.getValue(), 0.01);
        assertTrue(DataUtils.isSameDate(location.getLocationDate(), new Date()));
        assertTrue(DataUtils.isSameDate(location.getReturnDate(), DataUtils.getDataWithDifferentDate(1)));

        assertThat(location.getValue(), is(equalTo(7.0)));
        assertThat(location.getValue(), is(not(4.0)));
        assertThat(DataUtils.isSameDate(location.getLocationDate(), new Date()), is(true));
        assertThat(DataUtils.isSameDate(location.getReturnDate(), DataUtils.getDataWithDifferentDate(1)), is(true));

        error.checkThat(location.getValue(), is(equalTo(7.0)));
        error.checkThat(location.getValue(), is(not(4.0)));
        error.checkThat(DataUtils.isSameDate(location.getLocationDate(), new Date()), is(true));
        error.checkThat(DataUtils.isSameDate(location.getReturnDate(), DataUtils.getDataWithDifferentDate(1)), is(true));
    }

    // elegant way -> good when just the exception matters
    @Test(expected = MovieOutOfStockException.class)
    public void shouldThrowExceptionWhenMovieIsOutOfStock() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        User user = new User("User 1");
        Movie movie1 = new Movie("Movie 1", 2, 5.0);
        Movie movie2 = new Movie("Movie 2", 0, 2.0);
        List<Movie> movies = Arrays.asList(movie1, movie2);

        //action
        service.hireMovie(user, movies);
    }

    // robust way -> total control, checks message
    @Test
    public void shouldThrowExceptionWhenUserIsEmpty() throws MovieOutOfStockException {
        //scenery
        Movie movie1 = new Movie("Movie 1", 2, 5.0);
        Movie movie2 = new Movie("Movie 2", 0, 2.0);
        List<Movie> movies = Arrays.asList(movie1, movie2);

        //action
        try {
            service.hireMovie(null, movies);
            fail();
        } catch (RentalCompanyException e) {
            assertThat(e.getMessage(), is("User is empty."));
        }
    }

    // new way -> checks message, enough for most cases
    @Test
    public void shouldThrowExceptionWhenMovieIsEmpty() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        User user = new User("User 1");

        exception.expect(RentalCompanyException.class);
        exception.expectMessage("Movie is empty.");

        //action
        service.hireMovie(user, null);
        fail();
    }

    @Test
    public void shouldGive25percentOffOnTheThirdMovieHired() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        Movie movie1 = new Movie("Movie 1", 2, 5.0);
        Movie movie2 = new Movie("Movie 2", 3, 2.0);
        Movie movie3 = new Movie("Movie 3", 3, 2.0);

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3);
        User user = new User("User 1");

        //action
        Location location = service.hireMovie(user, movies);

        //verification
        assertThat(location.getValue(), is(8.5));
    }

    @Test
    public void shouldGive50percentOffOnTheFourthMovieHired() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        Movie movie1 = new Movie("Movie 1", 2, 5.0);
        Movie movie2 = new Movie("Movie 2", 3, 5.0);
        Movie movie3 = new Movie("Movie 3", 3, 4.0);
        Movie movie4 = new Movie("Movie 4", 3, 3.0);

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4);
        User user = new User("User 1");

        //action
        Location location = service.hireMovie(user, movies);

        //verification
        assertThat(location.getValue(), is(14.5));

    }

    @Test
    public void shouldGive75percentOffOnTheFifthMovieHired() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        Movie movie1 = new Movie("Movie 1", 2, 5.0);
        Movie movie2 = new Movie("Movie 2", 3, 5.0);
        Movie movie3 = new Movie("Movie 3", 3, 4.0);
        Movie movie4 = new Movie("Movie 4", 3, 3.0);
        Movie movie5 = new Movie("Movie 5", 3, 2.0);

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4, movie5);
        User user = new User("User 1");

        //action
        Location location = service.hireMovie(user, movies);

        //verification
        assertThat(location.getValue(), is(15.0));

    }

    @Test
    public void shouldGive100percentOffOnTheSixthMovieHired() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        Movie movie1 = new Movie("Movie 1", 2, 5.0);
        Movie movie2 = new Movie("Movie 2", 3, 5.0);
        Movie movie3 = new Movie("Movie 3", 3, 4.0);
        Movie movie4 = new Movie("Movie 4", 3, 3.0);
        Movie movie5 = new Movie("Movie 5", 3, 2.0);
        Movie movie6 = new Movie("Movie 6", 3, 1.0);


        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6);
        User user = new User("User 1");

        //action
        Location location = service.hireMovie(user, movies);

        //verification
        assertThat(location.getValue(), is(15.0));

    }

    @Test
    public void mustReturnOnMondayWhenRentingOnSaturday() throws MovieOutOfStockException, RentalCompanyException {
        Assume.assumeTrue(DataUtils.verifyDayOfWeek(new Date(), Calendar.SATURDAY)); //Only executes on saturday

        Movie movie1 = new Movie("Movie 1", 2, 5.0);
        List<Movie> movies = List.of(movie1);
        User user = new User("User 1");

        Location location = service.hireMovie(user, movies);

        boolean isMonday = DataUtils.verifyDayOfWeek(location.getReturnDate(), Calendar.MONDAY);
        assertTrue(isMonday);
    }
}
