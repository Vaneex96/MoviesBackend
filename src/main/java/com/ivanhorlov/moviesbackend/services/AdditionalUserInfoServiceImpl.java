package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.AdditionalUserInfo;
import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.User;
import com.ivanhorlov.moviesbackend.repositories.AdditionalUserInfoRepository;
import com.ivanhorlov.moviesbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdditionalUserInfoServiceImpl implements AdditionalUserInfoService {

    private final AdditionalUserInfoRepository userAdditionalInfoRepository;
    private final UserRepository userRepository;
    private final GenreService genreService;


    @Override
    public void saveInfo(AdditionalUserInfo additionalUserInfo) {
        userAdditionalInfoRepository.save(additionalUserInfo);
    }

    @Override
    public AdditionalUserInfo getAdditionalUserInfo(int userId) {
        Optional<User> userOptional = userRepository.findUserById(userId);
        AdditionalUserInfo additionalUserInfo = null;
        if (userOptional.isEmpty()){
            throw new NoSuchElementException("Not found user with id: " + userId);
        }

        Optional<AdditionalUserInfo> additionalUserInfoOptional = userAdditionalInfoRepository.getAdditionalUserInfoById(userId);
        if (additionalUserInfoOptional.isEmpty()){
            throw new NoSuchElementException("No detail information about user with id: " + userId);
        }

        additionalUserInfo = additionalUserInfoOptional.get();

        return additionalUserInfo;
    }

    @Override
    public Boolean checkGenreExist(String name) {
        return genreService.getGenreByName(name) == null;
    }

    @Override
    public void isGenreExist(String name) {
        if(genreService.getGenreByName(name) == null){
            throw new NoSuchElementException("Genre with name: s% doesn't exists");
        }
    }

}
