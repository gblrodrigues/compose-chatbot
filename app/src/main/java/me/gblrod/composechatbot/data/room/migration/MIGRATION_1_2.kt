package me.gblrod.composechatbot.data.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE chat_messages
            ADD COLUMN status TEXT NOT NULL DEFAULT 'sent'
            """
        )
    }
}