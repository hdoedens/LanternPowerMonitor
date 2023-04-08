package com.lanternsoftware.datamodel.currentmonitor;

import java.util.List;
import java.util.Objects;

import com.lanternsoftware.util.IIdentical;
import com.lanternsoftware.util.dao.annotations.DBSerializable;

@DBSerializable(autogen = false)
public class BreakerHub implements IIdentical<BreakerHub> {
	private double voltageCalibrationFactor;
	private double portCalibrationFactor;
	private List<Phase> phases;
	private int frequency;
	private String mqttBrokerUrl;
	private String mqttUsername;
	private String mqttPassword;
	private boolean needsCalibration;
	private boolean debug;

	public void setMqttBrokerUrl(String _mqttBrokerUrl) {
		mqttBrokerUrl = _mqttBrokerUrl;
	}

	public void setMqttUsername(String _mqttUsername) {
		mqttUsername = _mqttUsername;
	}

	public void setMqttPassword(String _mqttPassword) {
		mqttPassword = _mqttPassword;
	}

	public String getMqttBrokerUrl() {
		return mqttBrokerUrl;
	}

	public String getMqttUsername() {
		return mqttUsername;
	}

	public String getMqttPassword() {
		return mqttPassword;
	}

	public String getmqttBrokerUrl() {
		return mqttBrokerUrl;
	}

	public void setNeedsCalibration(boolean _needsCalibration) {
		needsCalibration = _needsCalibration;
	}

	public void setDebug(boolean _debug) {
		debug = _debug;
	}

	public boolean isNeedsCalibration() {
		return needsCalibration;
	}

	public boolean isDebug() {
		return debug;
	}

	public List<Phase> getPhases() {
		return phases;
	}

	public void setPhases(List<Phase> _phases) {
		phases = _phases;
	}

	public double getRawVoltageCalibrationFactor() {
		return voltageCalibrationFactor;
	}

	public double getVoltageCalibrationFactor() {
		return voltageCalibrationFactor == 0.0 ? 0.3445 : voltageCalibrationFactor;
	}

	public void setVoltageCalibrationFactor(double _voltageCalibrationFactor) {
		voltageCalibrationFactor = _voltageCalibrationFactor;
	}

	public double getRawPortCalibrationFactor() {
		return portCalibrationFactor;
	}

	public double getPortCalibrationFactor() {
		return portCalibrationFactor == 0.0 ? 1.20 : portCalibrationFactor;
	}

	public void setPortCalibrationFactor(double _portCalibrationFactor) {
		portCalibrationFactor = _portCalibrationFactor;
	}

	public int getPhaseCnt() {
		return phases.size();
	}

	public int getFrequency() {
		return frequency == 0 ? 60 : frequency;
	}

	public void setFrequency(int _frequency) {
		frequency = _frequency;
	}

	@Override
	public boolean equals(Object _o) {
		return true;
	}

	@Override
	public boolean isIdentical(BreakerHub _o) {
		if (this == _o)
			return true;
		return Double.compare(_o.voltageCalibrationFactor, voltageCalibrationFactor) == 0
				&& Double.compare(_o.portCalibrationFactor, portCalibrationFactor) == 0
				&& getPhaseCnt() == _o.getPhaseCnt()
				&& getFrequency() == _o.getFrequency();
	}

	@Override
	public int hashCode() {
		return Objects.hash("foo");
	}

	public Phase getPhaseForBreaker(Breaker breaker) {
		for (Phase phase : phases) {
			if (breaker.getPhaseId() == phase.getId())
				return phase;
		}
		return null;
	}
}
