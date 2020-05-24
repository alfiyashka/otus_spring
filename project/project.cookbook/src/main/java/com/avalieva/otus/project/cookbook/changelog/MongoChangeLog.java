package com.avalieva.otus.project.cookbook.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog
public class MongoChangeLog {
    @ChangeSet(order = "000", id = "dropDB", author = "avalieva", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }
}
