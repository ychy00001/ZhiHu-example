package com.rain.zhihu_example.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.rain.zhihu_example.global.RainApplication;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import greendao.dao.DaoMaster;
import greendao.dao.DaoSession;

/**
 * GreenDao的工具类
 * @author yangchunyu
 *         2016/3/9
 *         17:09
 */
public class GreenDaoUtil {

    public static final String DB_COLLECTION_NAME = "collection-db";

    private DaoMaster.DevOpenHelper mDbHelper;
    private String mDbName;//数据库名称

    private DaoMaster mDaoMaster;

    private GreenDaoUtil(){}

    private static class Holder {
        public static final GreenDaoUtil INSTANCE = new GreenDaoUtil();
    }

    private GreenDaoUtil init(Context context,String dbName){
        if (context == null)
            context = RainApplication.getContext();
        if(mDbHelper == null && !dbName.equals(mDbName)){
            mDbHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
            SQLiteDatabase mDB = mDbHelper.getWritableDatabase();
            mDbName = dbName;
            mDaoMaster = new DaoMaster(mDB);
        }
        return this;
    }

    public static GreenDaoUtil getInstance(Context context,String dbName) {
        return Holder.INSTANCE.init(context,dbName);
    }

    /**
     * 获取DaoMaster
     */
    public DaoMaster getDaoMaster(){
        return mDaoMaster;
    }

    /**
     * 获取DaoSession
     */
    public DaoSession getDaoSesstion(){
        return mDaoMaster.newSession();
    }

    //查询单个Bean对象
    @SuppressWarnings("all")
    public<T> T QueryBean(AbstractDao dao,WhereCondition cond,WhereCondition... condMore){
        QueryBuilder<T> qb = dao.queryBuilder();
        QueryBuilder<T> where = qb.where(cond,condMore);
        return where.unique();
    }
}
