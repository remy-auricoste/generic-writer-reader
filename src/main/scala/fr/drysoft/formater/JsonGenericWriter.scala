package fr.drysoft.formater

import play.api.libs.json._

object JsonGenericWriter extends GenericWriter[JsValue] {
  override protected def wrapMap(map: Seq[Pair[String, _]]): Any = JsObject(map.asInstanceOf[Seq[(String, JsValue)]])

  override protected def wrapSeq(seq: Seq[_]): Any = JsArray(seq.asInstanceOf[Seq[JsValue]])

  override protected def wrapOpt(opt: Option[_]): Any = opt.getOrElse(JsNull)

  registerWriter(JsString.apply)
  registerWriter[java.lang.Boolean, JsBoolean](x => JsBoolean.apply(x.booleanValue()))
  registerWriter[java.lang.Integer, JsNumber](x => JsNumber.apply(x.intValue()))
}
