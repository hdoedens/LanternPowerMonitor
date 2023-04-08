package com.lanternsoftware.datamodel.currentmonitor.hub;

import com.lanternsoftware.datamodel.currentmonitor.Breaker;
import com.lanternsoftware.util.dao.annotations.DBSerializable;

import java.util.List;

@DBSerializable
public class BreakerSample {
	private int space;
	private int phaseId;
	private List<PowerSample> samples;

	public int key() {
		return Breaker.intKey(phaseId, space);
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

	public List<PowerSample> getSamples() {
		return samples;
	}

	public void setSamples(List<PowerSample> _samples) {
		samples = _samples;
	}
}
