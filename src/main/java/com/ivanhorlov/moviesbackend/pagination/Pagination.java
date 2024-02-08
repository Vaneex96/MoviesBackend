package com.ivanhorlov.moviesbackend.pagination;

import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Data
public class Pagination <T> {

    private int page = 1;

    private int amountElements = 10;

    private List<T> list;

    public Pagination(int page, int amountElements) {
        this.page = page;
        this.amountElements = amountElements;
    }

    public Pagination() {
    }

//    public Pagination(List<T> list) {
//        this.list = list;
//    }
//
//    public Pagination(int page, List<T> list) {
//        this.page = page;
//        this.list = list;
//    }
//
//    public Pagination(int page, int amountElements, List<T> list) {
//        this.page = page;
//        this.amountElements = amountElements;
//        this.list = list;
//    }

    public List<T> listPagination(List<T> list){
        this.list = list;

        if (page == 0){
            return list;
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

        System.out.println("list size: "+list.size());


        List<T> paginatedList = list.subList(fromIndex, toIndex);

        return paginatedList;
    }
}
