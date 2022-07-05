package br.com.bvidotto.builder;

import static br.com.bvidotto.utils.DateUtils.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import br.com.bvidotto.entity.Location;
import br.com.bvidotto.entity.Movie;
import br.com.bvidotto.entity.User;

public class LocationBuilder {
    private Location element;
    private LocationBuilder(){}

    public static LocationBuilder oneLocation() {
        LocationBuilder builder = new LocationBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(LocationBuilder builder) {
        builder.element = new Location();
        Location element = builder.element;


        element.setUser(UserBuilder.oneUser().now());
        element.setMovies(Collections.singletonList(MovieBuilder.oneMovie().now()));
        element.setLocationDate(new Date());
        element.setReturnDate(getDataWithDifferentDate(1));
        element.setValue(4.0);
    }

    public LocationBuilder withUser(User param) {
        element.setUser(param);
        return this;
    }

    public LocationBuilder withMoviesList(Movie... params) {
        element.setMovies(Arrays.asList(params));
        return this;
    }

    public LocationBuilder withLocationDate(Date param) {
        element.setLocationDate(param);
        return this;
    }

    public LocationBuilder withReturnDate(Date param) {
        element.setReturnDate(param);
        return this;
    }

    public LocationBuilder withValue(Double param) {
        element.setValue(param);
        return this;
    }

    public Location now() {
        return element;
    }
}
