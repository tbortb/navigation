package de.volkswagen.f73.evnavigator.service;

import de.volkswagen.f73.evnavigator.model.IPlaceBase;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
public interface ServiceBase<T extends IPlaceBase> {
    public List<T> getAll();
    public T save(T obj);
    public void delete(T obj);
}
