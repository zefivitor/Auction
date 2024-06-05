package com.auction.springvproject.security.services;

import com.auction.springvproject.models.BankAccount;
import com.auction.springvproject.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;


    public BankAccount getBankAccount(String username) {
    return bankAccountRepository.findByUsername(username);
    }

    public void updateBankAccountBalance(BankAccount currentBankAccountState) {
   bankAccountRepository.save(currentBankAccountState);
    }
}
