package org.finos.morphir.service

import org.finos.morphir.util.vfile.*
import zio.*

trait MorphirBundlePlatformSpecific {
  val live: ULayer[MorphirBundle] = ZLayer.succeed(MorphirBundleLive)

  object MorphirBundleLive extends MorphirBundle {
    def bundle(outputPath: VPath, irFiles: List[VPath]): Task[Unit] =
      for {
        _ <- Console.printLine("Bundle command executing")
        _ <- Console.printLine(s"\toutputPath: $outputPath")
        _ <- Console.printLine(s"\tirFiles: $irFiles")
        _ <- Console.printLine("Bundle command executed")
      } yield ()

    def library(outputDir: VPath, irFiles: List[VPath]): Task[Unit] =
      for {
        _ <- Console.printLine("Library command executing")
        _ <- Console.printLine(s"\toutputDir: $outputDir")
        _ <- Console.printLine(s"\tirFiles: $irFiles")
        _ <- Console.printLine("Library command executed")
      } yield ()
  }
}
