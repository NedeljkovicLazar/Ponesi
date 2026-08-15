package com.lazar.ponesi.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object InitialDataCallback : RoomDatabase.Callback() {

    override fun onCreate(
        db: SupportSQLiteDatabase
    ) {
        super.onCreate(db)

        var nextCategoryId = 1
        var nextItemId = 1

        initialTravels.forEachIndexed { travelIndex, travel ->

            val travelId = travelIndex + 1

            db.execSQL(
                """
                INSERT INTO travels (
                    id,
                    name,
                    status,
                    date
                )
                VALUES (?, ?, ?, NULL)
                """.trimIndent(),
                arrayOf<Any?>(travelId, travel.name, "INACTIVE")
            )

            travel.categories.forEachIndexed {
                    categoryPosition,
                    category ->

                val categoryId = nextCategoryId
                nextCategoryId++

                db.execSQL(
                    """
                    INSERT INTO categories (
                        id,
                        travelId,
                        name,
                        position
                    )
                    VALUES (?, ?, ?, ?)
                    """.trimIndent(),
                    arrayOf<Any?>(
                        categoryId,
                        travelId,
                        category.name,
                        categoryPosition
                    )
                )

                category.items.forEachIndexed {
                        itemPosition,
                        itemName ->

                    val itemId = nextItemId
                    nextItemId++

                    db.execSQL(
                        """
                        INSERT INTO packing_items (
                            id,
                            categoryId,
                            name,
                            isChecked,
                            position
                        )
                        VALUES (?, ?, ?, 0, ?)
                        """.trimIndent(),
                        arrayOf<Any?>(
                            itemId,
                            categoryId,
                            itemName,
                            itemPosition
                        )
                    )
                }
            }
        }
    }

    private val initialTravels = listOf(

        InitialTravel(
            name = "Letovanje",
            categories = listOf(
                InitialCategory(
                    name = "Kofer",
                    items = listOf(
                        "Majice",
                        "Šorcevi",
                        "Pantalone",
                        "Donji veš",
                        "Čarape",
                        "Pidžama",
                        "Kupaći kostim",
                        "Papuče"
                    )
                ),
                InitialCategory(
                    name = "Neseser",
                    items = listOf(
                        "Četkica za zube",
                        "Pasta za zube",
                        "Šampon",
                        "Gel za tuširanje",
                        "Dezodorans",
                        "Krema za sunčanje"
                    )
                ),
                InitialCategory(
                    name = "Ručni prtljag",
                    items = listOf(
                        "Telefon",
                        "Punjač",
                        "Slušalice",
                        "Novčanik",
                        "Dokumenta"
                    )
                ),
                InitialCategory(
                    name = "Plaža",
                    items = listOf(
                        "Peškir za plažu",
                        "Naočare za sunce",
                        "Flašica vode"
                    )
                ),
                InitialCategory(
                    name = "Pre polaska",
                    items = listOf(
                        "Proveri dokumenta",
                        "Zatvori prozore",
                        "Isključi uređaje",
                        "Iznesi smeće",
                        "Zaključaj vrata"
                    )
                )
            )
        ),

        InitialTravel(
            name = "Vikend putovanje",
            categories = listOf(
                InitialCategory(
                    name = "Torba",
                    items = listOf(
                        "Majice",
                        "Pantalone",
                        "Donji veš",
                        "Čarape",
                        "Pidžama"
                    )
                ),
                InitialCategory(
                    name = "Neseser",
                    items = listOf(
                        "Četkica za zube",
                        "Pasta za zube",
                        "Dezodorans",
                        "Gel za tuširanje"
                    )
                ),
                InitialCategory(
                    name = "Elektronika",
                    items = listOf(
                        "Telefon",
                        "Punjač",
                        "Power bank",
                        "Slušalice"
                    )
                ),
                InitialCategory(
                    name = "Pre polaska",
                    items = listOf(
                        "Proveri dokumenta",
                        "Zatvori prozore",
                        "Isključi uređaje",
                        "Iznesi smeće",
                        "Zaključaj vrata"
                    )
                )
            )
        ),

        InitialTravel(
            name = "Jednodnevni izlet",
            categories = listOf(
                InitialCategory(
                    name = "Ranac",
                    items = listOf(
                        "Novčanik",
                        "Dokumenta",
                        "Maramice",
                        "Rezervna majica"
                    )
                ),
                InitialCategory(
                    name = "Hrana i piće",
                    items = listOf(
                        "Flašica vode",
                        "Užina"
                    )
                ),
                InitialCategory(
                    name = "Dodatna oprema",
                    items = listOf(
                        "Power bank",
                        "Kapa",
                        "Naočare za sunce",
                        "Kišobran"
                    )
                ),
                InitialCategory(
                    name = "Pre polaska",
                    items = listOf(
                        "Proveri vremensku prognozu",
                        "Napuni telefon",
                        "Proveri dokumenta",
                        "Zaključaj vrata"
                    )
                )
            )
        )
    )
}

private data class InitialTravel(
    val name: String,
    val categories: List<InitialCategory>
)

private data class InitialCategory(
    val name: String,
    val items: List<String>
)