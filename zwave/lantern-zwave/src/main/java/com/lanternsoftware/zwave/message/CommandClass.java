package com.lanternsoftware.zwave.message;

import java.util.HashMap;
import java.util.Map;

public enum CommandClass {
	NO_OPERATION((byte)0x00, "NO_OPERATION"),
	BASIC((byte)0x20, "BASIC"),
	CONTROLLER_REPLICATION((byte)0x21, "CONTROLLER_REPLICATION"),
	APPLICATION_STATUS((byte)0x22, "APPLICATION_STATUS"),
	ZIP_SERVICES((byte)0x23, "ZIP_SERVICES"),
	ZIP_SERVER((byte)0x24, "ZIP_SERVER"),
	SWITCH_BINARY((byte)0x25, "SWITCH_BINARY", true, 0),
	SWITCH_MULTILEVEL((byte)0x26, "SWITCH_MULTILEVEL", true, 0),
	SWITCH_ALL((byte)0x27, "SWITCH_ALL"),
	SWITCH_TOGGLE_BINARY((byte)0x28, "SWITCH_TOGGLE_BINARY"),
	SWITCH_TOGGLE_MULTILEVEL((byte)0x29, "SWITCH_TOGGLE_MULTILEVEL"),
	CHIMNEY_FAN((byte)0x2A, "CHIMNEY_FAN"),
	SCENE_ACTIVATION((byte)0x2B, "SCENE_ACTIVATION"),
	SCENE_ACTUATOR_CONF((byte)0x2C, "SCENE_ACTUATOR_CONF"),
	SCENE_CONTROLLER_CONF((byte)0x2D, "SCENE_CONTROLLER_CONF"),
	ZIP_CLIENT((byte)0x2E, "ZIP_CLIENT"),
	ZIP_ADV_SERVICES((byte)0x2F, "ZIP_ADV_SERVICES"),
	SENSOR_BINARY((byte)0x30, "SENSOR_BINARY"),
	SENSOR_MULTILEVEL((byte)0x31, "SENSOR_MULTILEVEL"),
	METER((byte)0x32, "METER", true, 60),
	ZIP_ADV_SERVER((byte)0x33, "ZIP_ADV_SERVER"),
	ZIP_ADV_CLIENT((byte)0x34, "ZIP_ADV_CLIENT"),
	METER_PULSE((byte)0x35, "METER_PULSE"),
	METER_TBL_CONFIG((byte)0x3C, "METER_TBL_CONFIG"),
	METER_TBL_MONITOR((byte)0x3D, "METER_TBL_MONITOR"),
	METER_TBL_PUSH((byte)0x3E, "METER_TBL_PUSH"),
	THERMOSTAT_HEATING((byte)0x38, "THERMOSTAT_HEATING"),
	THERMOSTAT_MODE((byte)0x40, "THERMOSTAT_MODE"),
	THERMOSTAT_OPERATING_STATE((byte)0x42, "THERMOSTAT_OPERATING_STATE"),
	THERMOSTAT_SETPOINT((byte)0x43, "THERMOSTAT_SETPOINT"),
	THERMOSTAT_FAN_MODE((byte)0x44, "THERMOSTAT_FAN_MODE"),
	THERMOSTAT_FAN_STATE((byte)0x45, "THERMOSTAT_FAN_STATE"),
	CLIMATE_CONTROL_SCHEDULE((byte)0x46, "CLIMATE_CONTROL_SCHEDULE"),
	THERMOSTAT_SETBACK((byte)0x47, "THERMOSTAT_SETBACK"),
	DOOR_LOCK_LOGGING((byte)0x4C, "DOOR_LOCK_LOGGING"),
	SCHEDULE_ENTRY_LOCK((byte)0x4E, "SCHEDULE_ENTRY_LOCK"),
	BASIC_WINDOW_COVERING((byte)0x50, "BASIC_WINDOW_COVERING"),
	MTP_WINDOW_COVERING((byte)0x51, "MTP_WINDOW_COVERING"),
	CRC_16_ENCAP((byte)0x5B, "CRC_16_ENCAP"),
	MULTI_INSTANCE((byte)0x60, "MULTI_INSTANCE"),
	DOOR_LOCK((byte)0x62, "DOOR_LOCK"),
	USER_CODE((byte)0x63, "USER_CODE"),
	CONFIGURATION((byte)0x70, "CONFIGURATION"),
	ALARM((byte)0x71, "ALARM"),
	MANUFACTURER_SPECIFIC((byte)0x72, "MANUFACTURER_SPECIFIC"),
	POWERLEVEL((byte)0x73, "POWERLEVEL"),
	PROTECTION((byte)0x75, "PROTECTION"),
	LOCK((byte)0x76, "LOCK"),
	NODE_NAMING((byte)0x77, "NODE_NAMING"),
	FIRMWARE_UPDATE_MD((byte)0x7A, "FIRMWARE_UPDATE_MD"),
	GROUPING_NAME((byte)0x7B, "GROUPING_NAME"),
	REMOTE_ASSOCIATION_ACTIVATE((byte)0x7C, "REMOTE_ASSOCIATION_ACTIVATE"),
	REMOTE_ASSOCIATION((byte)0x7D, "REMOTE_ASSOCIATION"),
	BATTERY((byte)0x80, "BATTERY", true, 3600),
	CLOCK((byte)0x81, "CLOCK"),
	HAIL((byte)0x82, "HAIL"),
	WAKE_UP((byte)0x84, "WAKE_UP"),
	ASSOCIATION((byte)0x85, "ASSOCIATION"),
	VERSION((byte)0x86, "VERSION"),
	INDICATOR((byte)0x87, "INDICATOR"),
	PROPRIETARY((byte)0x88, "PROPRIETARY"),
	LANGUAGE((byte)0x89, "LANGUAGE"),
	TIME((byte)0x8A, "TIME"),
	TIME_PARAMETERS((byte)0x8B, "TIME_PARAMETERS"),
	GEOGRAPHIC_LOCATION((byte)0x8C, "GEOGRAPHIC_LOCATION"),
	COMPOSITE((byte)0x8D, "COMPOSITE"),
	MULTI_INSTANCE_ASSOCIATION((byte)0x8E, "MULTI_INSTANCE_ASSOCIATION"),
	MULTI_CMD((byte)0x8F, "MULTI_CMD"),
	ENERGY_PRODUCTION((byte)0x90, "ENERGY_PRODUCTION"),
	MANUFACTURER_PROPRIETARY((byte)0x91, "MANUFACTURER_PROPRIETARY"),
	SCREEN_MD((byte)0x92, "SCREEN_MD"),
	SCREEN_ATTRIBUTES((byte)0x93, "SCREEN_ATTRIBUTES"),
	SIMPLE_AV_CONTROL((byte)0x94, "SIMPLE_AV_CONTROL"),
	AV_CONTENT_DIRECTORY_MD((byte)0x95, "AV_CONTENT_DIRECTORY_MD"),
	AV_RENDERER_STATUS((byte)0x96, "AV_RENDERER_STATUS"),
	AV_CONTENT_SEARCH_MD((byte)0x97, "AV_CONTENT_SEARCH_MD"),
	SECURITY((byte)0x98, "SECURITY"),
	AV_TAGGING_MD((byte)0x99, "AV_TAGGING_MD"),
	IP_CONFIGURATION((byte)0x9A, "IP_CONFIGURATION"),
	ASSOCIATION_COMMAND_CONFIGURATION((byte)0x9B, "ASSOCIATION_COMMAND_CONFIGURATION"),
	SENSOR_ALARM((byte)0x9C, "SENSOR_ALARM"),
	SILENCE_ALARM((byte)0x9D, "SILENCE_ALARM"),
	SENSOR_CONFIGURATION((byte)0x9E, "SENSOR_CONFIGURATION"),
	MARK((byte)0xEF, "MARK"),
	NON_INTEROPERABLE((byte)0xF0, "NON_INTEROPERABLE"),
	ALL((byte)0xFF, null);

	public final byte data;
	public final String label;
	public final boolean supportsPolling;
	public final int secondsRefreshInterval;
	private static final Map<Byte, CommandClass> classes = new HashMap<>();
	static {
		for (CommandClass cls : values()) {
			classes.put(cls.data, cls);
		}
	}

	CommandClass(byte _data, String _label) {
		this(_data, _label, false, 0);
	}

	CommandClass(byte _data, String _label, boolean _supportsPolling, int _secondsRefreshInterval) {
		data = _data;
		label = _label;
		supportsPolling = _supportsPolling;
		secondsRefreshInterval = _secondsRefreshInterval;
	}

	public static CommandClass fromByte(byte _code) {
		CommandClass cls = classes.get(_code);
		return cls == null ? CommandClass.NO_OPERATION : cls;
	}
}
