package uoc.tfg.raulberme.pruchase.entity;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "sale")
public class Ad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Float offeredAmount;

	@Column(nullable = false)
	private String offeredCurrency;

	@Column
	private Float demandedAmount;

	@Column(nullable = false)
	private String demandedCurrency;

	@Column(nullable = false)
	private String seller;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private AdStatusType status;

	@Column(nullable = false)
	private LocalDateTime date;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ad")
	private Sale sale;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ad")
	private Collection<PurchaseRequest> purchaseRequests;

	@Builder
	public Ad(final Float offeredAmount, final String offeredCurrency, final Float demandedAmount,
			final String demandedCurrency, final String seller, final String location) {
		this.offeredAmount = offeredAmount;
		this.offeredCurrency = offeredCurrency;
		this.demandedAmount = demandedAmount;
		this.demandedCurrency = demandedCurrency;
		this.seller = seller;
		this.location = location;
		this.status = AdStatusType.ACTIVATED;
		this.date = LocalDateTime.now();
	}

}
