package fr.drysoft.formater

import java.util.concurrent.ConcurrentHashMap

import scala.reflect.ClassTag

trait GenericWriter[B] {
  private val writerRegistry = new ConcurrentHashMap[String, _ => _]()

  @inline private def classToKey[T](clazz: Class[T]) = clazz.getName

  def registerWriter[T, U](writer: T => U)(implicit classTag: ClassTag[T]) =
    writerRegistry.put(classToKey(classTag.runtimeClass), writer)

  protected def wrapMap(map: Seq[(String, _)]): Any = map

  protected def wrapSeq(seq: Seq[_]): Any = seq

  protected def wrapOpt(opt: Option[_]): Any = opt

  def write[A](obj: A): B = {
    writeRecursive(obj).asInstanceOf[B]
  }

  private def writeRecursive[T](obj: T): Any = {
    obj match {
      case obj: Seq[(String, _)] => wrapMap(obj.map {
        case (key: String, value) =>
          key -> writeRecursive(value)
      })
      case obj: Map[String, _] => wrapMap(obj.mapValues(writeRecursive).toSeq)
      case obj: Seq[_] => wrapSeq(obj.map(writeRecursive))
      case obj: Option[_] => wrapOpt(obj.map(writeRecursive))
      case x =>
        writerRegistry.get(classToKey(obj.getClass)) match {
          case null => obj
          case writer => writer.asInstanceOf[T => _](obj)
        }
      case _ => obj
    }

  }
}
