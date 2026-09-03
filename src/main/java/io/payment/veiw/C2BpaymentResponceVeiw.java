package io.payment.veiw;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class C2BpaymentResponceVeiw {

    @JsonProperty("ResultCode") 
    private String resultCode;

    @JsonProperty("ResultDesc") 
    private String resultDescription;
}