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
public class AdDTO implements Serializable, Comparable<AdDTO> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The ad's id")
	@JsonProperty
	private Long id;

	@ApiModelProperty(value = "The ad's offered amount")
	@JsonProperty
	private Float offeredAmount;

	@ApiModelProperty(value = "The ad's offered currency")
	@JsonProperty
	private String offeredCurrency;

	@ApiModelProperty(value = "The ad's demanded amount")
	@JsonProperty
	private Float demandedAmount;

	@ApiModelProperty(value = "The ad's demanded currency")
	@JsonProperty
	private String demandedCurrency;

	@ApiModelProperty(value = "The ad's seller")
	@JsonProperty
	private String seller;

	@ApiModelProperty(value = "The ad's location")
	@JsonProperty
	private String location;

	@ApiModelProperty(value = "The ad's buyer", required = false)
	@JsonProperty
	private String buyer;

	@ApiModelProperty(value = "The ad's status")
	@JsonProperty
	private AdStatusType status;

	@ApiModelProperty(value = "The ad's date")
	@JsonProperty
	private LocalDateTime date;

	@Override
	public int compareTo(AdDTO other) {
		return other.getDate().compareTo(this.getDate());
	}

}
