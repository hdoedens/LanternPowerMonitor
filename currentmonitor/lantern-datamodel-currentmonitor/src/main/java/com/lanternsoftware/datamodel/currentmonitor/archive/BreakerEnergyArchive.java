package com.lanternsoftware.datamodel.currentmonitor.archive;

import com.lanternsoftware.util.dao.annotations.DBSerializable;

@DBSerializable
public class BreakerEnergyArchive {
	private int panel;
	private int group;
	private byte[] readings;

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

	public byte[] getReadings() {
		return readings;
	}

	public void setReadings(byte[] _readings) {
		readings = _readings;
	}
}
