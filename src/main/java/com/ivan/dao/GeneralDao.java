package com.ivan.dao;

public interface GeneralDao<K, E> { //k - ключ, e - entity
    E save(E entity);
}