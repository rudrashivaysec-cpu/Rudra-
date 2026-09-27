package com.example.util

enum class AppLanguage(val code: String, val displayName: String) {
    EN("EN", "English"),
    HI("HI", "हिंदी"),
    ES("ES", "Español")
}

object StringsHelper {
    fun get(key: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.HI -> when (key) {
                "brand_name" -> "राइडर ट्रैक 360"
                "brand_subtitle" -> "राइडर डिलीवरी ट्रैकर"
                "delivery_details" -> "डिलीवरी विवरण"
                "order_id" -> "ऑर्डर आईडी"
                "customer" -> "ग्राहक"
                "address" -> "पता"
                "location_map" -> "स्थान का नक्शा"
                "status" -> "स्थिति"
                "view_map" -> "📍 मैप देखें"
                "find_address" -> "🔍 पता खोजें"
                "navigate" -> "🧭 नेविगेट करें"
                "no_deliveries" -> "अभी कोई डिलीवरी असाइन नहीं है।"
                "all" -> "सभी"
                "pending" -> "लंबित"
                "out_for_delivery" -> "डिलीवरी के लिए रवाना"
                "delivered" -> "डिलीवर हो गया"
                "failed" -> "असफल"
                "active_orders" -> "सक्रिय ऑर्डर"
                "completed" -> "पूर्ण"
                "cod_collection" -> "कैश ऑन डिलीवरी"
                "add_delivery" -> "नया ऑर्डर जोड़ें"
                "table_view" -> "तालिका दृश्य"
                "card_view" -> "कार्ड दृश्य"
                "call_customer" -> "ग्राहक को कॉल करें"
                "update_status" -> "स्थिति अपडेट करें"
                "delivery_notes" -> "डिलीवरी निर्देश"
                else -> key
            }
            AppLanguage.ES -> when (key) {
                "brand_name" -> "Rider Track 360"
                "brand_subtitle" -> "Rastreador de Entregas"
                "delivery_details" -> "Detalles del Envío"
                "order_id" -> "ID de Pedido"
                "customer" -> "Cliente"
                "address" -> "Dirección"
                "location_map" -> "Mapa de Ubicación"
                "status" -> "Estado"
                "view_map" -> "📍 Ver Mapa"
                "find_address" -> "🔍 Buscar Dirección"
                "navigate" -> "🧭 Navegar"
                "no_deliveries" -> "No hay entregas asignadas aún."
                "all" -> "Todos"
                "pending" -> "Pendiente"
                "out_for_delivery" -> "En camino"
                "delivered" -> "Entregado"
                "failed" -> "Fallido"
                "active_orders" -> "Pedidos Activos"
                "completed" -> "Completados"
                "cod_collection" -> "Cobro contra entrega"
                "add_delivery" -> "Agregar Pedido"
                "table_view" -> "Vista Tabla"
                "card_view" -> "Vista Tarjeta"
                "call_customer" -> "Llamar al cliente"
                "update_status" -> "Actualizar Estado"
                "delivery_notes" -> "Notas de entrega"
                else -> key
            }
            AppLanguage.EN -> when (key) {
                "brand_name" -> "Rider Track 360"
                "brand_subtitle" -> "Rider Delivery Tracker"
                "delivery_details" -> "Delivery Details"
                "order_id" -> "Order ID"
                "customer" -> "Customer"
                "address" -> "Address"
                "location_map" -> "Location Map"
                "status" -> "Status"
                "view_map" -> "📍 View Map"
                "find_address" -> "🔍 Find Address"
                "navigate" -> "🧭 Navigate"
                "no_deliveries" -> "No deliveries assigned yet."
                "all" -> "All"
                "pending" -> "Pending"
                "out_for_delivery" -> "Out for delivery"
                "delivered" -> "Delivered"
                "failed" -> "Failed"
                "active_orders" -> "Active Orders"
                "completed" -> "Completed"
                "cod_collection" -> "COD Collection"
                "add_delivery" -> "Add Order"
                "table_view" -> "Table View"
                "card_view" -> "Cards View"
                "call_customer" -> "Call Customer"
                "update_status" -> "Update Status"
                "delivery_notes" -> "Delivery Instructions"
                else -> key
            }
        }
    }
}
