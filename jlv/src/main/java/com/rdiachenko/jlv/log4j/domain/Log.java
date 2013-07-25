package com.rdiachenko.jlv.log4j.domain;

public final class Log {

	private final String categoryName; 	// %c
	private final String className; 	// %C
	private final String date; 			// %d
	private final String fileName; 		// %F
	private final String locationInfo; 	// %l
	private final String lineNumber; 	// %L
	private final String methodName; 	// %M
	private final String level; 		// %p
	private final String ms; 			// %r
	private final String threadName; 	// %t
	private final String message; 		// %m
	private final String throwable; 	// %throwable

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
				+ "]";
	}

	public static class Builder {

		private String categoryName = ""; 	// %c
		private String className = ""; 		// %C
		private String date = ""; 			// %d
		private String fileName = ""; 		// %F
		private String locationInfo = ""; 	// %l
		private String lineNumber = ""; 	// %L
		private String methodName = ""; 	// %M
		private String level = ""; 			// %p
		private String ms = ""; 			// %r
		private String threadName = ""; 	// %t
		private String message = ""; 		// %m
		private String throwable = ""; 		// %throwable

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

		public Log build() {
			return new Log(this);
		}
	}
}
