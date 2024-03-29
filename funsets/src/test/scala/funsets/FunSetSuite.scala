package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  def toSet(s: List[Integer]): Set = {
    s match {
      case s1 :: Nil => singletonSet(s1)
      case s1 :: s2 :: Nil => union(singletonSet(s1), singletonSet(s2))
      case s1 :: tail => union(singletonSet(s1), toSet(tail))
      case Nil => Nil
    }
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect") {
    new TestSets {
      val s12 = union(s1, s2)
      val s23 = union(s2, s3)
      val i = intersect(s12, s23)
      assert(contains(i, 2), "contains 2")
      assert(!contains(i, 1), "not contains 1")
      assert(!contains(i, 3), "not contains 3")
    }
  }

  test("diff") {
    new TestSets {
      val s12 = union(s1, s2)
      val s23 = union(s2, s3)
      val d1 = diff(s12, s23)
      assert(contains(d1, 1), "contains 1")
      assert(!contains(d1, 2), "not contains 2")
      assert(!contains(d1, 3), "not contains 3")

      val d3 = diff(s23, s12)
      assert(!contains(d3, 1), "not contains 1")
      assert(!contains(d3, 2), "not contains 2")
      assert(contains(d3, 3), "contains 3")

      val empty = diff(s12, s12)
      assert(!contains(empty, 1), "not contains 1")
      assert(!contains(empty, 2), "not contains 2")
      assert(!contains(empty, 3), "not contains 3")
    }
  }

  test("filter") {
    new TestSets {
      val s12 = union(s1, s2)
      val f2 = filter(s12, e => e == 2)

      assert(!contains(f2, 1), "not contains 1")
      assert(contains(f2, 2), "contains 2")
    }
  }

  test("forall") {
    new TestSets {
      val s12 = union(s1, s2)
      val s123 = union(s12, s3)

      assert(forall(s123, a => a <= 3), "all <= 3")
      assert(!forall(s123, a => a == 2), "all == 2")

      assert(forall(s2, a => a == 2), "2 == 2")
    }
  }

  test("forall 1,2,3,4") {
    new TestSets {
      val s = union(union(union(singletonSet(1), singletonSet(2)), singletonSet(3)), singletonSet(4))
      assert(forall(s, x => x < 5), "all <= 5")
    }
  }

  test("exists 1,3,4,5,7,1000") {
    new TestSets {
      val s = toSet(List(1,3,4,5,7,1000))
      assert(!FunSets.exists(s, x => x==2))
    }
  }

  test("exists") {
    new TestSets {
      val s12 = union(s1, s2)
      val s123 = union(s12, s3)

      assert(exists(s123, a => a <= 3), "all <= 3")
      assert(exists(s123, a => a == 2), "all == 2")
    }
  }

  test("map") {
    new TestSets {
      val s12 = union(s1, s2)
      val s123 = union(s12, s3)
      val s369 = map(s123, e => e * 3);

      assert(contains(s369, 3), "3")
      assert(contains(s369, 6), "6")
      assert(contains(s369, 9), "9")
    }
  }

}
