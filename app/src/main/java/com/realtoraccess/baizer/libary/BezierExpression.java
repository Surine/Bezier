package com.realtoraccess.baizer.libary;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

import java.util.List;

/**
 * Created by Allen Zhang on 2017/6/24.
 */

public class BezierExpression implements TypeEvaluator<PointF> {

    private List<PointF> mPointFs;

    BezierExpression(List<PointF> pointFs) {
        this.mPointFs = pointFs;
    }


    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

        PointF pointF = new PointF();
        float disTime = 1 - fraction;

        //控制点个数
        int pointCount = mPointFs.size();
        //n介贝塞尔
        int number = pointCount - 1;

        float measureX = 0;
        float measureY = 0;
        for (int i = 0; i <= number; i++) {
            //求贝塞尔曲线公式系数
            measureX += (group(number, i) / fact(i)) * mPointFs.get(i).x * Math.pow(disTime, number - i) * Math.pow(fraction, i);
            measureY += (group(number, i) / fact(i)) * mPointFs.get(i).y * Math.pow(disTime, number - i) * Math.pow(fraction, i);
        }

        pointF.x = measureX;
        pointF.y = measureY;
        return pointF;
    }


    /**
     * 计算一个数的阶乘
     *
     * @param n
     * @return
     */
    private int fact(int n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * fact(n - 1);
        }
    }

    /**
     * 组合数分子的计算
     *
     * @param start 起始值  例如5
     * @param count 个数 3  则5*4*3
     * @return
     */
    private int group(int start, int count) {
        if (start <= 0 || count < 0 || count > start) {
            throw new IllegalArgumentException(getClass() + " arguments are illegal");
        }
        if (count == 0) {
            return 1;
        }
        int sumModulus = start;
        //如果当前个数是1 则返回原数
        if (count == 1) {
            return start;
        }
        //个数大于1计算
        for (int i = 1; i < count; i++) {
            sumModulus = sumModulus * (start - i);
            System.out.print(sumModulus);
        }
        return sumModulus;
    }
}
