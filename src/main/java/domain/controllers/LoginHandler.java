package domain.controllers;

import domain.scenes.LoginScene;

public class LoginHandler {
    private static LoginHandler loginHandler = new LoginHandler();
    private LoginScene loginScene;

    private LoginHandler() {
        loginScene = new LoginScene();
    }

    public static LoginHandler getInstance() {
        return loginHandler;
    }

    public boolean signIn(String username, String password) {
        return loginScene.singIn(username, password);
    }

    public boolean signUp(String username, String password) {
        return loginScene.signUp(username, password);
    }
}
