package com.lanternsoftware.datamodel.currentmonitor.dao;

import java.util.Collections;
import java.util.List;

import com.lanternsoftware.datamodel.currentmonitor.Phase;
import com.lanternsoftware.util.dao.AbstractDaoSerializer;
import com.lanternsoftware.util.dao.DaoEntity;
import com.lanternsoftware.util.dao.DaoProxyType;
import com.lanternsoftware.util.dao.DaoSerializer;

public class PhaseSerializer extends AbstractDaoSerializer<Phase> {

    @Override
    public Class<Phase> getSupportedClass() {
        return Phase.class;
    }

    @Override
    public List<DaoProxyType> getSupportedProxies() {
        return Collections.singletonList(DaoProxyType.MONGO);
    }

    @Override
    public DaoEntity toDaoEntity(Phase _o) {
        DaoEntity d = new DaoEntity();
        d.put("id", _o.getId());
        d.put("name", _o.getName());
        d.put("phase_offset_ns", _o.getPhaseOffsetNs());
        return d;
    }

    @Override
    public Phase fromDaoEntity(DaoEntity _d) {
        Phase o = new Phase();
        o.setId(DaoSerializer.getInteger(_d, "id"));
        o.setName(DaoSerializer.getString(_d, "name"));
        o.setPhaseOffsetNs(DaoSerializer.getInteger(_d, "phase_offset_ns"));
        return o;
    }
}
