package com.example.fooddelivery.data

object FoodData {
    val products = listOf(
        Product(
            id = 1,
            name = "Яблоко \"Гренни Смит\"",
            price = 10.0,
            images = listOf("https://images.unsplash.com/photo-1600585154340-be6161a56a0c", "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6"),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Кисло-сладкое зеленое яблоко с хрустящей мякотью, идеально для салатов и десертов.",
            attributes = mapOf("Вес" to "150 г", "Калорийность" to "52 ккал/100 г", "Срок хранения" to "2 месяца")
        ),
        Product(
            id = 2,
            name = "Груша \"Конференц\"",
            price = 12.0,
            images = listOf("https://images.unsplash.com/photo-1606820732558-2eadae7c68e8"),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Сочная и сладкая груша с мягкой текстурой, отличный перекус.",
            attributes = mapOf("Вес" to "180 г", "Калорийность" to "57 ккал/100 г", "Срок хранения" to "1 месяц")
        ),
        Product(
            id = 3,
            name = "Банан \"Кавендиш\"",
            price = 15.0,
            images = listOf(
                "https://images.unsplash.com/photo-1603834312919-4c11b54f75b0",
                "https://images.unsplash.com/photo-1571771894821-ce9b6c118b02",
                "https://images.unsplash.com/photo-1587132137056-bfbf01605601"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Спелый желтый банан, богатый калием, подходит для смузи и выпечки.",
            attributes = mapOf("Вес" to "120 г", "Калорийность" to "89 ккал/100 г", "Срок хранения" to "3 недели")
        ),
        Product(
            id = 4,
            name = "Апельсин \"Валенсия\"",
            price = 18.0,
            images = listOf("https://images.unsplash.com/photo-1582979512210-99c450b324r9", "https://images.unsplash.com/photo-1611080626919-7cf5a9dbab5b"),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Сочный апельсин с ярким цитрусовым ароматом, идеален для сока.",
            attributes = mapOf("Вес" to "200 г", "Калорийность" to "47 ккал/100 г", "Срок хранения" to "1.5 месяца")
        ),
        Product(
            id = 5,
            name = "Киви",
            price = 20.0,
            images = listOf("https://images.unsplash.com/photo-1584270451948-6b6b4053b2b0"),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Кислый и сладкий киви с яркой зеленой мякотью, богат витамином C.",
            attributes = mapOf("Вес" to "100 г", "Калорийность" to "61 ккал/100 г", "Срок хранения" to "2 месяца")
        ),
        Product(
            id = 6,
            name = "Морковь \"Нантская\"",
            price = 8.0,
            images = listOf("https://images.unsplash.com/photo-1598170845058-32b9d6a5da37", "https://images.unsplash.com/photo-1447175008436-054170c2e979"),
            category = "Базар",
            subcategory = "Овощи",
            description = "Сладкая и хрустящая морковь, идеальна для супов и салатов.",
            attributes = mapOf("Вес" to "150 г", "Калорийность" to "41 ккал/100 г", "Срок хранения" to "3 месяца")
        ),
        Product(
            id = 7,
            name = "Картофель \"Ред Скарлет\"",
            price = 5.0,
            images = listOf("https://images.unsplash.com/photo-1518977676601-b53f82aba655"),
            category = "Базар",
            subcategory = "Овощи",
            description = "Красный картофель с плотной мякотью, подходит для жарки и пюре.",
            attributes = mapOf("Вес" to "200 г", "Калорийность" to "77 ккал/100 г", "Срок хранения" to "6 месяцев")
        ),
        Product(
            id = 8,
            name = "Лук репчатый",
            price = 6.0,
            images = listOf(
                "https://images.unsplash.com/photo-1618512496248-a07fe46126e6",
                "https://images.unsplash.com/photo-1518972559570-7cc1d58e1f40",
                "https://images.unsplash.com/photo-1603046899798-0bcea4bf0e09"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Острый и ароматный лук, незаменим в кулинарии.",
            attributes = mapOf("Вес" to "120 г", "Калорийность" to "40 ккал/100 г", "Срок хранения" to "4 месяца")
        ),
        Product(
            id = 9,
            name = "Томат \"Черри\"",
            price = 12.0,
            images = listOf("https://images.unsplash.com/photo-1592925625178-8a4e85b34b9e", "https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2"),
            category = "Базар",
            subcategory = "Овощи",
            description = "Маленькие сладкие томаты, идеальны для закусок и салатов.",
            attributes = mapOf("Вес" to "20 г", "Калорийность" to "18 ккал/100 г", "Срок хранения" to "1 месяц")
        ),
        Product(
            id = 10,
            name = "Огурец \"Герман\"",
            price = 10.0,
            images = listOf(null), // Тест ошибки изображения
            category = "Базар",
            subcategory = "Овощи",
            description = "Свежий и хрустящий огурец, отлично подходит для салатов.",
            attributes = mapOf("Вес" to "150 г", "Калорийность" to "15 ккал/100 г", "Срок хранения" to "2 недели")
        )
    )

    val categories = listOf(
        Category(
            name = "Базар",
            subcategories = listOf(
                Subcategory("Фрукты", 0xFF4CAF50),
                Subcategory("Овощи", 0xFF8BC34A)
            ),
            gradient = listOf(0xFF4CAF50, 0xFF8BC34A)
        )
    )
}