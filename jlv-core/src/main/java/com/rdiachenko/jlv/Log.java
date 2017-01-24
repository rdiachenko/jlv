package com.rdiachenko.jlv;

import java.util.Objects;

public final class Log {
  
  private String date = "";
  private String level = "";
  private String threadName = "";
  private String loggerName = "";
  private String message = "";
  private String className = "";
  private String methodName = "";
  private String lineNumber = "";
  private String fileName = "";
  private String marker = "";
  private String throwable = "";
  private String ndc = "";
  private String mdc = "";
  
  private Log() {
    // static factory method is used instead
  }
  
  public static Builder newBuilder() {
    return new Log().new Builder();
  }
  
  public String getDate() {
    return date;
  }
  
  public String getLevel() {
    return level;
  }
  
  public String getThreadName() {
    return threadName;
  }
  
  public String getLoggerName() {
    return loggerName;
  }
  
  public String getMessage() {
    return message;
  }
  
  public String getClassName() {
    return className;
  }
  
  public String getMethodName() {
    return methodName;
  }
  
  public String getLineNumber() {
    return lineNumber;
  }
  
  public String getFileName() {
    return fileName;
  }
  
  public String getMarker() {
    return marker;
  }
  
  public String getThrowable() {
    return throwable;
  }
  
  public String getNdc() {
    return ndc;
  }
  
  public String getMdc() {
    return mdc;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(date, level, threadName, loggerName, message, className, methodName, lineNumber, fileName,
        marker, throwable, ndc, mdc);
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    Log log = (Log) other;
    return Objects.equals(date, log.date)
        && Objects.equals(level, log.level)
        && Objects.equals(threadName, log.threadName)
        && Objects.equals(loggerName, log.loggerName)
        && Objects.equals(message, log.message)
        && Objects.equals(className, log.className)
        && Objects.equals(methodName, log.methodName)
        && Objects.equals(lineNumber, log.lineNumber)
        && Objects.equals(fileName, log.fileName)
        && Objects.equals(marker, log.marker)
        && Objects.equals(throwable, log.throwable)
        && Objects.equals(ndc, log.ndc)
        && Objects.equals(mdc, log.mdc);
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(Log.class.getName());
    builder.append("[date=").append(date)
        .append(", level=").append(level)
        .append(", threadName=").append(threadName)
        .append(", loggerName=").append(loggerName)
        .append(", message=").append(message)
        .append(", className=").append(className)
        .append(", methodName=").append(methodName)
        .append(", lineNumber=").append(lineNumber)
        .append(", fileName=").append(fileName)
        .append(", marker=").append(marker)
        .append(", throwable=").append(throwable)
        .append(", ndc=").append(ndc)
        .append(", mdc=").append(mdc).append("]");
    return builder.toString();
  }
  
  public final class Builder {
    
    public Builder date(String val) {
      date = val;
      return this;
    }
    
    public Builder level(String val) {
      level = val;
      return this;
    }
    
    public Builder threadName(String val) {
      threadName = val;
      return this;
    }
    
    public Builder loggerName(String val) {
      loggerName = val;
      return this;
    }
    
    public Builder message(String val) {
      message = val;
      return this;
    }
    
    public Builder className(String val) {
      className = val;
      return this;
    }
    
    public Builder methodName(String val) {
      methodName = val;
      return this;
    }
    
    public Builder lineNumber(String val) {
      lineNumber = val;
      return this;
    }
    
    public Builder fileName(String val) {
      fileName = val;
      return this;
    }
    
    public Builder marker(String val) {
      marker = val;
      return this;
    }
    
    public Builder throwable(String val) {
      throwable = val;
      return this;
    }
    
    public Builder ndc(String val) {
      ndc = val;
      return this;
    }
    
    public Builder mdc(String val) {
      mdc = val;
      return this;
    }
    
    public Log build() {
      return Log.this;
    }
  }
}
