package com.sanradiance.mobilewpp.DataModels;

public class PlantDataModel {
    public int id;
    public String plantId;
    public String vendorInstalled;
    public String district;
    public String taluk;
    public String panchayat;
    public String village;
    public String habitation;
    public String plantCapacityLPH;
    public String eControllerMake;
    public String panelId;
    public String latitude;
    public String longitude;
    public String mobileNumber;
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

    public String getVendorInstalled() {
        return vendorInstalled;
    }

    public void setVendorInstalled(String vendorInstalled) {
        this.vendorInstalled = vendorInstalled;
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

    public String getHabitation() {
        return habitation;
    }

    public void setHabitation(String habitation) {
        this.habitation = habitation;
    }

    public String getPlantCapacityLPH() {
        return plantCapacityLPH;
    }

    public void setPlantCapacityLPH(String plantCapacityLPH) {
        this.plantCapacityLPH = plantCapacityLPH;
    }

    public String geteControllerMake() {
        return eControllerMake;
    }

    public void seteControllerMake(String eControllerMake) {
        this.eControllerMake = eControllerMake;
    }

    public String getPanelId() {
        return panelId;
    }

    public void setPanelId(String panelId) {
        this.panelId = panelId;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public PlantDataModel(int id, String plantId, String vendorInstalled, String district, String taluk, String panchayat, String village, String habitation, String plantCapacityLPH, String eControllerMake, String panelId, String latitude, String longitude, String mobileNumber, String serviceProvider, String createdAt, String updatedAt) {
        this.id = id;
        this.plantId = plantId;
        this.vendorInstalled = vendorInstalled;
        this.district = district;
        this.taluk = taluk;
        this.panchayat = panchayat;
        this.village = village;
        this.habitation = habitation;
        this.plantCapacityLPH = plantCapacityLPH;
        this.eControllerMake = eControllerMake;
        this.panelId = panelId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mobileNumber = mobileNumber;
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
