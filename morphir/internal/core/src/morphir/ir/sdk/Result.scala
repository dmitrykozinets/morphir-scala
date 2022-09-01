package morphir.mir.sdk

import zio.Chunk
import morphir.mir.Module
import morphir.mir.Module.ModuleName
import morphir.mir.Type.Specification.CustomTypeSpecification
import morphir.mir.Type.{Constructors, Type, UType, reference, variable}
import morphir.mir.Value.Value
import morphir.mir.Value.Value.{Apply, Constructor}
import morphir.mir.sdk.Common._
import morphir.mir.sdk.Maybe.maybeType
import morphir.syntax.NamingSyntax._
object Result {
  val moduleName: ModuleName = ModuleName.fromString("Result")

  val moduleSpec: Module.USpecification = Module.USpecification(
    types = Map(
      name("Result") ->
        CustomTypeSpecification(
          Chunk(name("e"), name("a")),
          Constructors(
            Map(
              name("Ok")  -> Chunk((name("value"), variable(name("a")))),
              name("Err") -> Chunk((name("error"), variable(name("e"))))
            )
          )
        ) ?? "Type that represents the result of a computation that can either succeed or fail."
    ),
    values = Map(
      vSpec(
        "andThen",
        "f"      -> tFun(tVar("a"))(resultType(tVar("x"), tVar("b"))),
        "result" -> resultType(tVar("x"), tVar("a"))
      )(resultType(tVar("x"), tVar("b"))),
      vSpec("map", "f" -> tFun(tVar("a"))(tVar("b")), "result" -> resultType(tVar("x"), tVar("a")))(
        resultType(tVar("x"), tVar("b"))
      ),
      vSpec(
        "map2",
        "f"       -> tFun(tVar("a"), tVar("b"))(tVar("r")),
        "result1" -> resultType(tVar("x"), tVar("a")),
        "result2" -> resultType(tVar("x"), tVar("b"))
      )(resultType(tVar("x"), tVar("r"))),
      vSpec(
        "map3",
        "f"       -> tFun(tVar("a"), tVar("b"), tVar("c"))(tVar("r")),
        "result1" -> resultType(tVar("x"), tVar("a")),
        "result2" -> resultType(tVar("x"), tVar("b")),
        "result2" -> resultType(tVar("x"), tVar("c"))
      )(resultType(tVar("x"), tVar("r"))),
      vSpec(
        "map4",
        "f"       -> tFun(tVar("a"), tVar("b"), tVar("c"), tVar("d"))(tVar("r")),
        "result1" -> resultType(tVar("x"), tVar("a")),
        "result2" -> resultType(tVar("x"), tVar("b")),
        "result2" -> resultType(tVar("x"), tVar("c")),
        "result2" -> resultType(tVar("x"), tVar("d"))
      )(resultType(tVar("x"), tVar("r"))),
      vSpec(
        "map5",
        "f"       -> tFun(tVar("a"), tVar("b"), tVar("c"), tVar("d"), tVar("e"))(tVar("r")),
        "result1" -> resultType(tVar("x"), tVar("a")),
        "result2" -> resultType(tVar("x"), tVar("b")),
        "result2" -> resultType(tVar("x"), tVar("c")),
        "result2" -> resultType(tVar("x"), tVar("d")),
        "result2" -> resultType(tVar("x"), tVar("e"))
      )(resultType(tVar("x"), tVar("r"))),
      vSpec("withDefault", "default" -> tVar("a"), "result" -> resultType(tVar("x"), tVar("a")))(tVar("a")),
      vSpec("toMaybe", "result" -> resultType(tVar("x"), tVar("a")))(maybeType(tVar("a"))),
      vSpec("fromMaybe", "error" -> tVar("x"), "maybe" -> maybeType(tVar("a")))(resultType(tVar("x"), tVar("a"))),
      vSpec("mapError", "f" -> tFun(tVar("x"))(tVar("y")), "result" -> resultType(tVar("x"), tVar("a")))(
        resultType(tVar("y"), tVar("a"))
      )
    )
  )

  def resultType(errorType: UType, itemType: UType): UType =
    reference(toFQName(moduleName, "result"), errorType, itemType)

  def resultType[A](attributes: A)(errorType: Type[A], itemType: Type[A]): Type[A] =
    reference(attributes, toFQName(moduleName, "result"), errorType, itemType)

  def ok[TA, VA](va: VA)(value: Value[TA, VA]): Value[TA, VA] =
    Apply(va, Constructor(va, toFQName(moduleName, "Ok")), (value))

  def err[TA, VA](va: VA)(error: Value[TA, VA]): Value[TA, VA] =
    Apply(va, Constructor(va, toFQName(moduleName, "Err")), error)
  // todo add nativefunctions
}