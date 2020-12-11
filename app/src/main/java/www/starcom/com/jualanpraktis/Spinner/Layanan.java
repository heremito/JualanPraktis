package www.starcom.com.jualanpraktis.Spinner;

import java.util.List;

public class Layanan {
    private String courier;
    private String service;
    private String description;
    private String etd;
    private int value;
    private String decimalValue;
    List<Layanan> layananList;


    @Override
    public String toString() {
        return service+"  -  "+decimalValue + "\n"+"Estimasi Pengiriman : " + etd+" hari";
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Layanan> getLayananList() {
        return layananList;
    }

    public void setLayananList(List<Layanan> layananList) {
        this.layananList = layananList;
    }

    public String getDecimalValue() {
        return decimalValue;
    }

    public void setDecimalValue(String decimalValue) {
        this.decimalValue = decimalValue;
    }
}
