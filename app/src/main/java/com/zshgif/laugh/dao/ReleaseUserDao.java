package com.zshgif.laugh.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.zshgif.laugh.dao.db.DaoSession;
import com.zshgif.laugh.model.ReleaseUser;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;



// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table RELEASEUSER.
*/
public class ReleaseUserDao extends AbstractDao<ReleaseUser, Long> {

    public static final String TABLENAME = "RELEASEUSER";

    /**
     * Properties of entity ReleaseUser.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NETid = new Property(1, long.class, "NETid", false, "NETID");
        public final static Property Username = new Property(2, String.class, "username", false, "USERNAME");
        public final static Property UserProfile = new Property(3, String.class, "userProfile", false, "USER_PROFILE");
    };


    public ReleaseUserDao(DaoConfig config) {
        super(config);
    }
    
    public ReleaseUserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'RELEASEUSER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NETID' INTEGER NOT NULL ," + // 1: NETid
                "'USERNAME' TEXT NOT NULL ," + // 2: username
                "'USER_PROFILE' TEXT NOT NULL );"); // 3: userProfile
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'RELEASEUSER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ReleaseUser entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getNETid());
        stmt.bindString(3, entity.getUsername());
        stmt.bindString(4, entity.getUserProfile());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ReleaseUser readEntity(Cursor cursor, int offset) {
        ReleaseUser entity = new ReleaseUser( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // NETid
            cursor.getString(offset + 2), // username
            cursor.getString(offset + 3) // userProfile
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ReleaseUser entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNETid(cursor.getLong(offset + 1));
        entity.setUsername(cursor.getString(offset + 2));
        entity.setUserProfile(cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ReleaseUser entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ReleaseUser entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
