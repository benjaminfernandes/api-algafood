package com.algaworks.algafood.domain.repository.infraestructure.service.storage;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

//Aula 14.23
@Service
public class S3FotoStorageService implements FotoStorageService{

	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private StorageProperties storageProperties;
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		String caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());
		var objectMetaData = new ObjectMetadata();
		objectMetaData.setContentType(novaFoto.getContentType());
		objectMetaData.setContentLength(novaFoto.getTamanho());
		try {
			var putObjectRequest = new PutObjectRequest(
					storageProperties.getS3().getBucket(), 
					caminhoArquivo, 
					novaFoto.getInputStream(), 
					objectMetaData)
				.withCannedAcl(CannedAccessControlList.PublicRead);
			
			amazonS3.putObject(putObjectRequest);
		}catch (Exception e) {
			throw new StorageException("Não foi possível enviar o arquivo para a amazon S3", e);
		}
		
	}

	@Override
	public void remover(String nomeArquivo) {
		
	}

	@Override
	public InputStream recuperar(String nomeArquivo) {
		return null;
	}
	
	private String getCaminhoArquivo(String nomeArquivo) {
		return String.format("%s/%s", this.storageProperties.getS3().getDiretorioFotos(),
				nomeArquivo);
	}
	
}
