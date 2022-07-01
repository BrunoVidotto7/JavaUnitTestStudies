package br.com.bvidotto.service;

import static br.com.bvidotto.builder.LocationBuilder.*;
import static br.com.bvidotto.utils.DataUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.bvidotto.builder.LocationBuilder;
import br.com.bvidotto.builder.MovieBuilder;
import br.com.bvidotto.builder.UserBuilder;
import br.com.bvidotto.dao.LocationDao;
import br.com.bvidotto.entity.Location;
import br.com.bvidotto.entity.Movie;
import br.com.bvidotto.entity.User;
import br.com.bvidotto.exceptions.MovieOutOfStockException;
import br.com.bvidotto.exceptions.RentalCompanyException;
import br.com.bvidotto.matchers.OwnMatchers;
import br.com.bvidotto.utils.DataUtils;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

public class LocationServiceTest {

    private LocationService locationService;

    private LocationDao dao;

    private EmailService emailService;

    private DebtService debtService;

    private static int count = 0;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        dao = mock(LocationDao.class);
        debtService = mock(DebtService.class);
        emailService = mock(EmailService.class);
        locationService = new LocationService();
        locationService.setLocationDao(dao);
        locationService.setDebtService(debtService);
        locationService.setEmailService(emailService);
    }

    @After
    public void tearDown() {
        System.out.printf("Test %d finished.%n", count);
        count = count + 1;
    }

    @Test
    public void shouldHireMovie() throws Exception {
        Assume.assumeFalse(verifyDayOfWeek(new Date(), Calendar.SATURDAY)); //Only execute if is not saturday
        //scenery
        User user = UserBuilder.oneUser().now();
        Movie movie1 = MovieBuilder.oneMovie().now();
        Movie movie2 = MovieBuilder.oneMovie().withValue(2.0).now();
        List<Movie> movies = Arrays.asList(movie1, movie2);

        //action
        Location location = locationService.hireMovie(user, movies);

        //validation
        assertEquals(7.0, location.getValue(), 0.01);
        assertTrue(isSameDate(location.getLocationDate(), new Date()));
        assertTrue(isSameDate(location.getReturnDate(), getDataWithDifferentDate(1)));

        assertThat(location.getValue(), is(equalTo(7.0)));
        assertThat(location.getValue(), is(not(4.0)));
        assertThat(isSameDate(location.getLocationDate(), new Date()), is(true));
        assertThat(isSameDate(location.getReturnDate(), getDataWithDifferentDate(1)), is(true));

        error.checkThat(location.getValue(), is(equalTo(7.0)));
        error.checkThat(location.getValue(), is(not(4.0)));
        error.checkThat(isSameDate(location.getLocationDate(), new Date()), is(true));
        error.checkThat(isSameDate(location.getReturnDate(), getDataWithDifferentDate(1)), is(true));
    }

    // elegant way -> good when just the exception matters
    @Test(expected = MovieOutOfStockException.class)
    public void shouldThrowExceptionWhenMovieIsOutOfStock() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        User user = UserBuilder.oneUser().now();
        Movie movie1 = MovieBuilder.oneMovie().now();
        Movie movie2 = MovieBuilder.oneMovie().noStock().now();
        List<Movie> movies = Arrays.asList(movie1, movie2);

        //action
        locationService.hireMovie(user, movies);
    }

    // robust way -> total control, checks message
    @Test
    public void shouldThrowExceptionWhenUserIsEmpty() throws MovieOutOfStockException {
        //scenery
        Movie movie1 = MovieBuilder.oneMovie().now();
        Movie movie2 = MovieBuilder.oneMovie().noStock().now();
        List<Movie> movies = Arrays.asList(movie1, movie2);

        //action
        try {
            locationService.hireMovie(null, movies);
            fail();
        } catch (RentalCompanyException e) {
            assertThat(e.getMessage(), is("User is empty."));
        }
    }

    // new way -> checks message, enough for most cases
    @Test
    public void shouldThrowExceptionWhenMovieIsEmpty() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        User user = UserBuilder.oneUser().now();

        exception.expect(RentalCompanyException.class);
        exception.expectMessage("Movie is empty.");

        //action
        locationService.hireMovie(user, null);
        fail();
    }

    @Test
    public void shouldGive25percentOffOnTheThirdMovieHired() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        Movie movie1 = MovieBuilder.oneMovie().now();
        Movie movie2 = new Movie("Movie 2", 3, 2.0);
        Movie movie3 = new Movie("Movie 3", 3, 2.0);

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3);
        User user = UserBuilder.oneUser().now();

        //action
        Location location = locationService.hireMovie(user, movies);

        //verification
        assertThat(location.getValue(), is(8.5));
    }

    @Test
    public void shouldGive50percentOffOnTheFourthMovieHired() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        Movie movie1 = MovieBuilder.oneMovie().now();
        Movie movie2 = new Movie("Movie 2", 3, 5.0);
        Movie movie3 = new Movie("Movie 3", 3, 4.0);
        Movie movie4 = new Movie("Movie 4", 3, 3.0);

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4);
        User user = UserBuilder.oneUser().now();

        //action
        Location location = locationService.hireMovie(user, movies);

        //verification
        assertThat(location.getValue(), is(14.5));

    }

    @Test
    public void shouldGive75percentOffOnTheFifthMovieHired() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        Movie movie1 = MovieBuilder.oneMovie().now();
        Movie movie2 = new Movie("Movie 2", 3, 5.0);
        Movie movie3 = new Movie("Movie 3", 3, 4.0);
        Movie movie4 = new Movie("Movie 4", 3, 3.0);
        Movie movie5 = new Movie("Movie 5", 3, 2.0);

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4, movie5);
        User user = UserBuilder.oneUser().now();

        //action
        Location location = locationService.hireMovie(user, movies);

        //verification
        assertThat(location.getValue(), is(15.0));

    }

    @Test
    public void shouldGive100percentOffOnTheSixthMovieHired() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        Movie movie1 = MovieBuilder.oneMovie().now();
        Movie movie2 = new Movie("Movie 2", 3, 5.0);
        Movie movie3 = new Movie("Movie 3", 3, 4.0);
        Movie movie4 = new Movie("Movie 4", 3, 3.0);
        Movie movie5 = new Movie("Movie 5", 3, 2.0);
        Movie movie6 = new Movie("Movie 6", 3, 1.0);


        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6);
        User user = UserBuilder.oneUser().now();

        //action
        Location location = locationService.hireMovie(user, movies);

        //verification
        assertThat(location.getValue(), is(15.0));

    }

    @Test
    public void mustReturnOnMondayWhenRentingOnSaturday() throws MovieOutOfStockException, RentalCompanyException {
        Assume.assumeTrue(verifyDayOfWeek(new Date(), Calendar.SATURDAY)); //Only executes on saturday

        Movie movie1 = MovieBuilder.oneMovie().now();
        List<Movie> movies = List.of(movie1);
        User user = UserBuilder.oneUser().now();

        Location location = locationService.hireMovie(user, movies);

        boolean isMonday = verifyDayOfWeek(location.getReturnDate(), Calendar.MONDAY);
        assertTrue(isMonday);
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws MovieOutOfStockException, RentalCompanyException{
        Assume.assumeTrue(verifyDayOfWeek(new Date(), Calendar.SATURDAY));

        User user = UserBuilder.oneUser().now();
        List<Movie> movies = List.of(new Movie("Filme 1", 1, 5.0));

        Location location = locationService.hireMovie(user, movies);

        assertThat(location.getReturnDate(), OwnMatchers.isMonday());
    }

    @Test
    public void shouldThrowExceptionWhenUserIsInDebt() throws MovieOutOfStockException {
        User user = UserBuilder.oneUser().now();
        List<Movie> movies = List.of(new Movie("Filme 1", 1, 5.0));

        when(debtService.isInDebt(user)).thenReturn(true);

        try {
            locationService.hireMovie(user, movies);
            fail();
        } catch (RentalCompanyException e) {
            assertThat(e.getMessage(), is("User is in debt!"));
        }

        verify(debtService).isInDebt(user);

    }

    @Test
    public void shouldNotifyLocationsDelay(){
        User user = UserBuilder.oneUser().now();
        List<Location> locations = List.of(oneLocation().withUser(user).withReturnDate(getDataWithDifferentDate(-2)).now());
        when(dao.getPendingLocations()).thenReturn(locations);

        locationService.notifyDelays();

        verify(emailService).notify(user);
    }
}
