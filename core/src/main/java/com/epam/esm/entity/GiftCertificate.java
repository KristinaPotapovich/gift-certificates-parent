package com.epam.esm.entity;


import java.util.Date;

public class GiftCertificate {
    private long id;
    private String name;
    private String description;
    private double price;
    private int durationInDays;
    private Date createDate;
    private Date lastUpdateDate;

    public GiftCertificate() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate)) return false;

        GiftCertificate that = (GiftCertificate) o;

        if (getId() != that.getId()) return false;
        if (Double.compare(that.getPrice(), getPrice()) != 0) return false;
        if (getDurationInDays() != that.getDurationInDays()) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getCreateDate() != null ? !getCreateDate().equals(that.getCreateDate()) : that.getCreateDate() != null)
            return false;
        return getLastUpdateDate() != null ? getLastUpdateDate().equals(that.getLastUpdateDate()) : that.getLastUpdateDate() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getDurationInDays();
        result = 31 * result + (getCreateDate() != null ? getCreateDate().hashCode() : 0);
        result = 31 * result + (getLastUpdateDate() != null ? getLastUpdateDate().hashCode() : 0);
        return result;
    }
}
