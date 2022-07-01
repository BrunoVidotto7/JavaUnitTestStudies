package br.com.bvidotto.service;

import static br.com.bvidotto.utils.DataUtils.addDays;
import static br.com.bvidotto.utils.DataUtils.verifyDayOfWeek;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.bvidotto.dao.LocationDao;
import br.com.bvidotto.entity.Location;
import br.com.bvidotto.entity.Movie;
import br.com.bvidotto.entity.User;
import br.com.bvidotto.exceptions.MovieOutOfStockException;
import br.com.bvidotto.exceptions.RentalCompanyException;

public class LocationService {

    public String vPublic;
    protected String vProtected;
    private String vPrivate;
    String vDefault;


    private LocationDao locationDao;
    private DebtService debtService;

    private EmailService emailService;

    public Location hireMovie(User user, List<Movie> movies) throws MovieOutOfStockException, RentalCompanyException {
        if (user == null) {
            throw new RentalCompanyException("User is empty.");
        }

        if (movies == null || movies.isEmpty()) {
            throw new RentalCompanyException("Movie is empty.");
        }

        for (Movie movie : movies) {
            if (movie.getInventory() == 0) {
                throw new MovieOutOfStockException();
            }
        }

        if (debtService.isInDebt(user)) {
            throw new RentalCompanyException("User is in debt!");
        }

        Location location = new Location();
        location.setMovies(movies);
        location.setUser(user);
        location.setLocationDate(new Date());
        Double totalValue = 0.0;

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            Double movieValue = movie.getLocationPrice();
            movieValue = setDiscount(i, movieValue);
            totalValue += movieValue;
        }
        location.setValue(totalValue);

        //Delivery next day
        Date deliveryDate = new Date();
        deliveryDate = addDays(deliveryDate, 1);
        if (verifyDayOfWeek(deliveryDate, Calendar.SUNDAY)) {
            deliveryDate = addDays(deliveryDate, 1);
        }
        location.setReturnDate(deliveryDate);

        locationDao.save(location);

        return location;
    }

    private Double setDiscount(int i, Double movieValue) {
        switch (i) {
            case 2 -> movieValue = movieValue * 0.75;
            case 3 -> movieValue = movieValue * 0.5;
            case 4 -> movieValue = movieValue * 0.25;
            case 5 -> movieValue = movieValue * 0;
        }
        return movieValue;
    }

    public void notifyDelays() {
        List<Location> locations = locationDao.getPendingLocations();
        for(Location location: locations) {
            emailService.notify(location.getUser());
        }
    }

    public void setLocationDao (LocationDao dao) {
        this.locationDao = dao;
    }

    public void setDebtService (DebtService debtService) {
        this.debtService = debtService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}