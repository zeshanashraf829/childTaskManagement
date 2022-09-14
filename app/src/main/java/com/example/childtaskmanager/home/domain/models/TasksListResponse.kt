import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class TasksListResponse(
	@SerializedName("_id")
	@Expose
	@Json(name = "_id")
	val _id : String?=null,
	@SerializedName("name")
	@Expose
	@Json(name = "name")
	val name : String?="",
	@SerializedName("schedule")
	@Expose
	@Json(name = "schedule")
	var schedule : Schedule?,
	@SerializedName("visualAidUrl")
	@Expose
	@Json(name = "visualAidUrl")
	val visualAidUrl : String?=null
)