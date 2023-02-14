package me.numin.smpcore.reporting;

public enum ReportType {
    PLAYER, BUG, MISC;

    public static ReportType getReportTypeFromString(String type) {
        for (ReportType reportType : ReportType.values()) {
            if (reportType.toString().equalsIgnoreCase(type))
                return reportType;
        }
        return null;
    }
}
