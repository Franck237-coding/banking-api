package com.banking.service;

import com.banking.model.Bank;
import com.banking.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    public Bank createBank(Bank bank) {
        if (bankRepository.existsByCode(bank.getCode())) {
            throw new RuntimeException("Une banque avec ce code existe déjà");
        }
        if (bankRepository.existsByNom(bank.getNom())) {
            throw new RuntimeException("Une banque avec ce nom existe déjà");
        }
        return bankRepository.save(bank);
    }

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Optional<Bank> getBankById(Long id) {
        return bankRepository.findById(id);
    }

    public Optional<Bank> getBankByCode(String code) {
        return bankRepository.findByCode(code);
    }

    public Bank updateBank(Long id, Bank bankDetails) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banque non trouvée avec l'ID: " + id));

        if (!bank.getCode().equals(bankDetails.getCode()) &&
            bankRepository.existsByCode(bankDetails.getCode())) {
            throw new RuntimeException("Une banque avec ce code existe déjà");
        }
        if (!bank.getNom().equals(bankDetails.getNom()) &&
            bankRepository.existsByNom(bankDetails.getNom())) {
            throw new RuntimeException("Une banque avec ce nom existe déjà");
        }

        bank.setNom(bankDetails.getNom());
        bank.setCode(bankDetails.getCode());
        bank.setAdresse(bankDetails.getAdresse());
        bank.setTelephone(bankDetails.getTelephone());

        return bankRepository.save(bank);
    }

    private void existsByNom(String string) {
        // TODO
    }

    public void deleteBank(Long id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banque non trouvée avec l'ID: " + id));
        bankRepository.delete(bank);
    }
}