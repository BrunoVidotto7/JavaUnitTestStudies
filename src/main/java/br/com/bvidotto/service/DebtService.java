package br.com.bvidotto.service;

import br.com.bvidotto.entity.User;

public interface DebtService {
    boolean isInDebt(User user) throws Exception;
}
