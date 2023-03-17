package com.agent.cashmoovui.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubscriberInfoModel {
    public String transactionId;
    public String requestTime;
    public String responseTime;
    public String resultCode;
    public String resultDescription;
    public Subscriber subscriber;
    public Customer customer;


    public SubscriberInfoModel(String transactionId, String requestTime, String responseTime, String resultCode, String resultDescription, Subscriber subscriber) {
        this.transactionId = transactionId;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
        this.subscriber = subscriber;
    }
    public SubscriberInfoModel(String transactionId, String requestTime, String responseTime, String resultCode, String resultDescription, Customer customer) {
        this.transactionId = transactionId;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
        this.customer = customer;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public static class Subscriber {
        public int id;
        public boolean walletExists;
        public String code;
        public String walletOwnerCategoryCode;
        public String ownerName;
        public String mobileNumber;
        public String idProofNumber;
        public String email;
        public String status;
        public String state;
        public String stage;
        public String idProofTypeCode;
        public String idProofTypeName;
        public String notificationLanguage;
        public String notificationTypeCode;
        public String notificationName;
        public String gender;
        public String dateOfBirth;
        public String lastName;
        public String registerCountryCode;
        public String registerCountryName;
        public String creationDate;
        public String profileTypeCode;
        public String profileTypeName;
        public String walletOwnerCatName;
        public String occupationTypeCode;
        public String occupationTypeName;
        public String requestedSource;
        public String regesterCountryDialCode;
        public String walletOwnerCode;

        public Subscriber(int id, boolean walletExists, String code, String walletOwnerCategoryCode, String ownerName, String mobileNumber, String idProofNumber, String email, String status, String state, String stage, String idProofTypeCode, String idProofTypeName, String notificationLanguage, String notificationTypeCode, String notificationName, String gender, String dateOfBirth, String lastName, String registerCountryCode, String registerCountryName, String creationDate, String profileTypeCode, String profileTypeName, String walletOwnerCatName, String occupationTypeCode, String occupationTypeName, String requestedSource, String regesterCountryDialCode, String walletOwnerCode) {
            this.id = id;
            this.walletExists = walletExists;
            this.code = code;
            this.walletOwnerCategoryCode = walletOwnerCategoryCode;
            this.ownerName = ownerName;
            this.mobileNumber = mobileNumber;
            this.idProofNumber = idProofNumber;
            this.email = email;
            this.status = status;
            this.state = state;
            this.stage = stage;
            this.idProofTypeCode = idProofTypeCode;
            this.idProofTypeName = idProofTypeName;
            this.notificationLanguage = notificationLanguage;
            this.notificationTypeCode = notificationTypeCode;
            this.notificationName = notificationName;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
            this.lastName = lastName;
            this.registerCountryCode = registerCountryCode;
            this.registerCountryName = registerCountryName;
            this.creationDate = creationDate;
            this.profileTypeCode = profileTypeCode;
            this.profileTypeName = profileTypeName;
            this.walletOwnerCatName = walletOwnerCatName;
            this.occupationTypeCode = occupationTypeCode;
            this.occupationTypeName = occupationTypeName;
            this.requestedSource = requestedSource;
            this.regesterCountryDialCode = regesterCountryDialCode;
            this.walletOwnerCode = walletOwnerCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isWalletExists() {
            return walletExists;
        }

        public void setWalletExists(boolean walletExists) {
            this.walletExists = walletExists;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getWalletOwnerCategoryCode() {
            return walletOwnerCategoryCode;
        }

        public void setWalletOwnerCategoryCode(String walletOwnerCategoryCode) {
            this.walletOwnerCategoryCode = walletOwnerCategoryCode;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getIdProofNumber() {
            return idProofNumber;
        }

        public void setIdProofNumber(String idProofNumber) {
            this.idProofNumber = idProofNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public String getIdProofTypeCode() {
            return idProofTypeCode;
        }

        public void setIdProofTypeCode(String idProofTypeCode) {
            this.idProofTypeCode = idProofTypeCode;
        }

        public String getIdProofTypeName() {
            return idProofTypeName;
        }

        public void setIdProofTypeName(String idProofTypeName) {
            this.idProofTypeName = idProofTypeName;
        }

        public String getNotificationLanguage() {
            return notificationLanguage;
        }

        public void setNotificationLanguage(String notificationLanguage) {
            this.notificationLanguage = notificationLanguage;
        }

        public String getNotificationTypeCode() {
            return notificationTypeCode;
        }

        public void setNotificationTypeCode(String notificationTypeCode) {
            this.notificationTypeCode = notificationTypeCode;
        }

        public String getNotificationName() {
            return notificationName;
        }

        public void setNotificationName(String notificationName) {
            this.notificationName = notificationName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getRegisterCountryCode() {
            return registerCountryCode;
        }

        public void setRegisterCountryCode(String registerCountryCode) {
            this.registerCountryCode = registerCountryCode;
        }

        public String getRegisterCountryName() {
            return registerCountryName;
        }

        public void setRegisterCountryName(String registerCountryName) {
            this.registerCountryName = registerCountryName;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public String getProfileTypeCode() {
            return profileTypeCode;
        }

        public void setProfileTypeCode(String profileTypeCode) {
            this.profileTypeCode = profileTypeCode;
        }

        public String getProfileTypeName() {
            return profileTypeName;
        }

        public void setProfileTypeName(String profileTypeName) {
            this.profileTypeName = profileTypeName;
        }

        public String getWalletOwnerCatName() {
            return walletOwnerCatName;
        }

        public void setWalletOwnerCatName(String walletOwnerCatName) {
            this.walletOwnerCatName = walletOwnerCatName;
        }

        public String getOccupationTypeCode() {
            return occupationTypeCode;
        }

        public void setOccupationTypeCode(String occupationTypeCode) {
            this.occupationTypeCode = occupationTypeCode;
        }

        public String getOccupationTypeName() {
            return occupationTypeName;
        }

        public void setOccupationTypeName(String occupationTypeName) {
            this.occupationTypeName = occupationTypeName;
        }

        public String getRequestedSource() {
            return requestedSource;
        }

        public void setRequestedSource(String requestedSource) {
            this.requestedSource = requestedSource;
        }

        public String getRegesterCountryDialCode() {
            return regesterCountryDialCode;
        }

        public void setRegesterCountryDialCode(String regesterCountryDialCode) {
            this.regesterCountryDialCode = regesterCountryDialCode;
        }

        public String getWalletOwnerCode() {
            return walletOwnerCode;
        }

        public void setWalletOwnerCode(String walletOwnerCode) {
            this.walletOwnerCode = walletOwnerCode;
        }

     }

    public static class Customer {
        public int id;
        public String code;
        public String firstName;
        public String lastName;
        public String email;
        public String mobileNumber;
        public String gender;
        public String idProofTypeCode;
        public String idProofTypeName;
        public String idProofNumber;
        public String idExpiryDate;
        public String dateOfBirth;
        public String countryCode;
        public String countryName;
        public String regionCode;
        public String regionName;
        public String city;
        public String address;
        public String issuingCountryCode;
        public String issuingCountryName;
        public String idProofUrl;
        public String status;
        public String creationDate;
        public String createdBy;
        public String modificationDate;
        public String modifiedBy;

        public Customer(int id, String code, String firstName, String lastName, String email, String mobileNumber, String gender, String idProofTypeCode, String idProofTypeName, String idProofNumber, String idExpiryDate, String dateOfBirth, String countryCode, String countryName, String regionCode, String regionName, String city, String address, String issuingCountryCode, String issuingCountryName, String idProofUrl, String status, String creationDate, String createdBy, String modificationDate, String modifiedBy) {
            this.id = id;
            this.code = code;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.mobileNumber = mobileNumber;
            this.gender = gender;
            this.idProofTypeCode = idProofTypeCode;
            this.idProofTypeName = idProofTypeName;
            this.idProofNumber = idProofNumber;
            this.idExpiryDate = idExpiryDate;
            this.dateOfBirth = dateOfBirth;
            this.countryCode = countryCode;
            this.countryName = countryName;
            this.regionCode = regionCode;
            this.regionName = regionName;
            this.city = city;
            this.address = address;
            this.issuingCountryCode = issuingCountryCode;
            this.issuingCountryName = issuingCountryName;
            this.idProofUrl = idProofUrl;
            this.status = status;
            this.creationDate = creationDate;
            this.createdBy = createdBy;
            this.modificationDate = modificationDate;
            this.modifiedBy = modifiedBy;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIdProofTypeCode() {
            return idProofTypeCode;
        }

        public void setIdProofTypeCode(String idProofTypeCode) {
            this.idProofTypeCode = idProofTypeCode;
        }

        public String getIdProofTypeName() {
            return idProofTypeName;
        }

        public void setIdProofTypeName(String idProofTypeName) {
            this.idProofTypeName = idProofTypeName;
        }

        public String getIdProofNumber() {
            return idProofNumber;
        }

        public void setIdProofNumber(String idProofNumber) {
            this.idProofNumber = idProofNumber;
        }

        public String getIdExpiryDate() {
            return idExpiryDate;
        }

        public void setIdExpiryDate(String idExpiryDate) {
            this.idExpiryDate = idExpiryDate;
        }

        public String getDateOfBirth() {

           /* try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = formatter.parse(dateOfBirth);
                String date = new SimpleDateFormat("MM-dd-yyyy").format(date1);
                return date;
            }catch (Exception e){
                return dateOfBirth;
            }*/
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIssuingCountryCode() {
            return issuingCountryCode;
        }

        public void setIssuingCountryCode(String issuingCountryCode) {
            this.issuingCountryCode = issuingCountryCode;
        }

        public String getIssuingCountryName() {
            return issuingCountryName;
        }

        public void setIssuingCountryName(String issuingCountryName) {
            this.issuingCountryName = issuingCountryName;
        }

        public String getIdProofUrl() {
            return idProofUrl;
        }

        public void setIdProofUrl(String idProofUrl) {
            this.idProofUrl = idProofUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getModificationDate() {
            return modificationDate;
        }

        public void setModificationDate(String modificationDate) {
            this.modificationDate = modificationDate;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        }

    }