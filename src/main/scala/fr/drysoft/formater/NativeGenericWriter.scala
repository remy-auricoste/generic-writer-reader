package fr.drysoft.formater

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object NativeGenericWriter extends GenericWriter[Seq[(String, _)]] {

  def getWriter[T](implicit classTag: ClassTag[T], typeTag: TypeTag[T]) =
    registerWriter(ReflectionHelper.getWriter[T])
}
