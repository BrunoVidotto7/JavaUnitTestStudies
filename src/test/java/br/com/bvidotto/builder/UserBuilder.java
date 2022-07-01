package br.com.bvidotto.builder;

import br.com.bvidotto.entity.User;

public class UserBuilder {

    private User user;

    private UserBuilder () {}

    public static UserBuilder oneUser() {
        UserBuilder builder = new UserBuilder();
        builder.user = new User();
        builder.user.setName("User 1");
        return builder;
    }

    public User now() {
        return user;
    }
}
