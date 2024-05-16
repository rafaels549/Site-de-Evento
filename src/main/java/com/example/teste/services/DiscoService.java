 package com.example.teste.services;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.InputStreamResource;

@Service
public class DiscoService {



	
	
	@Value("${contato.disco.raiz}")
    private String raiz;

    @Value("${contato.disco.diretorio-fotos}")
    private String diretorioFotos;

    @Value("${contato.disco.diretorio-qr}")
    private String diretorioQrcode;
      public void salvarQr(MultipartFile foto){
        this.salvar(this.diretorioQrcode, foto);      
      }
    public void salvarFoto(MultipartFile foto) {
        this.salvar(this.diretorioFotos, foto);
    }

    public void salvar(String diretorio, MultipartFile arquivo) {
        Path diretorioPath = Paths.get(this.raiz,  diretorio);
        Path arquivoPath = diretorioPath.resolve(arquivo.getOriginalFilename());
               
        try {
            Files.createDirectories(diretorioPath);
            arquivo.transferTo(arquivoPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Problemas na tentativa de salvar arquivo." + e.getMessage());
        }
    }


    public Resource carregarImagem(String nomeImagem) {
        Path imagePath = Paths.get("C:/Users/Pichau/Downloads/Estudo-JavaSpring-master/static/qrcode" + nomeImagem);
        Resource imageResource;
        try {
            imageResource = new InputStreamResource(Files.newInputStream(imagePath));
            
            return imageResource;
        } catch (IOException e) {
           
             return null;
        }



    }
}

