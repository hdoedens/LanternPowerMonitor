package com.lanternsoftware.datamodel.currentmonitor;

import java.util.List;

import com.lanternsoftware.util.IIdentical;
import com.lanternsoftware.util.dao.annotations.DBSerializable;
import com.lanternsoftware.util.dao.annotations.PrimaryKey;

@DBSerializable(autogen = false)
public class BreakerConfig implements IIdentical<BreakerConfig> {
	@PrimaryKey
	private List<BreakerPanel> panels;
	private BreakerHub breakerHub;
	private List<Breaker> breakers;
	private int version;

	public BreakerConfig() {
	}

	public List<Breaker> getBreakers() {
		return breakers;
	}

	public void setBreakers(List<Breaker> _breakers) {
		breakers = _breakers;
	}

	public List<BreakerPanel> getPanels() {
		return panels;
	}

	public void setPanels(List<BreakerPanel> _panels) {
		panels = _panels;
	}

	public BreakerHub getBreakerHub() {
		return breakerHub;
	}

	public void setBreakerHub(BreakerHub _breakerHub) {
		breakerHub = _breakerHub;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int _version) {
		version = _version;
	}

	public BreakerHub getHub() {
		return breakerHub;
	}

	@Override
	public boolean isIdentical(BreakerConfig _other) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'isIdentical'");
	}
}
