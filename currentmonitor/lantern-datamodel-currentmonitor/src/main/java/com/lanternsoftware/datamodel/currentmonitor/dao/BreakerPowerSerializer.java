package com.lanternsoftware.datamodel.currentmonitor.dao;

import com.lanternsoftware.datamodel.currentmonitor.BreakerPower;
import com.lanternsoftware.util.dao.AbstractDaoSerializer;
import com.lanternsoftware.util.dao.DaoEntity;
import com.lanternsoftware.util.dao.DaoProxyType;
import com.lanternsoftware.util.dao.DaoSerializer;

import java.util.Collections;
import java.util.List;

public class BreakerPowerSerializer extends AbstractDaoSerializer<BreakerPower> {
	@Override
	public Class<BreakerPower> getSupportedClass() {
		return BreakerPower.class;
	}

	@Override
	public List<DaoProxyType> getSupportedProxies() {
		return Collections.singletonList(DaoProxyType.MONGO);
	}

	@Override
	public DaoEntity toDaoEntity(BreakerPower _o) {
		DaoEntity d = new DaoEntity();
		d.put("_id", _o.getId());
		d.put("space", _o.getSpace());
		d.put("phase_id", _o.getPhaseId());
		d.put("key", _o.getKey());
		d.put("read_time", DaoSerializer.toLong(_o.getReadTime()));
		d.put("hub_version", _o.getHubVersion());
		d.put("power", _o.getPower());
		d.put("voltage", _o.getVoltage());
		return d;
	}

	@Override
	public BreakerPower fromDaoEntity(DaoEntity _d) {
		BreakerPower o = new BreakerPower();
		o.setSpace(DaoSerializer.getInteger(_d, "space"));
		o.setPhaseId(DaoSerializer.getInteger(_d, "phase_id"));
		o.setReadTime(DaoSerializer.getDate(_d, "read_time"));
		o.setHubVersion(DaoSerializer.getString(_d, "hub_version"));
		o.setPower(DaoSerializer.getDouble(_d, "power"));
		o.setVoltage(DaoSerializer.getDouble(_d, "voltage"));
		return o;
	}
}