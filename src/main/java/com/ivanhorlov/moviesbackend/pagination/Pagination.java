package com.ivanhorlov.moviesbackend.pagination;

import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class Pagination {

    public Pagination() {
    }

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
}
