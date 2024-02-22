package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.AdditionalUserInfo;

public interface AdditionalUserInfoService {
    void saveInfo(AdditionalUserInfo additionalUserInfo);
    AdditionalUserInfo getAdditionalUserInfo(int userId);
    Boolean checkGenreExist(String name);
    void isGenreExist(String name);
}
