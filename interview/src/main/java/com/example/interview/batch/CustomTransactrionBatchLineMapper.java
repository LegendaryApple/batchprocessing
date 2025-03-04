package com.example.interview.batch;

import com.example.interview.entity.MyTransactions;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;

import javax.swing.text.DateFormatter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CustomTransactrionBatchLineMapper  implements LineMapper<MyTransactions> {
    private final String delimiter = "\\|";

    private final int expectedCount = 6;

    @Override
    public MyTransactions mapLine(String line, int lineNumber) throws Exception {
        if (StringUtils.isBlank(line)) {
            return new MyTransactions();
        } else {
            String[] fields = line.split(delimiter);  // ✅ Split using the pipe delimiter "|"
            if (fields.length != expectedCount) {
                throw new IncorrectTokenCountException(expectedCount, fields.length);
            }

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            MyTransactions transaction = new MyTransactions();
            transaction.setAccountNumber(fields[0]);
            transaction.setTrxAmount(new BigDecimal(fields[1]));
            transaction.setDescription(fields[2]);
            transaction.setTrxDate(LocalDate.parse(fields[3]));
            transaction.setTrxTime(LocalTime.parse(fields[4], timeFormatter));
            transaction.setCustomerId(Long.parseLong(fields[5]));

            return transaction;
        }
    }
}
