package uoc.tfg.raulberme.pruchase.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import uoc.tfg.raulberme.pruchase.entity.AdStatusType;

@ApiModel(value = "RatioDTO", description = "Complete data of a Rest Ratio")
@Data
@Builder
public class PurchaseRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The ad's id")
	@JsonProperty
	private Long id;

	@ApiModelProperty(value = "The ad's amount")
	@JsonProperty
	private Float amount;

	@ApiModelProperty(value = "The ad's minimum expected amount", required = false)
	@JsonProperty
	private Float minimumExpectedAmount;

	@ApiModelProperty(value = "The ad's location")
	@JsonProperty
	private String location;

	@ApiModelProperty(value = "The ad's offeredCurrency")
	@JsonProperty
	private String offeredCurrency;

	@ApiModelProperty(value = "The ad's demandedCurrency")
	@JsonProperty
	private String demandedCurrency;

	@ApiModelProperty(value = "The ad's seller")
	@JsonProperty
	private String seller;

	@ApiModelProperty(value = "The ad's buyer", required = false)
	@JsonProperty
	private String buyer;

	@ApiModelProperty(value = "The ad's status")
	@JsonProperty
	private AdStatusType status;

	@ApiModelProperty(value = "The ad's date")
	@JsonProperty
	private LocalDateTime date;
}
