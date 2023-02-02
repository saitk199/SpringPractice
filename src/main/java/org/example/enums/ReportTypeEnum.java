package org.example.enums;

import lombok.Getter;

@Getter
public enum ReportTypeEnum {
    PDF("pdf"),
    EXCEL("xlsx"),
    HTML("html"),
    DOCX("docx");

    private final String fileType;

    ReportTypeEnum(final String fileType) {
        this.fileType = fileType;
    }
}
