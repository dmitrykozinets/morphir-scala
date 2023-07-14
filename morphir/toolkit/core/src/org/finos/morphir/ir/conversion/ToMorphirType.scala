package org.finos.morphir
package ir
package conversion

import org.finos.morphir.datamodel.{Concept, Label}
import org.finos.morphir.ir.{Type => T}
import org.finos.morphir.ir.Type.{Type, UType}

import scala.collection.immutable.{Map, Set}

trait ToMorphirType[A, +Attribs] { self =>
  def apply: Type[Attribs]
  final def morphirType: Type[Attribs] = apply

  def as[B]: ToMorphirType[B, Attribs] = new ToMorphirType[B, Attribs] {
    override def apply: Type[Attribs] = self.apply
  }
}

object ToMorphirType {
  def apply[A, Attribs](implicit toMorphirType: ToMorphirType[A, Attribs]): ToMorphirType[A, Attribs] = toMorphirType
  def summon[A]: SummonPartiallyApplied[A] = new SummonPartiallyApplied[A]

  def toUTypeConverter[A](f: => UType): ToMorphirUType[A] = new ToMorphirUType[A] {
    def apply: UType = f
  }

  implicit val unitUType: ToMorphirUType[scala.Unit]               = toUTypeConverter(T.unit)
  implicit val boolUType: ToMorphirUType[Boolean]                  = toUTypeConverter(sdk.Basics.boolType)
  implicit val intUType: ToMorphirUType[Int]                       = toUTypeConverter(sdk.Int.int32Type)
  implicit val stringUType: ToMorphirUType[String]                 = toUTypeConverter(sdk.String.stringType)
  implicit val byteUType: ToMorphirUType[Byte]                     = toUTypeConverter(sdk.Int.int8Type)
  implicit val shortUType: ToMorphirUType[Short]                   = toUTypeConverter(sdk.Int.int16Type)
  implicit val decimalUType: ToMorphirUType[scala.BigDecimal]      = toUTypeConverter(sdk.Decimal.decimalType)
  implicit val localDateUType: ToMorphirUType[java.time.LocalDate] = toUTypeConverter(sdk.LocalDate.localDateType)
  implicit val localTimeUType: ToMorphirUType[java.time.LocalTime] = toUTypeConverter(sdk.LocalTime.localTimeType)
  implicit val monthUType: ToMorphirUType[java.time.Month]         = toUTypeConverter(sdk.Month.dateType)
  implicit val charUType: ToMorphirUType[scala.Char]               = toUTypeConverter(sdk.Char.charType)
  implicit val bigIntUType: ToMorphirUType[scala.BigInt]           = toUTypeConverter(sdk.Basics.intType)

  final class SummonPartiallyApplied[A](private val dummy: Boolean = true) extends AnyVal {
    def withAttributesOf[Attribs](implicit toMorphirType: ToMorphirType[A, Attribs]): ToMorphirType[A, Attribs] =
      toMorphirType
  }
}
