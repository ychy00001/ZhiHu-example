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
    }

    private static void addNote(Schema schema) {
        Entity collection = schema.addEntity("Collection");
        collection.addIdProperty().primaryKey();
        collection.addStringProperty("storyId").notNull();
        collection.addStringProperty("image");
        collection.addStringProperty("title").notNull();
        collection.addIntProperty("type");
        Property userId = collection.addStringProperty("userId").notNull().getProperty();

        Entity user = schema.addEntity("User");
        user.addStringProperty("userId").primaryKey().notNull().getProperty();
        ToMany toMany = user.addToMany(collection, userId);
        toMany.setName("collections");

    }
}
