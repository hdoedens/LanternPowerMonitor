package com.lanternsoftware.datamodel.currentmonitor;

import java.util.Date;

import com.lanternsoftware.util.dao.annotations.DBSerializable;

@DBSerializable(autogen = false)
public class BreakerPower {
	private int panel;
	private int group;
	private Date readTime;
	private String hubVersion;
	private double power;
	private double voltage;

	public BreakerPower() {
	}

	public BreakerPower(int _panel, int _group, Date _readTime, double _power, double _voltage) {
		panel = _panel;
		group = _group;
		readTime = _readTime;
		power = _power;
		voltage = _voltage;
	}

	public String getId() {
		return String.format("%d-%d", panel, group);
	}

	public int getPanel() {
		return panel;
	}

	public void setPanel(int _panel) {
		panel = _panel;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int _group) {
		group = _group;
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
		return NumberUtils.round(power, 2);
	}

	public void setPower(double _power) {
		power = _power;
	}

	public double getVoltage() {
		return NumberUtils.round(voltage, 2);
	}

	public void setVoltage(double _voltage) {
		voltage = _voltage;
	}

	public String getKey() {
		return Breaker.key(panel, group);
	}
}
