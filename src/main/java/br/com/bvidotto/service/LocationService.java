package br.com.bvidotto.service;

import br.com.bvidotto.entity.Location;
import br.com.bvidotto.entity.Movie;
import br.com.bvidotto.entity.User;
import br.com.bvidotto.exceptions.MovieOutOfStockException;
import br.com.bvidotto.exceptions.RentalCompanyException;
import br.com.bvidotto.utils.DataUtils;
import java.util.Date;

public class LocationService {

	public String vPublic;
	protected String vProtected;
	private String vPrivate;
	String vDefault;
	public Location hireMovie(User user, Movie movie) throws MovieOutOfStockException, RentalCompanyException {
		if (user == null) {
			throw new RentalCompanyException("User is empty.");
		}

		if (movie == null) {
			throw new RentalCompanyException("Movie is empty.");
		}

		if (movie.getInventory() == 0) {
			throw new MovieOutOfStockException();
		}

		Location location = new Location();
		location.setFilme(movie);
		location.setUsuario(user);
		location.setLocationDate(new Date());
		location.setValue(movie.getLocationPrice());

		//Delivery next day
		Date deliveryDate = new Date();
		deliveryDate = DataUtils.adicionarDias(deliveryDate, 1);
		location.setReturnDate(deliveryDate);
		
		//Saving Location method...
		//TODO
		
		return location;
	}
}