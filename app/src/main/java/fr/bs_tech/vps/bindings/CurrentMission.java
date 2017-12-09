package fr.bs_tech.vps.bindings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;

import fr.bs_tech.vps.BR;

/**
 * Created by cpellerin on 06/12/2017.
 */

public class CurrentMission extends BaseObservable
{
    private String login;
    private String pwdHash;
    private String globalJourney;
    private String tvOt;
    private String tvDate;
    private String tvStatus;
    private String tvTransporterName;
    private String tvConvoyLeader;
    private String tvOrderTitle;
    private String editOrder;
    private String tvPhone;
    private String editDimP;
    private String editDimL;
    private String editDiml;
    private String editDimH;
    private String tvMainTitle;
    private String tvTimeDeparture;
    private String tvArrivalKm;
    private String tvDepartureKm;
    private String tvDeparture;
    private String tvArrival;
    private String tvArrKm;
    private String tvDepKm;
    private String editDepComments;
    private String bmpSigDischarge;
    private boolean bSignedDischarge;

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getPwdHash()
    {
        return pwdHash;
    }

    public void setPwdHash(String pwdHash)
    {
        this.pwdHash = pwdHash;
    }

    public String getGlobalJourney()
    {
        return globalJourney;
    }

    public void setGlobalJourney(String globalJourney)
    {
        this.globalJourney = globalJourney;
    }

    public String getTvOt()
    {
        return tvOt;
    }

    public void setTvOt(String tvOt)
    {
        this.tvOt = tvOt;
    }

    public String getTvDate()
    {
        return tvDate;
    }

    public void setTvDate(String tvDate)
    {
        this.tvDate = tvDate;
    }

    public String getTvStatus()
    {
        return tvStatus;
    }

    public void setTvStatus(String tvStatus)
    {
        this.tvStatus = tvStatus;
    }

    public String getTvTransporterName()
    {
        return tvTransporterName;
    }

    public void setTvTransporterName(String tvTransporterName)
    {
        this.tvTransporterName = tvTransporterName;
    }

    public String getTvConvoyLeader()
    {
        return tvConvoyLeader;
    }

    public void setTvConvoyLeader(String tvConvoyLeader)
    {
        this.tvConvoyLeader = tvConvoyLeader;
    }

    public String getTvOrderTitle()
    {
        return tvOrderTitle;
    }

    public void setTvOrderTitle(String tvOrderTitle)
    {
        this.tvOrderTitle = tvOrderTitle;
    }

    @Bindable
    public String getEditOrder()
    {
        return editOrder;
    }

    public void setEditOrder(String editOrder)
    {
        this.editOrder = editOrder;
        notifyPropertyChanged(BR.editOrder);
    }

    public String getTvPhone()
    {
        return tvPhone;
    }

    public void setTvPhone(String tvPhone)
    {
        this.tvPhone = tvPhone;
    }

    @Bindable
    public String getEditDimP()
    {
        return editDimP;
    }

    public void setEditDimP(String editDimP)
    {
        this.editDimP = editDimP;
        notifyPropertyChanged(BR.editDimP);
    }

    @Bindable
    public String getEditDimL()
    {
        return editDimL;
    }

    public void setEditDimL(String editDimL)
    {
        this.editDimL = editDimL;
        notifyPropertyChanged(BR.editDimL);
    }

    @Bindable
    public String getEditDiml()
    {
        return editDiml;
    }

    public void setEditDiml(String editDiml)
    {
        this.editDiml = editDiml;
        notifyPropertyChanged(BR.editDiml);
    }

    @Bindable
    public String getEditDimH()
    {
        return editDimH;
    }

    public void setEditDimH(String editDimH)
    {
        this.editDimH = editDimH;
        notifyPropertyChanged(BR.editDimH);
    }

    public String getTvMainTitle()
    {
        return tvMainTitle;
    }

    public void setTvMainTitle(String tvMainTitle)
    {
        this.tvMainTitle = tvMainTitle;
    }

    public String getTvTimeDeparture()
    {
        return tvTimeDeparture;
    }

    public void setTvTimeDeparture(String tvTimeDeparture)
    {
        this.tvTimeDeparture = tvTimeDeparture;
    }

    public String getTvArrivalKm()
    {
        return tvArrivalKm;
    }

    public void setTvArrivalKm(String tvArrivalKm)
    {
        this.tvArrivalKm = tvArrivalKm;
    }

    public String getTvDepartureKm()
    {
        return tvDepartureKm;
    }

    public void setTvDepartureKm(String tvDepartureKm)
    {
        this.tvDepartureKm = tvDepartureKm;
    }

    public String getTvDeparture()
    {
        return tvDeparture;
    }

    public void setTvDeparture(String tvDeparture)
    {
        this.tvDeparture = tvDeparture;
    }

    public String getTvArrival()
    {
        return tvArrival;
    }

    public void setTvArrival(String tvArrival)
    {
        this.tvArrival = tvArrival;
    }

    public String getTvArrKm()
    {
        return tvArrKm;
    }

    public void setTvArrKm(String tvArrKm)
    {
        this.tvArrKm = tvArrKm;
    }

    public String getTvDepKm()
    {
        return tvDepKm;
    }

    public void setTvDepKm(String tvDepKm)
    {
        this.tvDepKm = tvDepKm;
    }

    @Bindable
    public String getEditDepComments()
    {
        return editDepComments;
    }

    public void setEditDepComments(String editDepComments)
    {
        this.editDepComments = editDepComments;
        notifyPropertyChanged(BR.editDepComments);
    }

    public String getBmpSigDischarge()
    {
        return bmpSigDischarge;
    }

    public void setBmpSigDischarge(String bmpSigDischarge)
    {
        this.bmpSigDischarge = bmpSigDischarge;
    }

    public boolean isbSignedDischarge()
    {
        return bSignedDischarge;
    }

    public void setbSignedDischarge(boolean bSignedDischarge)
    {
        this.bSignedDischarge = bSignedDischarge;
    }
}
