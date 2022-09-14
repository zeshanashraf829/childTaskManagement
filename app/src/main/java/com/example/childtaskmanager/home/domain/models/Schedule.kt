import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class Schedule (

	@SerializedName("Mon")
	@Json(name="Mon")
	val mon : String?=null,
	@SerializedName("Tue")
	@Json(name="Tue")
	val tue : String?=null,
	@SerializedName("Wed")
	@Json(name="Wed")
	val wed : String?=null,
	@SerializedName("Thu")
	@Json(name="Thu")
	val thu : String?=null,
	@SerializedName("Fri")
	@Json(name="Fri")
	val fri : String?=null,
	@SerializedName("Sat")
	@Json(name="Sat")
	val sat : String?=null,
	@SerializedName("Sun")
	@Json(name="Sun")
	val sun : String?=null
)