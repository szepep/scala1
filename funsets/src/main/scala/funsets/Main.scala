package funsets

object Main extends App {
  import FunSets._
  println(contains(singletonSet(1), 1))

  printSet(toSet(List(1, 2, 3, 4)))
}
