package com.github.rd.jlv;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * The class represents a log entity.It has a builder for creating a log's object. After a build completes the object
 * becomes immutable.
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public final class Log {

	private final String categoryName;
	private final String className;
	private final String date;
	private final String fileName;
	private final String locationInfo;
	private final String lineNumber;
	private final String methodName;
	private final String level;
	private final String ms;
	private final String threadName;
	private final String message;
	private final String throwable;
	private final String ndc;
	private final String mdc;

	public static Builder newBuilder() {
		return new Builder();
	}

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

	@Override
	public int hashCode() {
		return Objects.hashCode(categoryName,
				className,
				date,
				fileName,
				locationInfo,
				lineNumber,
				methodName,
				level,
				ms,
				threadName,
				message,
				throwable,
				ndc,
				mdc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || obj.getClass() != getClass()) {
			return false;
		} else {
			Log that = (Log) obj;
			return Objects.equal(categoryName, that.getCategoryName())
					&& Objects.equal(className, that.getClassName())
					&& Objects.equal(date, that.getDate())
					&& Objects.equal(fileName, that.getFileName())
					&& Objects.equal(locationInfo, that.getLocationInfo())
					&& Objects.equal(lineNumber, that.getLineNumber())
					&& Objects.equal(methodName, that.getMethodName())
					&& Objects.equal(level, that.getLevel())
					&& Objects.equal(ms, that.getMs())
					&& Objects.equal(threadName, that.getThreadName())
					&& Objects.equal(message, that.getMessage())
					&& Objects.equal(throwable, that.getThrowable())
					&& Objects.equal(ndc, that.getNdc())
					&& Objects.equal(mdc, that.getMdc());
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("categoryName", categoryName)
				.add("className", className)
				.add("date", date)
				.add("fileName", fileName)
				.add("locationInfo", locationInfo)
				.add("lineNumber", lineNumber)
				.add("methodName", methodName)
				.add("level", level)
				.add("ms", ms)
				.add("threadName", threadName)
				.add("message", message)
				.add("throwable", throwable)
				.add("ndc", ndc)
				.add("mdc", mdc)
				.toString();
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
