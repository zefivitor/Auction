package com.auction.springvproject.controllers;


import com.auction.springvproject.models.BankAccount;
import com.auction.springvproject.security.services.BankAccountService;
import com.auction.springvproject.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bankaccount")
public class BankAccountController {
@Autowired
    BankAccountService bankAccountService;


    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String addCredit(@RequestBody BankAccount bankAccount) {
       BankAccount currentBankAccountState = bankAccountService.getBankAccount(bankAccount.getUsername());
       double totalBalance=currentBankAccountState.getBalance()+ bankAccount.getBalance();
       currentBankAccountState.setBalance(totalBalance);
       bankAccountService.updateBankAccountBalance(currentBankAccountState);

        return "Balance updated successfully ";
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('USER') or  hasRole('ADMIN')")
    public BankAccount getBalanceInformation(@PathVariable String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if(!userDetails.getUsername().equals(username))
             throw new RuntimeException("Only information for your Bank Account can be accessed");
        return bankAccountService.getBankAccount(username);
    }


}
