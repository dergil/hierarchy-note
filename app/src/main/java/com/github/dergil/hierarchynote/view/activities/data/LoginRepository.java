package com.github.dergil.hierarchynote.view.activities.data;

import android.app.Application;

import com.github.dergil.hierarchynote.AppExecutors;
import com.github.dergil.hierarchynote.model.dao.JwtDao;
import com.github.dergil.hierarchynote.model.db.FileRoomDatabase;
import com.github.dergil.hierarchynote.model.entity.JwtEntity;
import com.github.dergil.hierarchynote.view.activities.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

//    private AppExecutors appExecutors;
//    public LoginRepository(AppExecutors appExecutors) {
//        this.appExecutors = new AppExecutors();
//    }

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    private JwtDao jwtDao;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
//    private LoginRepository(Application application, LoginDataSource dataSource) {
//        this.dataSource = dataSource;
//        FileRoomDatabase db = FileRoomDatabase.getInstance(application, new AppExecutors());
//        jwtDao = db.jwtDao();
//    }


    private LoginRepository(FileRoomDatabase database, LoginDataSource dataSource) {
        this.dataSource = dataSource;
        jwtDao = database.jwtDao();
    }

    public static LoginRepository getInstance(FileRoomDatabase database, LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(database, dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password) {
//        appExecutors.networkIO().execute(() -> {

//todo: determine signup or login
            final Result<LoggedInUser> result;
            // handle login
            result = dataSource.login(username, password);
            if (result instanceof Result.Success) {
                LoggedInUser loggedInUser = ((Result.Success<LoggedInUser>) result).getData();
                setLoggedInUser(loggedInUser);
                JwtEntity jwtEntity = new JwtEntity();
                jwtEntity.setId(Long.parseLong(loggedInUser.getUserId()));
                jwtEntity.setEmail(loggedInUser.getEmail());
                jwtEntity.setJwt(loggedInUser.getJwt());
                System.out.println(jwtEntity.toString());
                jwtDao.insert(jwtEntity);
            }
            return result;
//        });

    }
}