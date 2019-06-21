package com.sanradiance.mobilewpp.DataModels;

public class PlantDataModel {
    public int id;
    public String plantId;
    public String address;
    public String district;
    public String taluk;
    public String panchayat;
    public String village;
    public String wpp_status;
    public String plantCapacity;
    public String dateOfInstallation;
    public String schemeinstalled;
    public String latitude;
    public String longitude;
    public String plantSupplier;
    public String aAndmagency;
    public String omforservice;
    public String serviceProvider;
    public String createdAt;
    public String updatedAt;

    public String operatorName;
    public Long operatorMobile;


    public void setOperatorMobile(Long operatorMobile) {
        this.operatorMobile = operatorMobile;
    }



    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperatorMobile() {
        return operatorMobile;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getaddress() {
        return address;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getPanchayat() {
        return panchayat;
    }

    public void setPanchayat(String panchayat) {
        this.panchayat = panchayat;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getwpp_status() {
        return wpp_status;
    }

    public void setwpp_status(String wpp_status) {
        this.wpp_status = wpp_status;
    }

    public String getPlantCapacity() {
        return plantCapacity;
    }

    public void setPlantCapacity(String plantCapacity) {
        this.plantCapacity = plantCapacity;
    }

    public String getdateOfInstallation() {
        return dateOfInstallation;
    }

    public void setdateOfInstallation(String dateOfInstallation) {
        this.dateOfInstallation = dateOfInstallation;
    }

    public String getschemeinstalled() {
        return schemeinstalled;
    }

    public void setschemeinstalled(String schemeinstalled) {
        this.schemeinstalled = schemeinstalled;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getplantSupplier() {
        return plantSupplier;
    }

    public void setplantSupplier(String plantSupplier) {
        this.plantSupplier = plantSupplier;
    }

    public String getaAndmagency() {
        return aAndmagency;
    }

    public void setaAndmagency(String aAndmagency) {
        this.aAndmagency = aAndmagency;
    }

    public String getomforservice() {
        return omforservice;
    }

    public void setomforservice(String omforservice) {
        this.omforservice = omforservice;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PlantDataModel(int id, String plantId,String district, String taluk, String panchayat, String village, String address, String latitude, String longitude,  String wpp_status, String dateOfInstallation, String schemeinstalled, String plantCapacity,String plantSupplier, String aAndmagency, String serviceProvider,String omforservice,String createdAt, String updatedAt) {
        this.id = id;
        this.plantId = plantId;
        this.district = district;
        this.taluk = taluk;
        this.panchayat = panchayat;
        this.village = village;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.wpp_status = wpp_status;
        this.dateOfInstallation = dateOfInstallation;
        this.schemeinstalled = schemeinstalled;
        this.plantCapacity = plantCapacity;
        this.plantSupplier = plantSupplier;
        this.aAndmagency = aAndmagency;
        this.omforservice = omforservice;
        this.serviceProvider = serviceProvider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PlantDataModel(int id, String plantId, String district, String operatorName, Long operatorMobile) {
        this.id = id;
        this.plantId = plantId;
        this.district = district;
        this.operatorName = operatorName;
        this.operatorMobile = operatorMobile;
    }
}
