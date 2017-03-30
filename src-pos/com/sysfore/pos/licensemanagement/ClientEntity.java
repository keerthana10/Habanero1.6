package com.sysfore.pos.licensemanagement;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author 	S.A. Mateen
 * @company     Sysfore Technologies
 * @on 		Nov 27, 2012 1:11:33 PM
 * @email 	mateen.sa@sysfore.com
 */
@XmlRootElement
public class ClientEntity {
	
	private String id;
	private String machineAddress;
	private String licesneKey;
	private String product;
	private String clientsysteminfo;
	private List<WSTrackErrorTypes> errors;
	private long fromDate;
	private long toDate;
	private BigDecimal noofuser;
	private String usageType;
	private String callType;
	private String strPurposeValue;
	private String stremail;
	private String noofOrg;
	private String contextName;
	private String dbName;
	private String duration;
	private String activated;
	private String status;
	private String activateFrom;
        private String activateTo;
        private String info9;
        private String info10;
	public ClientEntity() {

	}

//	public ClientEntity(String id, String machineAddress, String licesneKey) {
//		this.id = id;
//		this.machineAddress = machineAddress;
//		this.licesneKey = licesneKey;
//		toDate = fromDate = System.currentTimeMillis();
//	}
//
//	public ClientEntity(String id, String machineAddress, String licenseKey, List<WSTrackErrorTypes> errors){
//
//		this(id, machineAddress, licenseKey);
//		this.errors = errors;
//
//	}
//
//	public ClientEntity(String id, String machineAddress, String licesneKey, String product, String clientsysteminfo) {
//		this(id, machineAddress, licesneKey);
//		this.product = product;
//		this.clientsysteminfo = clientsysteminfo;
//	}
//
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
	
	public String getProduct() {
		return product;
	}
	
	public void setProduct(String product) {
		this.product = product;
	}
	
	public List<WSTrackErrorTypes> getErrors() {
		return errors;
	}
	
	public void setErrors(List<WSTrackErrorTypes>  errors) {
		this.errors = errors;
	}

	public void setClientsysteminfo(String clientsysteminfo) {
		this.clientsysteminfo = clientsysteminfo;
	}
	
	public String getClientsysteminfo() {
		return clientsysteminfo;
	}

	public long getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}
	
	public long getToDate() {
		return toDate;
	}
	
	public void setToDate(long toDate) {
		this.toDate = toDate;
	}
	public BigDecimal getNoofuser() {
		return noofuser;
	}

	public void setNoofuser(BigDecimal noofuser) {
		this.noofuser = noofuser;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}
	
	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}
	

	public String getEmail() {
		return stremail;
	}

	public void setEmail(String stremail) {
		this.stremail = stremail;
	}

	public String getNoofOrg() {
		return noofOrg;
	}

	public void setNoofOrg(String noofOrg) {
		this.noofOrg = noofOrg;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
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
	

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

  public String getActivateFrom() {
        Date Currentdate = null;
               if(getStrPurposeValue()!="E"){

                   activateFrom = new Timestamp(getFromDate()).toString();
           }
		return activateFrom;
	}

	public void setActivateFrom(String activateFrom) {
		this.activateFrom = activateFrom;
	}

	public String getActivateTo() {
               Date Currentdate = null;
                String activationToDate;
               int dayvalue =0;
               if(getStrPurposeValue().equals("E")){
                   Calendar now = Calendar.getInstance();
                   String days = getDuration();
                   try{
                     dayvalue = Integer.parseInt(days);
                       now.add(Calendar.DATE,dayvalue);
                       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                   activateTo = (now.get(Calendar.YEAR) +"-"+  (now.get(Calendar.MONTH) + 1) +"-"+ (now.get(Calendar.DATE)));
                   }catch(Exception ex){

                   }
                  
                 
           }else{
                   activateTo = new Timestamp(getToDate()).toString();
           }


	
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
	public String toString() {
		return "ClientEntity [id=" + id + ", machineAddress=" + machineAddress
				+ ", licesneKey=" + licesneKey + ", product=" + product
				+ ", clientsysteminfo=" + clientsysteminfo 
				+ ", noofuser=" + noofuser + ", usageType=" + usageType	
				+ ", callType=" + callType 
				+ ", stremail=" + stremail
				+ ", noofOrg=" + noofOrg 
				+ ", contextName=" + contextName 
				+ ", dbName=" + dbName 
				+ ", strPurposeValue=" + strPurposeValue  
				+ ", activated=" + activated  
				+ ", duration=" + duration  
				+ ", status=" + status  
				+ ", errors="
				+ errors + "]";
	}
	
}