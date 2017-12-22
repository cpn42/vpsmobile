package fr.bs_tech.vps.bindings;

/**
 * Created by cpellerin on 21/12/2017.
 */

public class MissionEvents
{
    private String date;
    private String departure;
    private String hdep;
    private String arrival;
    private String harr;
    private int km;

    public MissionEvents(String date, String departure, String hdep, String arrival, String harr, int km)
    {
        this.date = date;
        this.departure = departure;
        this.hdep = hdep;
        this.arrival = arrival;
        this.harr = harr;
        this.km = km;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDeparture()
    {
        return departure;
    }

    public void setDeparture(String departure)
    {
        this.departure = departure;
    }

    public String getHdep()
    {
        return hdep;
    }

    public void setHdep(String hdep)
    {
        this.hdep = hdep;
    }

    public String getArrival()
    {
        return arrival;
    }

    public void setArrival(String arrival)
    {
        this.arrival = arrival;
    }

    public String getHarr()
    {
        return harr;
    }

    public void setHarr(String harr)
    {
        this.harr = harr;
    }

    public int getKm()
    {
        return km;
    }

    public void setKm(int km)
    {
        this.km = km;
    }
}
