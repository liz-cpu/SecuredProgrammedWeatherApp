package com.example.nhlmobileapp.responses

data class CoordinatesResponse(
    val results: List<Map<String, Any>>
) {
    fun getCoords(): Pair<Double, Double>? {
        for (item: Map<String, Any> in this.results) {
            if (!item.containsKey("geometry")) {
                continue
            }

            if (!item.get("geometry")!!::class.simpleName.equals("LinkedHashTreeMap")) {
                continue
            }
            val values: Map<String, Double> = (item.get("geometry") as Map<String, Double>?)!!

            if (!values.containsKey("lat")){
                continue
            }
            if (!values.containsKey("lng")){
                continue
            }
            return Pair(values.get("lat") as Double, values.get("lng") as Double)
        }
        return null
    }
}
