package com.example.gr.database.entities;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "ALARM".
 */
public class Alarm implements com.example.gr.device.model.Alarm {

    private long deviceId;
    private long userId;
    private int position;
    private boolean enabled;
    private boolean smartWakeup;
    private Integer smartWakeupInterval;
    private boolean snooze;
    private int repetition;
    private int hour;
    private int minute;
    private boolean unused;
    private String title;
    private String description;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient AlarmDao myDao;

    private User user;
    private Long user__resolvedKey;

    private Device device;
    private Long device__resolvedKey;


    public Alarm() {
    }

    public Alarm(long deviceId, long userId, int position, boolean enabled, boolean smartWakeup, Integer smartWakeupInterval, boolean snooze, int repetition, int hour, int minute, boolean unused, String title, String description) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.position = position;
        this.enabled = enabled;
        this.smartWakeup = smartWakeup;
        this.smartWakeupInterval = smartWakeupInterval;
        this.snooze = snooze;
        this.repetition = repetition;
        this.hour = hour;
        this.minute = minute;
        this.unused = unused;
        this.title = title;
        this.description = description;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAlarmDao() : null;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getSmartWakeup() {
        return smartWakeup;
    }

    public void setSmartWakeup(boolean smartWakeup) {
        this.smartWakeup = smartWakeup;
    }

    public Integer getSmartWakeupInterval() {
        return smartWakeupInterval;
    }

    public void setSmartWakeupInterval(Integer smartWakeupInterval) {
        this.smartWakeupInterval = smartWakeupInterval;
    }

    public boolean getSnooze() {
        return snooze;
    }

    public void setSnooze(boolean snooze) {
        this.snooze = snooze;
    }

    public boolean isRepetitive() { return getRepetition() != ALARM_ONCE; } public boolean getRepetition(int dow) { return (this.repetition & dow) > 0; }
    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean getUnused() {
        return unused;
    }

    public void setUnused(boolean unused) {
        this.unused = unused;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** To-one relationship, resolved on first access. */
    public User getUser() {
        long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
            	user__resolvedKey = __key;
            }
        }
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new DaoException("To-one property 'userId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            userId = user.getId();
            user__resolvedKey = userId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Device getDevice() {
        long __key = this.deviceId;
        if (device__resolvedKey == null || !device__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DeviceDao targetDao = daoSession.getDeviceDao();
            Device deviceNew = targetDao.load(__key);
            synchronized (this) {
                device = deviceNew;
            	device__resolvedKey = __key;
            }
        }
        return device;
    }

    public void setDevice(Device device) {
        if (device == null) {
            throw new DaoException("To-one property 'deviceId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.device = device;
            deviceId = device.getId();
            device__resolvedKey = deviceId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
