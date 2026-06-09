package com.leituraadapt.service;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
public class PdfRenderService {

    public byte[] renderPageAsPng(String filePath, int pageNumber, float dpi) {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("Arquivo PDF não encontrado: " + filePath);
        }

        try (PDDocument document = PDDocument.load(file);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            int totalPages = document.getNumberOfPages();

            if (pageNumber < 1 || pageNumber > totalPages) {
                throw new RuntimeException("Página inválida. Página solicitada: " + pageNumber);
            }

            PDFRenderer renderer = new PDFRenderer(document);

            BufferedImage image = renderer.renderImageWithDPI(
                    pageNumber - 1,
                    dpi,
                    ImageType.RGB
            );

            ImageIO.write(image, "png", outputStream);
            image.flush();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao renderizar página do PDF", e);
        }
    }

    public int getTotalPages(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("Arquivo PDF não encontrado: " + filePath);
        }

        try (PDDocument document = PDDocument.load(file)) {
            return document.getNumberOfPages();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter total de páginas do PDF", e);
        }
    }

    public String extractText(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("Arquivo PDF não encontrado: " + filePath);
        }

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair texto do PDF", e);
        }
    }

    public String extractTextFromPage(String filePath, int pageNumber) {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("Arquivo PDF não encontrado: " + filePath);
        }

        try (PDDocument document = PDDocument.load(file)) {
            int totalPages = document.getNumberOfPages();

            if (pageNumber < 1 || pageNumber > totalPages) {
                throw new RuntimeException("Página inválida. Página solicitada: " + pageNumber);
            }

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(pageNumber);
            stripper.setEndPage(pageNumber);

            return stripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair texto da página do PDF", e);
        }
    }
}