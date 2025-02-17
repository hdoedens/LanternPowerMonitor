package com.lanternsoftware.datamodel.currentmonitor.hub.dao;

import com.lanternsoftware.datamodel.currentmonitor.hub.BreakerSample;
import com.lanternsoftware.datamodel.currentmonitor.hub.PowerSample;
import com.lanternsoftware.util.dao.AbstractDaoSerializer;
import com.lanternsoftware.util.dao.DaoEntity;
import com.lanternsoftware.util.dao.DaoProxyType;
import com.lanternsoftware.util.dao.DaoSerializer;
import java.util.Collections;
import java.util.List;

public class BreakerSampleSerializer extends AbstractDaoSerializer<BreakerSample> {
	@Override
	public Class<BreakerSample> getSupportedClass() {
		return BreakerSample.class;
	}

	@Override
	public List<DaoProxyType> getSupportedProxies() {
		return Collections.singletonList(DaoProxyType.MONGO);
	}

	@Override
	public DaoEntity toDaoEntity(BreakerSample _o) {
		DaoEntity d = new DaoEntity();
		d.put("space", _o.getSpace());
		d.put("phase_id", _o.getPhaseId());
		d.put("samples", DaoSerializer.toDaoEntities(_o.getSamples(), DaoProxyType.MONGO));
		return d;
	}

	@Override
	public BreakerSample fromDaoEntity(DaoEntity _d) {
		BreakerSample o = new BreakerSample();
		o.setSpace(DaoSerializer.getInteger(_d, "space"));
		o.setPhaseId(DaoSerializer.getInteger(_d, "phase_id"));
		o.setSamples(DaoSerializer.getList(_d, "samples", PowerSample.class));
		return o;
	}
}