package it.unicam.flashcards.model.context;

import java.util.List;
import java.util.function.Consumer;

import it.unicam.flashcards.model.entity.IdentifiableEntity;

public interface GenericRepository<T extends IdentifiableEntity> {

    public void add(T e);

    public T get(Long id);

    public List<T> getAll();

    public void remove(Long id);

    public void removeAll();

    public void update(Long id, Consumer<T> changes);
}
