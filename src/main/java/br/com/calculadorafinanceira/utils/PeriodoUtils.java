package br.com.calculadorafinanceira.utils;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PeriodoUtils {

  private static final int DOZE_MESES = 12;
  private static final String SUFIXO_ANO_SINGULAR = " Ano";
  private static final String SUFIXO_ANO_PLURAL = " Anos";
  private static final String SUFIXO_MES_SINGULAR = " Mês";
  private static final String SUFIXO_MES_PLURAL = " Meses";
  private static final String SEPARADOR = " e ";

  public static String gerarInformativoPeriodo(Long periodoInvestimento) {
    if (periodoInvestimento < 0) {
      throw new ServiceException("O período informado em meses deve ser maior que zero.");
    }
    return gerarInformativo(periodoInvestimento);
  }

  public static String gerarInformativoPeriodo(LocalDate dataInicio, LocalDate dataFim) {
    if (dataFim.isBefore(dataInicio)) {
      throw new ServiceException("A data de início do período deve ser inferior a data de término.");
    }

    Long periodoInvestimento = ChronoUnit.MONTHS.between(dataInicio, dataFim);

    return gerarInformativo(periodoInvestimento);
  }

  private static String gerarInformativo(Long periodoInvestimento) {
    String informativo;

    if (periodoInvestimento >= DOZE_MESES) {
      int valorAno = (int) (periodoInvestimento / DOZE_MESES);
      int valorMes = (int) (periodoInvestimento % DOZE_MESES);

      String sufixoAno = valorAno == 1 ? SUFIXO_ANO_SINGULAR : SUFIXO_ANO_PLURAL;
      String sufixoMes = valorMes == 1 ? SUFIXO_MES_SINGULAR : SUFIXO_MES_PLURAL;

      informativo = valorMes == 0
        ? valorAno + sufixoAno
        : valorAno + sufixoAno + SEPARADOR + valorMes + sufixoMes;
    }
    else {
      String sufixoMes = periodoInvestimento == 1 ? SUFIXO_MES_SINGULAR : SUFIXO_MES_PLURAL;
      informativo = periodoInvestimento + sufixoMes;
    }

    return informativo;
  }

}
