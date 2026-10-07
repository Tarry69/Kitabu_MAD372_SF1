package com.example.kitabu_mad372_sf1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

@Database(entities = [BookEntity::class, BookingEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kitabu_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor().execute {
                            seedDatabase(db)
                        }
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        Executors.newSingleThreadExecutor().execute {
                            val cursor = db.query("SELECT COUNT(*) FROM books")
                            if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
                                seedDatabase(db)
                            }
                            cursor.close()
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private fun seedDatabase(db: SupportSQLiteDatabase) {
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (1, 'Kotlin Programming: The Big Nerd Ranch Guide', 'Josh Skeen', 'Computer Science', 0, 'https://covers.openlibrary.org/b/isbn/9780135161630-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (2, 'Clean Architecture: A Craftsman Guide', 'Robert C. Martin', 'Software Engineering', 0, 'https://covers.openlibrary.org/b/isbn/9780134494166-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (3, 'Database System Concepts', 'Abraham Silberschatz', 'Information Systems', 0, 'https://covers.openlibrary.org/b/isbn/9780073523323-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (4, 'Introduction to Algorithms', 'Thomas H. Cormen', 'Mathematics', 1, 'https://covers.openlibrary.org/b/isbn/9780262033848-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (5, 'Modern Operating Systems', 'Andrew S. Tanenbaum', 'Computer Science', 1, 'https://covers.openlibrary.org/b/isbn/9780133591620-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (6, 'Design Patterns: Elements of Reusable Object-Oriented Software', 'Erich Gamma', 'Software Engineering', 1, 'https://covers.openlibrary.org/b/isbn/9780201633610-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (7, 'Artificial Intelligence: A Modern Approach', 'Stuart Russell', 'Computer Science', 1, 'https://covers.openlibrary.org/b/isbn/9780134610993-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (8, 'Cracking the Coding Interview', 'Gayle Laakmann McDowell', 'Career Development', 1, 'https://covers.openlibrary.org/b/isbn/9780984782857-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (9, 'Computer Networking: A Top-Down Approach', 'James F. Kurose', 'Computer Networks', 1, 'https://covers.openlibrary.org/b/isbn/9780133594140-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (10, 'Compilers: Principles, Techniques, and Tools', 'Alfred V. Aho', 'Computer Science', 1, 'https://covers.openlibrary.org/b/isbn/9780321486813-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (11, 'Code Complete: A Practical Handbook of Software Construction', 'Steve McConnell', 'Software Engineering', 1, 'https://covers.openlibrary.org/b/isbn/9780735619678-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (12, 'Deep Learning', 'Ian Goodfellow', 'Artificial Intelligence', 1, 'https://covers.openlibrary.org/b/isbn/9780262035613-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (13, 'Clean Code: A Handbook of Agile Software Craftsmanship', 'Robert C. Martin', 'Software Engineering', 1, 'https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (14, 'The Pragmatic Programmer', 'Andrew Hunt', 'Software Engineering', 1, 'https://covers.openlibrary.org/b/isbn/9780201616224-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (15, 'Structure and Interpretation of Computer Programs', 'Harold Abelson', 'Computer Science', 1, 'https://covers.openlibrary.org/b/isbn/9780262510875-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (16, 'Refactoring: Improving the Design of Existing Code', 'Martin Fowler', 'Software Engineering', 1, 'https://covers.openlibrary.org/b/isbn/9780201485677-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (17, 'Head First Design Patterns', 'Eric Freeman', 'Software Engineering', 1, 'https://covers.openlibrary.org/b/isbn/9780596007126-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (18, 'Computer Systems: A Programmer Perspective', 'Randal E. Bryant', 'Computer Systems', 1, 'https://covers.openlibrary.org/b/isbn/9780134092669-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (19, 'JavaScript: The Good Parts', 'Douglas Crockford', 'Web Development', 1, 'https://covers.openlibrary.org/b/isbn/9780596517748-L.jpg')")
            db.execSQL("INSERT OR IGNORE INTO books (bookId, title, author, category, isAvailable, imageUrl) VALUES (20, 'Python Crash Course', 'Eric Matthes', 'Computer Science', 1, 'https://covers.openlibrary.org/b/isbn/9781593279288-L.jpg')")

            val now = System.currentTimeMillis()
            val sevenDays = 7L * 24L * 60L * 60L * 1000L
            db.execSQL("INSERT OR IGNORE INTO bookings (bookingId, bookOwnerId, userName, bookingDate, returnDeadline, status) VALUES (1, 1, 'Alice Smith', $now, ${now + sevenDays}, 'PENDING')")
            db.execSQL("INSERT OR IGNORE INTO bookings (bookingId, bookOwnerId, userName, bookingDate, returnDeadline, status) VALUES (2, 2, 'Bob Jones', $now, ${now + sevenDays}, 'READY_FOR_PICKUP')")
            db.execSQL("INSERT OR IGNORE INTO bookings (bookingId, bookOwnerId, userName, bookingDate, returnDeadline, status) VALUES (3, 3, 'Charlie Brown', $now, ${now + sevenDays}, 'BORROWED')")
        }
    }
}
