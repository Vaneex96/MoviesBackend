package com.ivanhorlov.moviesbackend.pagination;

import com.ivanhorlov.moviesbackend.entities.Movie;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@Component
public class PaginationAndSorting {

    public <T> List<T> listPagination(List<T> list, int amountElements, int page){
        if (page <= 0){
            return list;
        }

        if(amountElements <= 0){
            amountElements = 10;
        }


        int fromIndex = 0;
        int toIndex = amountElements;

        fromIndex = (page - 1) * amountElements;

        if (fromIndex > list.size() || fromIndex < 0){
            return new ArrayList<>();
        }

        toIndex = page * amountElements;
        if(page * amountElements > list.size() - 1){
            toIndex = list.size();
        }

         return list.subList(fromIndex, toIndex);
    }

    public List<Movie> sortByPopularity(List<Movie> list, SortingTypes sortingType){
        if(sortingType.equals(SortingTypes.popularity_desc)){
            return list.stream().sorted((movie1, movie2) -> (int)(movie2.getPopularity() - movie1.getPopularity())).toList();
        }

        if(sortingType.equals(SortingTypes.popularity_asc)){
            return list.stream().sorted((movie1, movie2) -> (int)(movie1.getPopularity() - movie2.getPopularity())).toList();
        }

        return new ArrayList<>();
    }

    public List<Movie> sortByRating(List<Movie> list, SortingTypes sortingType){
        if(sortingType.equals(SortingTypes.rating_desc)){
            return list.stream().sorted((movie1, movie2) -> movie2.getVoteCount() - movie1.getVoteCount()).toList();
        }

        if(sortingType.equals(SortingTypes.rating_asc)){
            return list.stream().sorted(Comparator.comparingInt(Movie::getVoteCount)).toList();
        }

        return new ArrayList<>();
    }

    public List<Movie> sortByReleaseDate(List<Movie> list, SortingTypes sortingType){
        if(sortingType.equals(SortingTypes.release_date_desc)){
            return list.stream().sorted((movie1, movie2) -> movie2.getRelease_date().compareTo(movie1.getRelease_date())).toList();
        }

        if(sortingType.equals(SortingTypes.release_date_asc)){
            return list.stream().sorted((movie1, movie2) -> movie1.getRelease_date().compareTo(movie2.getRelease_date())).toList();
        }

        return new ArrayList<>();
    }
}
