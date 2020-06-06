package com.fly.operate.util;

import java.math.BigDecimal;

public class TransactionModel {

    /**
     * @param price 单价
     * @param step  一共分多少次
     * @param rate  每次的百分比递减幅度
     */
    public static BigDecimal avgModel(BigDecimal price, int step, BigDecimal rate) {
        BigDecimal cost = new BigDecimal(0);
        System.out.println("等量投资模型开始建立");
        BigDecimal lastP = new BigDecimal(0);
        for (int i = 0; i < step; i++) {
            final BigDecimal current = price.multiply(
                    new BigDecimal(1).subtract(
                            rate.divide(new BigDecimal(100))
                                    .multiply(new BigDecimal(i))
                    )
            );
            lastP = current;
            cost = cost.add(current
            );
            System.out.println("第" + (i + 1) + "次买入,数量为1手,单价为=" + current);

        }
        //成本价
        final BigDecimal avg = cost.divide(new BigDecimal(step));
        System.out.println("等量投资模型>>>总花费=" + cost + ",成本价=" + avg + ",总手数=" + step);
        //反弹后的单价
        final BigDecimal tPrice = lastP.multiply(new BigDecimal(1).add(rate.divide(new BigDecimal(100))));
        final BigDecimal totalValue = tPrice.multiply(new BigDecimal(step));
        final BigDecimal subtract = totalValue.subtract(cost);
        final BigDecimal divide = subtract.divide(cost,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));

        System.out.println("最后反弹一次,反弹的幅度为:" + rate + "个百分点,反弹后单价为=" + tPrice + ",最终盈利=" + subtract + "元。利润率为="+divide+"%");


        System.out.println("等量投资模型完成建立");
        return cost;

    }

    public static void main(String[] args) {
            BigDecimal basePrice = new BigDecimal(100);
            int step = 5;
            BigDecimal rate = new BigDecimal(5);
            System.out.println("一手的单价是:" + basePrice);
            System.out.println("一共分" + step + "次买入");
            System.out.println("买入的阶梯幅度为=" + rate + "个百分点");
            try {
                Thread.sleep(1000L);

            } catch (Exception e) {

            }
            avgModel(basePrice, step, rate);
            ladderModel(basePrice, step, rate);
    }

    public static BigDecimal ladderModel(BigDecimal price, int step, BigDecimal rate) {
        BigDecimal cost = new BigDecimal(0);
        double vSum = 0d;
        System.out.println("阶梯投资模型开始建立");
        BigDecimal lastP = new BigDecimal(0);
        for (int i = 0; i < step; i++) {
            final double v = Math.pow(2d, Double.valueOf(i));
            vSum += v;
            final BigDecimal curPrice = price.multiply(
                    new BigDecimal(1).subtract(
                            rate.divide(new BigDecimal(100))
                                    .multiply(new BigDecimal(i))
                    )
            );
            lastP = curPrice;
            final BigDecimal current = new BigDecimal(v).multiply(curPrice);
            cost = cost.add(current);
            System.out.println("第" + (i + 1) + "次买入,数量为" + v + "手,单价为=" + curPrice);

        }
        //最后来一次反弹5个点
        final BigDecimal tPrice = lastP.multiply(new BigDecimal(1).add(rate.divide(new BigDecimal(100))));
        final BigDecimal lastValue = tPrice.multiply(new BigDecimal(vSum));//最终市值
        final BigDecimal subtract = lastValue.subtract(cost);//最终的盈利

        final BigDecimal avg = cost.divide(new BigDecimal(vSum), 2, BigDecimal.ROUND_HALF_UP);
        System.out.println("ladderModel>>>总花费=" + cost + ",成本价=" + avg + ",总手数=" + vSum);
        final BigDecimal divide = subtract.divide(cost,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        System.out.println("最后反弹一次,反弹的幅度为:" + rate + "个百分点,反弹后单价为=" + tPrice + ",最终盈利=" + subtract + "元。利润率为="+divide+"%");
        System.out.println("阶梯投资模型完成建立");

        return cost;
    }

}
