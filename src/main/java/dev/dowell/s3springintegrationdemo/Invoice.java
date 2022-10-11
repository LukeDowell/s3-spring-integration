package dev.dowell.s3springintegrationdemo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
  private String opco;

  private String ra;

  private String acctNumber;

  private String acctName;

  private String invDate;

  private String invNumber;

  private String currentBalance;

  private String status;
}
