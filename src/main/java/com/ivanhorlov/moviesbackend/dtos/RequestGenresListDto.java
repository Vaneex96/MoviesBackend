package com.ivanhorlov.moviesbackend.dtos;

import com.ivanhorlov.moviesbackend.pagination.SortingTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestGenresListDto {
    private List<Integer> genresIds;
    private SortingTypes sortingType;

}
