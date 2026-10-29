package fr.alessdevita.valtherion.utils;

import fr.alessdevita.valtherion.crash.CrashReport;

public class ReportedException extends RuntimeException {
  private final CrashReport crashReport;

  public ReportedException(String message) {
    super(message);
    this.crashReport = null;
  }

  public ReportedException(CrashReport crashReport) {
    super(crashReport != null ? crashReport.toString() : "Reported exception");
    this.crashReport = crashReport;
  }

  public CrashReport getCrashReport() {
    return this.crashReport;
  }
}
