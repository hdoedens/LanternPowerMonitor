package com.lanternsoftware.datamodel.currentmonitor.dao;

import com.lanternsoftware.datamodel.currentmonitor.EnergyTotal;
import com.lanternsoftware.datamodel.currentmonitor.EnergyViewMode;
import com.lanternsoftware.util.dao.AbstractDaoSerializer;
import com.lanternsoftware.util.dao.DaoEntity;
import com.lanternsoftware.util.dao.DaoProxyType;
import com.lanternsoftware.util.dao.DaoSerializer;

import java.util.Collections;
import java.util.List;

public class EnergyTotalSerializer extends AbstractDaoSerializer<EnergyTotal> {
	@Override
	public Class<EnergyTotal> getSupportedClass() {
		return EnergyTotal.class;
	}

	@Override
	public List<DaoProxyType> getSupportedProxies() {
		return Collections.singletonList(DaoProxyType.MONGO);
	}

	@Override
	public DaoEntity toDaoEntity(EnergyTotal _o) {
		DaoEntity d = new DaoEntity();
		d.put("_id", _o.getId());
		d.put("group_id", _o.getGroupId());
		d.put("view_mode", DaoSerializer.toEnumName(_o.getViewMode()));
		d.put("start", DaoSerializer.toLong(_o.getStart()));
		d.put("sub_groups", DaoSerializer.toDaoEntities(_o.getSubGroups(), DaoProxyType.MONGO));
		d.put("joules", _o.getJoules());
		d.put("flow", _o.getFlow());
		d.put("peak_to_grid", _o.getPeakToGrid());
		d.put("peak_from_grid", _o.getPeakFromGrid());
		d.put("peak_production", _o.getPeakProduction());
		d.put("peak_consumption", _o.getPeakConsumption());
		return d;
	}

	@Override
	public EnergyTotal fromDaoEntity(DaoEntity _d) {
		EnergyTotal o = new EnergyTotal();
		o.setGroupId(DaoSerializer.getString(_d, "group_id"));
		o.setViewMode(DaoSerializer.getEnum(_d, "view_mode", EnergyViewMode.class));
		o.setStart(DaoSerializer.getDate(_d, "start"));
		o.setSubGroups(DaoSerializer.getList(_d, "sub_groups", EnergyTotal.class));
		o.setJoules(DaoSerializer.getDouble(_d, "joules"));
		o.setFlow(DaoSerializer.getDouble(_d, "flow"));
		o.setPeakToGrid(DaoSerializer.getDouble(_d, "peak_to_grid"));
		o.setPeakFromGrid(DaoSerializer.getDouble(_d, "peak_from_grid"));
		o.setPeakProduction(DaoSerializer.getDouble(_d, "peak_production"));
		o.setPeakConsumption(DaoSerializer.getDouble(_d, "peak_consumption"));
		return o;
	}
}