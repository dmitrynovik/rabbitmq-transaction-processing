package common.data;

import java.io.Serializable;

public record AtmTransaction (
    
     String processId,

     String termFiId,

     String termId1,

     String termId2,

     String cardFiId,

     String cardLn,

     String card1,

     String card2,

     String transCode,

     String messageType,

     String respCode,

     String fromAcct1,

     String fromAcct2,

     String seqNumber1,

     String seqNumber2,

     int amount1,

     int amount2,

     String postDate,

     String tranDate,

     int tranTime,

     String termCountry,

     int authCurrencyCode,

     String acqInstitutionId,

     String processFlag,

     String txnType,

     String threadNum) implements Serializable { }
