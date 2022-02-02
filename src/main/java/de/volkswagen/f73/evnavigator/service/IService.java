package de.volkswagen.f73.evnavigator.service;

import de.volkswagen.f73.evnavigator.model.IPlace;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
public interface IService<T extends IPlace> {
    List<T> getAll();
    T save(T obj);
    void delete(T obj);
}
