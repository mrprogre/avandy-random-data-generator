package com.avandy.dataset.generator;

import com.avandy.dataset.model.Row;
import com.avandy.dataset.ui.Gui;
import com.avandy.dataset.util.Randomizer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Generator {
    public static List<String> rows = new ArrayList<>();
    private final Data data;

    public Generator() {
        this.data = new Data();
    }

    public void generate(long rowCount) {
        generateRows(rowCount);
    }

    private void generateRows(long rowsCount) {
        final int minAge = 18;
        final int maxAge = 50;
        final int minGrade = 2;
        final int maxGrade = 5;

        // очистка таблицы и коллекции перед новой генерацией строк
        if (Gui.model.getRowCount() > 0) Gui.model.setRowCount(0);
        if (rows.size() > 0) rows.clear();

        Gui.setStatus("Generate..");
        long start = System.currentTimeMillis();
        for (int i = 1; i <= rowsCount; i++) {
            Row row = Row.builder()
                    .num(i)
                    .intData(Randomizer.getRandomInt())
                    .longData(Randomizer.getRandomLong())
                    .human(data.getRandomHuman())
                    .age(Randomizer.getRandomIntInterval(minAge, maxAge))
                    .doubleData(Randomizer.getRandomDouble(minGrade, maxGrade))
                    .car(data.getCar())
                    .color(data.getColor())
                    .country(data.getCountry())
                    .orderCount(Randomizer.getRandomIntInterval(1, 20))
                    .orderAmountSum(Randomizer.getRandomDouble(40, 8390))
                    .lastPurchaseDate(LocalDate.of(Randomizer.getRandomIntInterval(2000, 2022),
                            Randomizer.getRandomIntInterval(1, 12),
                            Randomizer.getRandomIntInterval(1, 28)))
                    .build();

            Gui.model.addRow(new Object[]{row.getNum(), row.getIntData(), row.getLongData(), row.getHuman(),
                    row.getAge(), row.getDoubleData(), row.getCar(), row.getColor(), row.getCountry(),
                    row.getOrderCount(), row.getOrderAmountSum(), row.getLastPurchaseDate()
            });
        }
        Gui.setStatus("Rows created in " + (System.currentTimeMillis() - start) + " ms.");
    }
}
