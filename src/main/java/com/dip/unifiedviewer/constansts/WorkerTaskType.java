
package com.dip.unifiedviewer.constansts;

public enum WorkerTaskType {
	DEFAULT(0, "DEFAULT"),
	CDR(1, "cdr"),
	ESAF(2,"esaf"),
	SMS(3,"sms"),
	LRL(4,"lrl"),
	NID(5,"nid"),
	PASSPORT(6,"passport"),
	DRIVINGLICENSE(7,"driving-license"),
	VEHICLEREGISTRATION(8,"vehicle-registration"),
	BIRTHREGISTRATION(9,"birth-registration"),
	NBR(10,"nbr"),
	LAND(11,"land"),
	LOCAL_ESAF(12, "local-esaf"),
    NAID(13, "naid"),
    FNF(14, "fnf"),
    VAS(15, "vas"),
    SUBSCRIBERBALANCE(16, "subscriber-balance"),
    LAND_BY_CITIZEN(17, "land-by-citizen"),
    PSTN_CDR(18, "pstn-cdr"),
    PSTN_ESAF(19, "pstn-esaf"),
    NEIR(20, "neir"),
    LCL(20,"lcl"),
    PIDS_SEARCH(21, "pids-search"),
    INMATE_PROFILE_DETAILS(22, "inmate-profile-details"),
    OTT_ESAF(24, "ott-esaf"),
    OTT_SMS(25, "ott-sms"),
    OTT_CDR(26, "ott-call"),
    ISP_INTERCEPTION(27, "isp-interception"),
    FINANCE_PERSONAL_DETAILS(28, "finance-division");


    private int value;
    private String index;

    private WorkerTaskType(int value, String index) {
        this.value = value;
        this.index = index;
    }

    public static WorkerTaskType getWorkerType(String requestData) {
        return WorkerTaskType.valueOf(requestData);
    }

    public int getValue() {
        return value;
    }

    public String getIndex() {
        return index;
    }
}