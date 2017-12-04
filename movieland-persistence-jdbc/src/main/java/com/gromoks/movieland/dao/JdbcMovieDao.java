package com.gromoks.movieland.dao;

import com.gromoks.movieland.dao.entity.MovieToCountry;
import com.gromoks.movieland.dao.entity.MovieToGenre;
import com.gromoks.movieland.dao.entity.MovieToReview;
import com.gromoks.movieland.dao.entity.CachedMovieRating;
import com.gromoks.movieland.dao.mapper.*;
import com.gromoks.movieland.dao.sqlbuilder.QueryBuilder;
import com.gromoks.movieland.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class JdbcMovieDao implements MovieDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BeanPropertyRowMapper<MovieToCountry> movieToCountryBeanPropertyRowMapper = new BeanPropertyRowMapper<>(MovieToCountry.class);

    private final MovieRowMapper movieRowMapper = new MovieRowMapper();

    private final MovieToCountryRowMapper movieToCountryRowMapper = new MovieToCountryRowMapper();

    private final MovieToGenreRowMapper movieToGenreRowMapper = new MovieToGenreRowMapper();

    private final MovieToReviewRowMapper movieToReviewRowMapper = new MovieToReviewRowMapper();

    private final MovieRatingRowMapper movieRatingRowMapper = new MovieRatingRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private String getAllMovieSQL;

    @Autowired
    private String getAllMovieToCountrySQL;

    @Autowired
    private String getAllMovieToGenreSQL;

    @Autowired
    private String getMovieToReviewSQL;

    @Autowired
    private String getRandomMovieSQL;

    @Autowired
    private String getMoviesByGenreIdSQL;

    @Autowired
    private String getMoviesByIdSQL;

    @Autowired
    private String addUserRatingSQL;

    @Autowired
    private String getMovieRatingSQL;

    @Autowired
    private String addMovieSQL;

    @Autowired
    private String addMovieToCountrySQL;

    @Autowired
    private String addMovieToGenreSQL;

    @Autowired
    private String updateMovieSQL;

    @Autowired
    private String deleteMovieToCountrySQL;

    @Autowired
    private String deleteMovieToGenreSQL;

    public List<Movie> getAll(LinkedHashMap<String, String> requestParamMap) {
        log.info("Start query to get all movies from DB");
        long startTime = System.currentTimeMillis();

        String resultQuery = QueryBuilder.enrichQueryWithOrderRequestParam(getAllMovieSQL, requestParamMap);
        List<Movie> movies = jdbcTemplate.query(resultQuery, movieRowMapper);

        log.info("Finish query to get all movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getRandom() {
        log.info("Start query to get 3 random movies from DB");
        long startTime = System.currentTimeMillis();

        List<Movie> movies = jdbcTemplate.query(getRandomMovieSQL, movieRowMapper);
        List<MovieToCountry> movieToCountries = getMovieToCountryList(movies);
        List<MovieToGenre> movieToGenres = getMovieToGenreList(movies);

        for (Movie movie : movies) {
            enrichMovieWithCountry(movie, movieToCountries);
            enrichMovieWithGenre(movie, movieToGenres);
        }

        log.info("Finish query to get 3 random movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getByGenreId(int id, LinkedHashMap<String, String> requestParamMap) {
        log.info("Start query to get movies by genre");
        long startTime = System.currentTimeMillis();

        String resultQuery = QueryBuilder.enrichQueryWithOrderRequestParam(getMoviesByGenreIdSQL, requestParamMap);
        List<Movie> movies = jdbcTemplate.query(resultQuery, movieRowMapper, id);

        log.info("Finish query to get movies by genre from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public Movie getById(int id) {
        log.info("Start query to get movies by id");
        long startTime = System.currentTimeMillis();

        Movie movie = jdbcTemplate.queryForObject(getMoviesByIdSQL, movieRowMapper, id);
        List<MovieToCountry> movieToCountries = getSingleMovieToCountryList(movie);
        List<MovieToGenre> movieToGenres = getSingleMovieToGenreList(movie);
        List<MovieToReview> movieToReviews = getSingleMovieToReviewList(movie);

        enrichMovieWithCountry(movie, movieToCountries);
        enrichMovieWithGenre(movie, movieToGenres);
        enrichMovieWithReview(movie, movieToReviews);

        log.info("Finish query to get movies by id from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movie;
    }

    @Override
    public void addMovieRatings(ConcurrentLinkedQueue<Rating> ratings) {
        log.info("Start query to add rating");
        long startTime = System.currentTimeMillis();

        List<Map<String, Object>> batchValues = new ArrayList<>(ratings.size());
        for (Rating rating : ratings) {
            batchValues.add(new MapSqlParameterSource("movieId", rating.getMovieId())
                    .addValue("userId", rating.getUserId())
                    .addValue("rating", rating.getRating())
                    .getValues());
        }

        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(addUserRatingSQL,
                batchValues.toArray(new Map[ratings.size()]));
        log.info("Finish query to add rating list to DB. It took {} ms to insert {} rows", System.currentTimeMillis() - startTime, updateCounts.length);
    }

    @Override
    public List<CachedMovieRating> getMovieRating() {
        log.info("Start query to get movie rating from DB");
        long startTime = System.currentTimeMillis();

        List<CachedMovieRating> cachedMovieRatingCaches = jdbcTemplate.query(getMovieRatingSQL, movieRatingRowMapper);

        log.info("Finish query to get movie rating from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return cachedMovieRatingCaches;
    }

    @Override
    public void add(Movie movie) {
        log.info("Start query to add movie {} to DB", movie);
        long startTime = System.currentTimeMillis();

        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();

            parameterSource.addValue("nameRussian", movie.getNameRussian());
            parameterSource.addValue("nameNative", movie.getNameNative());
            parameterSource.addValue("yearOfRelease", movie.getYearOfRelease());
            parameterSource.addValue("description", movie.getDescription());
            parameterSource.addValue("rating", movie.getRating());
            parameterSource.addValue("price", movie.getPrice());
            parameterSource.addValue("picturePath", movie.getPicturePath());

            namedParameterJdbcTemplate.update(addMovieSQL, parameterSource, keyHolder, new String[]{"id"});
            int movieId = keyHolder.getKey().intValue();

            addMovieToCountry(movieId, movie.getCountries());
            addMovieToGenre(movieId, movie.getGenres());

            transactionManager.commit(transactionStatus);

            log.info("Finish query to add movie {} to DB. It took {} ms", movie, System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw new RuntimeException("Operation has not been done");
        }
    }

    @Override
    public void edit(Movie movie) {
        log.info("Start query to update movie {} to DB", movie);
        long startTime = System.currentTimeMillis();

        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();

            parameterSource.addValue("nameRussian", movie.getNameRussian());
            parameterSource.addValue("nameNative", movie.getNameNative());
            parameterSource.addValue("picturePath", movie.getPicturePath());
            parameterSource.addValue("id", movie.getId());

            namedParameterJdbcTemplate.update(updateMovieSQL, parameterSource);

            removeMovieLink(movie.getId(), deleteMovieToCountrySQL);
            removeMovieLink(movie.getId(), deleteMovieToGenreSQL);
            addMovieToCountry(movie.getId(), movie.getCountries());
            addMovieToGenre(movie.getId(), movie.getGenres());

            transactionManager.commit(transactionStatus);

            log.info("Finish query to update movie {} to DB. It took {} ms", movie, System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw new RuntimeException("Operation has not been done");
        }

    }

    private void removeMovieLink(int movieId, String sql) {
        log.info("Start query to remove movie link");
        long startTime = System.currentTimeMillis();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("movieId", movieId);
        namedParameterJdbcTemplate.update(sql, parameterSource);

        log.info("Finish query to remove movie link to DB. It took {} ms", System.currentTimeMillis() - startTime);
    }

    private void addMovieToCountry(int movieId, List<Country> countries) {
        log.info("Start query to add movie to country link");
        long startTime = System.currentTimeMillis();

        List<MovieToCountry> movieToCountries = new ArrayList<>();
        for (Country country : countries) {
            MovieToCountry movieToCountry = new MovieToCountry();
            movieToCountry.setMovieId(movieId);
            movieToCountry.setCountryId(country.getId());
            movieToCountries.add(movieToCountry);
        }

        List<Map<String, Object>> batchValues = new ArrayList<>(movieToCountries.size());
        for (MovieToCountry movieToCountry : movieToCountries) {
            batchValues.add(new MapSqlParameterSource("movieId", movieToCountry.getMovieId())
                    .addValue("countryId", movieToCountry.getCountryId())
                    .getValues());
        }

        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(addMovieToCountrySQL,
                batchValues.toArray(new Map[movieToCountries.size()]));
        log.info("Finish query to add movie to country link to DB. It took {} ms to insert {} rows", System.currentTimeMillis() - startTime, updateCounts.length);
    }

    private void addMovieToGenre(int movieId, List<Genre> genres) {
        log.info("Start query to add movie to genre link");
        long startTime = System.currentTimeMillis();

        List<MovieToGenre> movieToGenres = new ArrayList<>();
        for (Genre genre : genres) {
            MovieToGenre movieToGenre = new MovieToGenre();
            movieToGenre.setMovieId(movieId);
            movieToGenre.setGenreId(genre.getId());
            movieToGenres.add(movieToGenre);
        }

        List<Map<String, Object>> batchValues = new ArrayList<>(movieToGenres.size());
        for (MovieToGenre movieToGenre : movieToGenres) {
            batchValues.add(new MapSqlParameterSource("movieId", movieToGenre.getMovieId())
                    .addValue("genreId", movieToGenre.getGenreId())
                    .getValues());
        }

        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(addMovieToGenreSQL,
                batchValues.toArray(new Map[movieToGenres.size()]));
        log.info("Finish query to add movie to genre link to DB. It took {} ms to insert {} rows", System.currentTimeMillis() - startTime, updateCounts.length);
    }

    private List<MovieToCountry> getMovieToCountryList(List<Movie> movies) {
        List<Integer> movieIds = new ArrayList<>();

        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToCountry> movieToCountries = namedParameterJdbcTemplate.query(getAllMovieToCountrySQL, namedParameters, movieToCountryRowMapper);

        return movieToCountries;
    }

    private List<MovieToCountry> getSingleMovieToCountryList(Movie movie) {
        List<Integer> movieIds = new ArrayList<>();
        movieIds.add(movie.getId());

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToCountry> movieToCountries = namedParameterJdbcTemplate.query(getAllMovieToCountrySQL, namedParameters, movieToCountryRowMapper);

        return movieToCountries;
    }

    private List<MovieToGenre> getMovieToGenreList(List<Movie> movies) {
        List<Integer> movieIds = new ArrayList<>();

        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToGenre> movieToGenres = namedParameterJdbcTemplate.query(getAllMovieToGenreSQL, namedParameters, movieToGenreRowMapper);

        return movieToGenres;
    }

    private List<MovieToGenre> getSingleMovieToGenreList(Movie movie) {
        List<Integer> movieIds = new ArrayList<>();
        movieIds.add(movie.getId());

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToGenre> movieToGenres = namedParameterJdbcTemplate.query(getAllMovieToGenreSQL, namedParameters, movieToGenreRowMapper);

        return movieToGenres;
    }

    private List<MovieToReview> getSingleMovieToReviewList(Movie movie) {
        log.info("Start query to get movie and review linkage");
        long startTime = System.currentTimeMillis();

        List<MovieToReview> movieToReviews = jdbcTemplate.query(getMovieToReviewSQL, movieToReviewRowMapper, movie.getId());

        log.info("Finish query to get movie and review linkage from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movieToReviews;
    }

    private void enrichMovieWithCountry(Movie movie, List<MovieToCountry> movieToCountries) {
        List<Country> countries = new ArrayList<>();

        for (MovieToCountry movieToCountry : movieToCountries) {
            if (movieToCountry.getMovieId() == movie.getId()) {
                countries.add(new Country(movieToCountry.getCountryId(), movieToCountry.getCountry()));
            }
        }

        movie.setCountries(countries);
    }

    private void enrichMovieWithGenre(Movie movie, List<MovieToGenre> movieToGenres) {
        List<Genre> genres = new ArrayList<>();

        for (MovieToGenre movieToGenre : movieToGenres) {
            if (movieToGenre.getMovieId() == movie.getId()) {
                genres.add(new Genre(movieToGenre.getGenreId(), movieToGenre.getGenre()));
            }
        }

        movie.setGenres(genres);
    }

    private void enrichMovieWithReview(Movie movie, List<MovieToReview> movieToReviews) {
        List<Review> reviews = new ArrayList<>();

        for (MovieToReview movieToReview : movieToReviews) {
            if (movieToReview.getMovieId() == movie.getId()) {
                reviews.add(new Review(movieToReview.getId(), movieToReview.getUser(), movieToReview.getText()));
            }
        }

        movie.setReviews(reviews);
    }

}
