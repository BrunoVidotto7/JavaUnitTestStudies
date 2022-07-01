package br.com.bvidotto.dao;

import java.util.List;

import br.com.bvidotto.entity.Location;

public interface LocationDao {

    void save(Location location);

    List<Location> getPendingLocations();
}
