package net.oups.new_years_revolution.application;


import net.oups.new_years_revolution.infrastructure.persistence.Location;
import net.oups.new_years_revolution.infrastructure.persistence.LocationRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class HelloService {

    private final LocationRepository repository;

    public HelloService(LocationRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initialize() {
        if (repository.findAll().isEmpty()) {
            repository.saveAndFlush(new Location("Paris"));
            repository.saveAndFlush(new Location("Chambéry"));
            repository.saveAndFlush(new Location("Lyon"));
            repository.saveAndFlush(new Location("Marseille"));
            repository.saveAndFlush(new Location("Bordeaux"));
            repository.saveAndFlush(new Location("Lille"));
        }
    }

    public List<Location> locations() {
        return repository.findAll();
    }

}