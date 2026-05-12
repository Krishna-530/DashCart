package com.example.myapplication.utils

import com.example.myapplication.model.Product

object DummyData {

    fun getProducts(): List<Product> = listOf(

        Product(101, "Freedom Refined Sunflower Oil", "Freedom", 352.0,
            "https://www.bigbasket.com/media/uploads/p/l/274424_14-freedom-refined-sunflower-oil.jpg",
            "Cooking Oil", description = "Light and healthy refined sunflower oil, perfect for everyday cooking.", weight = "1 ltr x 2", originalPrice = 430.0, isFrequentlyPurchased = true, isHighestDiscount = true, stockCount = 3, tags = listOf("Healthy", "Organic")),

        Product(102, "Bisleri Mineral Water", "Bisleri", 108.0,
            "https://www.bigbasket.com/media/uploads/p/l/1210452_3-bisleri-mineral-water.jpg",
            "Beverages", description = "Pure, safe and refreshing mineral water.", weight = "2 ltr x 4", originalPrice = 120.0, isFrequentlyPurchased = true, stockCount = 20),

        Product(103, "Kurkure Namkeen (Masala Munch)", "Kurkure", 18.0,
            "https://www.bigbasket.com/media/uploads/p/l/283428_18-kurkure-namkeen-masala-munch.jpg",
            "Snacks", description = "Crunchy, fiery, tangy — the ultimate Indian tea-time snack with a blend of authentic Indian spices.", weight = "84.9 g", originalPrice = 20.0, isFrequentlyPurchased = true),

        Product(104, "Mysore Sandal Soap", "Mysore Sandal", 35.0,
            "https://www.bigbasket.com/media/uploads/p/l/100003862_1-mysore-sandal-bathing-soap.jpg",
            "Personal Care", description = "Made from pure sandalwood oil, known for its therapeutic and beauty properties.", weight = "75 g", originalPrice = 38.0, isFrequentlyPurchased = true),

        Product(105, "Organic Chia Seeds", "True Elements", 250.0,
            "https://www.bigbasket.com/media/uploads/p/l/40112398_8-true-elements-raw-chia-seeds.jpg",
            "Pantry", description = "High-quality, raw organic chia seeds, perfect for smoothies and puddings.", weight = "250 g", originalPrice = 299.0, isNewlyAdded = true, tags = listOf("Organic", "Keto", "Vegan")),

        Product(106, "Almond Milk - Unsweetened", "So Good", 220.0,
            "https://www.bigbasket.com/media/uploads/p/l/40118557_5-so-good-plant-based-almond-beverage-unsweetened.jpg",
            "Dairy Alternatives", description = "Delicious, unsweetened almond milk. Vegan and lactose-free.", weight = "1 L", originalPrice = 250.0, isNewlyAdded = true, tags = listOf("Vegan", "Organic")),

        Product(107, "Surf Excel Easy Wash Detergent", "Surf Excel", 210.0,
            "https://www.bigbasket.com/media/uploads/p/l/40051187_3-surf-excel-easy-wash-detergent-powder.jpg",
            "Cleaning", description = "Tough stain removal in machine and hand wash.", weight = "1.5 kg", originalPrice = 285.0, isHighestDiscount = true),

        Product(108, "Aashirvaad Select Premium Sharbati Atta", "Aashirvaad", 299.0,
            "https://www.bigbasket.com/media/uploads/p/l/1214068_1-aashirvaad-select-premium-sharbati-atta.jpg",
            "Grains", description = "Premium quality wheat flour that makes perfectly soft rotis.", weight = "5 kg", originalPrice = 350.0, isHighestDiscount = true),

        // ── Dairy ─────────────────────────────────────────────────────────────────

        Product(1, "Homogenised Toned Milk", "Amul", 60.0,
            "https://www.bigbasket.com/media/uploads/p/l/306926_1-amul-homogenised-toned-milk.jpg",
            "Dairy", description = "Pure and fresh toned cow milk. Rich in calcium and essential vitamins A, D and B12. Pasteurised and homogenised for safety and longer shelf life.", weight = "1 L"),

        Product(2, "Toned Milk", "Mother Dairy", 32.0,
            "https://www.bigbasket.com/media/uploads/p/l/40147029_2-mother-dairy-toned-milk.jpg",
            "Dairy", description = "Nutritious toned milk in a convenient pack. Sourced from healthy, well-fed cows. Ideal for tea, coffee, shakes and everyday cooking.", weight = "500 ml"),

        Product(3, "Pasteurised Butter", "Amul", 55.0,
            "https://www.bigbasket.com/media/uploads/p/l/1200424_2-amul-butter-pasteurised.jpg",
            "Dairy", description = "India's most-loved butter, made from fresh pasteurised cream with just the right touch of salt. Golden, rich and flavourful.", weight = "100 g", originalPrice = 60.0, isHighestDiscount = true),

        Product(4, "Cheese Slices - Plain", "Britannia", 130.0,
            "https://www.bigbasket.com/media/uploads/p/l/104724_8-britannia-cheese-slices-100-veg-plain.jpg",
            "Dairy", description = "Perfectly portioned processed cheese slices that melt beautifully. Each pack contains 10 individually wrapped slices.", weight = "200 g"),

        Product(5, "Malai Paneer", "Amul", 90.0,
            "https://www.bigbasket.com/media/uploads/p/l/104772_8-amul-malai-paneer.jpg",
            "Dairy", description = "Soft, fresh cottage cheese made from pure toned milk. High in protein. Perfect for paneer butter masala, kadai paneer, and raw salads.", weight = "200 g"),

        Product(6, "Cheese Cubes", "Go Cheese", 99.0,
            "https://www.bigbasket.com/media/uploads/p/l/162238_3-go-cheese-cubes-plain.jpg",
            "Dairy", isOutOfStock = true, description = "Bite-sized cubes of smooth, mild processed cheese. Great for party platters, salads, pasta and snacking.", weight = "200 g"),

        // ── Bakery ────────────────────────────────────────────────────────────────

        Product(7, "Brown Bread", "Harvest Gold", 45.0,
            "https://www.bigbasket.com/media/uploads/p/l/40008064_11-harvest-gold-bread-brown.jpg",
            "Bakery", description = "Wholesome whole-wheat brown bread baked fresh daily. High in dietary fibre and complex carbohydrates.", weight = "400 g"),

        Product(8, "Daily Fresh White Bread", "Britannia", 35.0,
            "https://www.bigbasket.com/media/uploads/p/l/40082736_5-britannia-daily-fresh-white-bread.jpg",
            "Bakery", description = "Soft, fluffy white sandwich bread for everyday use. Made with enriched wheat flour.", weight = "400 g", stockCount = 1),

        Product(9, "Garlic Bread", "Bonn", 65.0,
            "https://www.bigbasket.com/media/uploads/p/l/40194883_1-bonn-garlic-bread.jpg",
            "Bakery", isOutOfStock = true, description = "Ready-to-bake artisan garlic bread loaded with real garlic butter and Italian herbs.", weight = "250 g"),

        Product(10, "Multigrain Bread", "English Oven", 55.0,
            "https://www.bigbasket.com/media/uploads/p/l/40122234_4-english-oven-bread-multigrain.jpg",
            "Bakery", description = "Nutritious multigrain bread baked with 5 different grains. High in protein and dietary fibre.", weight = "400 g", tags = listOf("Healthy", "Vegan")),

        // ── Grains ────────────────────────────────────────────────────────────────

        Product(11, "Basmati Rice - Feast Rozzana", "India Gate", 120.0,
            "https://www.bigbasket.com/media/uploads/p/l/241600_5-india-gate-basmati-rice-feast-rozzana.jpg",
            "Grains", description = "Premium aged Basmati rice from the Himalayan foothills. Long, slender grains that cook fluffy and fragrant.", weight = "1 kg", stockCount = 2),

        Product(12, "Rozzana Super Basmati Rice", "Daawat", 150.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000412_14-daawat-rozzana-super-basmati-rice.jpg",
            "Grains", isOutOfStock = true, description = "Super premium Basmati rice, aged for maximum aroma and extra-long grain length. The choice of professional chefs.", weight = "1 kg"),

        Product(13, "Whole Wheat Atta", "Aashirvaad", 75.0,
            "https://www.bigbasket.com/media/uploads/p/l/126903_8-aashirvaad-atta-whole-wheat.jpg",
            "Grains", description = "Stone-ground whole wheat atta made from 100% MP wheat. Contains natural wheat germ and bran.", weight = "1 kg"),

        Product(14, "Organic Poha - Thick", "24 Mantra", 55.0,
            "https://www.bigbasket.com/media/uploads/p/l/40126731_4-24-mantra-organic-poha-thick.jpg",
            "Grains", description = "Certified organic flattened rice (poha) for a healthy, quick breakfast. Ready in under 10 minutes.", weight = "500 g", tags = listOf("Organic", "Vegan")),

        // ── Fruits ────────────────────────────────────────────────────────────────

        Product(15, "Apple - Shimla", "Fresho", 80.0,
            "https://www.bigbasket.com/media/uploads/p/l/40033819_29-fresho-apple-shimla.jpg",
            "Fruits", description = "Fresh, crispy Himachali Red Delicious apples, picked at peak ripeness.", weight = "4 pcs", stockCount = 2, tags = listOf("Organic", "Vegan")),

        Product(16, "Banana - Yelakki", "Fresho", 40.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000031_21-fresho-banana-yelakki.jpg",
            "Fruits", description = "Fresh, perfectly ripe Yelakki bananas. Rich in potassium and natural sugars.", weight = "500 g"),

        Product(17, "Kiwi - Green", "Fresho", 120.0,
            "https://www.bigbasket.com/media/uploads/p/l/20000911_30-fresho-kiwi-green.jpg",
            "Fruits", isOutOfStock = true, description = "Premium Green Kiwis with a lively tangy-sweet flavour. Extremely high in Vitamin C.", weight = "3 pcs"),

        Product(18, "Grapes - Green Seedless", "Fresho", 70.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000143_14-fresho-grapes-green-seedless.jpg",
            "Fruits", description = "Fresh, juicy seedless green grapes. Sweet, crisp and refreshing.", weight = "500 g"),

        // ── Beverages ─────────────────────────────────────────────────────────────

        Product(19, "100% Orange Juice", "Tropicana", 95.0,
            "https://www.bigbasket.com/media/uploads/p/l/265675_8-tropicana-100-juice-orange.jpg",
            "Beverages", description = "100% pure pressed orange juice with no added sugar, no artificial colours.", weight = "1 L"),

        Product(20, "Mango Nectar", "Real", 85.0,
            "https://www.bigbasket.com/media/uploads/p/l/1202868_1-real-fruit-power-mango.jpg",
            "Beverages", description = "Made from the king of fruits — the Alphonso mango. This thick, rich mango nectar tastes like summer.", weight = "1 L"),

        Product(21, "Green Tea - Honey Lemon", "Lipton", 150.0,
            "https://www.bigbasket.com/media/uploads/p/l/262947_11-lipton-honey-lemon-green-tea-bags.jpg",
            "Beverages", description = "Light, smooth and refreshing green tea with honey and lemon flavour.", weight = "25 bags", tags = listOf("Healthy", "Organic")),

        Product(22, "Energy Drink", "Red Bull", 125.0,
            "https://www.bigbasket.com/media/uploads/p/l/113110_4-red-bull-energy-drink.jpg",
            "Beverages", description = "Red Bull Energy Drink. Boosts energy levels and sharpens mental focus. Best served ice cold.", weight = "250 ml"),

        // ── Snacks ────────────────────────────────────────────────────────────────

        Product(23, "Potato Chips - Classic Salted", "Lay's", 20.0,
            "https://www.bigbasket.com/media/uploads/p/l/294293_16-lays-potato-chips-classic-salted.jpg",
            "Snacks", description = "Thin, perfectly crispy potato chips lightly seasoned with pure sea salt.", weight = "52 g"),

        Product(24, "Masala Munch", "Kurkure", 20.0,
            "https://www.bigbasket.com/media/uploads/p/l/283428_18-kurkure-namkeen-masala-munch.jpg",
            "Snacks", description = "Crunchy, fiery, tangy — the ultimate Indian tea-time snack with a blend of 7 authentic Indian spices.", weight = "90 g", originalPrice = 25.0, isHighestDiscount = true),

        Product(25, "Good Day Cashew Cookies", "Britannia", 35.0,
            "https://www.bigbasket.com/media/uploads/p/l/112613_6-britannia-good-day-cashew-cookies.jpg",
            "Snacks", description = "Buttery, crumbly, melt-in-the-mouth cookies generously studded with cashews.", weight = "120 g"),

        Product(26, "Multigrain Chips - Tomato", "Too Yumm", 30.0,
            "https://www.bigbasket.com/media/uploads/p/l/40131498_4-too-yumm-multigrain-chips-spanish-tomato.jpg",
            "Snacks", isOutOfStock = true, description = "Guilt-free munching! Baked, not fried, with 50% less fat. Spanish Tomato flavour.", weight = "54 g", tags = listOf("Healthy", "Keto")),

        // ── Personal Care ─────────────────────────────────────────────────────────

        Product(27, "Deeply Nourishing Body Wash", "Dove", 280.0,
            "https://www.bigbasket.com/media/uploads/p/l/40158280_8-dove-deeply-nourishing-body-wash.jpg",
            "Personal Care", description = "Dove's iconic body wash with 1/4 moisturising cream. Gently cleanses while leaving skin noticeably softer.", weight = "190 ml"),

        Product(28, "Anti-Dandruff Shampoo", "Head & Shoulders", 199.0,
            "https://www.bigbasket.com/media/uploads/p/l/40124317_4-head-shoulders-anti-dandruff-shampoo-cool-menthol.jpg",
            "Personal Care", description = "Clinically proven formula that eliminates dandruff. Cool Menthol variant for a refreshing feel.", weight = "180 ml"),

        Product(29, "Strong Teeth Toothpaste", "Colgate", 95.0,
            "https://www.bigbasket.com/media/uploads/p/l/1205364_1-colgate-strong-teeth-anticavity-toothpaste-with-amino-shakti.jpg",
            "Personal Care", description = "Colgate Strong Teeth with Amino Shakti technology strengthens teeth from deep within.", weight = "200 g"),

        // ── Cleaning ─────────────────────────────────────────────────────────────

        Product(30, "Dishwash Gel - Lemon", "Vim", 75.0,
            "https://www.bigbasket.com/media/uploads/p/l/266946_17-vim-dishwash-gel-lemon.jpg",
            "Cleaning", description = "Vim Dishwash Gel with active salt cuts through grease and stubborn food residue effortlessly.", weight = "500 ml"),

        Product(31, "Power Plus Toilet Cleaner", "Harpic", 120.0,
            "https://www.bigbasket.com/media/uploads/p/l/212686_11-harpic-power-plus-toilet-cleaner-original.jpg",
            "Cleaning", description = "Harpic Power Plus — kills 99.9% of germs. Powerful thick formula removes toughest stains.", weight = "500 ml"),

        Product(32, "Disinfectant Liquid", "Dettol", 99.0,
            "https://www.bigbasket.com/media/uploads/p/l/10014022_15-dettol-liquid-disinfectant-cleaner-for-home.jpg",
            "Cleaning", description = "Dettol Liquid Disinfectant cleaner kills 99.9% of bacteria and viruses on all floor surfaces.", weight = "500 ml"),

        // ── More Dairy ────────────────────────────────────────────────────────────

        Product(33, "Full Cream Milk", "Amul", 68.0,
            "https://www.bigbasket.com/media/uploads/p/l/40090893_2-amul-gold-full-cream-milk.jpg",
            "Dairy", description = "Rich and creamy full cream milk with high fat content. Perfect for making tea, coffee, sweets and desserts.", weight = "1 L"),

        Product(34, "Dahi (Curd)", "Amul", 55.0,
            "https://www.bigbasket.com/media/uploads/p/l/104851_6-amul-masti-dahi.jpg",
            "Dairy", description = "Fresh, thick and creamy curd made from pure toned milk. Perfect with rice, paratha or as raita.", weight = "400 g", originalPrice = 60.0),

        Product(35, "Shrikhand - Mango", "Amul", 90.0,
            "https://www.bigbasket.com/media/uploads/p/l/40026295_3-amul-shrikhand-mango-amrakhand.jpg",
            "Dairy", description = "Delicious strained yogurt dessert with natural mango flavour. A traditional Indian sweet treat.", weight = "200 g", isNewlyAdded = true),

        Product(36, "Processed Cheese Block", "Amul", 195.0,
            "https://www.bigbasket.com/media/uploads/p/l/104737_7-amul-processed-cheese-block.jpg",
            "Dairy", description = "Classic processed cheese block, perfect for slicing, grating, and melting. Great for sandwiches and pizzas.", weight = "200 g", originalPrice = 210.0),

        Product(37, "Lassi - Sweet", "Mother Dairy", 45.0,
            "https://www.bigbasket.com/media/uploads/p/l/40065066_4-mother-dairy-lassi-sweet.jpg",
            "Dairy", description = "Chilled, thick and sweet traditional Indian lassi made from pure curd. Refreshing and filling.", weight = "200 ml", isFrequentlyPurchased = true),

        // ── Vegetables ────────────────────────────────────────────────────────────

        Product(38, "Tomato", "Fresho", 30.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000201_13-fresho-tomato-hybrid.jpg",
            "Vegetables", description = "Fresh, ripe, firm red tomatoes. Rich in lycopene and Vitamin C. Essential for Indian cooking.", weight = "500 g"),

        Product(39, "Onion", "Fresho", 25.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000149_14-fresho-onion.jpg",
            "Vegetables", description = "Fresh farm-sourced onions. The base of every Indian dish. Pungent, flavourful, essential.", weight = "1 kg", isFrequentlyPurchased = true),

        Product(40, "Potato", "Fresho", 35.0,
            "https://www.bigbasket.com/media/uploads/p/l/40078535_7-fresho-potato.jpg",
            "Vegetables", description = "Fresh, clean potatoes great for sabzi, aloo paratha, fries and everything in between.", weight = "1 kg", isFrequentlyPurchased = true),

        Product(41, "Capsicum - Green", "Fresho", 40.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000176_14-fresho-capsicum-green.jpg",
            "Vegetables", description = "Fresh green capsicum, mildly flavoured. Great for stir-fries, curries, salads and stuffed recipes.", weight = "250 g"),

        Product(42, "Carrot", "Fresho", 28.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000164_14-fresho-carrot.jpg",
            "Vegetables", description = "Fresh, crunchy carrots loaded with beta-carotene. Great for halwa, salads, soups and juices.", weight = "500 g"),

        Product(43, "Spinach", "Fresho", 20.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000188_14-fresho-palak.jpg",
            "Vegetables", description = "Tender, fresh spinach leaves packed with iron, calcium and vitamins. Use in palak paneer or salads.", weight = "250 g", isNewlyAdded = true),

        // ── More Fruits ───────────────────────────────────────────────────────────

        Product(44, "Mango - Alphonso", "Fresho", 299.0,
            "https://www.bigbasket.com/media/uploads/p/l/30003344_7-fresho-alphonso-mango.jpg",
            "Fruits", description = "The king of mangoes! Premium Alphonso mangoes from Ratnagiri, hand-picked at perfect ripeness.", weight = "6 pcs", isNewlyAdded = true, originalPrice = 349.0),

        Product(45, "Orange - Nagpur", "Fresho", 65.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000267_14-fresho-orange-nagpur.jpg",
            "Fruits", description = "Juicy Nagpur oranges — the most famous variety from India. Tangy-sweet, seedless and refreshing.", weight = "4 pcs"),

        Product(46, "Pomegranate", "Fresho", 110.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000269_14-fresho-pomegranate.jpg",
            "Fruits", description = "Fresh, plump pomegranates bursting with juicy ruby-red arils. Antioxidant powerhouse.", weight = "1 pc"),

        Product(47, "Watermelon", "Fresho", 49.0,
            "https://www.bigbasket.com/media/uploads/p/l/10000280_14-fresho-watermelon.jpg",
            "Fruits", description = "Big, ripe and refreshingly sweet watermelon. The ultimate summer fruit.", weight = "1 pc (approx 3 kg)"),

        // ── Beverages ─────────────────────────────────────────────────────────────

        Product(48, "Nimbooz Masala Soda", "7UP", 30.0,
            "https://www.bigbasket.com/media/uploads/p/l/40072887_3-7up-nimbooz-masala-soda.jpg",
            "Beverages", description = "India's favourite nimbu soda! Real lemon juice with masala spice. No preservatives.", weight = "350 ml"),

        Product(49, "Iced Tea - Lemon", "Lipton", 45.0,
            "https://www.bigbasket.com/media/uploads/p/l/40097556_3-lipton-ice-tea-lemon.jpg",
            "Beverages", description = "Cool, refreshing iced tea with a zesty lemon flavour. Ready-to-drink, no added sugar.", weight = "500 ml", isNewlyAdded = true),

        Product(50, "Coconut Water", "Raw Pressery", 65.0,
            "https://www.bigbasket.com/media/uploads/p/l/40083436_4-raw-pressery-coconut-water.jpg",
            "Beverages", description = "Pure, natural coconut water cold-pressed for freshness. Hydrating with natural electrolytes.", weight = "200 ml", originalPrice = 75.0, isHighestDiscount = true),

        Product(51, "Chocolate Milk Drink", "Amul", 45.0,
            "https://www.bigbasket.com/media/uploads/p/l/40126990_4-amul-kool-chocolate-milk-drink.jpg",
            "Beverages", description = "Thick and creamy chocolate-flavoured milk drink packed with energy. A favourite for kids!", weight = "200 ml", isFrequentlyPurchased = true),

        // ── Snacks & Packaged Foods ───────────────────────────────────────────────

        Product(52, "Bourbon Biscuits", "Britannia", 30.0,
            "https://www.bigbasket.com/media/uploads/p/l/112616_6-britannia-bourbon-biscuits.jpg",
            "Snacks", description = "Classic dark chocolate cream-filled biscuits. The perfect tea-time companion for over 50 years.", weight = "150 g", isFrequentlyPurchased = true),

        Product(53, "Puffed Rice (Murmura)", "Haldiram's", 25.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009733_4-haldirams-puffed-rice-murmura.jpg",
            "Snacks", description = "Light, crispy puffed rice. The base for bhel puri, jhal muri or a quick evening munch.", weight = "200 g"),

        Product(54, "Instant Noodles - Masala", "Maggi", 14.0,
            "https://www.bigbasket.com/media/uploads/p/l/40010714_9-maggi-2-minute-masala-noodles.jpg",
            "Snacks", description = "India's most loved 2-minute noodles with the iconic Masala tastemaker. Quick, easy, delicious.", weight = "70 g", isFrequentlyPurchased = true, originalPrice = 16.0),

        Product(55, "Dark Fantasy Choco Fills", "Sunfeast", 35.0,
            "https://www.bigbasket.com/media/uploads/p/l/40106178_5-sunfeast-dark-fantasy-choco-fills.jpg",
            "Snacks", description = "Premium chocolate-filled cookies with a dark cocoa shell. Pure indulgence in every bite.", weight = "75 g", isNewlyAdded = true),

        Product(56, "Cornflakes", "Kellogg's", 185.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009587_8-kelloggs-corn-flakes.jpg",
            "Pantry", description = "Classic golden cornflakes — light, crispy and a wholesome start to your morning.", weight = "300 g"),

        Product(57, "Oats", "Quaker", 199.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009720_8-quaker-oats.jpg",
            "Pantry", description = "100% whole grain oats. High in fibre and protein for a healthy and filling breakfast.", weight = "500 g", originalPrice = 220.0, isHighestDiscount = true),

        // ── More Personal Care ────────────────────────────────────────────────────

        Product(58, "Aloe Vera Gel", "Patanjali", 75.0,
            "https://www.bigbasket.com/media/uploads/p/l/40029846_5-patanjali-saundarya-aloe-vera-gel.jpg",
            "Personal Care", description = "Pure Aloe Vera gel for skin moisturising, sunburn relief, and hair nourishment.", weight = "150 g", isNewlyAdded = true),

        Product(59, "Neem Face Wash", "Himalaya", 100.0,
            "https://www.bigbasket.com/media/uploads/p/l/40010532_8-himalaya-purifying-neem-face-wash.jpg",
            "Personal Care", description = "Purifying neem face wash that removes impurities and controls oil for clear, healthy skin.", weight = "100 ml"),

        Product(60, "Moisturising Lotion", "Vaseline", 199.0,
            "https://www.bigbasket.com/media/uploads/p/l/40036534_5-vaseline-intensive-care-deep-restore-body-lotion.jpg",
            "Personal Care", description = "Vaseline Intensive Care deep restoring body lotion for smooth, soft, moisturised skin.", weight = "300 ml", originalPrice = 230.0, isHighestDiscount = true),

        // ── Cooking Essentials ────────────────────────────────────────────────────

        Product(61, "Turmeric Powder", "Everest", 65.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009628_8-everest-turmeric-powder.jpg",
            "Pantry", description = "Pure, vibrant Everest Haldi (turmeric) powder. Essential for every Indian meal.", weight = "200 g", isFrequentlyPurchased = true),

        Product(62, "Red Chilli Powder", "Everest", 70.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009627_8-everest-red-chilli-powder.jpg",
            "Pantry", description = "Fiery red chilli powder made from select dried red chillies. Adds heat and colour to dishes.", weight = "200 g", isFrequentlyPurchased = true),

        Product(63, "Mustard Oil", "Patanjali", 180.0,
            "https://www.bigbasket.com/media/uploads/p/l/40076083_4-patanjali-mustard-oil.jpg",
            "Cooking Oil", description = "Cold-pressed pure mustard oil with a strong pungent flavour typical of North Indian and Bengali cuisine.", weight = "1 L"),

        Product(64, "Coconut Oil", "Parachute", 220.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009668_8-parachute-coconut-oil.jpg",
            "Cooking Oil", description = "100% pure coconut oil, great for cooking South Indian dishes, hair care and skin moisturising.", weight = "500 ml", originalPrice = 249.0),

        Product(65, "Salt - Iodised", "Tata", 25.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009586_8-tata-salt-iodised.jpg",
            "Pantry", description = "Refined iodised salt from Tata — India's most trusted salt brand for over 100 years.", weight = "1 kg", isFrequentlyPurchased = true),

        Product(66, "Sugar", "Uttam", 55.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009585_6-uttam-sugar.jpg",
            "Pantry", description = "Clean, pure refined sugar for everyday use in tea, coffee, desserts and baking.", weight = "1 kg", isFrequentlyPurchased = true),

        Product(67, "Cumin Seeds (Jeera)", "Everest", 55.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009631_8-everest-cumin-seeds.jpg",
            "Pantry", description = "Aromatic, whole cumin seeds for tempering dals, rice, and curries. An Indian kitchen staple.", weight = "100 g"),

        Product(68, "Ghee", "Amul", 299.0,
            "https://www.bigbasket.com/media/uploads/p/l/40009641_8-amul-pure-ghee.jpg",
            "Dairy", description = "Pure cow milk ghee with a rich aroma and golden colour. Made using traditional bilona method.", weight = "200 ml", originalPrice = 340.0, isHighestDiscount = true, isFrequentlyPurchased = true)
    )

    fun getCategories() = listOf("All") + getProducts().map { it.category }.distinct()

    fun getById(id: Int) = getProducts().find { it.id == id }

    fun getCategoryEmoji(category: String) = when (category) {
        "All"                -> "🏪"
        "Dairy"              -> "🥛"
        "Bakery"             -> "🍞"
        "Grains"             -> "🌾"
        "Fruits"             -> "🍎"
        "Vegetables"         -> "🥦"
        "Beverages"          -> "🧃"
        "Snacks"             -> "🍿"
        "Personal Care"      -> "🧴"
        "Cleaning"           -> "🧹"
        "Cooking Oil"        -> "🫙"
        "Pantry"             -> "🥜"
        "Dairy Alternatives" -> "🌱"
        else                 -> "📦"
    }
}