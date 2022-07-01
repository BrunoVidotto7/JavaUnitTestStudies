package br.com.bvidotto.service;

import br.com.bvidotto.entity.User;

public interface DebtService {
    public boolean isInDebt(User user);
}
