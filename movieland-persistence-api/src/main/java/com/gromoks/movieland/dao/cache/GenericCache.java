package com.gromoks.movieland.dao.cache;

import java.util.List;

public interface GenericCache <T> {
    List<T> getAll();
    void invalidate();
}
