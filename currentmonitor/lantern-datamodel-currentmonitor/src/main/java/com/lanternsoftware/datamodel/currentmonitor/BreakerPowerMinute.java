package com.lanternsoftware.datamodel.currentmonitor;

import com.lanternsoftware.util.dao.annotations.DBSerializable;

import java.util.List;

@DBSerializable(autogen = false)
public class BreakerPowerMinute {
	private int panel;
	private int group;
	private List<Float> readings;

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

	public String breakerKey() {
		return Breaker.key(panel, group);
	}

	public int breakerIntKey() {
		return Breaker.intKey(panel, group);
	}

	public List<Float> getReadings() {
		return readings;
	}

	public void setReadings(List<Float> _readings) {
		readings = _readings;
	}
}
