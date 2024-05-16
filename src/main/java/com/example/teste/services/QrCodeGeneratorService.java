
package com.example.teste.services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.mock.web.MockMultipartFile;



@Service
public class QrCodeGeneratorService {

     @Autowired
     DiscoService disco;

   

    public MultipartFile generateQrCodeImage(String content, int width, int height) throws IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hintsMap = new HashMap<>();
        hintsMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hintsMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code image.", e);
        }
         
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage qrImage = toBufferedImage(bitMatrix);
        try {
            ImageIO.write(qrImage, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write QR code image to output stream.", e);
        }

        byte[] imageBytes = outputStream.toByteArray();

      

       
        String fileName = generateRandomFileName("qr_code_", ".png");
        return new MockMultipartFile(fileName, fileName, MediaType.IMAGE_PNG_VALUE, imageBytes);
    }
     


     
    private String generateRandomFileName(String prefix, String suffix) {
        UUID uniqueId = UUID.randomUUID();
        return prefix + uniqueId.toString() + suffix;
    }

    private BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (matrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }
        return image;
    }



    
}