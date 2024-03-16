package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.entities.AdditionalUserInfo;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.entities.User;
import com.ivanhorlov.moviesbackend.exception_handling.NoMatchPasswordsException;
import com.ivanhorlov.moviesbackend.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class UserService implements UserDetailsService {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${user-with-activation-code.lifetime}")
    private Duration userWithoutActivationLifetime;

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final MovieService movieService;
    private final UsersFavoriteMoviesService usersFavoriteMoviesService;
    private final AdditionalUserInfoService additionalUserServiceInfo;
    private final BCryptPasswordEncoder encoder;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public Optional<User> findByUserName(String username){
        return userRepository.findUserByUserName(username);
    }

    public User findUserByUserName(String username){
        Optional<User> userOptional = userRepository.findUserByUserName(username);
        User user = null;
        if(userOptional.isPresent()){
            user = userOptional.get();
        }

        return user;
    }

//    public void createNewUser(User user){
//        if(userRepository.findUserByUserName(user.getUserName()).isPresent()){
//            throw new UserAlreadyExistsException(String.format("User '%s' already exits", user.getUserName()));
//        }
//
//        user.setRoles(List.of(roleService.getRoleByName("ROLE_USER")));
//    }


    public User getUserByUserId(int id){
        Optional<User> user = userRepository.findUserById(id);
        if (user.isEmpty()){
            throw new NoSuchElementException("User with id: " + id + " not found");
        }

        return user.get();
    }


    public Integer getUserIdByUserName(String username){
        Optional<User> userOptional = userRepository.findUserByUserName(username);
        if(userOptional.isEmpty()){
            throw new NoSuchElementException("User " + username + " not found");
        }
        User user = userOptional.get();
        return user.getId();
    }


    public String getEmailByUserId(int id){
        return getUserByUserId(id).getEmail();
    }

    public User getUserByEmail(String email){
        User user = null;
        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if(userOptional.isPresent()){
            user = userOptional.get();
        }

        return user;
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public void addMovieToFavorite(int movieId, int userId) {
        User user = getUserByUserId(userId);
        Movie movie = movieService.getMovieById(movieId);
        List<Movie> movieCollection =(List<Movie>) user.getMovies();

        if(movieCollection.contains(movie)){
//            throw new NoSuchElementException("This movie already exits in favorite");

            boolean removed = movieCollection.remove(movie);

            System.out.println("Removing " +  removed);
        }else{
            movieCollection.add(movie);
        }

        user.setMovies(movieCollection);
        userRepository.save(user);
    }

    public AdditionalUserInfo addAdditionalUserInfo(AdditionalUserInfo userInfo,int id){
        AdditionalUserInfo additionalUserInfo = null;

        try{
            additionalUserInfo = additionalUserServiceInfo.getAdditionalUserInfo(id);
        }catch(NoSuchElementException e){
            log.debug(e.getMessage());
        }

        if(additionalUserInfo == null){
            userInfo.setId(id);
            additionalUserServiceInfo.saveInfo(userInfo);
            System.out.println(userInfo);
            return userInfo;
        }

        userInfo.setId(id);

        if(userInfo.getFavoriteGenre() == null) userInfo.setFavoriteGenre(additionalUserInfo.getFavoriteGenre());

        additionalUserServiceInfo.isGenreExist(userInfo.getFavoriteGenre());

        if(userInfo.getRealName() == null) userInfo.setRealName(additionalUserInfo.getRealName());

        if(userInfo.getSurname() == null) userInfo.setSurname(additionalUserInfo.getSurname());

        if(userInfo.getPhoneNumber() == null) userInfo.setPhoneNumber(additionalUserInfo.getPhoneNumber());

        if(userInfo.getCountry() == null) userInfo.setCountry(additionalUserInfo.getCountry());

        if(userInfo.getLang() == null) userInfo.setLang(additionalUserInfo.getLang());

        if(userInfo.getAboutMe() == null) userInfo.setAboutMe(additionalUserInfo.getAboutMe());


        additionalUserServiceInfo.saveInfo(userInfo);

        return userInfo;
    }

    public boolean isUserExist(String userName) {
        Optional<User> user = findByUserName(userName);

        if(user.isEmpty()){
            return false;
        }

        return true;
    }

    public User addUser(RegistrationUserDto registrationUser){
        if(isUserExist(registrationUser.getUsername())){
            return null;
        }

        User user = new User();
        String activationCode = UUID.randomUUID().toString();
        user.setUserName(registrationUser.getUsername());
        user.setEmail(registrationUser.getEmail());
        user.setActivationCode(activationCode);
        user.setRoles(List.of(roleService.getRoleByName("ROLE_USER")));


        if(registrationUser.getPassword().equals(registrationUser.getConfirmPassword())){
            user.setPassword(encoder.encode(registrationUser.getPassword()));
        } else {
            throw new NoMatchPasswordsException("Password doesn't match the confirm password");
        }

        userRepository.save(user);

        return user;
    }

    @Transactional
    public void deleteUserWithoutConfirmEmail(){
        Query query = entityManager.createQuery("delete from User where activationCode != 'NULL'");
        entityManager.joinTransaction();
        query.executeUpdate();
    }

    public boolean isActivationCode(String code){
        Optional<User> userOptional = userRepository.findUserByActivationCode(code);

        if(userOptional.isEmpty()){
            return false;
        }

        User user = userOptional.get();
        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }

    public String saveAvatar(MultipartFile file, int id){
        if(file.isEmpty()){
            return null;
        }

        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()){
            uploadDir.mkdir();
        }

        String uuid = UUID.randomUUID().toString();
        String resultName = uuid + "." + file.getOriginalFilename();

        try {
            file.transferTo(new File(uploadPath + "\\" + resultName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AdditionalUserInfo additionalUserInfo = additionalUserServiceInfo.getAdditionalUserInfo(id);
        additionalUserInfo.setAvatarPath(resultName);
        additionalUserServiceInfo.saveInfo(additionalUserInfo);


        return resultName;

    }

    public boolean isEmailExist(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if(user.isPresent()){
            return true;
        }

        return false;
    }

    public Boolean isMovieInFavorite(int id, int movieId) {
        User user = userRepository.findUserById(id).get();
        List<Movie> movieList = user.getMovies().stream().filter(movie -> movie.getId() == movieId).toList();

        return !movieList.isEmpty();
    }
}




//    public void deleteUserWithoutConfirmEmail(User user){
//        Timer timer = new Timer();
//
//
//        TimerTask timerTask2 = new TimerTask() {
//
//            @Override
//            public void run() {
//
//                String code = getUserByUserId(user.getId()).getActivationCode();
//
//                if(code != null){
//                    System.out.println(user.getUserName() + ": " + user.getActivationCode());
//
//                    userRepository.delete(user);
//                    System.out.println("done!");
//                }
//
//                timer.purge();
//            }
//        };
//
//        timer.schedule(timerTask2, userWithoutActivationLifetime.toMillis());
////        System.out.println("Deleted")
//    }