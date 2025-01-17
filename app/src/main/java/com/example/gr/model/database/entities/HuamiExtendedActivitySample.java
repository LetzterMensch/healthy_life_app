package com.example.gr.model.database.entities;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * This class represents a sample specific to the device. Values like activity kind or
 * intensity, are device specific. Normalized values can be retrieved through the
 * corresponding {@link SampleProvider}.
 */
public class HuamiExtendedActivitySample extends MiBandActivitySample {

    private int timestamp;
    private long deviceId;
    private long userId;
    private int rawIntensity;
    private int steps;
    private int rawKind;
    private int heartRate;
    private Integer unknown1;
    private Integer sleep;
    private Integer deepSleep;
    private Integer remSleep;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient HuamiExtendedActivitySampleDao myDao;

    private Device device;
    private Long device__resolvedKey;

    private User user;
    private Long user__resolvedKey;


    public HuamiExtendedActivitySample() {
    }

    public HuamiExtendedActivitySample(int timestamp, long deviceId) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
    }

    public HuamiExtendedActivitySample(int timestamp, long deviceId, long userId, int rawIntensity, int steps, int rawKind, int heartRate, Integer unknown1, Integer sleep, Integer deepSleep, Integer remSleep) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.userId = userId;
        this.rawIntensity = rawIntensity;
        this.steps = steps;
        this.rawKind = rawKind;
        this.heartRate = heartRate;
        this.unknown1 = unknown1;
        this.sleep = sleep;
        this.deepSleep = deepSleep;
        this.remSleep = remSleep;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHuamiExtendedActivitySampleDao() : null;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public long getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public int getRawIntensity() {
        return rawIntensity;
    }

    @Override
    public void setRawIntensity(int rawIntensity) {
        this.rawIntensity = rawIntensity;
    }

    @Override
    public int getSteps() {
        return steps;
    }

    @Override
    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public int getRawKind() {
        return rawKind;
    }

    @Override
    public void setRawKind(int rawKind) {
        this.rawKind = rawKind;
    }

    @Override
    public int getHeartRate() {
        return heartRate;
    }

    @Override
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(Integer unknown1) {
        this.unknown1 = unknown1;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

    public Integer getDeepSleep() {
        return deepSleep;
    }

    public void setDeepSleep(Integer deepSleep) {
        this.deepSleep = deepSleep;
    }

    public Integer getRemSleep() {
        return remSleep;
    }

    public void setRemSleep(Integer remSleep) {
        this.remSleep = remSleep;
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
