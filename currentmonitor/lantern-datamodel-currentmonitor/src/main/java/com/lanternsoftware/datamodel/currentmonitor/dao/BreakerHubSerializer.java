package com.lanternsoftware.datamodel.currentmonitor.dao;

import java.util.Collections;
import java.util.List;

import com.lanternsoftware.datamodel.currentmonitor.BreakerHub;
import com.lanternsoftware.datamodel.currentmonitor.Phase;
import com.lanternsoftware.util.dao.AbstractDaoSerializer;
import com.lanternsoftware.util.dao.DaoEntity;
import com.lanternsoftware.util.dao.DaoProxyType;
import com.lanternsoftware.util.dao.DaoSerializer;

public class BreakerHubSerializer extends AbstractDaoSerializer<BreakerHub> {
	@Override
	public Class<BreakerHub> getSupportedClass() {
		return BreakerHub.class;
	}

	@Override
	public List<DaoProxyType> getSupportedProxies() {
		return Collections.singletonList(DaoProxyType.MONGO);
	}

	@Override
	public DaoEntity toDaoEntity(BreakerHub _o) {
		DaoEntity d = new DaoEntity();
		d.put("voltage_calibration_factor", _o.getRawVoltageCalibrationFactor());
		d.put("port_calibration_factor", _o.getRawPortCalibrationFactor());
		d.put("phase_cnt", _o.getPhaseCnt());
		d.put("frequency", _o.getFrequency());
		d.put("needs_calibration", _o.getFrequency());
		d.put("mqtt_broker_url", _o.getFrequency());
		d.put("mqtt_user_name", _o.getFrequency());
		d.put("mqtt_password", _o.getFrequency());
		d.put("debug", _o.getFrequency());
		return d;
	}

	@Override
	public BreakerHub fromDaoEntity(DaoEntity _d) {
		BreakerHub o = new BreakerHub();
		o.setVoltageCalibrationFactor(DaoSerializer.getDouble(_d, "voltage_calibration_factor"));
		o.setPortCalibrationFactor(DaoSerializer.getDouble(_d, "port_calibration_factor"));
		o.setPhases(DaoSerializer.getList(_d, "phases", Phase.class));
		o.setFrequency(DaoSerializer.getInteger(_d, "frequency"));
		o.setNeedsCalibration(DaoSerializer.getBoolean(_d, "needs_calibration"));
		o.setMqttBrokerUrl(DaoSerializer.getString(_d, "mqtt_broker_url"));
		o.setMqttUsername(DaoSerializer.getString(_d, "mqtt_user_name"));
		o.setMqttPassword(DaoSerializer.getString(_d, "mqtt_password"));
		o.setDebug(DaoSerializer.getBoolean(_d, "debug"));
		return o;
	}
}