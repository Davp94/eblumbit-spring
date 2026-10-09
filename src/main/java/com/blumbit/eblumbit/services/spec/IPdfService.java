package com.blumbit.eblumbit.services.spec;

import java.util.Map;

public interface IPdfService {

    byte[] generatePdfReport(String templateName, Map<String, Object> data);
}
