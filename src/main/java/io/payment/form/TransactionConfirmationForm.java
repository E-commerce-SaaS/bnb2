package io.payment.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionConfirmationForm {
    @JsonProperty("Initiator")
    private String initiator;

    @JsonProperty("SecurityCredential")
    private String securityCredential;

    @JsonProperty("CommandID")
    private String commandID = "TransactionStatusQuery";

    @JsonProperty("TransactionID")
    private String transactionID;

    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    @JsonProperty("PartyA")
    private String partyA;

    @JsonProperty("IdentifierType")
    private String identifierType = "4";

    @JsonProperty("ResultURL")
    private String resultURL;

    @JsonProperty("QueueTimeOutURL")
    private String queueTimeOutURL;

    @JsonProperty("Remarks")
    private String remarks = "Manual reconciliation pull";

    @JsonProperty("Occasion")
    private String occasion = "Missing Callback Sync";
}
