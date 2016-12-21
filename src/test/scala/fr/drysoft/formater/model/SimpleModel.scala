package fr.drysoft.formater.model

import fr.drysoft.formater.{JsonGenericWriter, NativeGenericWriter, ReflectionHelper}
import play.api.libs.json.{JsValue, Json}

case class SimpleModel(string: String, int: Int, bool: Boolean)

object SimpleModel {
  NativeGenericWriter.getWriter[SimpleModel]

  val nativeWriter = ReflectionHelper.getWriter[SimpleModel]

  JsonGenericWriter.registerWriter[SimpleModel, JsValue] { model =>
    JsonGenericWriter.write(nativeWriter(model))
  }
  implicit val jsonWriter = Json.writes[SimpleModel]
}
