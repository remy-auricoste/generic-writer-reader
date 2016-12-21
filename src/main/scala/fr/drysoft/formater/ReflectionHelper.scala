package fr.drysoft.formater

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => ru}

object ReflectionHelper {
  val mirror: _root_.scala.reflect.runtime.universe.Mirror = ru.runtimeMirror(getClass.getClassLoader)

  private def getFields[A: TypeTag]: Seq[_root_.scala.reflect.runtime.universe.MethodSymbol] = {
    ru.typeOf[A].members.collect {
      case m: MethodSymbol if m.isCaseAccessor => m
    }.toList.reverse
  }

  def getWriter[A: TypeTag](implicit classTag: ClassTag[A]): A => Seq[(String, _)] = {
    val fields = getFields[A]
    obj =>
      val objMirror: _root_.scala.reflect.runtime.universe.InstanceMirror = mirror.reflect(obj)
      fields.map { field =>
        val value = objMirror.reflectField(field).get
        field.name.toString -> value
      }
  }

  def getReader[B, A: TypeTag](getter: B => String => Any)(implicit classTag: ClassTag[A]): B => A = {
    val fields = getFields[A]
    val clazzType = ru.typeOf[A].typeSymbol.asClass
    val constructorType = ru.typeOf[A].decl(ru.termNames.CONSTRUCTOR).asMethod
    val constructor = mirror.reflectClass(clazzType).reflectConstructor(constructorType)
    obj => {
      val instanceGetter: (String) => Any = getter(obj)
      val values: Seq[Any] = fields.map { field =>
        instanceGetter(field.name.toString)
      }
      val instance = constructor(values: _*)
      instance.asInstanceOf[A]
    }
  }
}
