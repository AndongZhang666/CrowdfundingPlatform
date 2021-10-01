package com.dong.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailReturnVO {

	private Integer returnId;

	private Integer supportMoney;

	private Integer signalPurchase;

	private Integer purchase;

	private Integer supproterCount;

	private Integer freight;

	private Integer returnDate;

	private String content;

}
