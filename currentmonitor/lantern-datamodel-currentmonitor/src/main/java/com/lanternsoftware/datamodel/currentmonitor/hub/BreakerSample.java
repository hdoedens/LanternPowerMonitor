package com.lanternsoftware.datamodel.currentmonitor.hub;

import com.lanternsoftware.datamodel.currentmonitor.Breaker;
import com.lanternsoftware.util.dao.annotations.DBSerializable;

import java.util.List;

@DBSerializable
public class BreakerSample {
	private int panel;
	private int group;
	private List<PowerSample> samples;

	public int key() {
		return Breaker.intKey(panel, group);
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

	public List<PowerSample> getSamples() {
		return samples;
	}

	public void setSamples(List<PowerSample> _samples) {
		samples = _samples;
	}
}
