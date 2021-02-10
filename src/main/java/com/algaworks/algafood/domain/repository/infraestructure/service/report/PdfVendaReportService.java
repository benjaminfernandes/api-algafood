package com.algaworks.algafood.domain.repository.infraestructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//Aula 13.20
@Component
public class PdfVendaReportService implements VendaReportService{

	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		try {
			
		var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");

		//Passa parametros para o PDF, pode ser usado para passar o que foi selecionado pelo user para filtro
		var parametros = new HashMap<String, Object>();
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
		
		var dataSource = new JRBeanCollectionDataSource(vendasDiarias);
		
		var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
		
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
		}
	}

}
