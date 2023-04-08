package com.lanternsoftware.datamodel.currentmonitor;

import com.lanternsoftware.util.dao.annotations.DBSerializable;

import java.util.Date;

@DBSerializable(autogen = false)
public class BreakerPower {
	private int space;
	private int phaseId;
	private Date readTime;
	private String hubVersion;
	private double power;
	private double voltage;

	public BreakerPower() {
	}

	public BreakerPower(int _phaseId, int _space, Date _readTime, double _power, double _voltage) {
		space = _space;
		phaseId = _phaseId;
		readTime = _readTime;
		power = _power;
		voltage = _voltage;
	}

	public String getId() {
		return String.format("%d-%d", space, phaseId);
	}

	public int getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(int _phaseId) {
		phaseId = _phaseId;
	}

	public int getSpace() {
		return space;
	}

	public void setSpace(int _space) {
		space = _space;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date _readTime) {
		readTime = _readTime;
	}

	public String getHubVersion() {
		return hubVersion;
	}

	public void setHubVersion(String _hubVersion) {
		hubVersion = _hubVersion;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double _power) {
		power = _power;
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double _voltage) {
		voltage = _voltage;
	}

	public String getKey() {
		return Breaker.key(space, phaseId);
	}
}
