package common.data;

import java.io.Serializable;

public class AtmTransaction implements Serializable {
    
    public String processId;

    public String termFiId;

    public String termId1;

    public String termId2;

    public String cardFiId;

    public String cardLn;

    public String card1;

    public String card2;

    public String transCode;

    public String messageType;

    public String respCode;

    public String fromAcct1;

    public String fromAcct2;

    public String seqNumber1;

    public String seqNumber2;

    public int amount1;

    public int amount2;

    public String postDate;

    public String tranDate;

    public int tranTime;

    public String termCountry;

    public int authCurrencyCode;

    public String acqInstitutionId;

    public String processFlag;

    public String txnType;

    public String threadNum;
}
