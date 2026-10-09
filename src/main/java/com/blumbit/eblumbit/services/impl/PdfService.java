package com.blumbit.eblumbit.services.impl;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.blumbit.eblumbit.services.spec.IPdfService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service 
public class PdfService implements IPdfService{
    
    private final TemplateEngine templateEngine;
    
    @Override
    public byte[] generatePdfReport(String templateName, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process(templateName, context);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }

}
