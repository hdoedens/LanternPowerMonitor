package com.lanternsoftware.datamodel.currentmonitor.dao;

import java.util.Collections;
import java.util.List;

import com.lanternsoftware.datamodel.currentmonitor.Breaker;
import com.lanternsoftware.datamodel.currentmonitor.BreakerConfig;
import com.lanternsoftware.datamodel.currentmonitor.BreakerHub;
import com.lanternsoftware.datamodel.currentmonitor.BreakerPanel;
import com.lanternsoftware.util.dao.AbstractDaoSerializer;
import com.lanternsoftware.util.dao.DaoEntity;
import com.lanternsoftware.util.dao.DaoProxyType;
import com.lanternsoftware.util.dao.DaoSerializer;

public class BreakerConfigSerializer extends AbstractDaoSerializer<BreakerConfig> {
	@Override
	public Class<BreakerConfig> getSupportedClass() {
		return BreakerConfig.class;
	}

	@Override
	public List<DaoProxyType> getSupportedProxies() {
		return Collections.singletonList(DaoProxyType.MONGO);
	}

	@Override
	public DaoEntity toDaoEntity(BreakerConfig _o) {
		DaoEntity d = new DaoEntity();
		d.put("panels", DaoSerializer.toDaoEntities(_o.getPanels(), DaoProxyType.MONGO));
		d.put("hub", _o.getBreakerHub());
		d.put("breakers", DaoSerializer.toDaoEntities(_o.getBreakers(), DaoProxyType.MONGO));
		d.put("version", _o.getVersion());
		return d;
	}

	@Override
	public BreakerConfig fromDaoEntity(DaoEntity _d) {
		BreakerConfig o = new BreakerConfig();
		o.setPanels(DaoSerializer.getList(_d, "panels", BreakerPanel.class));
		o.setBreakerHub(DaoSerializer.getObject(_d, "hub", BreakerHub.class));
		o.setBreakers(DaoSerializer.getList(_d, "breakers", Breaker.class));
		o.setVersion(DaoSerializer.getInteger(_d, "version"));
		return o;
	}
}