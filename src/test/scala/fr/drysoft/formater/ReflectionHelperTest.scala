package fr.drysoft.formater

import java.util
import java.util.Date

import fr.drysoft.formater.model.SimpleModel
import play.api.libs.json.{JsArray, Json, Writes}
import play.api.test.PlaySpecification

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

class ReflectionHelperTest extends PlaySpecification {
  val helper = ReflectionHelper

  def testWriter[T: TypeTag](instance: T, seqInstance: Seq[(String, Any)])(implicit classTag: ClassTag[T]) = {
    val writer = helper.getWriter[T]
    writer(instance) === seqInstance
  }

  def testReader[T: TypeTag](instance: T, seqInstance: Seq[(String, Any)])(implicit classTag: ClassTag[T]) = {
    val reader = helper.getReader[Seq[(String, Any)], T] {
      seq: Seq[(String, Any)] =>
        {
          val map = seq.toMap
          fieldName => map(fieldName)
        }
    }
    reader(seqInstance) === instance
  }

  def testFormat[T: TypeTag](instance: T, seqInstance: Seq[(String, Any)])(implicit classTag: ClassTag[T]) = {
    testWriter(instance, seqInstance)
    testReader(instance, seqInstance)
  }

  //  "getWriter and getReader methods" should {
  //    "work with a simple object" in {
  //      val instance = SimpleModel("string", 3, bool = true)
  //      val seqInstance = Seq(
  //        "string" -> "string",
  //        "int" -> 3,
  //        "bool" -> true
  //      )
  //      testFormat(instance, seqInstance)
  //    }
  //
  //    "work with an object with subtypes" in {
  //      testFormat(TestWithSubTypeModel(
  //        string = "string",
  //        int = 3,
  //        bool = true,
  //        subModel = null,
  //        subOpt = None,
  //        subList = Seq()
  //      ), Seq(
  //        "string" -> "string",
  //        "int" -> 3,
  //        "bool" -> true,
  //        "subModel" -> null,
  //        "subOpt" -> None,
  //        "subList" -> Seq()
  //      ))
  //    }
  //
  //    "work with an object with subtypes that have values" in {
  //      testFormat(TestWithSubTypeModel(
  //        string = "string",
  //        int = 3,
  //        bool = true,
  //        subModel = SubModel("sub1"),
  //        subOpt = Option(SubModel("sub2")),
  //        subList = Seq(SubModel("sub3"), SubModel("sub4"))
  //      ), Seq(
  //        "string" -> "string",
  //        "int" -> 3,
  //        "bool" -> true,
  //        "subModel" -> SubModel("sub1"),
  //        "subOpt" -> Option(SubModel("sub2")),
  //        "subList" -> Seq(SubModel("sub3"), SubModel("sub4"))
  //      ))
  //    }
  //  }

  def timer[T](callback: => T): T = {
    val start = new Date().getTime
    val result = callback
    println(s"time: ${new Date().getTime - start}")
    result
  }

  "GenericWriter" should {
    SimpleModel
    val writer = JsonGenericWriter

    //    "work with simple object" in {
    //      val instance = SimpleModel("string", 3, bool = true)
    //      val seqInstance = Seq(
    //        "string" -> "string",
    //        "int" -> 3,
    //        "bool" -> true
    //      )
    //      writer.write(instance) === seqInstance
    //    }

    "performance test" in {
      val objects = (0 until 100000).map { i =>
        SimpleModel("string", i, bool = true)
      }
      val finalResult = new util.ArrayList[Any]()
      timer {
        finalResult.add(JsArray(objects.map(x => writer.write(x))).toString())
      }
      timer {
        finalResult.add(Json.toJson(objects)(Writes.seq[SimpleModel]).toString())
      }
      1 === 1
    }
  }
}