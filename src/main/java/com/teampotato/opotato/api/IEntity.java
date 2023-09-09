package com.teampotato.opotato.api;

public interface IEntity {
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean shouldMove();
    void setShouldMove(boolean shouldMove);
}
