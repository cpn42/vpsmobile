package fr.bs_tech.vps.utils;

/**
 * Created by cpellerin on 20/12/2017.
 */

public class Pair
{
    private double x = 0.0;
    private double y = 0.0;

    public Pair(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }
}
