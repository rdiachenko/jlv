package com.github.rd.jlv.log4j.domain;

/**
 * The class represents a log entity.It has a builder for creating a log's object. After a build completes the object
 * becomes immutable.
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public final class Log {

	private final String categoryName; // %c
	private final String className; // %C
	private final String date; // %d
	private final String fileName; // %F
	private final String locationInfo; // %l
	private final String lineNumber; // %L
	private final String methodName; // %M
	private final String level; // %p
	private final String ms; // %r
	private final String threadName; // %t
	private final String message; // %m
	private final String throwable; // %throwable
	private final String ndc; // %x
	private final String mdc; // %X{key}

	private Log(Builder builder) {
		categoryName = builder.categoryName;
		className = builder.className;
		date = builder.date;
		fileName = builder.fileName;
		locationInfo = builder.locationInfo;
		lineNumber = builder.lineNumber;
		methodName = builder.methodName;
		level = builder.level;
		ms = builder.ms;
		threadName = builder.threadName;
		message = builder.message;
		throwable = builder.throwable;
		ndc = builder.ndc;
		mdc = builder.mdc;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getClassName() {
		return className;
	}

	public String getDate() {
		return date;
	}

	public String getFileName() {
		return fileName;
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getLevel() {
		return level;
	}

	public String getMs() {
		return ms;
	}

	public String getThreadName() {
		return threadName;
	}

	public String getMessage() {
		return message;
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

	public static Builder newBuilder() {
		return new Builder();
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + categoryName.hashCode();
		result = 31 * result + className.hashCode();
		result = 31 * result + date.hashCode();
		result = 31 * result + fileName.hashCode();
		result = 31 * result + locationInfo.hashCode();
		result = 31 * result + lineNumber.hashCode();
		result = 31 * result + methodName.hashCode();
		result = 31 * result + level.hashCode();
		result = 31 * result + ms.hashCode();
		result = 31 * result + threadName.hashCode();
		result = 31 * result + message.hashCode();
		result = 31 * result + throwable.hashCode();
		result = 31 * result + ndc.hashCode();
		result = 31 * result + mdc.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (!(obj instanceof Log)) {
			return false;
		} else {
			Log log = (Log) obj;

			if (!categoryName.equals(log.getCategoryName())
					|| !className.equals(log.getClassName())
					|| !date.equals(log.getDate())
					|| !fileName.equals(log.getFileName())
					|| !locationInfo.equals(log.getLocationInfo())
					|| !lineNumber.equals(log.getLineNumber())
					|| !methodName.equals(log.getMethodName())
					|| !level.equals(log.getLevel())
					|| !ms.equals(log.getMs())
					|| !threadName.equals(log.getThreadName())
					|| !message.equals(log.getMessage())
					|| !throwable.equals(log.getThrowable())
					|| !ndc.equals(log.getNdc())
					|| !mdc.equals(log.getMdc())) {

				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "[categoryName=" + categoryName
				+ "; className=" + className
				+ "; date=" + date
				+ "; fileName=" + fileName
				+ "; locationInfo=" + locationInfo
				+ "; lineNumber=" + lineNumber
				+ "; methodName=" + methodName
				+ "; level=" + level
				+ "; ms=" + ms
				+ "; threadName=" + threadName
				+ "; message=" + message
				+ "; throwable=" + throwable
				+ "; ndc=" + ndc
				+ "; mdc=" + mdc
				+ "]";
	}

	public static class Builder {

		private String categoryName = "";
		private String className = "";
		private String date = "";
		private String fileName = "";
		private String locationInfo = "";
		private String lineNumber = "";
		private String methodName = "";
		private String level = "";
		private String ms = "";
		private String threadName = "";
		private String message = "";
		private String throwable = "";
		private String ndc = "";
		private String mdc = "";

		public Builder categoryName(String name) {
			categoryName = name;
			return this;
		}

		public Builder className(String name) {
			className = name;
			return this;
		}

		public Builder date(String date) {
			this.date = date;
			return this;
		}

		public Builder fileName(String name) {
			fileName = name;
			return this;
		}

		public Builder locationInfo(String info) {
			locationInfo = info;
			return this;
		}

		public Builder lineNumber(String line) {
			lineNumber = line;
			return this;
		}

		public Builder methodName(String name) {
			methodName = name;
			return this;
		}

		public Builder level(String level) {
			this.level = level;
			return this;
		}

		public Builder ms(String ms) {
			this.ms = ms;
			return this;
		}

		public Builder threadName(String name) {
			threadName = name;
			return this;
		}

		public Builder message(String msg) {
			message = msg;
			return this;
		}

		public Builder throwable(String throwable) {
			this.throwable = throwable;
			return this;
		}

		public Builder ndc(String ndc) {
			this.ndc = ndc;
			return this;
		}

		public Builder mdc(String mdc) {
			this.mdc = mdc;
			return this;
		}

		public Log build() {
			return new Log(this);
		}
	}
}
