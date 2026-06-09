package com.backend.mvc.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import org.springframework.stereotype.Service;


/* 
    * Esta classe será responsável por lidar diretamente
    * com arquivos PDF, utilizando a biblioteca Apache PDFBox. 
*/

@Service
public class PdfService {

    // método para contar páginas
    public int getTotalPages(File file) throws IOException{

        //Abre o arquivo PDF
        try (PDDocument document = Loader.loadPDF(file)) { 

            //Descobre quantas páginas tem
            return document.getNumberOfPages();
        }
    }

    //
    public byte[] renderPageAsImage(File file, int pageNumber, float dpi) throws IOException{
        try(PDDocument document = Loader.loadPDF(file)) {

            int totalPages = document.getNumberOfPages();

            // Impede que o usuário peça página invalida
            if (pageNumber < 1 || pageNumber > totalPages){
                throw new IllegalArgumentException("Página Válida.");
            }

            // convertee a ágina da API para o índice interno do PDFbox que comeca em 0;
            int pageIndex = pageNumber - 1;

            // Cria o renderizador de páginas
            PDFRenderer renderer = new PDFRenderer(document);
            // Transforma a página em uma imagem na memória
            BufferedImage image = renderer.renderImageWithDPI(pageIndex, dpi);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // Convertee a imagem para o formato PNG
            ImageIO.write(image, "png", outputStream);

            // Retorna os bytes da images para o controller responder como image/png
            return outputStream.toByteArray();
        }
    }

}
