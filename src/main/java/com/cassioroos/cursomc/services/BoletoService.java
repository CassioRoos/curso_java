package com.cassioroos.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.cassioroos.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherPagamentoComBoleto(PagamentoComBoleto boleto, Date instant) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instant);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		boleto.setDataVencimento(cal.getTime());
	}
}
