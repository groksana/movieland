package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.cache.GenericCache;
import com.gromoks.movieland.entity.Genre;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class GenreCacheImplTest {

    @Mock
    private GenericCache<Genre> mockGenericCache;

    @InjectMocks
    private JdbcGenreDao jdbcGenreDao;

    private List<Genre> genres;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        genres = new ArrayList<>();
        Genre genre = new Genre(1, "детектив");
        genres.add(genre);
    }

    @Test
    public void testGetAllGenres() {
        when(mockGenericCache.getAll()).thenReturn(genres);
        assertEquals(genres.get(0).getId(), 1);
        assertEquals(genres.get(0).getName(), "детектив");
    }
}
