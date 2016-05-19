package com.zshgif.laugh.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.zshgif.laugh.dao.db.DaoSession;
import com.zshgif.laugh.model.DuanZiBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DUAN_ZI_BEAN.
*/
public class DuanZiBeanDao extends AbstractDao<DuanZiBean, Long> {

    public static final String TABLENAME = "DUAN_ZI_BEAN";

    /**
     * Properties of entity DuanZiBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NETid = new Property(1, long.class, "NETid", false, "NETID");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Category_name = new Property(3, String.class, "category_name", false, "CATEGORY_NAME");
        public final static Property Type = new Property(4, int.class, "type", false, "TYPE");
        public final static Property Digg_count = new Property(5, int.class, "digg_count", false, "DIGG_COUNT");
        public final static Property Bury_count = new Property(6, int.class, "bury_count", false, "BURY_COUNT");
        public final static Property Comments_count = new Property(7, int.class, "comments_count", false, "COMMENTS_COUNT");
        public final static Property Share_url = new Property(8, String.class, "share_url", false, "SHARE_URL");
    };


    public DuanZiBeanDao(DaoConfig config) {
        super(config);
    }
    
    public DuanZiBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DUAN_ZI_BEAN' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NETID' INTEGER NOT NULL ," + // 1: NETid
                "'CONTENT' TEXT NOT NULL ," + // 2: content
                "'CATEGORY_NAME' TEXT NOT NULL ," + // 3: category_name
                "'TYPE' INTEGER NOT NULL ," + // 4: type
                "'DIGG_COUNT' INTEGER NOT NULL ," + // 5: digg_count
                "'BURY_COUNT' INTEGER NOT NULL ," + // 6: bury_count
                "'COMMENTS_COUNT' INTEGER NOT NULL ," + // 7: comments_count
                "'SHARE_URL' TEXT NOT NULL );"); // 8: share_url
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DUAN_ZI_BEAN'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DuanZiBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getNETid());
        stmt.bindString(3, entity.getContent());
        stmt.bindString(4, entity.getCategory_name());
        stmt.bindLong(5, entity.getType());
        stmt.bindLong(6, entity.getDigg_count());
        stmt.bindLong(7, entity.getBury_count());
        stmt.bindLong(8, entity.getComments_count());
        stmt.bindString(9, entity.getShare_url());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DuanZiBean readEntity(Cursor cursor, int offset) {
        DuanZiBean entity = new DuanZiBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // NETid
            cursor.getString(offset + 2), // content
            cursor.getString(offset + 3), // category_name
            cursor.getInt(offset + 4), // type
            cursor.getInt(offset + 5), // digg_count
            cursor.getInt(offset + 6), // bury_count
            cursor.getInt(offset + 7), // comments_count
            cursor.getString(offset + 8) // share_url
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DuanZiBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNETid(cursor.getLong(offset + 1));
        entity.setContent(cursor.getString(offset + 2));
        entity.setCategory_name(cursor.getString(offset + 3));
        entity.setType(cursor.getInt(offset + 4));
        entity.setDigg_count(cursor.getInt(offset + 5));
        entity.setBury_count(cursor.getInt(offset + 6));
        entity.setComments_count(cursor.getInt(offset + 7));
        entity.setShare_url(cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DuanZiBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DuanZiBean entity) {
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