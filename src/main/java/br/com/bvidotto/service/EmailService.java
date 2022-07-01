package br.com.bvidotto.service;

import br.com.bvidotto.entity.User;

public interface EmailService {
    void notify(User user);
}
