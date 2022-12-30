package com.lanternsoftware.datamodel.currentmonitor.archive;

import com.lanternsoftware.util.DateUtils;
import com.lanternsoftware.util.dao.annotations.DBSerializable;

import java.util.Date;

@DBSerializable(autogen = false)
public class ArchiveStatus {
	private Date month;
	private float progress;

	public ArchiveStatus() {
	}

	public ArchiveStatus(Date _month, float _progress) {
		month = _month;
		progress = _progress;
	}

	public String getId() {
		return String.format("%d-%d", DateUtils.toLong(month));
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date _month) {
		month = _month;
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float _progress) {
		progress = _progress;
	}
}
