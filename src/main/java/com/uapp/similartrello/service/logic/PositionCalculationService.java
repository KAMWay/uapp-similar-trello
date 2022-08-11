package com.uapp.similartrello.service.logic;

import java.util.List;

public interface PositionCalculationService<T> {

    int getUpdatePosition(int listSize, T entity);

    List<T> getList4UpdatePositionIfDelete(List<T> entities, T entity);

    List<T> getList4UpdatePositionIfSave(List<T> entities, T entity);
}
