package com.rain.greendao;

import de.greenrobot.daogenerator.*;

/**
 * GreenDao
 *
 * @author yangchunyu
 *         2016/3/8
 *         10:50
 */
public class DaoGeneratorFactory {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "greendao.bean");
        schema.setDefaultJavaPackageDao("greendao.dao");

        schema.enableActiveEntitiesByDefault();
        schema.enableKeepSectionsByDefault();

        addNote(schema);

        new DaoGenerator().generateAll(schema, "../ZhiHu-example/app/src/main/java-gen");
//        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void addNote(Schema schema) {
        Entity collection = schema.addEntity("Collection");
        collection.addIntProperty("storyId").primaryKey().notNull();
        collection.addStringProperty("image").notNull();
        collection.addStringProperty("title").notNull();
        collection.addIntProperty("type").notNull();

        Entity user = schema.addEntity("User");
        Property userPro = user.addIntProperty("userId").primaryKey().notNull().getProperty();
        ToMany toMany = user.addToMany(collection, userPro);
        toMany.setName("collections");

    }
}
