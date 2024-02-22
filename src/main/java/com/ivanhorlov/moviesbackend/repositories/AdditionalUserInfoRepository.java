package com.ivanhorlov.moviesbackend.repositories;

import com.ivanhorlov.moviesbackend.entities.AdditionalUserInfo;
import com.ivanhorlov.moviesbackend.services.AdditionalUserInfoService;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdditionalUserInfoRepository extends CrudRepository<AdditionalUserInfo, Integer> {
    Optional<AdditionalUserInfo> getAdditionalUserInfoById(int id);
}
