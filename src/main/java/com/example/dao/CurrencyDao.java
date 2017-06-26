package com.example.dao;


import com.example.domain.Currency;

public class CurrencyDao {

    public String retrieveCurrencyDefault(String clientProfile) {
        return "FL";
    }

    public Currency retrieveCurrencyOnId(Integer id) {
        Currency currency = new Currency();
        currency.setCurrencyType("EUR");
        currency.setCode(1);

        return currency;
    }
}
