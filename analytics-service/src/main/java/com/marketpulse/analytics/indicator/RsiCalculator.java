
package com.marketpulse.analytics.indicator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class RsiCalculator {

    private static final MathContext MC = MathContext.DECIMAL128;

    public BigDecimal calculate(List<BigDecimal> closingPrices, int period) {
        if (period <= 0) {
            throw new IllegalArgumentException(
                    "RSI period must be greater than zero");
        }

        if (closingPrices == null
                || closingPrices.size() < period + 1) {
            throw new IllegalArgumentException(
                    "At least " + (period + 1)
                            + " closing prices are required for RSI");
        }
        for (BigDecimal price : closingPrices) {
            if (price == null) {
                throw new IllegalArgumentException(
                        "Closing prices must not contain null values");
            }
        }

        BigDecimal gains = BigDecimal.ZERO;
        BigDecimal losses = BigDecimal.ZERO;

        for (int i = 1; i <= period; i++) {
            BigDecimal change = closingPrices.get(i)
                    .subtract(closingPrices.get(i - 1), MC);

            if (change.signum() > 0) {
                gains = gains.add(change, MC);
            } else if (change.signum() < 0) {
                losses = losses.add(change.abs(), MC);
            }
        }

        BigDecimal averageGain = gains.divide(
                BigDecimal.valueOf(period), MC);
        BigDecimal averageLoss = losses.divide(
                BigDecimal.valueOf(period), MC);

        for (int i = period + 1; i < closingPrices.size(); i++) {
            BigDecimal change = closingPrices.get(i)
                    .subtract(closingPrices.get(i - 1), MC);

            BigDecimal gain = change.signum() > 0
                    ? change : BigDecimal.ZERO;
            BigDecimal loss = change.signum() < 0
                    ? change.abs() : BigDecimal.ZERO;

            averageGain = averageGain
                    .multiply(BigDecimal.valueOf(period - 1L), MC)
                    .add(gain, MC)
                    .divide(BigDecimal.valueOf(period), MC);

            averageLoss = averageLoss
                    .multiply(BigDecimal.valueOf(period - 1L), MC)
                    .add(loss, MC)
                    .divide(BigDecimal.valueOf(period), MC);
        }

        if (averageLoss.signum() == 0) {
            return averageGain.signum() == 0
                    ? BigDecimal.valueOf(50)
                    : BigDecimal.valueOf(100);
        }

        BigDecimal relativeStrength = averageGain.divide(
                averageLoss, MC);

        return BigDecimal.valueOf(100).subtract(
                BigDecimal.valueOf(100).divide(
                        BigDecimal.ONE.add(relativeStrength, MC), MC),
                MC);
    }
}
