package app.repositories;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityRepositoryMock<E extends Identifiable> {
    private final List<E> entities;

    public AbstractEntityRepositoryMock() {
        this.entities = new ArrayList<>();
        initializeEntities();
    }

    protected abstract void initializeEntities();


    public List<E> findAll() {
        return entities;
    }
}
