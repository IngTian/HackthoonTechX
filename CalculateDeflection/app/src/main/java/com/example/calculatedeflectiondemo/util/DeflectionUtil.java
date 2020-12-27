package com.example.calculatedeflectiondemo.util;

public class DeflectionUtil {

    /**
     * Calculate the angle between two points.
     *
     * @param pntFirst First coordinate
     * @param pntNext  Second coordinate
     * @return Angle between pntFirst and pntSecond.
     */
    public static double getAngle(Coordinate pntFirst, Coordinate pntNext) {
        double first_x = pntFirst.getX(), first_y = pntFirst.getY(), second_x = pntNext.getX(), second_y = pntNext.getY();
        double dRotateAngle = Math.atan2(Math.abs(first_x - second_x), Math.abs(first_y - second_y));
        if (second_x >= first_x) {
            if (second_y >= first_y) {
            } else {
                dRotateAngle = Math.PI - dRotateAngle;
            }
        } else {
            if (second_y >= first_y) {
                dRotateAngle = 2 * Math.PI - dRotateAngle;
            } else {
                dRotateAngle = Math.PI + dRotateAngle;
            }
        }
        dRotateAngle = dRotateAngle * 180 / Math.PI;
        return dRotateAngle;
    }
}
