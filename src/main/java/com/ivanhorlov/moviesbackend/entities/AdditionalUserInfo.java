package com.ivanhorlov.moviesbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="userdetails")
public class AdditionalUserInfo {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int id;

    @Column(name="real_name")
    private String realName;

    @Column(name="surname")
    private String surname;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="country")
    private String country;

    @Column(name="lang")
    private String lang;

    @Column(name="favorite_genre")
    private String favoriteGenre;

    @Column(name="some_info")
    private String aboutMe;

    @Override
    public String toString() {
        return "AdditionalUserInfo{" +
                "id=" + id +
                ", realName='" + realName + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", country='" + country + '\'' +
                ", lang='" + lang + '\'' +
                ", favoriteGenre='" + favoriteGenre + '\'' +
                ", aboutMe='" + aboutMe + '\'' +
                '}';
    }
}
