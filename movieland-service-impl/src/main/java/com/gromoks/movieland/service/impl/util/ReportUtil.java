package com.gromoks.movieland.service.impl.util;

import com.gromoks.movieland.service.entity.ReportRequest;

public class ReportUtil {
    public static String getReportNameFromRequest(ReportRequest reportRequest) {
        return reportRequest.getRequestUuid() + "." + reportRequest.getReportOutputType().toString();
    }

    public static String createReportEmailMessage(ReportRequest reportRequest) {
        String reportLink = reportRequest.getRequestUrl() + "/" + getReportNameFromRequest(reportRequest);
        StringBuilder stringBuilder = new StringBuilder(
                "Dear " + reportRequest.getRequestedUser().getNickname() + "," +
                        "\n\nPlease find link by requested report:" +
                        "\n" + reportLink);
        String text = stringBuilder.toString();
        return text;
    }
}
