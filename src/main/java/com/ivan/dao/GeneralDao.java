package com.ivan.dao;

/**
 * The GeneralDao interface defines a generic data access object with key and entity types.
 *
 * @param <K> The key type used for identifying entities.
 * @param <E> The entity type representing objects to be saved.
 */
public interface GeneralDao<K, E> { //k - key, e - entity

    /**
     * Saves the entity of type E.
     *
     * @param entity The entity object to be saved.
     * @return The saved entity object.
     */
    E save(E entity);
}