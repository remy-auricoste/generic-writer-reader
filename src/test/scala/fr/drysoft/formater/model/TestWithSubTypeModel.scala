package fr.drysoft.formater.model

case class TestWithSubTypeModel(string: String,
  int: Int,
  bool: Boolean,
  subModel: SubModel,
  subOpt: Option[SubModel],
  subList: Seq[SubModel])