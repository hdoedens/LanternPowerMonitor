package com.lanternsoftware.datamodel.currentmonitor.hub;

import com.lanternsoftware.util.DateUtils;
import com.lanternsoftware.util.dao.annotations.DBSerializable;

import java.util.Date;
import java.util.List;

@DBSerializable(autogen = false)
public class HubSample {
	private Date sampleDate;
	private List<BreakerSample> breakers;

	public String getId() {
		return String.format("%d-%d", DateUtils.toLong(sampleDate));
	}

	public Date getSampleDate() {
		return sampleDate;
	}

	public void setSampleDate(Date _sampleDate) {
		sampleDate = _sampleDate;
	}

	public List<BreakerSample> getBreakers() {
		return breakers;
	}

	public void setBreakers(List<BreakerSample> _breakers) {
		breakers = _breakers;
	}
}
