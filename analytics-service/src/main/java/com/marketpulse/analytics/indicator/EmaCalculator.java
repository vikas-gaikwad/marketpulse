package com.marketpulse.analytics.indicator;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class EmaCalculator {

    private static final MathContext MC = MathContext.DECIMAL128;

    public BigDecimal calculate(List<BigDecimal> closingPrices, int period) {
        if (period <= 0) {
            throw new IllegalArgumentException(
                    "EMA period must be greater than zero");
        }

        if (closingPrices == null || closingPrices.size() < period) {
            throw new IllegalArgumentException(
                    "At least " + period + " closing prices are required");
        }

        BigDecimal sum = BigDecimal.ZERO;

        for (int i = 0; i < period; i++) {
            BigDecimal price = closingPrices.get(i);

            if (price == null) {
                throw new IllegalArgumentException(
                        "Closing prices must not contain null values");
            }

            sum = sum.add(price, MC);
        }

        BigDecimal ema = sum.divide(
                BigDecimal.valueOf(period), MC);

        BigDecimal alpha = BigDecimal.valueOf(2)
                .divide(BigDecimal.valueOf(period + 1L), MC);

        for (int i = period; i < closingPrices.size(); i++) {
            BigDecimal price = closingPrices.get(i);

            if (price == null) {
                throw new IllegalArgumentException(
                        "Closing prices must not contain null values");
            }

            ema = price.multiply(alpha, MC)
                    .add(
                            ema.multiply(
                                    BigDecimal.ONE.subtract(alpha, MC), MC),
                            MC);
        }

        return ema;
    }
}
