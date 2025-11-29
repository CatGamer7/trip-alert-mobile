import androidx.room.TypeConverter
import com.example.tripalert.domain.models.GeoPoint
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun fromGeoPoint(point: GeoPoint): String = "${point.latitude},${point.longitude}"

    @TypeConverter
    fun toGeoPoint(data: String): GeoPoint {
        val parts = data.split(",")
        return GeoPoint(parts[0].toDouble(), parts[1].toDouble())
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String = date.toString()

    @TypeConverter
    fun toLocalDateTime(data: String): LocalDateTime = LocalDateTime.parse(data)
}
