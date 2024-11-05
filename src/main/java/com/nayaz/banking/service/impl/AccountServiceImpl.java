package com.nayaz.banking.service.impl;

import com.nayaz.banking.dto.AccountDto;
import com.nayaz.banking.entity.Account;
import com.nayaz.banking.mapper.AccountMapper;
import com.nayaz.banking.repository.AccountRepository;
import com.nayaz.banking.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
       Account account = accountRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Account does not exists")
        );
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Account does not exists")
        );
       double totalAmount = account.getBalance() + amount;
       account.setBalance(totalAmount);
      Account savedAccount  = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {

        Account account = accountRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Account does not exists")
        );
        if(account.getBalance() < amount) {
            throw new RuntimeException("Insufficient Amount");
        }
        double totalAmount = account.getBalance() - amount;
        account.setBalance(totalAmount);
        Account savedAccount  = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
      List<Account> accounts =  accountRepository.findAll();
        return  accounts.stream().map((account)-> AccountMapper.mapToAccountDto(account)).collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        Account employee = accountRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Employee is not exists with given id : " +id));

        accountRepository.deleteById(id);
    }
}
