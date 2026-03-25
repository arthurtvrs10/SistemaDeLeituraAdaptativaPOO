package com.leituraadapt.service;

import com.leituraadapt.model.AccessibilityProfile;
import com.leituraadapt.model.Document;

import java.util.List;
import java.util.Objects;

public class ReadingService {

    private final TextWrapper textWrapper = new TextWrapper();

    public ReadingSession startSession(Document document, AccessibilityProfile profile) {

        Objects.requireNonNull(document, "document nao pode ser nulo");
        Objects.requireNonNull(profile, "nao pode ser nulo");

        int columnWidth = profile.getSettings().getColumnWidth();

        List<String> lines = textWrapper.wrap(
                document.getContent(),
                columnWidth
        );

        int pageSize = calculatePageSize(profile);

        return new ReadingSession(
                document,
                profile,
                lines,
                pageSize,
                0
        );
    }

    private int calculatePageSize(AccessibilityProfile profile) {
        double lineHeight = profile.getSettings().getLineHeight();

        if(lineHeight >= 1.8){
            return 4;
        } else if(lineHeight >= 1.5) {
            return 5;
        } else {
            return 6;
        }
    }
}
