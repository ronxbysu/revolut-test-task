package com.drastic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private Map<String, BigDecimal> store;

    public Database() {
        store = new HashMap<String, BigDecimal>() {{
            put("CashMachine", new BigDecimal(1000));
        }};
    }

    public Map<String, BigDecimal> getStore() {
        return store;
    }
}
