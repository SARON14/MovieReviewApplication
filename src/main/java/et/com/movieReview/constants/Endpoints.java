package et.com.movieReview.constants;

public class Endpoints {

    public static final String ADD_MOVIE="/movies";
    public static final String GET_MOVIE_DETAIL = "/movies/{movieId:[0-9]+}";
    public static final String GET_MOVIE_DETAIL_BY_ImdbID = "/movies/{imdbID:tt[A-Za-z0-9]+}";
    public static final String ADD_USER = "/users ";
    public static final String ADD_REVIEW= "/reviews";
    public static final String GET_REVIEW_BY_USERID= "/reviews/{userId}";
    public static final String SEARCH_MOVIE ="/movies";

}
