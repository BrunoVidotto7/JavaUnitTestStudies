package br.com.bvidotto.service;

import static br.com.bvidotto.builder.LocationBuilder.oneLocation;
import static br.com.bvidotto.builder.MovieBuilder.oneMovie;
import static br.com.bvidotto.utils.DateUtils.getDataWithDifferentDate;
import static br.com.bvidotto.utils.DateUtils.isSameDate;
import static br.com.bvidotto.utils.DateUtils.verifyDayOfWeek;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.bvidotto.builder.UserBuilder;
import br.com.bvidotto.dao.LocationDao;
import br.com.bvidotto.entity.Location;
import br.com.bvidotto.entity.Movie;
import br.com.bvidotto.entity.User;
import br.com.bvidotto.exceptions.MovieOutOfStockException;
import br.com.bvidotto.exceptions.RentalCompanyException;
import br.com.bvidotto.matchers.OwnMatchers;
import br.com.bvidotto.utils.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocationService.class, DateUtils.class})
@PowerMockIgnore("jdk.internal.reflect.*")
public class LocationServiceTest {

    @InjectMocks
    private LocationService locationService;

    @Mock
    private LocationDao dao;
    @Mock
    private EmailService emailService;
    @Mock
    private DebtService debtService;

    private static int count = 0;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        System.out.printf("Test %d finished.%n", count);
        count = count + 1;
    }

    @Test
    public void shouldHireMovie() throws Exception {
        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DateUtils.getDate(28,4,2017));
        //scenery
        User user = UserBuilder.oneUser().now();
        Movie movie1 = oneMovie().now();
        Movie movie2 = oneMovie().withValue(2.0).now();
        List<Movie> movies = Arrays.asList(movie1, movie2);

        //action
        Location location = locationService.hireMovie(user, movies);

        //validation
        assertEquals(7.0, location.getValue(), 0.01);
        assertTrue(isSameDate(location.getLocationDate(), new Date()));
        assertTrue(isSameDate(location.getReturnDate(), DateUtils.getDate(29,4,2017)));

        assertThat(location.getValue(), is(equalTo(7.0)));
        assertThat(location.getValue(), is(not(4.0)));
        assertThat(isSameDate(location.getLocationDate(), new Date()), is(true));
        assertThat(isSameDate(location.getReturnDate(), DateUtils.getDataWithDifferentDate(1)), is(true));

        error.checkThat(location.getValue(), is(equalTo(7.0)));
        error.checkThat(location.getValue(), is(not(4.0)));
        error.checkThat(isSameDate(location.getLocationDate(), DateUtils.getDate(28, 4,2017)), is(true));
        error.checkThat(isSameDate(location.getReturnDate(), DateUtils.getDataWithDifferentDate(1)), is(true));
    }

    // elegant way -> good when just the exception matters
    @Test(expected = MovieOutOfStockException.class)
    public void shouldThrowExceptionWhenMovieIsOutOfStock() throws MovieOutOfStockException, RentalCompanyException {
        //scenery
        User user = UserBuilder.oneUser().now();
        Movie movie1 = oneMovie().now();
        Movie movie2 = oneMovie().noStock().now();
        List<Movie> movies = Arrays.asList(movie1, movie2);

        //action
        locationService.hireMovie(user, movies);
    }

    // robust way -> total control, checks message
    @Test
    public void shouldThrowExceptionWhenUserIsEmpty() throws MovieOutOfStockException {
        //scenery
        Movie movie1 = oneMovie().now();
        Movie movie2 = oneMovie().noStock().now();
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
        Movie movie1 = oneMovie().now();
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
        Movie movie1 = oneMovie().now();
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
        Movie movie1 = oneMovie().now();
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
        Movie movie1 = oneMovie().now();
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
    public void mustReturnOnMondayWhenRentingOnSaturday() throws Exception {
        Movie movie1 = oneMovie().now();
        List<Movie> movies = Collections.singletonList(movie1);
        User user = UserBuilder.oneUser().now();

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DateUtils.getDate(29,4,2017));

        Location location = locationService.hireMovie(user, movies);

        boolean isMonday = verifyDayOfWeek(location.getReturnDate(), Calendar.MONDAY);
        assertTrue(isMonday);
    }

    @Test
    public void shouldGoBackMondayWhenHireOnSaturday() throws Exception {
        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DateUtils.getDate(29,4,2017));

        User user = UserBuilder.oneUser().now();
        List<Movie> movies = Collections.singletonList(new Movie("Filme 1", 1, 5.0));

        Location location = locationService.hireMovie(user, movies);

        assertThat(location.getReturnDate(), OwnMatchers.isMonday());
        PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();
    }

    @Test
    public void shouldThrowExceptionWhenUserIsInDebt() throws Exception {
        User user = UserBuilder.oneUser().now();
        List<Movie> movies = Collections.singletonList(new Movie("Filme 1", 1, 5.0));

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
        User user2 = UserBuilder.oneUser().withName("User with no delay").now();
        List<Location> locations = Arrays.asList(
            oneLocation().withUser(user).withReturnDate(getDataWithDifferentDate(-2)).now(),
            oneLocation().withUser(user2).now());
        when(dao.getPendingLocations()).thenReturn(locations);

        locationService.notifyDelays();

        verify(emailService).notify(user);
    }

    @Test
    public void shouldTreatErrorOnDebtService() throws Exception {
        User user = UserBuilder.oneUser().now();
        List<Movie> movies = Collections.singletonList(oneMovie().now());

        when(debtService.isInDebt(user)).thenThrow(new Exception("Fail at all"));

        exception.expect(RentalCompanyException.class);
        exception.expectMessage("System is not working now, try again later.");

        locationService.hireMovie(user, movies);

    }

    @Test
    public void shouldPostponeALocation() {
        Location location = oneLocation().now();

        locationService.postponeLocation(location,3);

        ArgumentCaptor<Location> argumentCaptor = ArgumentCaptor.forClass(Location.class);
        Mockito.verify(dao).save(argumentCaptor.capture());
        Location returnedLocation = argumentCaptor.getValue();

        Assert.assertThat(returnedLocation.getValue(), is(12.0));
    }
}
