package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int = {
    c match {
      case 0 | `r` => 1
      case _ => pascal(c - 1, r - 1) + pascal(c, r - 1)
    }
  }

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    def _balance(chars: List[Char], cnt: Int): Boolean = {
      if (cnt < 0) return false
      chars match {
        case '(' :: tail => _balance(tail, cnt + 1)
        case ')' :: tail => _balance(tail, cnt - 1)
        case _ :: tail => _balance(tail, cnt)
        case Nil => cnt == 0
      }
    }
    _balance(chars, 0)
  }

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0) return 1
    if (money < 0) return 0
    if (coins.isEmpty) return 0
    countChange(money - coins.head, coins) + countChange(money, coins.tail)
  }
}
