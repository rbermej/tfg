package uoc.tfg.raulberme.pruchase.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Float amount;

	@Column
	private Float minimumExpectedAmount;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private String offeredCurrency;

	@Column(nullable = false)
	private String demandedCurrency;

	@Column(nullable = false)
	private String seller;

	@Column(nullable = false)
	private AdStatusType status;

	@Column(nullable = false)
	private LocalDateTime date;

	@OneToOne
	@JoinColumn(nullable = true)
	private Sale sale;

	@Builder
	public Ad(final Float amount, final Float minimumExpectedAmount, final String location,
			final String offeredCurrency, final String demandedCurrency, final String seller) {
		this.amount = amount;
		this.minimumExpectedAmount = minimumExpectedAmount;
		this.location = location;
		this.offeredCurrency = offeredCurrency;
		this.demandedCurrency = demandedCurrency;
		this.seller = seller;
		this.status = AdStatusType.ACTIVATED;
		this.date = LocalDateTime.now();
	}

}
