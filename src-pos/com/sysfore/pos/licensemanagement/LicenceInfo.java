package com.sysfore.pos.licensemanagement;


import java.util.List;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.io.Serializable;
public class LicenceInfo implements SerializableRead, SerializableWrite , Serializable{

    private String id;
    private String machineAddress;
    private String licesneKey;
    private String licenseTypeId;
    private String callType;
    private String strPurposeValue;
    private String email;
    private String activated;
    private String status;
    private String activateFrom;
    private String activateTo;
    private String info9;
    private String info10;
    private List<WSTrackErrorTypes>  errors;
	public LicenceInfo() {

	}
   public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        machineAddress = dr.getString(2);
        licesneKey = dr.getString(3);
        licenseTypeId = dr.getString(4);
        callType = dr.getString(5);
        strPurposeValue = dr.getString(6);
        email = dr.getString(7);
        activateFrom = dr.getString(8);
        activateTo = dr.getString(9);
        activated = dr.getString(10);
        info10= dr.getString(11);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, machineAddress);
        dp.setString(3, licesneKey);
        dp.setString(4, licenseTypeId);
        dp.setString(5, callType);
        dp.setString(6, strPurposeValue);
        dp.setString(7, email);
        dp.setString(8, activateFrom);
        dp.setString(9, activateTo);
        dp.setString(10, activated);
         dp.setString(11, info10);



    }
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMachineAddress() {
		return machineAddress;
	}

	public void setMachineAddress(String machineAddress) {
		this.machineAddress = machineAddress;
	}

	public String getLicesneKey() {
		return licesneKey;
	}

	public void setLicesneKey(String licesneKey) {
		this.licesneKey = licesneKey;
	}

	
	public List<WSTrackErrorTypes> getErrors() {
		return errors;
	}

	public void setErrors(List<WSTrackErrorTypes>  errors) {
		this.errors = errors;
	}

	public String getLicenseType() {
		return licenseTypeId;
	}

	public void setLicenseType(String licenseTypeId) {
		this.licenseTypeId = licenseTypeId;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	public String getStrPurposeValue() {
		return strPurposeValue;
	}

	public void setStrPurposeValue(String strPurposeValue) {
		this.strPurposeValue = strPurposeValue;
	}


	public String getActivated() {
		return activated;
	}

	public void setActivated(String activated) {
		this.activated = activated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
        public String getActivateFrom() {
		return activateFrom;
	}

	public void setActivateFrom(String activateFrom) {
		this.activateFrom = activateFrom;
	}

	public String getActivateTo() {
        
		return activateTo;
	}

	public void setActivateTo(String activateTo) {
		this.activateTo = activateTo;
	}
        public String getInfo9() {
		return info9;
	}

	public void setInfo9(String info9) {
		this.info9 = info9;
	}
        public String getInfo10() {
		return info10;
	}

	public void setInfo10(String info10) {
		this.info10 = info10;
	}


}
