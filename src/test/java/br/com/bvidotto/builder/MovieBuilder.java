package br.com.bvidotto.builder;

import br.com.bvidotto.entity.Movie;

public class MovieBuilder {

    private Movie movie;

    private MovieBuilder(){}

    public static MovieBuilder oneMovie() {
        MovieBuilder builder = new MovieBuilder();
        builder.movie = new Movie();
        builder.movie.setInventory(2);
        builder.movie.setName("Movie 1");
        builder.movie.setLocationPrice(5.0);
        return builder;
    }

    public MovieBuilder noStock(){
        movie.setInventory(0);
        return this;
    }

    public MovieBuilder withValue(Double value){
        movie.setLocationPrice(2.0);
        return this;

    }

    public Movie now(){
        return movie;
    }
}
